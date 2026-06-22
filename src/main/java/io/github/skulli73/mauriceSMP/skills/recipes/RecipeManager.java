package io.github.skulli73.mauriceSMP.skills.recipes;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {
    private final FileConfiguration config;
    private List<String> disabledRecipes = new ArrayList<>(); //uncraftable items
    public RecipeManager () {
        this.config = MauriceSMP.getInstance().getConfig();
        disabledRecipes = config.getStringList("disabled_recipes");
    }
    public boolean isDisabled (String string) {
        return disabledRecipes.contains(string);
    }
    public boolean isDisabled (Material material) {
        return isDisabled(material.name());
    }
    public boolean isDisabled (ItemStack itemStack) {
        return isDisabled(itemStack.getType());
    }
}
