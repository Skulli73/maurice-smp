package io.github.skulli73.mauriceSMP.skills.listeners;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.Skill;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class OnCraftListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftItem(CraftItemEvent event) { //crafting table
        ItemStack itemStack = event.getRecipe().getResult();
        Player player = (Player) event.getWhoClicked();
        if (MauriceSMP.getInstance().getRecipeManager().isDisabled(event.getRecipe().getResult())) {
            event.getWhoClicked().sendMessage("§4This item is disabled.§!");
            event.setCancelled(true);
            player.playSound(player, Sound.ENTITY_VILLAGER_HURT, 1, 1);
            return;
        }

        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();

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

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftCrafter (CrafterCraftEvent event) {
        if (MauriceSMP.getInstance().getRecipeManager().isDisabled(event.getRecipe().getResult())) {
            event.setCancelled(true);
        }
    }

}
