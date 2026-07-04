package io.github.skulli73.mauriceSMP.skills.listeners;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        Map<String, SkillWithNumber> blockMiningXp = skillsManager.getBlockMiningXp();
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        Block block = event.getBlock();
        BlockData blockData = block.getBlockData();
        String blockName = block.getType().name();
        if (blockMiningXp.containsKey(blockName) && skillsManager.getItemXpMap().containsKey(heldItem.getType().name()) && !heldItem.containsEnchantment(Enchantment.SILK_TOUCH)) {
            if (blockData instanceof Ageable ageable) {
                if (ageable.getAge() != ageable.getMaximumAge()) {
                    return;
                }
            }
            SkillWithNumber skillWithNumber = blockMiningXp.get(blockName);
            SkillsManager.addXP(event.getPlayer(), skillWithNumber.getSkillType(), skillWithNumber.getNumber());
        }
    }
}
