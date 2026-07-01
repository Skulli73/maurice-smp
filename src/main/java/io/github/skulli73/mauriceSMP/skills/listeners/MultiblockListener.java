package io.github.skulli73.mauriceSMP.skills.listeners;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.Dispenser;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        if (block != null && block.getType() == Material.CRAFTING_TABLE && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block upperBlock = block.getRelative(BlockFace.UP);
            Block lowerBlock = block.getRelative(BlockFace.DOWN);
            Player player = event.getPlayer();
            SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
            if (upperBlock.getType() == Material.GRINDSTONE && lowerBlock.getType() == Material.DISPENSER) {
                Container inventoryHolder = (Container) lowerBlock.getState();
                Inventory inventory = inventoryHolder.getInventory();
                ItemStack toEnchant = null;
                for (ItemStack item : inventory.getContents()) {
                    if (skillsManager.hasBonusEnchantments(item)) {
                        toEnchant = item;
                        break;
                    }
                }
                if (toEnchant == null) {
                    player.sendMessage("§4Please put an item with possible bonus enchantments into the machine.§!");
                    event.setCancelled(true);
                    player.playSound(player, Sound.ENTITY_VILLAGER_HURT, 1, 1);
                    return;
                }
                int amountDiamonds = 0;
                if (inventory.contains(Material.DIAMOND)) {

                    if (inventory.containsAtLeast(new ItemStack(Material.DIAMOND), 11)) {
                        for (ItemStack item : inventory.getContents()) {
                            if (item == null)
                                continue;
                            if (item.getType() == Material.DIAMOND) {
                                if (amountDiamonds + item.getAmount() == 10) {
                                    amountDiamonds = 9;
                                    inventory.removeItem(item);
                                    break;
                                }
                                if (amountDiamonds + item.getAmount() > 10) {
                                    item.setAmount(item.getAmount()-(10-amountDiamonds));
                                    amountDiamonds = 10;
                                    break;
                                }
                                amountDiamonds += item.getAmount();
                                inventory.removeItem(item);
                            }
                        }
                    } else {
                        for (ItemStack item : inventory.getContents()) {
                            if (item == null)
                                continue;
                            if (item.getType() == Material.DIAMOND) {
                                amountDiamonds += item.getAmount();
                                inventory.removeItem(item);
                            }
                        }
                    }
                    skillsManager.enchantItemBonus(toEnchant, Math.log10(amountDiamonds)*5, player);
                    player.playSound(player, Sound.BLOCK_CRAFTER_CRAFT, 1, 1);
                } else {
                    player.sendMessage("§4Please put diamonds into the machine.§!");
                    event.setCancelled(true);
                    player.playSound(player, Sound.ENTITY_VILLAGER_HURT, 1, 1);
                    return;
                }
                event.setCancelled(true);
            }
        }
    }
}
