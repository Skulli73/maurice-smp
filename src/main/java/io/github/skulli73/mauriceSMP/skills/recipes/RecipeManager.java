package io.github.skulli73.mauriceSMP.skills.recipes;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

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
                ShapelessRecipe recipe = (ShapelessRecipe) initializeRecipe(key, true);
                if (recipe == null) continue;
                for (String string : unshapedRecipesSection.getStringList(key)) {
                    Material material2 = Material.getMaterial(string.toUpperCase());
                    if (material2 == null)
                        continue;
                    recipe.addIngredient(material2);
                }

                Bukkit.addRecipe(recipe);
            }

        ConfigurationSection shapedRecipesSection = config.getConfigurationSection("custom_shaped_recipes");
        if (shapedRecipesSection != null)
            for (String key : shapedRecipesSection.getKeys(false)) {
                ShapedRecipe recipe = (ShapedRecipe) initializeRecipe(key, false);
                if (recipe == null) continue;
                List<String> stringList = shapedRecipesSection.getStringList(key);
                int amountItems = stringList.size();
                String recipeShape = "ABCDEFGHI";
                while (stringList.size() > 9)
                    stringList.remove(9);
                if (amountItems < 9) {
                    recipeShape = recipeShape.substring(0, amountItems);
                    recipeShape = recipeShape + "         ";
                    recipeShape = recipeShape.substring(0, 9);
                }
                int i = 0;
                for (String string : stringList) {
                    Material material2 = Material.getMaterial(string.toUpperCase());
                    if (material2 == null)
                        continue;
                    if (material2 == Material.AIR) {
                        if (i == 0)
                            recipeShape = " " + recipeShape.substring(1, 9);
                        else if (i == 8)
                            recipeShape = recipeShape.substring(0, 8) + " ";
                        else
                            recipeShape = recipeShape.substring(0, i) + " " + recipeShape.substring(i+1, 9);
                    }
                    i++;
                }
                recipe.shape(recipeShape.substring(0, 3), recipeShape.substring(3, 6), recipeShape.substring(6, 9));
                i = 0;
                for (String string : stringList) {
                    Material material2 = Material.getMaterial(string.toUpperCase());
                    System.out.println(material2);
                    System.out.println(recipeShape.charAt(i));
                    if (material2 != null && !material2.isAir())
                        recipe.setIngredient(recipeShape.charAt(i), material2);
                    i++;
                }

                Bukkit.addRecipe(recipe);
            }
    }

    private @Nullable CraftingRecipe initializeRecipe(String key, boolean shapeless) {
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
            return null;
        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        CraftingRecipe recipe;
        if (shapeless)
            recipe = new ShapelessRecipe(namespacedKey, item);
        else
            recipe = new ShapedRecipe(namespacedKey, item);
        return recipe;
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
