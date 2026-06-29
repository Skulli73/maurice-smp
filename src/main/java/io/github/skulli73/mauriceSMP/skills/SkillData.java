package io.github.skulli73.mauriceSMP.skills;

import lombok.Getter;
import lombok.Setter;

public class SkillData {

    private final @Getter SkillType type;
    private @Setter @Getter double totalXp;

    public SkillData(SkillType type, double totalXp) {
        this.type = type;
        this.totalXp = totalXp;
    }

    public int getCurrentLevel() {
        return SkillUtils.xpToLevel(totalXp);
    }

}
