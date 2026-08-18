package io.github.skulli73.mauriceSMP.customItems.listener;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.BlockDataManager;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.Location;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.PlacedBlock;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class RedstoneListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDispense (BlockDispenseEvent event) {
        Block block = event.getBlock();
        Location location = new Location(block.getX(), block.getY(), block.getZ(), block.getWorld());
        BlockDataManager blockDataManager = MauriceSMP.getInstance().getBlockDataManager();
        PlacedBlock placedBlock = blockDataManager.getPlacedBlocks().get(location);
        if (placedBlock != null) {
            placedBlock.getItem().onDispense(event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNote (NotePlayEvent event) {
        Block block = event.getBlock();
        Location location = new Location(block.getX(), block.getY(), block.getZ(), block.getWorld());
        BlockDataManager blockDataManager = MauriceSMP.getInstance().getBlockDataManager();
        PlacedBlock placedBlock = blockDataManager.getPlacedBlocks().get(location);
        if (placedBlock != null) {
            placedBlock.getItem().onNote(event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPistonExtendEvent (BlockPistonExtendEvent event) {
        Block block = event.getBlock();
        if (event.getBlocks().stream().anyMatch(c->{
            Location location = new Location(block.getX(), block.getY(), block.getZ(), block.getWorld());
            BlockDataManager blockDataManager = MauriceSMP.getInstance().getBlockDataManager();
            PlacedBlock placedBlock = blockDataManager.getPlacedBlocks().get(location);
            return placedBlock != null;
        })) {
            event.setCancelled(true);
            return;
        }
        Location location = new Location(block.getX(), block.getY(), block.getZ(), block.getWorld());
        BlockDataManager blockDataManager = MauriceSMP.getInstance().getBlockDataManager();
        PlacedBlock placedBlock = blockDataManager.getPlacedBlocks().get(location);
        if (placedBlock != null) {
            placedBlock.getItem().onPiston(event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPortalIgnite (PortalCreateEvent event) {
        if (event.getWorld().getEnvironment() == World.Environment.NETHER || event.getWorld().getEnvironment() == World.Environment.NORMAL)
            event.setCancelled(true);
    }
}
