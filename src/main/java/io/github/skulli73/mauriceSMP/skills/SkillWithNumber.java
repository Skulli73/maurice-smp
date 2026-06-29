package io.github.skulli73.mauriceSMP.skills;

import lombok.Getter;

public class SkillWithNumber {
    @Getter
    private final SkillType skillType;
    @Getter
    private final double xp;
    public SkillWithNumber(SkillType skillType, double xp) {
        this.skillType = skillType;
        this.xp = xp;
    }
}