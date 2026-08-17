package io.github.skulli73.mauriceSMP.customItems.item.items;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import io.github.skulli73.mauriceSMP.customItems.item.blocks.Location;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Driller extends AbstractBlock {
    public Driller () {
        super(getItemStack(), "DRILLER", Category.MACHINES);
    }
    private static ItemStack getItemStack () {
        ItemStack itemStack = new ItemStack(Material.PISTON);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null)
            meta.setItemName("§a§bDriller§r");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public String getName() {
        return "Driller";
    }

    @Override
    public void onPiston(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            Location location = new Location(block.getX(), block.getY(), block.getZ(), block.getWorld());
            if (MauriceSMP.getInstance().getBlockDataManager().getPlacedBlocks().get(location) != null)
                return;
            if (block.getType() != Material.PISTON && block.getType() != Material.PISTON_HEAD && block.getType() != Material.AIR && !block.getDrops().isEmpty())
                block.breakNaturally();
        }
    }
}
