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
    private final PostLikeRepository postLikeRepository;

    private final StringRedisTemplate redisTemplate;

    private static final String LIKE_COUNT_PREFIX = "likeCount:";
    private static final String LIKED_PREFIX = "liked:";
    private static final String DIR_NAME = "posts";

    @Transactional
    public CreatePostResponseDTO createPost(CreatePostRequestDTO request, String userCode) {
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
            List<Image> images = imageRepository.findByPost(post);

            String[] imageUrls = images.stream()
                .map(Image::getUrl)
                .toArray(String[]::new);

            GetPostResponseDTO responseDTO = buildDTO(post, imageUrls);

            list.add(responseDTO);

            // 캐시 관련
            getLikeCountFromCache(post.getCode());
            getIsLikedFromCache(userCode, post.getCode());
        }
        return list;
    }

    public GetPostResponseDTO getPost(int postCode) {
        Post post = postRepository.findByCode(postCode);
        List<Image> images = imageRepository.findByPost(post);
        String[] imageUrls = images.stream()
            .map(Image::getUrl)
            .toArray(String[]::new);

        GetPostResponseDTO responseDTO = buildDTO(post, imageUrls);

        // 캐시 관련
        getLikeCountFromCache(post.getCode());

        return responseDTO;
    }

    @Transactional
    public GetPostResponseDTO updatePost(int postCode, UpdatePostRequestDTO requestDTO) {
        Post post = postRepository.findByCode(postCode);
        post.setContent(requestDTO.getContent());
        Post savedPost = postRepository.save(post);

        String[] imageUrls = requestDTO.getImages();
        for (String url : imageUrls) {
            s3Service.delete(url);
            imageRepository.deleteByUrl(url);
        }

        List<Image> images = imageRepository.findByPost(savedPost);

        String[] updateImageUrls = images.stream()
            .map(Image::getUrl)
            .toArray(String[]::new);

        GetPostResponseDTO responseDTO = buildDTO(post,updateImageUrls);

        return responseDTO;
    }

    @Transactional
    public void deletePost(int postCode) {
        Post post = postRepository.findByCode(postCode);
        List<Image> images = imageRepository.findByPost(post);
        String[] imageUrls = images.stream()
            .map(Image::getUrl)
            .toArray(String[]::new);
        for (String url : imageUrls) {
            s3Service.delete(url);
        }
        postRepository.deleteByCode(postCode);
    }

    private void getLikeCountFromCache(int postCode) {
        String likeCountKey = LIKE_COUNT_PREFIX + postCode;
        String cachedLikeCount = redisTemplate.opsForValue().get(likeCountKey);

        if (cachedLikeCount == null) {
            // 캐시가 없으면 데이터베이스에서 조회하여 캐싱
            int likeCount = fetchLikeCountFromDB(postCode);
            redisTemplate.opsForValue().set(likeCountKey, String.valueOf(likeCount));
        }
    }

    private int fetchLikeCountFromDB(int postCode) {
        return postLikeRepository.countById_PostCode(postCode);
    }

    private void getIsLikedFromCache(String userCode, int postCode) {
        String likedKey = userCode + LIKED_PREFIX + postCode;
        String cachedLiked = redisTemplate.opsForValue().get(likedKey);

        if (cachedLiked == null) {
            // 캐시가 없으면 데이터베이스에서 조회하여 캐싱
            boolean liked = fetchIsLikedFromDB(userCode, postCode);
            redisTemplate.opsForValue().set(likedKey, String.valueOf(liked));
        }
    }

    private boolean fetchIsLikedFromDB(String userCode, int postCode) {
        return postLikeRepository.existsById_UserCodeAndId_PostCode(userCode, postCode);
    }


    public GetPostResponseDTO buildDTO (Post post, String[] imageUrls) {
        return GetPostResponseDTO.builder()
            .code(post.getCode())
            .userCode(post.getUserCode())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .images(imageUrls)
            .build();
    }
}