package io.github.skulli73.mauriceSMP.customItems.item;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import lombok.Getter;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractCustomItem {
    @Getter
    private final ItemStack item;
    @Getter
    private final String id;
    @Getter
    private final Set<SkillWithNumber> requiredSkills = new HashSet<>();
    public AbstractCustomItem (ItemStack itemStack, String id) {
        this.item = itemStack;
        this.id = id;
    }
    public void onBlockBreakEvent (BlockBreakEvent event) {
    }
    public void register(ItemManager itemManager) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore;
            if (meta.hasLore()) {
                lore = meta.getLore();
            } else {
                lore = new ArrayList<>();
            }
            for (SkillWithNumber skill : getRequiredSkills()) {
                lore.addFirst("§3Requires §lLevel " + (int) skill.getXp() + " "+skill.getSkillType().getName() + "§! \n");
            }
            lore.addFirst("§i§8" + getName() + "§! \n");
            meta.getPersistentDataContainer().set(itemManager.getIdKey(), PersistentDataType.STRING, id);
            meta.setLore(lore);

            item.setItemMeta(meta);
        }
        itemManager.getCustomItems().put(id, this);
    }
    public abstract String getName ();
 }
