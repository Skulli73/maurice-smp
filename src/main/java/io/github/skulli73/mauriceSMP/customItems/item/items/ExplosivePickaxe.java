package io.github.skulli73.mauriceSMP.customItems.item.items;

import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillWithNumber;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

public class ExplosivePickaxe extends AbstractCustomItem {
    private static boolean breaking = false;
    public ExplosivePickaxe () {
        super(getItemStack(), "EXPLOSIVE_PICKAXE", Category.TOOLS);
        super.getRequiredSkills().add(new SkillWithNumber(SkillType.PICKAXE, 3));
    }
    private static ItemStack getItemStack () {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null)
            meta.setItemName("§a§bExplosive Pickaxe§!");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onBlockBreakEvent(BlockBreakEvent event) {
        super.onBlockBreakEvent(event);
        Player player = event.getPlayer();
        if (breaking || player.isSneaking())
            return;
        breaking = true;
        Block broken = event.getBlock();
        for (int j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {
                for (int l = -1; l < 2; l++) {
                    if (j == 0 && k == 0 && l == 0)
                        continue;
                    Block block = broken.getRelative(j, k, l);
                    if (!block.getDrops(player.getInventory().getItemInMainHand()).isEmpty()) {
                        player.breakBlock(block);
                    }
                }
            }
        }
        breaking = false;
    }

    @Override
    public String getName() {
        return "Explosive Pickaxe";
    }
}
