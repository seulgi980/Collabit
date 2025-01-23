package com.collabit.community.controller;

import com.collabit.community.domain.dto.LikeResponseDTO;
import com.collabit.community.service.LikeService;
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

    @PostMapping("/")
    public ResponseEntity<?> likePost(@PathVariable("postCode")int postCode){
        // 유저 코드
        String userCode = "1";
        LikeResponseDTO responseDTO = LikeResponseDTO.builder()
            .likeCount(likeService.like(postCode, userCode))
            .build();
        return ResponseEntity.status(201).body(responseDTO);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> cancelLikePost(@PathVariable("postCode")int postCode){
        // 유저 코드
        String userCode = "1";
        LikeResponseDTO responseDTO = LikeResponseDTO.builder()
            .likeCount(likeService.cancelLike(postCode, userCode))
            .build();
        return ResponseEntity.status(204).body(responseDTO);
    }
}
