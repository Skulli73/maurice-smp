package io.github.skulli73.mauriceSMP.skills.listeners;

import com.google.common.util.concurrent.Service;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import io.github.skulli73.mauriceSMP.skills.VillagerManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class VillagerListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        VillagerManager villagerManager = MauriceSMP.getInstance().getVillagerManager();
        if (event.getInventory().getType() == InventoryType.MERCHANT) {
            MerchantInventory merchantInventory = (MerchantInventory) event.getInventory();
            if (event.getSlot() == 2) {
                ItemStack resultItem = merchantInventory.getItem(2);
                if (resultItem != null && resultItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) resultItem.getItemMeta();
                    if (storageMeta != null) {
                        Map<Enchantment, Integer> enchantments = storageMeta.getStoredEnchants();
                        if (enchantments.containsKey(Enchantment.MENDING)){
                            event.setCancelled(true);
                            event.getWhoClicked().sendMessage("This trade is disabled!");
                        }
                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                            SkillWithNumber skillWithNumber = villagerManager.getRequiredLevel(entry.getKey(), entry.getValue());
                            if (skillWithNumber != null) {
                                HumanEntity humanEntity = event.getViewers().getFirst();
                                if (humanEntity instanceof Player player) {
                                    if (SkillsManager.getLevel(player, skillWithNumber.getSkillType()) < skillWithNumber.getXp()) {
                                        event.setCancelled(true);
                                        event.getWhoClicked().sendMessage("§4You need §b§lLevel " + (int) skillWithNumber.getXp() + " " + skillWithNumber.getSkillType().getName() + " for this trade!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
