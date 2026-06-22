package io.github.skulli73.mauriceSMP.skills.listeners;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.SkillsManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftItem(EntityDamageEvent event) { //Bow XP when hitting something with a bow
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            Entity hitEntity = event.getEntity();
            Entity causingEntity = event.getDamageSource().getCausingEntity();
            if (causingEntity instanceof Player) {
                Player player = (Player) causingEntity;
                double xp = (double) Math.round(calculateHorizontalDistance(causingEntity.getLocation(), hitEntity.getLocation())) /100;
                if (xp > 1)
                    xp = 1;
                SkillsManager.addXP(player, SkillType.BOW, xp);
            }
        }
    }

    private double calculateHorizontalDistance (Location l1, Location l2) {
        return Math.sqrt(Math.pow(l1.getX() - l2.getX(), 2) + Math.pow(l1.getZ() - l2.getZ(), 2));
    }
}
