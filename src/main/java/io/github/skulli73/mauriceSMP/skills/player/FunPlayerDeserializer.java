package io.github.skulli73.mauriceSMP.skills.player;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import io.github.skulli73.mauriceSMP.skills.SkillData;
import io.github.skulli73.mauriceSMP.skills.SkillType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FunPlayerDeserializer implements JsonDeserializer<FunPlayer> {
    @Override
    public FunPlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();

        return new FunPlayer(
                UUID.fromString(object.get("uniqueId").getAsString()),
                object.has("skills") ? context.deserialize(object.get("skills"), new TypeToken<Map<SkillType, SkillData>>() {}.getType()) : new HashMap<>()
        );
    }
}