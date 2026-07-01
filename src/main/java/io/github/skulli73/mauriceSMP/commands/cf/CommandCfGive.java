package io.github.skulli73.mauriceSMP.commands.cf;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandCfGive implements CfCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("maurice-smp.cf-admin") && !commandSender.isOp()) {
            commandSender.sendMessage("§4You do not have the necessary permissions.§!");
            return false;
        }
        int length = strings.length;
        if (length <= 1 || length > 4)
            return false;
        ItemManager itemManager = MauriceSMP.getInstance().getItemManager();
        String itemId = strings[1].toUpperCase();
        if (!itemManager.getCustomItems().containsKey(itemId)) {
            commandSender.sendMessage("§4This item does not exist.§!");
            return false;
        }
        AbstractCustomItem item = itemManager.getCustomItems().get(itemId);
        Player player;
        int amount = 1;
        if (length >= 3) {
            player = commandSender.getServer().getPlayer(strings[2]);
            if (player == null) {
                commandSender.sendMessage("§4This player does not exist.§!");
                return false;
            }
            if (length == 4) {
                if (!isNumeric(strings[3])) {
                    commandSender.sendMessage("§4Please enter a valid number.§!");
                    return false;
                }
                else
                    amount = (int) Double.parseDouble(strings[3]);
            }
        } else {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("§cThis command cannot be applied to the console.§!");
                return false;
            }
            player = ((Player) commandSender);
        }
        ItemStack itemStack = item.getItem().clone();
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        int length = strings.length;
        List<String> results = new ArrayList<>();
        if (length == 3) {
            String enteredName = strings[1];
            for (Player player : MauriceSMP.getInstance().getServer().getOnlinePlayers()) {
                String playerName = player.getName().toLowerCase();
                if (Objects.equals(enteredName, "") || playerName.toLowerCase().startsWith(enteredName.toLowerCase())) {
                    results.add(player.getName());
                }
            }
            return results;
        }
        if (length == 2) {
            String enteredItem = strings[2];
            for (AbstractCustomItem customItem : MauriceSMP.getInstance().getItemManager().getCustomItems().values()) {
                String itemName = customItem.getId().toLowerCase();
                if (Objects.equals(enteredItem, "") || itemName.toLowerCase().startsWith(enteredItem.toLowerCase())) {
                    results.add(customItem.getId());
                }
            }
            return results;
        }
        return results;

    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
