package io.github.skulli73.mauriceSMP.skills.listeners;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) { //Bow XP when hitting something with a bow
        Entity hitEntity = event.getHitEntity();
        if (hitEntity == null)
            return;
        ProjectileSource causingEntity = event.getEntity().getShooter();
        if (causingEntity instanceof Player player && event.getEntity().getType() == EntityType.ARROW && hitEntity instanceof Damageable) {
            double xp = (double) Math.round(calculateHorizontalDistance(player.getLocation(), hitEntity.getLocation())) / 100;
            if (xp > 1)
                xp = 1;
            SkillsManager.addXP(player, SkillType.BOW, xp);
        }
    }

    private double calculateHorizontalDistance (Location l1, Location l2) {
        return Math.sqrt(Math.pow(l1.getX() - l2.getX(), 2) + Math.pow(l1.getZ() - l2.getZ(), 2));
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn (EntitySpawnEvent event) {
        if (MauriceSMP.getInstance().getEntityManager().isDisabled(event.getEntity()))
            event.setCancelled(true);
    }
}
