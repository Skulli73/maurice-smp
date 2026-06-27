package io.github.skulli73.mauriceSMP.skills.listeners;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.services.BlockMassDetectionService;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;


public class OnBlockBreakListener implements Listener {
    private BlockMassDetectionService detectionService;
    private boolean currentlyBreaking = false;
    public OnBlockBreakListener () {
        detectionService = new BlockMassDetectionService();
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        if (skillsManager.isLumberAxe(item) && !currentlyBreaking) {
            currentlyBreaking = true;
            Set<Block> blocks = detectionService.identifyTreeStructure(event.getBlock());
            for (Block block : blocks) {
                if (player.getInventory().getItemInMainHand().getType().isAir())
                    break;
                player.breakBlock(block);
            }
            currentlyBreaking = false;
        }
    }

}
