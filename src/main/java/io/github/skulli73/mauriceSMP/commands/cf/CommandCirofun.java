package io.github.skulli73.mauriceSMP.commands.cf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandCirofun implements CommandExecutor, TabExecutor {
    private Map<String, CfCommand> cfCommands = new HashMap<>();
    public CommandCirofun () {
        cfCommands.put("give", new CommandCfGive());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0 && cfCommands.containsKey(strings[0])) {
            return cfCommands.get(strings[0]).onCommand(commandSender, command, s, strings);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        int length = strings.length;
        if (length > 1 && cfCommands.containsKey(strings[0])) {
            return cfCommands.get(strings[0]).onTabComplete(commandSender, command, s, strings);
        } else if (length <= 1){
            return new ArrayList<>(cfCommands.keySet());
        }
        return List.of();
    }
}
