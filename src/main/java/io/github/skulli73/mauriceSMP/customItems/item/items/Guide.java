package io.github.skulli73.mauriceSMP.customItems.item.items;

import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Guide extends AbstractCustomItem {
    public Guide() {
        super(getItemStack(), "CF_GUIDE", Category.MISCELLANEOUS);

    }
    private static ItemStack getItemStack () {
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null)
            meta.setItemName("§bCiroFun Guide§!");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        event.getPlayer().performCommand("/cf guide"); //this doesnt work yet
    }

    @Override
    public String getName() {
        return "CiroFun Guide";
    }
}
