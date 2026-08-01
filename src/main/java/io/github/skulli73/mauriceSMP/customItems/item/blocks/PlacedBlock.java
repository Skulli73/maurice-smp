package io.github.skulli73.mauriceSMP.customItems.item.blocks;

import com.google.gson.annotations.JsonAdapter;
import io.github.skulli73.mauriceSMP.customItems.item.AbstractCustomItem;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayerDeserializer;
import lombok.Getter;

@JsonAdapter(FunPlayerDeserializer.class)
public class PlacedBlock {
    @Getter
    private final AbstractCustomItem item;
    @Getter
    private final Location location;
    public PlacedBlock (AbstractCustomItem item, Location location) {
        this.item = item;
        this.location = location;
    }


}
