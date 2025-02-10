package com.collabit.portfolio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 한글이름
    @Column(nullable = false, length = 255)
    private String name;

    // 영어이름
    @Column(nullable = false, length = 255)
    private String code;

    @Column(nullable = false, length = 1000)
    private String feedback;

    @Column(nullable = false)
    private Boolean isPositive;

}
