package io.github.skulli73.mauriceSMP.skills;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private FileConfiguration config;
    private List<String> disabledEntities;
    public EntityManager () {
        config = MauriceSMP.getInstance().getConfig();
        disabledEntities = config.getStringList("disabled_entities");
    }

    public boolean isDisabled (String id) {
        return disabledEntities.contains(id);
    }
    public boolean isDisabled (Entity entity) {
        return isDisabled(entity.getType().name());
    }
}
