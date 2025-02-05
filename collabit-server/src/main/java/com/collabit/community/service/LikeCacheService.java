package com.collabit.community.service;

import com.collabit.community.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeCacheService {

    private final PostLikeRepository postLikeRepository;

    @Cacheable(key = "#postCode", value = "likeCount", cacheManager = "cacheManager")
    public int getLikeCount(int postCode) {
        log.debug("Cache miss - Fetching like count from DB for post: {}", postCode);
        int count = postLikeRepository.countById_PostCode(postCode);
        log.debug("Found {} likes for post: {}", count, postCode);
        return count;
    }

    @Cacheable(key = "#userCode + 'likes' + #postCode", value = "isLiked", cacheManager = "cacheManager")
    public boolean getIsLiked(String userCode, int postCode) {
        log.debug("Cache miss - Checking like status in DB for post: {} and user: {}", postCode, userCode);
        boolean isLiked = postLikeRepository.existsById_UserCodeAndId_PostCode(userCode, postCode);
        log.debug("Like status for post: {} by user: {} is: {}", postCode, userCode, isLiked);
        return isLiked;
    }
}