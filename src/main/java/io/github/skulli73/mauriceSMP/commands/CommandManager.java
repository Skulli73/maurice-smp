package io.github.skulli73.mauriceSMP.commands;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.commands.cf.CommandCirofun;

public class CommandManager {
    public CommandManager () {
        MauriceSMP.getInstance().getCommand("addskillxp").setExecutor(new CommandAddSkillXp());
        MauriceSMP.getInstance().getCommand("skills").setExecutor(new CommandSkills());
        MauriceSMP.getInstance().getCommand("cf").setExecutor(new CommandCirofun());
    }
}
