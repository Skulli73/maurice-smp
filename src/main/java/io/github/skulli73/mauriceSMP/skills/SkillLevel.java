package io.github.skulli73.mauriceSMP.skills;

import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

public class SkillLevel { //this is used to check if a player has the needed level for crafting something
    private final @Getter SkillType skillType;
    private final @Getter int level;
    public SkillLevel(SkillType skillType, int level) {
        this.skillType = skillType;
        this.level = level;
    }
    public boolean playerHasLevel(Player player) {
        FunPlayer funPlayer = FunPlayer.get(player);
        return funPlayer != null && funPlayer.getSkillData(skillType).getCurrentLevel() >= level;
    }
}
