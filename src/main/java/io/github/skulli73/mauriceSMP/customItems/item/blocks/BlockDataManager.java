package io.github.skulli73.mauriceSMP.customItems.item.blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.customItems.ItemManager;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.customItems.item.items.AbstractBlock;
import io.github.skulli73.mauriceSMP.skills.JsonData;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;
import lombok.Getter;
import org.bukkit.block.Block;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BlockDataManager {


    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private final @Getter Map<Location, PlacedBlock> placedBlocks = new HashMap<>();

    private Map<String, String> blocksString = new HashMap<>();

    public BlockDataManager () {
        try {
            loadBlockData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    PlacedBlock getBlockData(Location location) {
        return placedBlocks.get(location);
    }

    private void loadBlockData() throws FileNotFoundException {
        File file = new File(MauriceSMP.getInstance().getDATA_PATH_BLOCKS().formatted("blocks"));
        if (!file.exists())
            return;
        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        blocksString = GSON.fromJson(new FileReader(file), mapType);
        ItemManager itemManager = MauriceSMP.getInstance().getItemManager();

        for (Map.Entry<String, String> entry : blocksString.entrySet()) {
            Location location = Location.fromString(entry.getKey());
            if (location == null)
                continue;
            AbstractCustomItem customItem = itemManager.getCustomItems().get(entry.getValue());
            if (customItem instanceof AbstractBlock abstractBlock)
                placedBlocks.put(location, new PlacedBlock(abstractBlock ,location));
        }
    }

    public void addPlacedBlock (PlacedBlock placedBlock) {
        placedBlocks.put(placedBlock.getLocation(), placedBlock);
        blocksString.put(placedBlock.getLocation().toString(), placedBlock.getItem().getId());
        saveBlockData();
    }

    public void removedPlacedBlock (PlacedBlock placedBlock) {
        placedBlocks.remove(placedBlock.getLocation());
        blocksString.remove(placedBlock.getLocation().toString());
        saveBlockData();
    }

    public void saveBlockData() {
        File file = new File(MauriceSMP.getInstance().getDATA_PATH_BLOCKS().formatted("blocks"));

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(GSON.toJson(blocksString));
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
