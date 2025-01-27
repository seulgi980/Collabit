package com.collabit.community.service;

import com.collabit.community.exception.PostNotFoundException;
import com.collabit.global.service.S3Service;
import com.collabit.community.domain.dto.CreatePostRequestDTO;
import com.collabit.community.domain.dto.CreatePostResponseDTO;
import com.collabit.community.domain.dto.GetPostResponseDTO;
import com.collabit.community.domain.dto.UpdatePostRequestDTO;
import com.collabit.community.domain.entity.Image;
import com.collabit.community.domain.entity.Post;
import com.collabit.community.repository.ImageRepository;
import com.collabit.community.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final LikeCacheService likeCacheService;

    private static final String DIR_NAME = "posts";

    @Transactional
    public CreatePostResponseDTO createPost(String userCode, CreatePostRequestDTO request) {
        // RequestDTO로 post 생성
        Post post = Post.builder()
            .userCode(userCode)
            .content(request.getContent())
            .build();
        Post savedPost = postRepository.save(post);
        
        // Image가 있으면 image 업로드 및 DB저장
        if(request.getImages() != null) {
            Arrays.stream(request.getImages())
                .map(file -> {
                    String url = s3Service.upload(file, DIR_NAME); // S3에 업로드 후 URL 반환
                    return Image.builder()
                        .url(url)
                        .post(savedPost)
                        .build(); // 반환된 URL을 이용해 Image 객체 생성
                })
                .forEach(imageRepository::save);
        }

        // savedPost로 responseDTO 생성 후 반환
        CreatePostResponseDTO responseDTO = CreatePostResponseDTO.builder()
            .code(savedPost.getCode()).build();
        return responseDTO;
    }

    public List<GetPostResponseDTO> getPostList(String userCode) {
        // 게시글 목록을 담을 List
        List<GetPostResponseDTO> list = new ArrayList<>();
        
        // 전체 Post 조회
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            // post들로 responseDTO 생성
            GetPostResponseDTO responseDTO = buildDTO(post,userCode);
            // list에 추가
            list.add(responseDTO);
        }
        // list 반환
        return list;
    }

    public GetPostResponseDTO getPost(String userCode, int postCode) {
        // postCode에 해당하는 post 조회
        Post post = postRepository.findByCode(postCode);
        // 해당 post가 없으면 예외 처리
        if(post==null) throw new PostNotFoundException();
        // responseDTO 생성
        GetPostResponseDTO responseDTO = buildDTO(post,userCode);
        // 반환
        return responseDTO;
    }

    @Transactional
    public GetPostResponseDTO updatePost(String userCode, int postCode, UpdatePostRequestDTO requestDTO) {
        // postCode에 해당하는 post 조회
        Post post = postRepository.findByCode(postCode);
        // 해당 post가 없으면 예외 처리
        if(post==null) throw new PostNotFoundException();
        // 가지고 온 post의 userCode와 요청 userCode가 일치하지 않으면 예외 처리
        if(!post.getUserCode().equals(userCode)) throw new RuntimeException();
        // Post 내용 업데이트
        post.setContent(requestDTO.getContent());
        // 삭제할 이미지 URL 배열
        String[] imageUrls = requestDTO.getImages();
        // Post에 연결된 이미지 리스트 가져오기
        List<Image> postImages = post.getImages();
        // 삭제할 URL에 해당하는 이미지 삭제
        postImages.removeIf(image -> {
            boolean shouldRemove = Arrays.asList(imageUrls).contains(image.getUrl());
            if (shouldRemove) {
                // S3에서 이미지 삭제
                s3Service.delete(image.getUrl());
            }
            // 삭제 조건에 해당하면 리스트에서 제거
            return shouldRemove;
        });
        
        // 바뀐 Post update
        Post updatedPost = postRepository.save(post);
        
        // updatedPost와 userCode로 responseDTO 생성
        GetPostResponseDTO responseDTO = buildDTO(updatedPost,userCode);

        return responseDTO;
    }

    @Transactional
    public void deletePost(String userCode, int postCode) {
        // postCode에 해당하는 post 조회
        Post post = postRepository.findByCode(postCode);
        // 해당 post가 없으면 예외 처리
        if(post==null) throw new PostNotFoundException();
        // 가지고 온 post의 userCode와 요청 userCode가 일치하지 않으면 예외 처리
        if(!post.getUserCode().equals(userCode)) throw new RuntimeException();
        // Post에 연결된 이미지 리스트 가져오기
        List<Image> images = post.getImages();
        // 삭제할 URL에 해당하는 이미지 삭제
        String[] imageUrls = images.stream()
            .map(Image::getUrl)
            .toArray(String[]::new);
        for (String url : imageUrls) {
            s3Service.delete(url);
        }
        // 해당 post 삭제
        postRepository.deleteByCode(postCode);
    }
    
    // Post와 userCode로 responseDTO 만드는 메서드
    public GetPostResponseDTO buildDTO (Post post, String userCode) {
        // Post에 연결된 이미지 리스트 가져오기
        List<Image> images = post.getImages();
        // 이미지에서 url 가져오기
        String[] imageUrls = images.stream()
            .map(Image::getUrl)
            .toArray(String[]::new);
        // likeCount를 Look Aside 전략으로 조회
        int likeCount = likeCacheService.getLikeCount((post.getCode()));
        // isLiked를 Look Aside 전략으로 조회
        boolean isLiked = likeCacheService.getIsLiked(userCode, post.getCode());
        // responseDTO를 생성 후 반환
        return GetPostResponseDTO.builder()
            .code(post.getCode())
            .userCode(post.getUserCode())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .images(imageUrls)
            .likeCount(likeCount)
            .isLiked(isLiked)
            .build();
    }
}