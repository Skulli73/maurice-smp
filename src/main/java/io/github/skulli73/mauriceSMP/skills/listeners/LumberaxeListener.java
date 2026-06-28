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

import java.util.HashSet;
import java.util.Set;


public class LumberaxeListener implements Listener {
    private BlockMassDetectionService detectionService;
    private boolean currentlyBreaking = false;
    private Set<String> lumberAxeBlocks = new HashSet<>();
    private Set<String> veinMinerBlocks = new HashSet<>();
    public LumberaxeListener() {
        detectionService = new BlockMassDetectionService();
        lumberAxeBlocks.addAll(MauriceSMP.getInstance().getConfig().getStringList("lumber_axe_blocks"));
        veinMinerBlocks.addAll(MauriceSMP.getInstance().getConfig().getStringList("vein_miner_blocks"));
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking())
            return;
        ItemStack item = player.getInventory().getItemInMainHand();
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        Block destroyed = event.getBlock();
        if (lumberAxeBlocks.contains(destroyed.getType().name()) && skillsManager.isLumberAxe(item) && !currentlyBreaking) {
            currentlyBreaking = true;
            Set<Block> blocks = detectionService.identifyTreeStructure(destroyed);
            for (Block block : blocks) {
                if (player.getInventory().getItemInMainHand().getType().isAir())
                    break;
                player.breakBlock(block);
            }
            currentlyBreaking = false;
        }
        if (veinMinerBlocks.contains(destroyed.getType().name()) && skillsManager.isVeinMiner(item) && !currentlyBreaking ) {
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
