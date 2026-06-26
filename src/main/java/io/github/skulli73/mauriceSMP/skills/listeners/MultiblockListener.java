package io.github.skulli73.mauriceSMP.skills.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MultiblockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.CRAFTING_TABLE) {
            Block upperBlock = block.getRelative(BlockFace.UP);
            Block lowerBlock = block.getRelative(BlockFace.DOWN);
            if (upperBlock.getType() == Material.GRINDSTONE && lowerBlock.getType() == Material.DISPENSER) {
                Container inventoryHolder = (Container) lowerBlock.getState();
                Inventory inventory = inventoryHolder.getInventory();
                if (inventory.contains(Material.DIAMOND)) {
                    System.out.println("a");
                    if (inventory.containsAtLeast(new ItemStack(Material.DIAMOND), 10)) {
                        System.out.println("b");
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
