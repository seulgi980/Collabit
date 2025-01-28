package com.collabit.community.service;

import com.collabit.community.domain.dto.CreateCommentRequestDTO;
import com.collabit.community.domain.dto.CreateCommentResponseDTO;
import com.collabit.community.domain.dto.GetCommentResponseDTO;
import com.collabit.community.domain.dto.UpdateCommentRequestDTO;
import com.collabit.community.domain.entity.Comment;
import com.collabit.community.domain.entity.Post;
import com.collabit.community.exception.CommentNotFoundException;
import com.collabit.community.exception.ParentCommentNotFoundException;
import com.collabit.community.exception.PostNotFoundException;
import com.collabit.community.repository.CommentRepository;
import com.collabit.community.repository.PostRepository;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserDiffrentException;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public CreateCommentResponseDTO createComment(CreateCommentRequestDTO requestDTO,
        int postCode,String userCode) {
        // requestDTO와 postCode, userCode로 comment 생성
        Comment comment = buildComment(requestDTO,postCode,userCode);
        Comment savedComment = commentRepository.save(comment);
        // savedComment로 responseDTO 생성
        CreateCommentResponseDTO responseDTO = CreateCommentResponseDTO.builder().code(
            savedComment.getCode()).build();
        return responseDTO;
    }

    public List<GetCommentResponseDTO> getCommentList(int postCode) {
        List<GetCommentResponseDTO> list = new ArrayList<>();
        // postCode에 해당하는 commentList 조회
        List<Comment> commentList = commentRepository.findByPostCode(postCode);
        
        for (Comment comment : commentList) {
            // 삭제되지 않은 comment들만 responseDTO로 만들어서 list에 추가
            if(!comment.isDeleted()) list.add(buildDTO(comment));
        }
        return list;
    }

    public GetCommentResponseDTO updateComment(String userCode, int commentCode, UpdateCommentRequestDTO requestDTO) {
        // commentCode에 해당하는 comment 조회
        Comment comment = commentRepository.findByCode(commentCode)
            .orElseThrow(() -> new CommentNotFoundException());
        // 가지고 온 comment의 userCode와 요청 userCode가 일치하지 않으면 예외 처리
        if(!comment.getUser().getCode().equals(userCode)) throw new UserDiffrentException();
        // comment의 내용 수정
        comment.setContent(requestDTO.getContent());
        // DB 저장
        Comment updatedComment = commentRepository.save(comment);
        // updatedComment로 responseDTO 생성
        GetCommentResponseDTO responseDTO = buildDTO(updatedComment);
        return responseDTO;
    }

    public void deleteComment(String userCode, int commentCode) {
        // commentCode에 해당하는 comment 조회
        Comment comment = commentRepository.findByCode(commentCode)
            .orElseThrow(() -> new CommentNotFoundException());
        // 가지고 온 comment의 userCode와 요청 userCode가 일치하지 않으면 예외 처리
        if(!comment.getUser().getCode().equals(userCode)) throw new UserDiffrentException();
        // comment 삭제
        comment.setDeleted(true);
        // DB 저장
        commentRepository.save(comment);
    }
    
    // Comment 생성 메서드
    public Comment buildComment(CreateCommentRequestDTO requestDTO, int postCode,String userCode) {
        User user = userRepository.findByCode(userCode)
            .orElseThrow(() -> new UserNotFoundException());
        Post post = postRepository.findByCode(postCode)
            .orElseThrow(() -> new PostNotFoundException());
        Comment comment = Comment.builder().content(requestDTO.getContent()).post(post).user(user).build();
        if(requestDTO.getParentCommentCode()!=null){
            Comment parentComment = commentRepository.findByCode(requestDTO.getParentCommentCode())
                .orElseThrow(() -> new ParentCommentNotFoundException());
            comment.setParentComment(parentComment);
        }
        return comment;
    }

    // responseDTO 생성 메서드
    public GetCommentResponseDTO buildDTO(Comment comment) {
        GetCommentResponseDTO responseDTO = GetCommentResponseDTO.builder()
            .code(comment.getCode())
            .postCode(comment.getPost().getCode())
            .userNickname(comment.getUser().getNickname())
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
