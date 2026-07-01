package io.github.skulli73.mauriceSMP.skills;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class VillagerManager {
    private Map<Enchantment, SkillType> blockedEnchantments = new HashMap<>();
    public VillagerManager () {
        for (SkillType skillType : SkillType.values()) {
            for (String enchantmentString: MauriceSMP.getInstance().getConfig().getStringList("villager_enchantments_locking." + skillType.getId())) {
                Enchantment enchantment = SkillsManager.getEnchantment(enchantmentString);
                if (enchantment == null)
                    continue;
                blockedEnchantments.put(enchantment, skillType);
            }
        }
    }
    public @Nullable SkillWithNumber getRequiredLevel (Enchantment enchantment, int level) {
        if (!blockedEnchantments.containsKey(enchantment))
            return null;
        double percentageMaxEnchantment = (double) level /enchantment.getMaxLevel();
        return new SkillWithNumber(blockedEnchantments.get(enchantment), percentageMaxEnchantment*8);
    }
}
