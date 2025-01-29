package com.collabit.community.controller;

import com.collabit.community.domain.dto.LikeResponseDTO;
import com.collabit.community.service.LikeService;
import com.collabit.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "LikeController", description = "게시글 좋아요 API")
@RestController
@RequestMapping("/api/post/{postCode}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary="게시글 좋아요",description = "게시글 좋아요 요청 API입니다.")
    @PostMapping("/")
    public ResponseEntity<?> likePost(@PathVariable("postCode")int postCode){
        String userCode = SecurityUtil.getCurrentUserCode();
        LikeResponseDTO responseDTO = likeService.like(userCode, postCode);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @Operation(summary="게시글 좋아요 취소",description = "게시글 좋아요 취소 API입니다.")
    @DeleteMapping("/")
    public ResponseEntity<?> cancelLikePost(@PathVariable("postCode")int postCode){
        String userCode = SecurityUtil.getCurrentUserCode();
        LikeResponseDTO responseDTO = likeService.cancelLike(userCode, postCode);
        return ResponseEntity.status(201).body(responseDTO);
    }
}
