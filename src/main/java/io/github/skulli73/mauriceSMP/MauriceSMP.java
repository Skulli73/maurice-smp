package io.github.skulli73.mauriceSMP;

import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.listeners.OnCraftListener;
import io.github.skulli73.mauriceSMP.skills.player.PlayerDataManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MauriceSMP extends JavaPlugin {
    private FileConfiguration config = getConfig();
    @Getter
    private SkillsManager skillsManager = new SkillsManager(config);
    @Getter
    private PlayerDataManager playerDataManager = new PlayerDataManager();
    @Getter
    private static MauriceSMP instance;
    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new OnCraftListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
