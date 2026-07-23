package io.github.skulli73.mauriceSMP.customItems;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import io.github.skulli73.mauriceSMP.customItems.item.items.Driller;
import io.github.skulli73.mauriceSMP.customItems.item.items.ExplosivePickaxe;
import io.github.skulli73.mauriceSMP.customItems.item.items.Guide;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemManager {
    private MauriceSMP instance;
    @Getter
    private Map<String, AbstractCustomItem> customItems = new HashMap<>();
    @Getter
    private NamespacedKey idKey;
    @Getter
    private final Map<Category, Set<AbstractCustomItem>> categoryItemMap = new HashMap<>();
    public ItemManager () {
        instance = MauriceSMP.getInstance();
        idKey = new NamespacedKey(instance, "custom_item_id");
        registerItems();
    }
    public void registerItems () {
        new ExplosivePickaxe().register(this);
        new Guide().register(this);
        new Driller().register(this);
    }
    public AbstractCustomItem stringToCustomItem (String string) {
        return getCustomItems().getOrDefault(string.toUpperCase(), null);
    }
    public AbstractCustomItem itemStackToCustomItem (ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        ItemManager itemManager = MauriceSMP.getInstance().getItemManager();
        if (meta == null)
            return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(itemManager.getIdKey())) {
            Map<String, AbstractCustomItem> customItems = itemManager.getCustomItems();
            String itemId = container.get(itemManager.getIdKey(), PersistentDataType.STRING);
            if (customItems.containsKey(itemId)) {
                return customItems.get(itemId);
            }
        }
        return null;
    }
}
