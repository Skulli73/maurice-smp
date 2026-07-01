package io.github.skulli73.mauriceSMP;

import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.listener.OnBlockBreakListener;
import io.github.skulli73.mauriceSMP.dropsChanges.listener.DropsListener;
import io.github.skulli73.mauriceSMP.skills.EntityManager;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.commands.CommandManager;
import io.github.skulli73.mauriceSMP.skills.VillagerManager;
import io.github.skulli73.mauriceSMP.skills.listeners.*;
import io.github.skulli73.mauriceSMP.skills.player.PlayerDataManager;
import io.github.skulli73.mauriceSMP.recipes.RecipeManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
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
    private EntityManager entityManager;
    @Getter
    private ItemManager itemManager;
    @Getter
    private VillagerManager villagerManager;
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
        getServer().getPluginManager().registerEvents(new EnchantmentListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new MultiblockListener(), this);
        getServer().getPluginManager().registerEvents(new LumberaxeListener(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new DropsListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerListener(), this);
        playerDataManager =  new PlayerDataManager();
        commandManager = new CommandManager();
        skillsManager = new SkillsManager();
        itemManager = new ItemManager();
        recipeManager = new RecipeManager();
        entityManager = new EntityManager();
        villagerManager = new VillagerManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
