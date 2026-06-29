package io.github.skulli73.mauriceSMP.skills.commands;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAddSkillXp implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        System.out.println(args);
        if (args.length < 2 || args.length > 3) {
            return false;
        }
        Player player;
        String valueString;
        int value;
        Server server = sender.getServer();
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command cannot be applied to the console.§!");
                return false;
            }
            player = (Player) sender;
            valueString = args[0];
        } else {
            player = server.getPlayer(args[0]);
            valueString = args[1];
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
        SkillType skillType = skillsManager.getSkillTypeFromString(args[2]);
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
}

