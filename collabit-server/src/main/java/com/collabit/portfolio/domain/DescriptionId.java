package com.collabit.portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// Description entity 의 복합키 클래스
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DescriptionId implements Serializable {
    @Column(nullable = false, length = 255)
    private String code;

    @Column(nullable = false)
    private Boolean isPositive;
}
