package io.github.skulli73.mauriceSMP.customItems.listener;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class OnBlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakBlock (BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        AbstractCustomItem customItem = MauriceSMP.getInstance().getItemManager().itemStackToCustomItem(item);
        if (customItem == null)
            return;
        customItem.onBlockBreakEvent(event);
    }
}
