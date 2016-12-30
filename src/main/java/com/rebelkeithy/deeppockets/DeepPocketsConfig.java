package com.rebelkeithy.deeppockets;

import java.util.ArrayList;
import java.util.List;

import com.rebelkeithy.deeppockets.Items.ItemMiningPack.StoredOre;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class DeepPocketsConfig 
{
	public static List<String> allowedItems = new ArrayList<String>();
	public static List<String> allowedMeta = new ArrayList<String>();
	
	public static void init(Configuration config)
	{
		config.load();
		
		String[] items = config.get("Mining Pack", "Allowed Items", new String[] {"minecraft:coal", "minecraft:redstone", "minecraft:dye:4", "minecraft:diamond", "minecraft:emerald", "minecraft:quartz"}, "Items other than ore blocks").getDefaults();
		
		for(String s : items)
		{
			String[] split = s.split(":");
			
			if(split.length == 3)
			{
				allowedMeta.add(s); 
			}
			else
			{
				allowedItems.add(s);
			}
		}
		
		config.save();
	}

	// Returns null if not an ore, otherwise returns the oredict name
	public static String isOre(ItemStack stack)
	{
		if(DeepPocketsConfig.allowedItems.contains(stack.getItem().getRegistryName().toString()))
		{
			return stack.getItem().getRegistryName().toString();
		}
		if(DeepPocketsConfig.allowedMeta.contains(stack.getItem().getRegistryName().toString() + ":" + stack.getItemDamage()))
		{
			return stack.getItem().getRegistryName().toString() + ":" + stack.getItemDamage();
		}
		
		System.out.println("test");
		
		int[] ids = OreDictionary.getOreIDs(stack);
		if(ids.length > 0)
		{
			String name =  OreDictionary.getOreName(ids[0]);
			return name.contains("ore") ? name : null;
		}
		
		return null;
	}
	
	public static ItemStack getStack(StoredOre ore, int amount)
	{
		Item item = Item.getByNameOrId(ore.registryName);
		return new ItemStack(item, amount, ore.meta);
	}
}
