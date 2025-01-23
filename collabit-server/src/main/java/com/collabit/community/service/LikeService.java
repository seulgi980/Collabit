package com.collabit.community.service;

import com.collabit.community.domain.entity.PostLike;
import com.collabit.community.domain.entity.PostLikeId;
import com.collabit.community.repository.PostLikeRepository;
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

    private static final String LIKE_COUNT_PREFIX = "likeCount:";
    private static final String LIKE_USERS_PREFIX = "likeUsers:";  // 좋아요 누른 사용자 정보

    // 좋아요 수 증가 (Redis에서 증가시키기)
    public int like(int postCode, String userId) {
        String likeCountKey = LIKE_COUNT_PREFIX + postCode;
        String likeUsersKey = LIKE_USERS_PREFIX + postCode;

        // Redis에서 likeCount 키를 1 증가시킨다
        redisTemplate.opsForValue().increment(likeCountKey, 1);

        // Redis에 사용자 정보 추가 (Set에 userId 추가)
        redisTemplate.opsForSet().add(likeUsersKey, userId);

        return Integer.parseInt(redisTemplate.opsForValue().get(likeCountKey));
    }

    // 좋아요 수 감소 (Redis에서 감소시키기)
    public int cancelLike(int postCode, String userId) {
        String likeCountKey = LIKE_COUNT_PREFIX + postCode;
        String likeUsersKey = LIKE_USERS_PREFIX + postCode;

        // Redis에서 likeCount 키를 1 감소시킨다
        redisTemplate.opsForValue().increment(likeCountKey, -1);

        // Redis에서 사용자 정보 삭제 (Set에서 userId 삭제)
        redisTemplate.opsForSet().remove(likeUsersKey, userId);

        return Integer.parseInt(redisTemplate.opsForValue().get(likeCountKey));
    }

    @Scheduled(fixedRate = 7200000)  // 2시간 마다 실행
    public void syncLikeCountToDb() {
        // Redis에서 모든 postCode에 대해 좋아요 수를 가져온다
        Set<String> keys = redisTemplate.keys(LIKE_USERS_PREFIX + "*");

        if (keys != null) {
            for (String key : keys) {
                int postCode = Integer.parseInt(key.substring(LIKE_USERS_PREFIX.length()));
                Set<String> userCodes = redisTemplate.opsForSet().members(key);
                if (userCodes != null) {
                    for (String userCode : userCodes) {
                        postLikeRepository.save(
                            PostLike.builder()
                                .id(new PostLikeId(userCode,postCode))
                                .build());
                    }
                }
                redisTemplate.delete(key);
            }
        }
    }
}
