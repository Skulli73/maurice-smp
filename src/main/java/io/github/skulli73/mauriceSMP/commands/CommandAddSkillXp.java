package io.github.skulli73.mauriceSMP.commands;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandAddSkillXp implements CommandExecutor, TabExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        System.out.println(Arrays.toString(args));
        if (args.length < 2 || args.length > 3) {
            return false;
        }
        Player player;
        String valueString;
        int value;
        Server server = sender.getServer();
        SkillType skillType;
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command cannot be applied to the console.§!");
                return false;
            }
            player = (Player) sender;
            valueString = args[1];
            skillType  = skillsManager.getSkillTypeFromString(args[0]);
        } else {
            player = server.getPlayer(args[0]);
            valueString = args[2];
            skillType = skillsManager.getSkillTypeFromString(args[1]);
        }
        if (!isNumeric(valueString)) {
            sender.sendMessage("§cInvalid xp amount.§!");
            return false;
        }
        if (player == null) {
            sender.sendMessage("§cInvalid player.§!");
            return false;
        }
        value = Integer.parseInt(valueString);

        if(skillType == null) {
            sender.sendMessage("§cInvalid Skill Type.");
            return false;
        }
        SkillsManager.addXP(player, skillType, value);
        return true;
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> results = new ArrayList<>();
        int length = strings.length;
        if (length == 1) {
            for (Player player : MauriceSMP.getInstance().getServer().getOnlinePlayers()) {
                results.add(player.getName());
            }
            return results;
        }
        if (length == 2) {
            for (SkillType skill : SkillType.values()) {
                results.add(skill.getId());
            }
            return results;
        }
        return results;
    }
}

