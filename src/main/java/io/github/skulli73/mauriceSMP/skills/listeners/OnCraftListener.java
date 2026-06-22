package io.github.skulli73.mauriceSMP.skills.listeners;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.Skill;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class OnCraftListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftItem(CraftItemEvent event) {
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        FunPlayer player;
        try {
            player = FunPlayer.get((Player) event.getWhoClicked());
        } catch (Exception e) {
            return;
        }
        if (player == null)
            return;
        ItemStack itemStack = event.getRecipe().getResult();
        for (Skill skill : skillsManager.getSkills()) {
            itemStack = skill.enchantItem(itemStack, player.getSkillData(skill.getSkillType()).getCurrentLevel());
        }
        event.setCurrentItem(itemStack);
    }
}
