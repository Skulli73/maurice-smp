package io.github.skulli73.mauriceSMP.skills;

import java.util.List;

public class Skill {
    private List<String> items;
    private SkillType skillType;
    public Skill(SkillType skillType, List<String> items) { // items affected by said skill
        this.skillType = skillType;
        this.items = items;
    }

    @Override
    public String toString() {
        return skillType.toString() + ": " + items.toString();
    }
}
