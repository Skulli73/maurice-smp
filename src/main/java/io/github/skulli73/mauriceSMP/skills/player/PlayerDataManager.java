package io.github.skulli73.mauriceSMP.skills.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.JsonData;
import lombok.Getter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {


    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private final @Getter Map<UUID, FunPlayer> funPlayers = new HashMap<>();

    FunPlayer getPlayerData(UUID uuid) {
        return funPlayers.computeIfAbsent(uuid, this::loadPlayerData);
    }

    private FunPlayer loadPlayerData(UUID uuid) {
        File file = new File(MauriceSMP.getInstance().getDATA_PATH().formatted(uuid.toString()));
        if (!file.exists()) {
            return new FunPlayer(
                    uuid
            );
        }

        JsonData data = new JsonData(file);
        data.load();

        return gson.fromJson(data.getJsonObject(), FunPlayer.class);
    }

    void savePlayerData(FunPlayer funPlayer) {
        File file = new File(MauriceSMP.getInstance().getDATA_PATH().formatted(funPlayer.getUniqueId().toString()));

        JsonObject obj = gson.toJsonTree(funPlayer).getAsJsonObject();

        JsonData data = new JsonData(file, obj);

        data.save();
    }

}
