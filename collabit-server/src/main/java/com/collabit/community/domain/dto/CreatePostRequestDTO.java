package com.collabit.community.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatePostRequestDTO {
    String content;
    MultipartFile[] images;
}
