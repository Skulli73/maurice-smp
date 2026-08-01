package io.github.skulli73.mauriceSMP.customItems.listener;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.BlockDataManager;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.Location;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.PlacedBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBreakBlockBlock (BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = new Location(block.getX(), block.getY(), block.getZ(), block.getWorld());
        BlockDataManager blockDataManager = MauriceSMP.getInstance().getBlockDataManager();
        if (blockDataManager.getPlacedBlocks().containsKey(location)) {
            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), blockDataManager.getPlacedBlocks().get(location).getItem().getItem());
        }
    }
}
