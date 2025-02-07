package com.collabit.portfolio.domain.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class HighestLowestSkillDTO {
    private Skill highestSkill;
    private Skill lowestSkill;

    @AllArgsConstructor
    @Builder
    @ToString
    @NoArgsConstructor
    @Getter
    public static class Skill {
        private String name;
        private String description;
    }
}