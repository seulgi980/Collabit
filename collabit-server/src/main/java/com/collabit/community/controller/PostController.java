package com.collabit.community.controller;

import com.collabit.community.domain.dto.CreatePostRequestDTO;
import com.collabit.community.domain.dto.CreatePostResponseDTO;
import com.collabit.community.domain.dto.GetPostResponseDTO;
import com.collabit.community.domain.dto.UpdatePostRequestDTO;
import com.collabit.community.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PostController", description = "게시판 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary="게시글 등록",description = "게시글을 등록하는 API입니다.")
    @PostMapping("/")
    public ResponseEntity<?> createPost(@ModelAttribute CreatePostRequestDTO requestDTO){
        String userCode = "1";
        CreatePostResponseDTO responseDTO = postService.createPost(userCode, requestDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @Operation(summary="게시글 목록 조회",description = "게시글 목록을 조회하는 API입니다.")
    @GetMapping("/")
    public ResponseEntity<?> getPostList(){
        String userCode = "1";
        List<GetPostResponseDTO> list = postService.getPostList(userCode);
        if (list.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.status(200).body(list);
    }

    @Operation(summary="게시글 상세 조회",description = "게시글을 상세 조회하는 API입니다.")
    @GetMapping("/{postCode}")
    public ResponseEntity<?> getPost(@PathVariable int postCode){
        String userCode = "1";
        GetPostResponseDTO responseDTO = postService.getPost(userCode, postCode);
        return ResponseEntity.status(200).body(responseDTO);
    }

    @Operation(summary="게시글 수정",description = "게시글을 수정하는 API입니다.")
    @PutMapping("/{postCode}")
    public ResponseEntity<?> updatePost(@PathVariable int postCode,@RequestBody UpdatePostRequestDTO requestDTO){
        String userCode = "1";
        GetPostResponseDTO responseDTO = postService.updatePost(userCode, postCode, requestDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @Operation(summary="게시글 삭제",description = "게시글을 삭제하는 API입니다.")
    @DeleteMapping("/{postCode}")
    public ResponseEntity<?> deletePost(@PathVariable int postCode){
        String userCode = "1";
        postService.deletePost(userCode, postCode);
        return ResponseEntity.status(204).build();
    }
}