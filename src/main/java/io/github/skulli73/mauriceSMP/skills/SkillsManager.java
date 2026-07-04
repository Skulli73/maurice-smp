package io.github.skulli73.mauriceSMP.skills;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsManager {
    private final FileConfiguration config;
    @Getter
    private final List<Skill> skills = new ArrayList<>();
    @Getter
    private final Map<String, List<SkillWithNumber>> itemXpMap = new HashMap<>();
    private final Map<String, List<EnchantmentWithSkill>> bonusEnchantmentMap = new HashMap<>();
    private final NamespacedKey bonusEnchantedKey;
    private final NamespacedKey loggingEnchantmentKey;
    private final NamespacedKey veinMiningEnchantmentKey;
    private List<String> veinMinerItems;
    private List<String> lumberaxeItems;
    @Getter
    private final Map<String, SkillWithNumber> blockMiningXp = new HashMap<>();

    public SkillsManager() {
        this.config = MauriceSMP.getInstance().getConfig();
        importConfigs();
        bonusEnchantedKey = new NamespacedKey(MauriceSMP.getInstance(), "bonus-enchanted");
        loggingEnchantmentKey = new NamespacedKey(MauriceSMP.getInstance(), "logging-enchantment");
        veinMiningEnchantmentKey = new NamespacedKey(MauriceSMP.getInstance(), "veinmining-enchantment");


    }
    private void importConfigs() {
        veinMinerItems = config.getStringList("vein_miner_items");
        lumberaxeItems = config.getStringList("lumber_axe_items");
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
                    itemXpMap.get(key).add(new SkillWithNumber(skillType, value));
                } else {
                    ArrayList<SkillWithNumber> list = new ArrayList<>();
                    list.add(new SkillWithNumber(skillType, value));
                    itemXpMap.put(key, list);
                }
            }
        }
        for (SkillType skillType : SkillType.values()) {
            ConfigurationSection gainableXPSection = config.getConfigurationSection("bonus_enchantments." + skillType.getId());
            if (gainableXPSection == null)
                continue;
            for (String key : gainableXPSection.getKeys(false)) {
                List<EnchantmentWithSkill> enchantmentWithSkills = new ArrayList<>();
                for (String enchantmentString : gainableXPSection.getStringList(key)) {
                    Enchantment enchantment = getEnchantment(enchantmentString);
                    if (enchantment == null)
                        continue;
                    enchantmentWithSkills.add(new EnchantmentWithSkill(enchantment, skillType));
                }
                bonusEnchantmentMap.put(key, enchantmentWithSkills);
            }
        }

        for (SkillType skillType : SkillType.values()) {
            ConfigurationSection blockMiningXpSection = config.getConfigurationSection("block_mining_xp." + skillType.getId());
            if (blockMiningXpSection == null)
                continue;
            for (String key: blockMiningXpSection.getKeys(false)) {
                double xp = blockMiningXpSection.getDouble(key);
                blockMiningXp.put(key, new SkillWithNumber(skillType, xp));
            }
        }
    }

    public void addXPForCrafting (Player player, String itemName) {
        if (!itemXpMap.containsKey(itemName))
            return;
        for (SkillWithNumber skillWithXP : itemXpMap.get(itemName)) {
            SkillsManager.addXP(player, skillWithXP.getSkillType(), skillWithXP.getNumber());
        }
    }
    public void addXPForCrafting (Player player, String itemName, double multiplier) {
        if (!itemXpMap.containsKey(itemName))
            return;
        multiplier = (double) ((int) (multiplier*1000))/1000;
        for (SkillWithNumber skillWithXP : itemXpMap.get(itemName)) {
            SkillsManager.addXP(player, skillWithXP.getSkillType(), skillWithXP.getNumber()*multiplier);
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


        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6You have gained " + xp + " XP in " + skill.getName() + "!"));
        if (newLevel > currentLevel) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§b" + (skill.getName()) + " has been increased to Level " + newLevel + "!"));
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

    public boolean hasBonusEnchantments (String item) {
        return bonusEnchantmentMap.containsKey(item);
    }

    public boolean hasBonusEnchantments (ItemStack item) {
        if (item == null || (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(bonusEnchantedKey)))
            return false;
        return hasBonusEnchantments(item.getType().name());
    }

    public ItemStack enchantItemBonus (ItemStack item, double bonusSkill, Player player) {
        if (hasBonusEnchantments(item)) {
            for (EnchantmentWithSkill enchantmentWithSkill : bonusEnchantmentMap.get(item.getType().name())) {
                Enchantment enchantment = enchantmentWithSkill.enchantment;
                if (enchantment == null)
                    continue;
                double skillLevel = SkillsManager.getLevel(player, enchantmentWithSkill.skillType) + bonusSkill;
                int skillLevelRandom = (int) Math.floor(skillLevel + Math.random() * 10 - 5);
                int enchantmentLevel = Math.round(skillLevelRandom * ((float) enchantment.getMaxLevel() / 13));
                if (enchantmentLevel > 0) {
                    if (item.containsEnchantment(enchantment)) {
                        int currentLevel = item.getEnchantmentLevel(enchantment);
                        int min = Math.min(currentLevel, enchantmentLevel);
                        int max = Math.max(currentLevel, enchantmentLevel);
                        if (min > 1)
                            item.addUnsafeEnchantment(enchantment, max + (int) Math.floor((double) min / 2));
                    } else
                        item.addUnsafeEnchantment(enchantment, enchantmentLevel);
                }
            }
            ItemMeta meta = item.getItemMeta();
            String materialName = item.getType().name();
            if (lumberaxeItems.contains(materialName) && meta != null) {
                double skillLevel = SkillsManager.getLevel(player, SkillType.AXE);
                int skillLevelRandom = (int) Math.floor(skillLevel + Math.random() * 10 - 5);
                if (skillLevelRandom >= 10) {
                    List<String> lore = meta.getLore();
                    if (lore == null)
                        lore = new ArrayList<>();
                    lore.add("§7Logging§!");
                    meta.setLore(lore);
                    meta.getPersistentDataContainer().set(loggingEnchantmentKey, PersistentDataType.BOOLEAN, true);
                }
            }
            if (veinMinerItems.contains(materialName) && meta != null) {
                double skillLevel = SkillsManager.getLevel(player, SkillType.PICKAXE);
                int skillLevelRandom = (int) Math.floor(skillLevel + Math.random() * 10 - 5);
                if (skillLevelRandom >= 10) {
                    List<String> lore = meta.getLore();
                    if (lore == null)
                        lore = new ArrayList<>();
                    lore.add("§7Vein Miner§!");
                    meta.setLore(lore);
                    meta.getPersistentDataContainer().set(veinMiningEnchantmentKey, PersistentDataType.BOOLEAN, true);
                }
            }



            if (meta != null) {
                meta.getPersistentDataContainer().set(bonusEnchantedKey, PersistentDataType.BOOLEAN, true);
            }
            item.setItemMeta(meta);
            addXPForCrafting(player, materialName, bonusSkill);
        }
        return item;
    }
    public boolean isLumberAxe (ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        return persistentDataContainer.has(loggingEnchantmentKey) && Boolean.TRUE.equals(persistentDataContainer.get(loggingEnchantmentKey, PersistentDataType.BOOLEAN));
    }
    public boolean isVeinMiner (ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        return persistentDataContainer.has(veinMiningEnchantmentKey) && Boolean.TRUE.equals(persistentDataContainer.get(veinMiningEnchantmentKey, PersistentDataType.BOOLEAN));
    }


    private static class EnchantmentWithSkill {
        private final SkillType skillType;
        private final Enchantment enchantment;
        private EnchantmentWithSkill (Enchantment enchantment, SkillType skillType) {
            this.skillType = skillType;
            this.enchantment = enchantment;
        }
    }
}
