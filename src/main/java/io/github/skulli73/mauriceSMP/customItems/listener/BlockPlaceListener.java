package io.github.skulli73.mauriceSMP.customItems.listener;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.BlockDataManager;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.Location;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.PlacedBlock;
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
        if (event.isCancelled())
            return;
        BlockDataManager blockDataManager = MauriceSMP.getInstance().getBlockDataManager();
        Location location = new Location(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ(), event.getBlock().getWorld());
        PlacedBlock placedBlock = new PlacedBlock(customItem, location);
        blockDataManager.addPlacedBlock(placedBlock);
    }
}
