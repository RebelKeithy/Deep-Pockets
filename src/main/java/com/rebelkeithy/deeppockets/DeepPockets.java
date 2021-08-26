package com.rebelkeithy.deeppockets;

import com.rebelkeithy.deeppockets.config.Config;
import com.rebelkeithy.deeppockets.config.ConfigLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DeepPockets implements ModInitializer {

	public static final String MOD_ID = "deep-pockets";

	public static final Tag<Item> BAGGABLE = TagRegistry.item(new Identifier("c", "baggable"));
	public static PackItem PACK;
	public static PackItem ADVANCED_PACK;

	private static final String CONFIG_FILE = "deep-pockets-config.json";
	public static Config config;

	@Override
	public void onInitialize() {
		config = new ConfigLoader().loadConfigFile(CONFIG_FILE);
		PACK = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pack"), new PackItem(new FabricItemSettings().group(ItemGroup.MISC), 5));
		ADVANCED_PACK = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "advanced_pack"), new PackItem(new FabricItemSettings().group(ItemGroup.MISC), 10));
	}
}
