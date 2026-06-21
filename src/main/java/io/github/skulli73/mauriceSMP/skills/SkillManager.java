package io.github.skulli73.mauriceSMP.skills;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillManager {
    FileConfiguration config;
    @Getter
    List<Skill> skills = new ArrayList<>();

    public SkillManager(FileConfiguration config) {
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
}
