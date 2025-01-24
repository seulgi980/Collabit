package com.collabit.community.service;

import com.collabit.community.domain.dto.LikeResponseDTO;
import com.collabit.community.domain.entity.PostLike;
import com.collabit.community.domain.entity.PostLikeId;
import com.collabit.community.exception.DuplicateLikeException;
import com.collabit.community.repository.PostLikeRepository;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final StringRedisTemplate redisTemplate;
    private final PostLikeRepository postLikeRepository;
    private final LikeCacheService likeCacheService;

    private static final String CREATE_LIKE_PREFIX = "like::";
    private static final String DELETE_LIKE_PREFIX = "cancel::";
    
    // 좋아요 추가 (Redis에서 증가시키기)
    public LikeResponseDTO like(String userCode, int postCode) {
        // 좋아요가 이미 눌러져있으면 예외 처리
        if(likeCacheService.getIsLiked(userCode, postCode)) throw new DuplicateLikeException();

        String isLikedKey = "isLiked::" + userCode + "likes" + postCode;
        String likeCountKey = "likeCount::" + postCode;
        String createKey = CREATE_LIKE_PREFIX + postCode;
        String deleteKey = DELETE_LIKE_PREFIX + postCode;

        // Redis에 좋아요 상태를 저장하고 TTL 설정
        redisTemplate.opsForValue().set(isLikedKey, String.valueOf(true));
        redisTemplate.expire(isLikedKey, Duration.ofHours(2));

        // 현재 좋아요 수를 가져오고 1을 더해서 Redis에 업데이트
        int likeCount = likeCacheService.getLikeCount(postCode);
        redisTemplate.opsForValue().set(likeCountKey, String.valueOf(likeCount+1));
        redisTemplate.expire(likeCountKey, Duration.ofHours(2));

        // Redis에 좋아요 취소 요청이 있는 경우 삭제, 없는 경우 좋아요 요청
        if(redisTemplate.opsForSet().isMember(deleteKey, userCode)){
            redisTemplate.opsForSet().remove(deleteKey, userCode);
        }
        else{
            redisTemplate.opsForSet().add(createKey, userCode);
        }

        // 갱신된 좋아요 수를 반환
        LikeResponseDTO responseDTO = LikeResponseDTO.builder()
            .likeCount(likeCount+1)
            .isLiked(true)
            .build();

        return responseDTO;
    }

    // 좋아요 취소 (Redis에서 감소시키기)
    public LikeResponseDTO cancelLike(String userCode, int postCode) {
        // 좋아요가 추가되어있지 않으면 예외 처리
        if(!likeCacheService.getIsLiked(userCode, postCode)) throw new DuplicateLikeException();
        
        String isLikedKey = "isLiked::" + userCode + "likes" + postCode;
        String likeCountKey = "likeCount::" + postCode;
        String createKey = CREATE_LIKE_PREFIX + postCode;
        String deleteKey = DELETE_LIKE_PREFIX + postCode;

        // Redis에 좋아요 상태를 저장하고 TTL 설정
        redisTemplate.opsForValue().set(isLikedKey, String.valueOf(false));
        redisTemplate.expire(isLikedKey, Duration.ofHours(2));

        // 현재 좋아요 수를 가져오고 1을 빼서 Redis에 업데이트
        int likeCount = likeCacheService.getLikeCount(postCode);
        redisTemplate.opsForValue().set(likeCountKey, String.valueOf(likeCount-1));
        redisTemplate.expire(likeCountKey, Duration.ofHours(2));

        // Redis에 좋아요 요청이 있는 경우 삭제, 없는 경우 좋아요 취소 요청
        if(redisTemplate.opsForSet().isMember(createKey, userCode)){
            redisTemplate.opsForSet().remove(createKey, userCode);
        }
        else{
            redisTemplate.opsForSet().add(deleteKey, userCode);
        }

        // 갱신된 좋아요 수를 반환
        LikeResponseDTO responseDTO = LikeResponseDTO.builder()
            .likeCount(likeCount-1)
            .isLiked(false)
            .build();

        return responseDTO;
    }

    @Scheduled(fixedRate = 3000000)  // 50분 마다 실행
    public void syncLikeCountToDb() {
        // Redis에서 모든 postCode에 대해 좋아요 요청을 가져온다
        Set<String> keys = redisTemplate.keys(CREATE_LIKE_PREFIX + "*");

        if (keys != null) {
            for (String key : keys) {
                int postCode = Integer.parseInt(key.substring(CREATE_LIKE_PREFIX.length()));
                Set<String> userCodes = redisTemplate.opsForSet().members(key);
                // DB에 저장
                if (userCodes != null) {
                    for (String userCode : userCodes) {
                        postLikeRepository.save(
                            PostLike.builder()
                                .id(new PostLikeId(userCode, postCode))
                                .build());
                    }
                }
                // 요청이 성공하면 cache에서 제거
                redisTemplate.delete(key);
            }
        }
        // Redis에서 모든 postCode에 대해 좋아요 취소 요청을 가져온다
        keys = redisTemplate.keys(DELETE_LIKE_PREFIX + "*");

        if (keys != null) {
            for (String key : keys) {
                int postCode = Integer.parseInt(key.substring(DELETE_LIKE_PREFIX.length()));
                Set<String> userCodes = redisTemplate.opsForSet().members(key);
                // DB에 저장
                if (userCodes != null) {
                    for (String userCode : userCodes) {
                        postLikeRepository.delete(
                            PostLike.builder()
                                .id(new PostLikeId(userCode, postCode))
                                .build());
                    }
                }
                // 요청이 성공하면 cache에서 삭제
                redisTemplate.delete(key);
            }
        }
    }
}
