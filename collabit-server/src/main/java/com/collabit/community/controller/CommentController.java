package com.collabit.community.controller;

import com.collabit.community.domain.dto.CreateCommentRequestDTO;
import com.collabit.community.domain.dto.CreateCommentResponseDTO;
import com.collabit.community.domain.dto.GetCommentResponseDTO;
import com.collabit.community.domain.dto.UpdateCommentRequestDTO;
import com.collabit.community.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CommentController", description = "댓글 API")
@RestController
@RequestMapping("/api/post/{postCode}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary="댓글 등록",description = "댓글을 등록하는 API입니다.")
    @PostMapping("/")
    public ResponseEntity<?> createComment(@PathVariable("postCode") int postCode,@RequestBody CreateCommentRequestDTO requestDTO) {
        CreateCommentResponseDTO responseDTO = commentService.createComment(requestDTO,postCode,"1");
        return ResponseEntity.status(201).body(responseDTO);
    }

    @Operation(summary="댓글 목록 조회",description = "댓글목록을 조회하는 API입니다.")
    @GetMapping("/")
    public ResponseEntity<?> getCommentList(@PathVariable("postCode") int postCode) {
        List<GetCommentResponseDTO> list = commentService.getCommentList(postCode);
        if (list.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.status(200).body(list);
    }

    @Operation(summary="댓글 수정",description = "댓글을 수정하는 API입니다.")
    @PutMapping("/{commentCode}")
    public ResponseEntity<?> getCommentList(@PathVariable("commentCode") int commentCode,@RequestBody UpdateCommentRequestDTO requestDTO) {
        // 유저 권한 확인
        String userCode = "1";
        GetCommentResponseDTO responseDTO = commentService.updateComment(userCode,commentCode,requestDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @Operation(summary="댓글 삭제",description = "댓글을 삭제하는 API입니다.")
    @DeleteMapping("/{commentCode}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentCode){
        // 유저 권한 확인
        String userCode = "1";
        commentService.deleteComment(userCode,commentCode);
        return ResponseEntity.status(204).build();
    }
}
