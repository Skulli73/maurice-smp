package io.github.skulli73.mauriceSMP.customItems.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import io.github.skulli73.mauriceSMP.recipes.RecipeManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class GUIManager {
    @Getter
    private ChestGui mainMenu;
    @Getter
    private final Map<Category, ChestGui> categoryGuis = new HashMap<>();
    @Getter
    private final Map<AbstractCustomItem, List<ChestGui>> itemGuis = new HashMap<>();
    public GUIManager () {
        Category[] categories = Category.values();
        ItemManager itemManager = MauriceSMP.getInstance().getItemManager();
        RecipeManager recipeManager = MauriceSMP.getInstance().getRecipeManager();



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

        for (int j = pane.getItems().size(); j < rowAmount*9; j++ ) {
            addGreyGlass(pane);
        }
        mainMenu.addPane(Slot.fromIndex(0), pane);

        //category menu
        for (Category category : categories) {
            ChestGui categoryMenu = new ChestGui(rowAmount, category.getName());
            Set<AbstractCustomItem> customItems = itemManager.getCategoryItemMap().get(category);
            int rowAmount2 = (int) Math.ceil((double) customItems.size() /9);
            OutlinePane pane2 = new OutlinePane(9, rowAmount2);
            for (AbstractCustomItem customItem : customItems) {
                GuiItem guiItem = new GuiItem(customItem.getItem(),inventoryClickEvent -> {
                    HumanEntity humanEntity = inventoryClickEvent.getView().getPlayer();
                    if (humanEntity instanceof Player player && itemGuis.containsKey(customItem)) {
                        player.performCommand("cf guide open item " + customItem.getId() + " 0");
                    }
                    inventoryClickEvent.setCancelled(true);
                });
                pane2.addItem(guiItem);
            }
            for (int j = pane2.getItems().size(); j < rowAmount2*9; j++ ) {
                addGreyGlass(pane2);
            }
            categoryMenu.addPane(Slot.fromIndex(0), pane2);
            categoryGuis.put(category, categoryMenu);
        }
        List<Recipe> recipes = new java.util.ArrayList<>(recipeManager.getRecipes().stream().toList());
        for (AbstractCustomItem customItem : itemManager.getCustomItems().values()) {
            List<Recipe> customItemRecipes = recipes.stream().filter(c->c.getResult().equals(customItem.getItem())).toList();
            if (customItemRecipes.isEmpty())
                continue;
            recipes.removeAll(customItemRecipes);
            List<ChestGui> guis = new ArrayList<>();
            for (Recipe recipe : customItemRecipes) {
                ChestGui gui = new ChestGui(3, customItem.getName());
                OutlinePane recipePane = new OutlinePane(3, 3);
                OutlinePane topRow = new OutlinePane(6, 1);
                OutlinePane bottomRow = new OutlinePane(6, 1);
                OutlinePane middleRow = new OutlinePane(6, 1);
                addGreyGlass(topRow, 6);
                addGreyGlass(middleRow);
                addItemToPane(middleRow, recipe.getResult());
                addGreyGlass(middleRow);
                addGreyGlass(bottomRow, 4);
                if (guis.isEmpty()) {
                    addGreyGlass(bottomRow);
                } else {
                    ItemStack itemStack = new ItemStack(Material.RED_WOOL);
                    ItemMeta meta = itemStack.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName("Left");
                        meta.setCustomModelData(1200);
                    }
                    itemStack.setItemMeta(meta);
                    GuiItem left = new GuiItem(itemStack, event -> {
                        if (event.getWhoClicked() instanceof Player player && itemGuis.containsKey(customItem)) {
                            player.performCommand("cf guide open item " + customItem.getId() + " " + guis.size());
                        }
                    });
                    bottomRow.addItem(left);
                }
                if (customItemRecipes.size() == guis.size() + 1) {
                    addGreyGlass(bottomRow);
                } else {
                    ItemStack itemStack = new ItemStack(Material.BLUE_WOOL);
                    ItemMeta meta = itemStack.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName("Right");
                        meta.setCustomModelData(1200);
                    }
                    itemStack.setItemMeta(meta);
                    GuiItem right = new GuiItem(itemStack, event -> {
                        if (event.getWhoClicked() instanceof Player player && itemGuis.containsKey(customItem)) {
                            player.performCommand("cf guide open item " + customItem.getId() + " " + (guis.size()-2));
                        }
                    });
                    bottomRow.addItem(right);
                }
                if (recipe instanceof ShapedRecipe shapedRecipe) {
                    for (String line : shapedRecipe.getShape() ) {
                        for (char itemChar : line.toCharArray()) {
                            if (itemChar != ' ') {
                                ItemStack ingredient = shapedRecipe.getIngredientMap().get(itemChar);
                                if (ingredient != null) {
                                    GuiItem guiItem = itemStackToGuiItem(ingredient);
                                    recipePane.addItem(guiItem);
                                    continue;
                                }
                            }
                            addGreyGlass(recipePane);
                        }
                    }
                    ItemStack craftingTable = new ItemStack(Material.CRAFTING_TABLE);
                    addItemToPane(middleRow, craftingTable);
                }

                if (recipePane == null)
                    continue;


                addGreyGlass(middleRow, 2);
                gui.addPane(Slot.fromIndex(0), recipePane);
                gui.addPane(Slot.fromIndex(3), topRow);
                gui.addPane(Slot.fromIndex(12), middleRow);
                gui.addPane(Slot.fromIndex(21), bottomRow);
                guis.add(gui);
            }
            itemGuis.put(customItem, guis);
        }
    }

    private static void addItemToPane(OutlinePane pane, ItemStack item) {
        pane.addItem(new GuiItem(item, event -> event.setCancelled(true)));
    }

    private static void addGreyGlass(OutlinePane pane) {
        ItemStack greyGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = greyGlass.getItemMeta();
        if (meta != null)
            meta.setDisplayName(" ");
        greyGlass.setItemMeta(meta);
        GuiItem guiItem = new GuiItem(greyGlass, inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
        });
        pane.addItem(guiItem);
    }
    private static void addGreyGlass(OutlinePane pane, int amount) {
        for (int i = 0; i<amount; i++) {
            addGreyGlass(pane);
        }
    }

    private @NotNull GuiItem itemStackToGuiItem(ItemStack ingredient) {
        ItemManager itemManager = MauriceSMP.getInstance().getItemManager();
        Consumer<InventoryClickEvent> clickEvent = inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
        };
        AbstractCustomItem customItem = itemManager.itemStackToCustomItem(ingredient);
        if (customItem != null) {
            clickEvent = inventoryClickEvent -> {
                HumanEntity humanEntity = inventoryClickEvent.getView().getPlayer();
                if (humanEntity instanceof Player player && itemGuis.containsKey(customItem)) {
                    player.performCommand("cf guide open item " + customItem.getId() + " 0");
                }
                inventoryClickEvent.setCancelled(true);
            };
        }
        return new GuiItem(ingredient, clickEvent);
    }

}
