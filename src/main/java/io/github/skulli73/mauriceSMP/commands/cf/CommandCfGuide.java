package io.github.skulli73.mauriceSMP.commands.cf;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.guis.GUIManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CommandCfGuide implements CfCommand {
    public CommandCfGuide () {

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        int length = strings.length;
        if (!(commandSender instanceof Player player))
            return false;
        if (length == 1) {
            player.getInventory().addItem(MauriceSMP.getInstance().getItemManager().getCustomItems().get("CF_GUIDE").getItem());
            return true;
        }
        GUIManager guiManager = MauriceSMP.getInstance().getGuiManager();
        if (strings[1].equals("open")) {
            if (length == 2) {
                guiManager.getMainMenu().show(player);
                return true;
            }
            if (length >= 4) {
                if (strings[2].equals("category")) {
                    String value = strings[3];
                    if (Arrays.stream(Category.values()).anyMatch(category -> category.name().equalsIgnoreCase(value))) {
                        Category category = Category.valueOf(value.toUpperCase());
                        guiManager.getCategoryGuis().get(category).show(player);
                        return true;
                    }
                } else if (strings[2].equals("item")) {
                    int page = 0;
                    if (length >= 5) {
                        if (isNumeric(strings[4])) {
                            page = (int) Double.parseDouble(strings[4]);
                        }
                        AbstractCustomItem customItem = MauriceSMP.getInstance().getItemManager().stringToCustomItem(strings[3]);
                        if (customItem != null) {
                            List<ChestGui> itemGuis = guiManager.getItemGuis().get(customItem);
                            if (itemGuis.size() <= page)
                                page = 0;
                            itemGuis.get(page).show(player);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }


    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
