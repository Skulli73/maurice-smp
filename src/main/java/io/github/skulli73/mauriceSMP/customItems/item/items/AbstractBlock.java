package io.github.skulli73.mauriceSMP.customItems.item.items;

import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractBlock extends AbstractCustomItem {
    public AbstractBlock (ItemStack itemStack, String id, Category category) {
        super(itemStack, id, category);
    }
    @Override
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Block block = event.getBlock();
    }
}
