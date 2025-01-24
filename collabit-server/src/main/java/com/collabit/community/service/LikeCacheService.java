package com.collabit.community.service;

import com.collabit.community.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeCacheService {

    private final PostLikeRepository postLikeRepository;

    @Cacheable(key = "#postCode", value = "likeCount",cacheManager = "cacheManager")
    public int getLikeCount(int postCode) {
        // 캐시에 데이터가 없을 때만 호출
        return postLikeRepository.countById_PostCode(postCode);
    }

    @Cacheable(key = "#userCode + 'likes' + #postCode", value = "isLiked", cacheManager = "cacheManager")
    public boolean getIsLiked(String userCode, int postCode) {
        // 캐시에 데이터가 없을 때만 호출
        return postLikeRepository.existsById_UserCodeAndId_PostCode(userCode, postCode);
    }
}
