package com.collabit.project.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "description")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Description {

    @EmbeddedId
    private DescriptionId id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;
}
