package com.collabit.community.service;

import com.collabit.community.domain.dto.CreateCommentRequestDTO;
import com.collabit.community.domain.dto.CreateCommentResponseDTO;
import com.collabit.community.domain.dto.GetCommentResponseDTO;
import com.collabit.community.domain.dto.UpdateCommentRequestDTO;
import com.collabit.community.domain.entity.Comment;
import com.collabit.community.domain.entity.Post;
import com.collabit.community.repository.CommentRepository;
import com.collabit.community.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CreateCommentResponseDTO createComment(CreateCommentRequestDTO requestDTO,
        int postCode,String userCode) {
        Comment comment = buildComment(requestDTO,postCode,userCode);
        Comment savedComment = commentRepository.save(comment);
        CreateCommentResponseDTO responseDTO = CreateCommentResponseDTO.builder().code(
            savedComment.getCode()).build();
        return responseDTO;
    }

    public List<GetCommentResponseDTO> getCommentList(int postCode) {
        List<Comment> commentList = commentRepository.findByPostCode(postCode);
        List<GetCommentResponseDTO> list = new ArrayList<>();

        for (Comment comment : commentList) {
            if(!comment.isDeleted()) list.add(buildDTO(comment));
        }
        return list;
    }

    public GetCommentResponseDTO updateComment(int commentCode, UpdateCommentRequestDTO requestDTO) {
        Comment comment = commentRepository.findByCode(commentCode);
        comment.setContent(requestDTO.getContent());
        Comment savedComment = commentRepository.save(comment);
        GetCommentResponseDTO responseDTO = buildDTO(savedComment);
        return responseDTO;
    }

    public void deletePost(int commentCode) {
        Comment comment = commentRepository.findByCode(commentCode);
        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    public Comment buildComment(CreateCommentRequestDTO requestDTO, int postCode,String userCode) {
        Post post = postRepository.findByCode(postCode);
        Comment comment = Comment.builder().content(requestDTO.getContent()).post(post).userCode(userCode).build();
        if(requestDTO.getParentCommentCode()!=null){
            Comment parentComment = commentRepository.findByCode(requestDTO.getParentCommentCode());
            comment.setParentComment(parentComment);
        }
        return comment;
    }

    public GetCommentResponseDTO buildDTO(Comment comment) {
        GetCommentResponseDTO responseDTO = GetCommentResponseDTO.builder()
            .code(comment.getCode())
            .postCode(comment.getPost().getCode())
            .userCode(comment.getUserCode())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .isDeleted(comment.isDeleted())
            .build();
        if(comment.getParentComment()!=null){
            responseDTO.setParentCommentCode(comment.getParentComment().getCode());
        }
        return responseDTO;
    }

}
