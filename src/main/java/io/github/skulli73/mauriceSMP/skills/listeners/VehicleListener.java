package io.github.skulli73.mauriceSMP.skills.listeners;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.List;

public class VehicleListener implements Listener {
    List<String> iceTypes;
    public VehicleListener() {
        iceTypes = MauriceSMP.getInstance().getConfig().getStringList("vehicle_break_blocks");
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleMove(VehicleMoveEvent event) {
        Material material1 = event.getFrom().getBlock().getType();
        Material material2 = event.getFrom().getBlock().getRelative(BlockFace.DOWN).getType();
        if (iceTypes.contains(material1.name()) || iceTypes.contains(material2.name())) {
            event.getVehicle().eject();
        }
    }
}
