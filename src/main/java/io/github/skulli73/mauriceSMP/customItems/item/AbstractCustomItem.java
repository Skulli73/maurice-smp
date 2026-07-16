package io.github.skulli73.mauriceSMP.customItems.item;

import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import lombok.Getter;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public abstract class AbstractCustomItem {
    @Getter
    private final ItemStack item;
    @Getter
    private final String id;
    @Getter
    private final Set<SkillWithNumber> requiredSkills = new HashSet<>();
    @Getter
    private final Category category;
    public AbstractCustomItem (ItemStack itemStack, String id, Category category) {
        this.item = itemStack;
        this.id = id;
        this.category = category;
    }
    public void onBlockBreakEvent (BlockBreakEvent event) {
    }
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

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
                lore.addFirst("§3Requires §lLevel " + (int) skill.getNumber() + " "+skill.getSkillType().getName() + "§r \n");
            }
            lore.addFirst("§i§8" + getName() + "§r \n");
            meta.getPersistentDataContainer().set(itemManager.getIdKey(), PersistentDataType.STRING, id);
            meta.setLore(lore);

            item.setItemMeta(meta);
        }
        itemManager.getCustomItems().put(id, this);
        Map<Category, Set<AbstractCustomItem>> categoryItemMap = itemManager.getCategoryItemMap();
        Set<AbstractCustomItem> customItems = categoryItemMap.get(category);
        if (customItems == null) {
            categoryItemMap.put(category, Set.of(this));
        } else {
            customItems.add(this);
        }
    }
    public abstract String getName ();
 }
