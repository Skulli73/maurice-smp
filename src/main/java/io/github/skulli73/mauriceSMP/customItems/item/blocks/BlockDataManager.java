package io.github.skulli73.mauriceSMP.customItems.item.blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.JsonData;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockDataManager {


    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private final @Getter Map<Location, PlacedBlock> placedBlocks = new HashMap<>();

    PlacedBlock getBlockData(Location location) {
        return placedBlocks.computeIfAbsent(location, this::loadBlockData);
    }

    private PlacedBlock loadBlockData(Location location) {
        File file = new File(MauriceSMP.getInstance().getDATA_PATH_BLOCKS().formatted(location.toString()));
        if (!file.exists()) {
            return null;
        }

        JsonData data = new JsonData(file);
        data.load();
        PlacedBlock placedBlock = GSON.fromJson(data.getJsonObject(), PlacedBlock.class);
        placedBlocks.put(placedBlock.getLocation(), placedBlock);
        return placedBlock;
    }

    public void addPlacedBlock (PlacedBlock placedBlock) {
        placedBlocks.put(placedBlock.getLocation(), placedBlock);
        saveBlockData(placedBlock);
    }

    void saveBlockData(PlacedBlock placedBlock) {
        File file = new File(MauriceSMP.getInstance().getDATA_PATH().formatted(placedBlock.getLocation()));

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(placedBlock.writeJson());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
