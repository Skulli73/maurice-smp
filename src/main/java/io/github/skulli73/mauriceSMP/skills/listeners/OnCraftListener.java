package io.github.skulli73.mauriceSMP.skills.listeners;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.Skill;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class OnCraftListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftItem(CraftItemEvent event) {
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        Player player = (Player) event.getWhoClicked();
        FunPlayer funPlayer;
        try {
            funPlayer = FunPlayer.get(player);
        } catch (Exception e) {
            return;
        }
        if (funPlayer == null)
            return;
        if (event.getCursor().getType() != Material.AIR)
            return;
        ItemStack itemStack = event.getRecipe().getResult();
        String itemName = itemStack.getType().name();
        //give xp
        if (skillsManager.getItemXpMap().containsKey(itemName)) {
            skillsManager.addXPForCrafting(player, itemName);
        }
        //enchanting the item
        for (Skill skill : skillsManager.getSkills()) {
            itemStack = skill.enchantItem(itemStack, funPlayer.getSkillData(skill.getSkillType()).getCurrentLevel());
        }
        event.setCurrentItem(itemStack);
    }
}
