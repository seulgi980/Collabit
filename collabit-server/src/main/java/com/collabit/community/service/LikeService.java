package com.collabit.community.service;

import com.collabit.community.domain.dto.LikeResponseDTO;
import com.collabit.community.domain.entity.PostLike;
import com.collabit.community.domain.entity.PostLikeId;
import com.collabit.community.exception.DuplicateLikeException;
import com.collabit.community.repository.PostLikeRepository;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final StringRedisTemplate redisTemplate;
    private final PostLikeRepository postLikeRepository;
    private final LikeCacheService likeCacheService;

    private static final String CREATE_LIKE_PREFIX = "like::";
    private static final String DELETE_LIKE_PREFIX = "cancel::";
    private final UserRepository userRepository;

    public LikeResponseDTO like(String userCode, int postCode) {
        log.debug("Attempting to add like for post: {} by user: {}", postCode, userCode);

        userRepository.findByCode(userCode)
            .orElseThrow(() -> new UserNotFoundException());

        if(likeCacheService.getIsLiked(userCode, postCode)) {
            log.debug("Like already exists for post: {} by user: {}", postCode, userCode);
            throw new DuplicateLikeException();
        }

        String isLikedKey = "isLiked::" + userCode + "likes" + postCode;
        String likeCountKey = "likeCount::" + postCode;
        String createKey = CREATE_LIKE_PREFIX + postCode;
        String deleteKey = DELETE_LIKE_PREFIX + postCode;

        log.debug("Setting like state in Redis for post: {}", postCode);
        redisTemplate.opsForValue().set(isLikedKey, String.valueOf(true));
        redisTemplate.expire(isLikedKey, Duration.ofHours(2));

        int likeCount = likeCacheService.getLikeCount(postCode);
        log.debug("Current like count for post {}: {}", postCode, likeCount);

        redisTemplate.opsForValue().set(likeCountKey, String.valueOf(likeCount+1));
        redisTemplate.expire(likeCountKey, Duration.ofHours(2));

        if(redisTemplate.opsForSet().isMember(deleteKey, userCode)){
            log.debug("Removing cancel request for post: {} by user: {}", postCode, userCode);
            redisTemplate.opsForSet().remove(deleteKey, userCode);
        } else {
            log.debug("Adding like request for post: {} by user: {}", postCode, userCode);
            redisTemplate.opsForSet().add(createKey, userCode);
        }

        log.debug("Like successfully added for post: {} by user: {}", postCode, userCode);
        return LikeResponseDTO.builder()
            .likeCount(likeCount+1)
            .isLiked(true)
            .build();
    }

    public LikeResponseDTO cancelLike(String userCode, int postCode) {
        log.debug("Attempting to cancel like for post: {} by user: {}", postCode, userCode);

        userRepository.findByCode(userCode)
            .orElseThrow(() -> new UserNotFoundException());

        if(!likeCacheService.getIsLiked(userCode, postCode)) {
            log.debug("Like does not exist for post: {} by user: {}", postCode, userCode);
            throw new DuplicateLikeException();
        }

        String isLikedKey = "isLiked::" + userCode + "likes" + postCode;
        String likeCountKey = "likeCount::" + postCode;
        String createKey = CREATE_LIKE_PREFIX + postCode;
        String deleteKey = DELETE_LIKE_PREFIX + postCode;

        log.debug("Setting unlike state in Redis for post: {}", postCode);
        redisTemplate.opsForValue().set(isLikedKey, String.valueOf(false));
        redisTemplate.expire(isLikedKey, Duration.ofHours(2));

        int likeCount = likeCacheService.getLikeCount(postCode);
        log.debug("Current like count for post {}: {}", postCode, likeCount);

        redisTemplate.opsForValue().set(likeCountKey, String.valueOf(likeCount-1));
        redisTemplate.expire(likeCountKey, Duration.ofHours(2));

        if(redisTemplate.opsForSet().isMember(createKey, userCode)){
            log.debug("Removing like request for post: {} by user: {}", postCode, userCode);
            redisTemplate.opsForSet().remove(createKey, userCode);
        } else {
            log.debug("Adding cancel request for post: {} by user: {}", postCode, userCode);
            redisTemplate.opsForSet().add(deleteKey, userCode);
        }

        log.debug("Like successfully cancelled for post: {} by user: {}", postCode, userCode);
        return LikeResponseDTO.builder()
            .likeCount(likeCount-1)
            .isLiked(false)
            .build();
    }

    @Scheduled(fixedRate = 3000000)
    public void syncLikeCountToDb() {
        log.debug("Starting likes synchronization to database");

        Set<String> keys = redisTemplate.keys(CREATE_LIKE_PREFIX + "*");
        if (keys != null) {
            log.debug("Found {} like requests to process", keys.size());

            for (String key : keys) {
                int postCode = Integer.parseInt(key.substring(CREATE_LIKE_PREFIX.length()));
                Set<String> userCodes = redisTemplate.opsForSet().members(key);

                if (userCodes != null) {
                    log.debug("Processing {} likes for post: {}", userCodes.size(), postCode);
                    for (String userCode : userCodes) {
                        PostLike postLike = PostLike.builder()
                            .id(new PostLikeId(userCode, postCode))
                            .build();
                        postLikeRepository.save(postLike);
                        log.debug("Saved like for post: {} by user: {}", postCode, userCode);
                    }
                }
                redisTemplate.delete(key);
                log.debug("Cleared like requests for post: {}", postCode);
            }
        }

        keys = redisTemplate.keys(DELETE_LIKE_PREFIX + "*");
        if (keys != null) {
            log.debug("Found {} unlike requests to process", keys.size());

            for (String key : keys) {
                int postCode = Integer.parseInt(key.substring(DELETE_LIKE_PREFIX.length()));
                Set<String> userCodes = redisTemplate.opsForSet().members(key);

                if (userCodes != null) {
                    log.debug("Processing {} unlikes for post: {}", userCodes.size(), postCode);
                    for (String userCode : userCodes) {
                        PostLike postLike = PostLike.builder()
                            .id(new PostLikeId(userCode, postCode))
                            .build();
                        postLikeRepository.delete(postLike);
                        log.debug("Deleted like for post: {} by user: {}", postCode, userCode);
                    }
                }
                redisTemplate.delete(key);
                log.debug("Cleared unlike requests for post: {}", postCode);
            }
        }

        log.debug("Completed likes synchronization to database");
    }
}