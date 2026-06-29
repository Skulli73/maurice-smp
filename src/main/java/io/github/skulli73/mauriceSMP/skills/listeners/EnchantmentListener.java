package io.github.skulli73.mauriceSMP.skills.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;

public class EnchantmentListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onEnchantItem(EnchantItemEvent event) { //disable enchanting table
        Inventory inventory = event.getInventory();
        if (event.getEnchantBlock().getType() == Material.ENCHANTING_TABLE) {
            event.setCancelled(true);
            event.getEnchanter().sendMessage("§4Enchanting is disabled.§!");
            event.getEnchanter().playSound(event.getEnchanter(), Sound.ENTITY_VILLAGER_HURT, 1, 1);
        }

    }

}