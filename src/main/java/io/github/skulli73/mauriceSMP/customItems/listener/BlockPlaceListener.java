package io.github.skulli73.mauriceSMP.customItems.listener;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        AbstractCustomItem customItem = MauriceSMP.getInstance().getItemManager().itemStackToCustomItem(item);
        if (customItem == null)
            return;
        customItem.onBlockPlaceEvent(event);
    }
}
