package io.github.skulli73.mauriceSMP.skills.commands;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillData;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillUtils;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class CommandSkills implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        System.out.println(args);
        if (args.length > 1) {
            return false;
        }
        Player player;
        Server server = sender.getServer();
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command cannot be applied to the console.§!");
                return false;
            }
            player = (Player) sender;
        } else {
            player = server.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("§cInvalid player.§!");
                return false;
            }
        }
        FunPlayer funPlayer = FunPlayer.get(player);
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("§eYour Skills:");

        for(SkillType skillType : SkillType.values()) {
            SkillData skillData;
            if(funPlayer != null)
                skillData = funPlayer.getSkillData(skillType);
            else
                skillData = new SkillData(skillType, 0);
            int level = skillData.getCurrentLevel();
            double currentXp = skillData.getTotalXp();
            double levelXp = currentXp - SkillUtils.levelToXP(level);
            double neededXp = SkillUtils.levelToXP(level + 1) - SkillUtils.levelToXP(level);
            joiner.add("\n§e%s§r: Level §b%s §r(§b%s§r/§b%s§r)".formatted(
                    skillType.getName(),
                    level,
                    (int) Math.floor(levelXp),
                    (int) Math.floor(neededXp)
            ));
        }

        sender.sendMessage(joiner.toString());

        return true;
    }
}

