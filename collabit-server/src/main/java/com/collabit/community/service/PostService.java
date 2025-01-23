package com.collabit.community.service;

import com.collabit.global.service.S3Service;
import com.collabit.community.domain.dto.CreatePostRequestDTO;
import com.collabit.community.domain.dto.CreatePostResponseDTO;
import com.collabit.community.domain.dto.GetPostResponseDTO;
import com.collabit.community.domain.dto.UpdatePostRequestDTO;
import com.collabit.community.domain.entity.Image;
import com.collabit.community.domain.entity.Post;
import com.collabit.community.repository.ImageRepository;
import com.collabit.community.repository.PostLikeRepository;
import com.collabit.community.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final PostCacheService postCacheService;

    private static final String DIR_NAME = "posts";

    @Transactional
    public CreatePostResponseDTO createPost(String userCode, CreatePostRequestDTO request) {
        Post post = Post.builder()
            .userCode(userCode)
            .content(request.getContent())
            .build();
        Post savedPost = postRepository.save(post);

        Arrays.stream(request.getImages())
            .map(file -> {
                String url = s3Service.upload(file, DIR_NAME); // S3에 업로드 후 URL 반환
                return Image.builder()
                    .url(url)
                    .post(savedPost)
                    .build(); // 반환된 URL을 이용해 Image 객체 생성
            })
            .forEach(imageRepository::save);

        CreatePostResponseDTO responseDTO = CreatePostResponseDTO.builder()
            .code(savedPost.getCode()).build();
        return responseDTO;
    }

    public List<GetPostResponseDTO> getPostList(String userCode) {
        List<GetPostResponseDTO> list = new ArrayList<>();

        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            GetPostResponseDTO responseDTO = buildDTO(post,userCode);
            list.add(responseDTO);
        }
        return list;
    }

    public GetPostResponseDTO getPost(String userCode, int postCode) {
        Post post = postRepository.findByCode(postCode);
        GetPostResponseDTO responseDTO = buildDTO(post,userCode);

        return responseDTO;
    }

    @Transactional
    public GetPostResponseDTO updatePost(String userCode, int postCode, UpdatePostRequestDTO requestDTO) {
        Post post = postRepository.findByCode(postCode);
        post.setContent(requestDTO.getContent());
        Post savedPost = postRepository.save(post);

        String[] imageUrls = requestDTO.getImages();
        for (String url : imageUrls) {
            s3Service.delete(url);
            imageRepository.deleteByUrl(url);
        }

        GetPostResponseDTO responseDTO = buildDTO(savedPost,userCode);

        return responseDTO;
    }

    @Transactional
    public void deletePost(int postCode) {
        Post post = postRepository.findByCode(postCode);
        // List<Image> images = imageRepository.findByPost(post);
        List<Image> images = post.getImages();
        String[] imageUrls = images.stream()
            .map(Image::getUrl)
            .toArray(String[]::new);
        for (String url : imageUrls) {
            s3Service.delete(url);
        }
        postRepository.deleteByCode(postCode);
    }

    public GetPostResponseDTO buildDTO (Post post, String userCode) {
        List<Image> images = post.getImages();
        String[] imageUrls = images.stream()
            .map(Image::getUrl)
            .toArray(String[]::new);
        int likeCount = postCacheService.getLikeCount((post.getCode()));
        boolean isLiked = postCacheService.getIsLiked(userCode, post.getCode());
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