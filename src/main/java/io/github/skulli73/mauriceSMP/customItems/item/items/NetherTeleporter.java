package io.github.skulli73.mauriceSMP.customItems.item.items;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.item.Category;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class NetherTeleporter extends AbstractBlock {
    public NetherTeleporter() {
        super(getItemStack(), "NETHER_TELEPORTER", Category.MACHINES);
    }
    private static ItemStack getItemStack () {
        ItemStack itemStack = new ItemStack(Material.NOTE_BLOCK);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null)
            meta.setItemName("§a§bNether Teleporter§r");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public String getName() {
        return "Nether Teleporter";
    }

    @Override
    public void onNote (NotePlayEvent event) {

        //event.setCancelled(true);
        Block block = event.getBlock();
        Location location = block.getLocation();
        World world = location.getWorld();
        if (world == null)
            return;
        Block chestBlock = null;
        for (BlockFace face : BlockFace.values()) {
            if (block.getRelative(face).getType() == Material.CHEST) {
                chestBlock = block.getRelative(face);
                break;
            }
        }
        if (chestBlock == null)
            return;
        if (chestBlock.getType() != Material.CHEST)
            return;
        if (!(chestBlock.getState() instanceof Container container))
            return;
        Inventory inventory = container.getInventory();
        boolean b = false;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == Material.ENDER_PEARL) {
                itemStack.setAmount(itemStack.getAmount() - 1);
                b = true;
                break;
            }
        }
        if (!b)
            return;
        world.getNearbyEntities(location, 5, 5, 5).stream().filter(c->c instanceof Player).forEach(
                c-> {
                    if (c.getWorld().getEnvironment() == World.Environment.NORMAL) {
                        World netherWorld = MauriceSMP.getInstance().getServer().getWorlds().stream().filter(d->d.getEnvironment() == World.Environment.NETHER).findFirst().orElse(null);
                        summonLightnings(c.getLocation());
                        if (netherWorld != null) {
                            Location netherLocation = netherWorld.getSpawnLocation();
                            c.teleport(netherLocation, PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                            summonLightnings(c.getLocation());
                        }

                    }
                    else if (c.getWorld().getEnvironment() == World.Environment.NETHER) {
                        World overworld = MauriceSMP.getInstance().getServer().getWorlds().stream().filter(d->d.getEnvironment() == World.Environment.NORMAL).findFirst().orElse(null);
                        summonLightnings(c.getLocation());
                        if (overworld != null) {
                            Location overworldLocation = ((Player) c).getRespawnLocation();
                            if (overworldLocation == null)
                                overworldLocation = overworld.getSpawnLocation();
                            c.teleport(overworldLocation, PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                            summonLightnings(c.getLocation());
                        }

                    }

                }
        );

    }

    private void summonLightnings (Location location) {
        World world = location.getWorld();
        if (world == null)
            return;
        Location[] locations = new Location[4];
        locations[0] = location.add(1,-2, 1);
        locations[1] = location.add(1,-2, -1);
        locations[2] = location.add(-1,-2, 1);
        locations[3] = location.add(-1,-2, -1);
        for (Location location1 : locations) {
            world.spawnEntity(location1, EntityType.LIGHTNING_BOLT);
        }
    }

}
