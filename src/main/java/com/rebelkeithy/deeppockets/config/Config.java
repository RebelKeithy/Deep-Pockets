package com.rebelkeithy.deeppockets.config;

import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public List<String> items = List.of(
            Registry.ITEM.getId(Items.RAW_IRON).toString(),
            Registry.ITEM.getId(Items.RAW_COPPER).toString(),
            Registry.ITEM.getId(Items.RAW_GOLD).toString(),
            Registry.ITEM.getId(Items.COAL).toString(),
            Registry.ITEM.getId(Items.EMERALD).toString(),
            Registry.ITEM.getId(Items.DIAMOND).toString(),
            Registry.ITEM.getId(Items.LAPIS_LAZULI).toString(),
            Registry.ITEM.getId(Items.REDSTONE).toString(),
            Registry.ITEM.getId(Items.GOLD_NUGGET).toString(),
            Registry.ITEM.getId(Items.FLINT).toString(),
            Registry.ITEM.getId(Items.ANCIENT_DEBRIS).toString(),
            Registry.ITEM.getId(Items.QUARTZ).toString(),
            Registry.ITEM.getId(Items.NETHER_QUARTZ_ORE).toString()
    );
    public List<String> tags = List.of(
            "minecraft:coal_ores",
            "minecraft:copper_ores",
            "minecraft:diamond_ores",
            "minecraft:emerald_ores",
            "minecraft:gold_ores",
            "minecraft:iron_ores",
            "minecraft:lapis_ores",
            "minecraft:redstone_ores"
    );
}
