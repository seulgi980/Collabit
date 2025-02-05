package com.collabit.community.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PostLikeId implements Serializable {

    private String userCode;
    private int postCode;

    public PostLikeId(String userCode, int postCode) {
        this.userCode = userCode;
        this.postCode = postCode;
    }
}