package com.collabit.project.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Description.java (복합키 클래스)
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DescriptionId {
    @Column(name = "code")
    private String code;

    @Column(name = "is_positive")
    private Boolean isPositive;
}
