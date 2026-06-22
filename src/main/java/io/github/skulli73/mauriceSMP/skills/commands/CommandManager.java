package io.github.skulli73.mauriceSMP.skills.commands;

import io.github.skulli73.mauriceSMP.MauriceSMP;

public class CommandManager {
    public CommandManager () {
        MauriceSMP.getInstance().getCommand("addskillxp").setExecutor(new CommandAddSkillXp());
        MauriceSMP.getInstance().getCommand("skills").setExecutor(new CommandSkills());
    }
}
