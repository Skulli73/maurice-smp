package io.github.skulli73.mauriceSMP.customItems.item.blocks;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import org.bukkit.World;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class Location {
    private int x;
    private int y;
    private int z;
    private World world;
    public Location (int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj instanceof Location location) {
            return Objects.equals(location.toString(), this.toString());
        }
        return false;
    }
    public String toString() {

        return String.format("%08d", x+50000000) + String.format("%08d", y+50000000) + String.format("%08d", z+50000000) + world.getName();
    }

    public String toStringFormatted () {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    @Nullable
    public static Location fromString (String str) {
        if (str.length() < 25)
            return null;
        String xStr = str.substring(0, 8);
        if (!isNumeric(xStr))
            return null;
        int x = (int)Double.parseDouble(xStr)-50000000;
        String yStr = str.substring(8, 16);
        if (!isNumeric(xStr))
            return null;
        int y = (int)Double.parseDouble(yStr)-50000000;
        String zStr = str.substring(16, 24);
        if (!isNumeric(zStr))
            return null;
        int z = (int)Double.parseDouble(zStr)-50000000;
        String worldStr = str.substring(24);
        World world = MauriceSMP.getInstance().getServer().getWorld(worldStr);
        if (world == null)
            return null;
        return new Location(x, y, z, world);
    }

    public String writeJson () {
        return BlockDataManager.GSON.toJson(toString());
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, world);
    }
}
