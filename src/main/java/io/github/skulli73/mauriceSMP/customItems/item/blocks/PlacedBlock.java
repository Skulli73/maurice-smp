package io.github.skulli73.mauriceSMP.customItems.item.blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.JsonAdapter;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayerDeserializer;
import lombok.Getter;
import org.bukkit.block.Block;


public class PlacedBlock {
    @Getter
    public final AbstractCustomItem item;
    @Getter
    public final Location location;

    public PlacedBlock (AbstractCustomItem item, Location location) {
        this.item = item;
        this.location = location;
    }
    public String writeJson () {
        Gson gson = BlockDataManager.GSON;
        return "";
    }


}
