package io.github.skulli73.mauriceSMP.recipes;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RecipeManager {
    private FileConfiguration config;
    private List<String> disabledRecipes; //uncraftable items
    private Map<String, Integer> amountOfRecipes = new HashMap<>();
    @Getter
    private Set<Recipe> recipes = new HashSet<>();
    @Getter
    private Map<String, List<SkillWithNumber>> requiredLevels = new HashMap<>();
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
                recipes.add(recipe);
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
                    if (material2 != null && !material2.isAir())
                        recipe.setIngredient(recipeShape.charAt(i), material2);
                    i++;
                }

                Bukkit.addRecipe(recipe);
                recipes.add(recipe);
            }

        ConfigurationSection requiredLevelsSection = config.getConfigurationSection("required_skills_recipes");
        if (requiredLevelsSection != null)
            for (String key : requiredLevelsSection.getKeys(false)) {
                ConfigurationSection itemSection = config.getConfigurationSection("required_skills_recipes." + key);
                if (itemSection != null) {
                    requiredLevels.put(key, new ArrayList<>());
                    for (SkillType skillType : SkillType.values()) {
                        if (itemSection.contains(skillType.getId()) && itemSection.isInt(skillType.getId())) {
                            requiredLevels.get(key).add(new SkillWithNumber(skillType, itemSection.getInt(skillType.getId())));
                        }
                    }
                }
            }
    }
    public boolean canCraft (Player player, String item) {
        if (!requiredLevels.containsKey(item))
            return true;
        SkillsManager skillsManager = MauriceSMP.getInstance().getSkillsManager();
        for (SkillWithNumber skill : requiredLevels.get(item)) {
            if (SkillsManager.getLevel(player, skill.getSkillType()) < skill.getNumber())
                return false;
        }
        return true;
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
        ItemStack item;
        if (material == null) {
            ItemManager itemManager = MauriceSMP.getInstance().getItemManager();
            AbstractCustomItem customItem = itemManager.stringToCustomItem(newKey);
            if (customItem != null) {
                item = customItem.getItem();
            } else
                return null;
        } else {
            item = new ItemStack(material);
            item.setAmount(amount);
        }

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
