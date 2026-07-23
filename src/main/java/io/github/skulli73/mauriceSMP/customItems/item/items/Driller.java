package io.github.skulli73.mauriceSMP.customItems.item.items;

import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Driller extends AbstractCustomItem {
    public Driller () {
        super(getItemStack(), "DRILLER", Category.MACHINES);
    }
    private static ItemStack getItemStack () {
        ItemStack itemStack = new ItemStack(Material.DISPENSER);
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
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        event.setCancelled(true);
    }
}
