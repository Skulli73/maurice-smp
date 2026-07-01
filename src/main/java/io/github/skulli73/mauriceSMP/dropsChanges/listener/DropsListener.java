package io.github.skulli73.mauriceSMP.dropsChanges.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class DropsListener implements Listener {
    private Set<Material> hoes = new HashSet<>();
    public DropsListener () {
        hoes.add(Material.IRON_HOE);
        hoes.add(Material.STONE_HOE);
        hoes.add(Material.GOLDEN_HOE);
        hoes.add(Material.DIAMOND_HOE);
        hoes.add(Material.NETHERITE_HOE);
        hoes.add(Material.COPPER_HOE);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockDropItemEvent event) {
        BlockState broken = event.getBlockState();
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (broken.getBlockData() instanceof Ageable ageable && ageable.getAge() == ageable.getMaximumAge() && broken.getType() == Material.WHEAT) { //fortune for crops
            if (item.containsEnchantment(Enchantment.FORTUNE) && hoes.contains(item.getType())) {
                int fortune = item.getEnchantmentLevel(Enchantment.FORTUNE);
                Item wheat = null;
                for (Item drop : event.getItems()) {
                    if (drop.getItemStack().getType() == Material.WHEAT) {
                        wheat = drop;
                        break;
                    }
                }
                if (wheat != null) {
                    ItemStack wheatItemStack = wheat.getItemStack();
                    int drops = wheatItemStack.getAmount();
                    double newDropsDouble = drops * Math.pow(1.2, fortune);
                    int newDrops = (int) Math.floor(newDropsDouble);
                    if (Math.random() < newDropsDouble - newDrops) {
                        newDrops++;
                    }
                    wheatItemStack.setAmount(newDrops);
                }
            }
        }
    }
}
