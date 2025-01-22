package com.collabit.community.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class PostLikeId implements Serializable {

    private String userCode;
    private int postCode;

    public PostLikeId(String userCode, int postCode) {
        this.userCode = userCode;
        this.postCode = postCode;
    }
}