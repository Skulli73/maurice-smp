package io.github.skulli73.mauriceSMP.skills;

import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsManager {
    FileConfiguration config;
    @Getter
    List<Skill> skills = new ArrayList<>();

    public SkillsManager(FileConfiguration config) {
        this.config = config;
        importConfigs();
    }
    private void importConfigs() {
        for (SkillType skillType : SkillType.values()) {
            List<String> items = config.getStringList("gainable_enchants." + skillType.getId());
            skills.add(new Skill(skillType, items));
        }
    }

    public Map<SkillType, SkillData> getEmptySkillDataMap() {
        Map<SkillType, SkillData> result = new HashMap<>();
        for (SkillType skillType : SkillType.values()) {
            result.put(skillType, new SkillData(skillType, 0));
        }
        return result;
    }

    public SkillType getSkillTypeFromString(String str) {
        for (SkillType skillType : SkillType.values()) {
            if (skillType.name().equalsIgnoreCase(str))
                return skillType;
        }
        return null;
    }

    public static void addXP(Player p, SkillType skill, double xp) {
        FunPlayer funPlayer = FunPlayer.get(p);
        if (funPlayer == null) {
            funPlayer = new FunPlayer(p.getUniqueId());
        }
        SkillData data = funPlayer.getSkillData(skill);


        int currentLevel = data.getCurrentLevel();
        double currentXp = data.getTotalXp();

        data.setTotalXp(currentXp + xp);

        int newLevel = data.getCurrentLevel();

        p.sendMessage("§6You have gained " + xp + " XP in " + skill.getName() + "!");
        if (newLevel > currentLevel) {
            p.sendMessage("§b" + (skill.getName()) + " has been increased to Level " + newLevel + "!");
            p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }

        funPlayer.save();
    }


    public static double getLevel(Player p, SkillType skill) {
        FunPlayer funPlayer = FunPlayer.get(p);
        if (funPlayer == null)
            return 0;
        return funPlayer.getSkillData(skill).getCurrentLevel();
    }
}
