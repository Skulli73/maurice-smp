package io.github.skulli73.mauriceSMP.skills;

import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsManager {
    private final FileConfiguration config;
    @Getter
    private final List<Skill> skills = new ArrayList<>();
    @Getter
    private final Map<String, List<SkillWithXP>> itemXpMap = new HashMap<>();

    public SkillsManager(FileConfiguration config) {
        this.config = config;
        importConfigs();
    }
    private void importConfigs() {
        for (SkillType skillType : SkillType.values()) {
            List<String> items = config.getStringList("skill_categories." + skillType.getId());
            List<String> enchantmentsString = config.getStringList("gainable_enchantments." + skillType.getId());
            List<Enchantment> enchantments = new ArrayList<>();
            for (String enchantment : enchantmentsString) {
                enchantments.add(getEnchantment(enchantment));
            }
            skills.add(new Skill(skillType, items, enchantments));
        }
        for (SkillType skillType : SkillType.values()) {
            ConfigurationSection gainableXPSection = config.getConfigurationSection("gainable_xp." + skillType.getId());
            if (gainableXPSection == null)
                continue;
            for (String key : gainableXPSection.getKeys(false)) {
                double value = gainableXPSection.getDouble(key);
                if (itemXpMap.containsKey(key)) {
                    itemXpMap.get(key).add(new SkillWithXP(skillType, value));
                } else {
                    ArrayList<SkillWithXP> list = new ArrayList<>();
                    list.add(new SkillWithXP(skillType, value));
                    itemXpMap.put(key, list);
                }
            }
        }
    }

    public void addXPForCrafting (Player player, String itemName) {
        if (!itemXpMap.containsKey(itemName))
            return;
        for (SkillWithXP skillWithXP : itemXpMap.get(itemName)) {
            SkillsManager.addXP(player, skillWithXP.skillType, skillWithXP.xp);
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
    public static Enchantment getEnchantment (String enchantment) {
        return switch (enchantment) {
            case "AQUA_AFFINITY" -> Enchantment.AQUA_AFFINITY;
            case "BLAST_PROTECTION" -> Enchantment.BLAST_PROTECTION;
            case "BANE_OF_AHTROPODS" -> Enchantment.BANE_OF_ARTHROPODS;
            case "BREACH" -> Enchantment.BREACH;
            case "BINDING_CURSE" -> Enchantment.BINDING_CURSE;
            case "CHANNELING" -> Enchantment.CHANNELING;
            case "DENSITY" -> Enchantment.DENSITY;
            case "DEPTH_STRIDER" -> Enchantment.DEPTH_STRIDER;
            case "EFFICIENCY" -> Enchantment.EFFICIENCY;
            case "FEATHER_FALLING" -> Enchantment.FEATHER_FALLING;
            case "FIRE_PROTECTION" -> Enchantment.FIRE_PROTECTION;
            case "FLAME" -> Enchantment.FLAME;
            case "FORTUNE" -> Enchantment.FORTUNE;
            case "FIRE_ASPECT" -> Enchantment.FIRE_ASPECT;
            case "FROST_WALKER" -> Enchantment.FROST_WALKER;
            case "IMPALING" -> Enchantment.IMPALING;
            case "INFINITY" -> Enchantment.INFINITY;
            case "KNOCKBACK" -> Enchantment.KNOCKBACK;
            case "LOOTING" -> Enchantment.LOOTING;
            case "LOYALTY" -> Enchantment.LOYALTY;
            case "LURE" -> Enchantment.LURE;
            case "LUNGE" -> Enchantment.LUNGE;
            case "LUCK_OF_THE_SEA" -> Enchantment.LUCK_OF_THE_SEA;
            case "MENDING" -> Enchantment.MENDING;
            case "MULTISHOT" -> Enchantment.MULTISHOT;
            case "PROTECTION" -> Enchantment.PROTECTION;
            case "PIERCING" -> Enchantment.PIERCING;
            case "POWER" -> Enchantment.POWER;
            case "PUNCH" -> Enchantment.PUNCH;
            case "PROJECTILE_PROTECTION" -> Enchantment.PROJECTILE_PROTECTION;
            case "QUICK_CHARGE" -> Enchantment.QUICK_CHARGE;
            case "RESPIRATION" -> Enchantment.RESPIRATION;
            case "RIPTIDE" -> Enchantment.RIPTIDE;
            case "SHARPNESS" -> Enchantment.SHARPNESS;
            case "SMITE" -> Enchantment.SMITE;
            case "SILK_TOUCH" -> Enchantment.SILK_TOUCH;
            case "SOUL_SPEED" -> Enchantment.SOUL_SPEED;
            case "SWEEPING_EDGE" -> Enchantment.SWEEPING_EDGE;
            case "SWIFT_SNEAK" -> Enchantment.SWIFT_SNEAK;
            case "THORNS" -> Enchantment.THORNS;
            case "UNBREAKING" -> Enchantment.UNBREAKING;
            case "VANISHING_CURSE" -> Enchantment.VANISHING_CURSE;
            case "WIND_BURST" -> Enchantment.WIND_BURST;
            default -> null;
        };
    }
    private class SkillWithXP {
        private SkillType skillType;
        private double xp;
        private SkillWithXP (SkillType skillType, double xp) {
            this.skillType = skillType;
            this.xp = xp;
        }
    }
}
