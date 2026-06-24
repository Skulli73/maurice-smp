package io.github.skulli73.mauriceSMP.skills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftItem(PlayerItemDamageEvent event) {
        event.setDamage(event.getDamage()*2);
    }
}
