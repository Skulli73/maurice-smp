package io.github.skulli73.mauriceSMP.skills;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum SkillType {
    ARMOUR("ARMOUR", "Armouring"),
    BLADESMITHING("BLADESMITHING", "Bladesmithing"),
    PICKAXE("PICKAXE", "Mining"),
    AXE("AXE", "Forestry"),
    HOE("HOE", "Farming"),
    BOW("BOW", "Fletchery"),
    SHOVEL("SHOVEL", "Digging");
    @Getter
    private final String id;
    @Getter
    private final String name;
    SkillType (String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id;
    }


    @NotNull
    public SkillData generateDefault() {
        return new SkillData(this, 0);
    }
}
