package io.github.skulli73.mauriceSMP.skills.recipes;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManager {
    private FileConfiguration config;
    private List<String> disabledRecipes = new ArrayList<>(); //uncraftable items
    private Map<String, Integer> amountOfRecipes = new HashMap<>();
    public RecipeManager () {
        this.config = MauriceSMP.getInstance().getConfig();
        disabledRecipes = config.getStringList("disabled_recipes");

        ConfigurationSection unshapedRecipesSection = config.getConfigurationSection("custom_unshaped_recipes");
        if (unshapedRecipesSection != null)
            for (String key : unshapedRecipesSection.getKeys(false)) {
                String newKey = key;
                if (isNumeric("" + key.charAt(0)))
                    newKey = key.substring(1);
                String[] keySeparated = newKey.split(" ");
                int amount = 1;
                if (keySeparated.length > 1) {
                    newKey = keySeparated[0];
                    String amountString = keySeparated[1];
                    if (isNumeric(amountString))
                        amount = Integer.parseInt(amountString);
                }
                int index;
                if (amountOfRecipes.containsKey(newKey)) {
                    index = amountOfRecipes.get(newKey);
                    index++;
                } else
                    index = 1000;
                amountOfRecipes.put(newKey, index);
                NamespacedKey namespacedKey = new NamespacedKey(MauriceSMP.getInstance() ,newKey + index);
                Material material = Material.getMaterial(newKey.toUpperCase());
                if (material == null)
                    continue;
                ItemStack item = new ItemStack(material);
                item.setAmount(amount);
                ShapelessRecipe recipe = new ShapelessRecipe(namespacedKey, item);
                for (String string : unshapedRecipesSection.getStringList(key)) {
                    Material material2 = Material.getMaterial(string.toUpperCase());
                    if (material2 == null)
                        continue;
                    recipe.addIngredient(material2);
                }

                Bukkit.addRecipe(recipe);
            }

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

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
