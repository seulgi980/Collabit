package com.collabit.portfolio.domain.entity;

import com.collabit.portfolio.domain.DescriptionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Description {
    // 복합키
    @EmbeddedId
    private DescriptionId id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;
}
