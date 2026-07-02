package io.github.skulli73.mauriceSMP.commands.cf;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (strings[1].equals("open")) {
            if (length == 2) {
                MauriceSMP.getInstance().getGuiManager().getMainMenu().show(player);
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
