package io.github.skulli73.mauriceSMP.customItems.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIManager {
    @Getter
    private final ChestGui mainMenu;
    public GUIManager () {
        Category[] categories = Category.values();
        int rowAmount = (int) Math.ceil((double) categories.length /9);
        mainMenu = new ChestGui(rowAmount, "CiroFun Guide");
        int i = 0;
        OutlinePane pane = new OutlinePane(9, rowAmount);
        for (Category category : categories) {
            GuiItem guiItem = new GuiItem(category.getDisplayItem(), inventoryClickEvent -> {
                HumanEntity humanEntity = inventoryClickEvent.getView().getPlayer();
                if (humanEntity instanceof Player player) {
                    player.performCommand("cf guide open category " + category.name());
                }
                inventoryClickEvent.setCancelled(true);

            });
            pane.addItem(guiItem);
            i++;
        }
        ItemStack greyGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = greyGlass.getItemMeta();
        if (meta != null)
            meta.setDisplayName("");
        greyGlass.setItemMeta(meta);
        for (int j = pane.getItems().size(); j < rowAmount*9; j++ ) {
            GuiItem guiItem = new GuiItem(greyGlass,inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
            });
            pane.addItem(guiItem);
        }
        mainMenu.addPane(Slot.fromIndex(0), pane);
    }
}
