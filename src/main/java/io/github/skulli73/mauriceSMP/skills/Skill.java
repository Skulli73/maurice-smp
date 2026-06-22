package io.github.skulli73.mauriceSMP.skills;

import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Skill {
    private List<String> items;
    @Getter
    private SkillType skillType;
    private List<Enchantment> enchantments;
    public Skill(SkillType skillType, List<String> items, List<Enchantment> enchantments) { // items affected by said skill
        this.skillType = skillType;
        this.items = items;
        this.enchantments = enchantments;
    }

    @Override
    public String toString() {
        return skillType.toString() + ": " + items.toString();
    }

    public ItemStack enchantItem (ItemStack item, int skillLevel) {
        if (skillLevel <= 1)
            return item;
        skillLevel = skillLevel - 1;
        if (items.contains(item.getType().name())) {
            for (Enchantment enchantment : enchantments) {
                int skillLevelRandom = skillLevel + (int)Math.floor(Math.random()*10-5);
                int enchantmentLevel = Math.round(skillLevelRandom * ((float) enchantment.getMaxLevel() /10));
                if (enchantmentLevel > 0) {
                    if (item.containsEnchantment(enchantment)) {
                        item.addUnsafeEnchantment(enchantment, item.getEnchantmentLevel(enchantment) + enchantmentLevel);
                    } else
                        item.addUnsafeEnchantment(enchantment, enchantmentLevel);
                }
            }
        }
        return item;
    }
}
