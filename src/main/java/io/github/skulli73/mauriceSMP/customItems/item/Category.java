package io.github.skulli73.mauriceSMP.customItems.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Category {
    TOOLS(new ItemStack(Material.IRON_PICKAXE), "Tools", 'a'),
    MISCELLANEOUS(new ItemStack(Material.NETHER_STAR), "Miscellaneous", 'b');
    @Getter
    private final ItemStack displayItem;
    @Getter
    private final String name;
    Category (ItemStack displayItem, String name, char colourCode) {
        ItemMeta meta = displayItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§" + colourCode + name + "§!");
            displayItem.setItemMeta(meta);
        }
        this.displayItem = displayItem;
        this.name = name;
    }
}
