package io.github.skulli73.mauriceSMP.customItems.item.blocks;


import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import io.github.skulli73.mauriceSMP.skills.SkillData;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import io.github.skulli73.mauriceSMP.skills.player.FunPlayer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlacedBlockDeserializer implements JsonDeserializer<FunPlayer> {
    @Override
    public FunPlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();

        return null;
    }
}