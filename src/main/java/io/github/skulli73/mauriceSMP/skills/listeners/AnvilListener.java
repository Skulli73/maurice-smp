package io.github.skulli73.mauriceSMP.skills.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

public class AnvilListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAnvilItem(PrepareAnvilEvent event) {//disable upgrading enchantments with anvil
        AnvilInventory anvilInventory = event.getInventory();
        if (anvilInventory.getItem(1) != null)
            if (!anvilInventory.getItem(1).getEnchantments().isEmpty() && (!(anvilInventory.getItem(1).getType() == Material.ENCHANTED_BOOK)) ) {
                    event.setResult(anvilInventory.getItem(0));
                }
    }
}
