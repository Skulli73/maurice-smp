package io.github.skulli73.mauriceSMP;

import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.commands.CommandManager;
import io.github.skulli73.mauriceSMP.skills.listeners.EntityListener;
import io.github.skulli73.mauriceSMP.skills.listeners.OnCraftListener;
import io.github.skulli73.mauriceSMP.skills.player.PlayerDataManager;
import io.github.skulli73.mauriceSMP.skills.recipes.RecipeManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MauriceSMP extends JavaPlugin {
    private FileConfiguration config = getConfig();
    @Getter
    private SkillsManager skillsManager;
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private CommandManager commandManager;
    @Getter
    private static MauriceSMP instance;
    @Getter
    private RecipeManager recipeManager;
    @Getter
    private final String DATA_PATH = getDataFolder().getAbsolutePath() + File.separator + "player" + File.separator + "%s.json";
    public MauriceSMP () {
        super();
        instance = this;
    }
    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new OnCraftListener(), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        playerDataManager =  new PlayerDataManager();
        commandManager = new CommandManager();
        skillsManager = new SkillsManager();
        recipeManager = new RecipeManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
