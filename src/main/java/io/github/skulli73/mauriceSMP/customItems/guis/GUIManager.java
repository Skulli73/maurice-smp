package io.github.skulli73.mauriceSMP.customItems.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GUIManager {
    @Getter
    private final ChestGui mainMenu;
    @Getter
    private final Map<Category, ChestGui> categoryGuis = new HashMap<>();
    public GUIManager () {
        Category[] categories = Category.values();
        ItemManager itemManager = MauriceSMP.getInstance().getItemManager();



        // main menu
        int rowAmount = (int) Math.ceil((double) categories.length /9);
        mainMenu = new ChestGui(rowAmount, "CiroFun Guide");
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
        }
        ItemStack greyGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = greyGlass.getItemMeta();
        if (meta != null)
            meta.setDisplayName(" ");
        greyGlass.setItemMeta(meta);
        for (int j = pane.getItems().size(); j < rowAmount*9; j++ ) {
            GuiItem guiItem = new GuiItem(greyGlass,inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
            });
            pane.addItem(guiItem);
        }
        mainMenu.addPane(Slot.fromIndex(0), pane);

        //category menu
        for (Category category : categories) {
            ChestGui categoryMenu = new ChestGui(rowAmount, category.getName());
            Set<AbstractCustomItem> customItems = itemManager.getCategoryItemMap().get(category);
            OutlinePane pane2 = getOutlinePane(customItems, greyGlass);
            categoryMenu.addPane(Slot.fromIndex(0), pane2);
            categoryGuis.put(category, categoryMenu);
        }
    }

    private static @NotNull OutlinePane getOutlinePane(Set<AbstractCustomItem> customItems, ItemStack greyGlass) {
        int rowAmount2 = (int) Math.ceil((double) customItems.size() /9);
        OutlinePane pane2 = new OutlinePane(9, rowAmount2);
        for (AbstractCustomItem customItem : customItems) {
            GuiItem guiItem = new GuiItem(customItem.getItem(),inventoryClickEvent -> {
                HumanEntity humanEntity = inventoryClickEvent.getView().getPlayer();
                if (humanEntity instanceof Player player) {
                    player.performCommand("cf guide open item " + customItem.getId() + " 0");
                }
                inventoryClickEvent.setCancelled(true);
            });
            pane2.addItem(guiItem);
        }
        for (int j = pane2.getItems().size(); j < rowAmount2*9; j++ ) {
            GuiItem guiItem = new GuiItem(greyGlass, inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
            });
            pane2.addItem(guiItem);
        }
        return pane2;
    }
}
