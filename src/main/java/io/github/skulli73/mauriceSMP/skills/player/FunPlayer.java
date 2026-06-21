package io.github.skulli73.mauriceSMP.skills.player;

import com.google.gson.annotations.JsonAdapter;
import io.github.skulli73.mauriceSMP.MauriceSMP;
import io.github.skulli73.mauriceSMP.skills.SkillData;
import io.github.skulli73.mauriceSMP.skills.SkillType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

@JsonAdapter(FunPlayerDeserializer.class)
public class FunPlayer {

    private final @Getter UUID uniqueId;
    private final Map<SkillType, SkillData> skills;

    private transient @Getter @Setter long loadedAt = System.currentTimeMillis();

    public FunPlayer(UUID uniqueId, Map<SkillType, SkillData> skills) {
        this.uniqueId = uniqueId;
        this.skills = skills;
    }
    public FunPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.skills = MauriceSMP.getInstance().getSkillManager().getEmptySkillDataMap();
    }
    @NotNull
    public SkillData getSkillData(SkillType type) {
        return skills.computeIfAbsent(type, SkillType::generateDefault);
    }

    @NotNull
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    public boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    public void save() {
        MauriceSMP.getInstance().getPlayerDataManager().savePlayerData(this);
    }

    @Nullable
    public static FunPlayer get(Player player) {
        return MauriceSMP.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());
    }

}
