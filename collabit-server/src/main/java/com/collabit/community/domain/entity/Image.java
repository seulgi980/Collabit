package com.collabit.community.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_code")
    private Post post;
}