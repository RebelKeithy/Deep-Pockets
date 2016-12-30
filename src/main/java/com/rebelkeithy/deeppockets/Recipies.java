package com.rebelkeithy.deeppockets;

import com.rebelkeithy.deeppockets.Items.DeepPocketsItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipies 
{
	public static void registerRecipies()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(DeepPocketsItems.miningPack), DeepPocketsItems.miningPack);
		GameRegistry.addShapelessRecipe(new ItemStack(DeepPocketsItems.advancedMiningPack), DeepPocketsItems.advancedMiningPack);
		
		GameRegistry.addRecipe(new ItemStack(DeepPocketsItems.miningPack), "LGL", "ICI", "LLL", 'L', Items.LEATHER, 'C', Blocks.CHEST, 'I', Items.IRON_INGOT, 'G', Items.GOLD_INGOT);
		GameRegistry.addRecipe(new ItemStack(DeepPocketsItems.miningPack), "LGL", "ICI", "LLL", 'L', Blocks.WOOL, 'C', Blocks.CHEST, 'I', Items.IRON_INGOT, 'G', Items.GOLD_INGOT);
		
		GameRegistry.addRecipe(new ItemStack(DeepPocketsItems.advancedMiningPack), "EPE", "P P", "EPE", 'E', Items.ENDER_EYE, 'P', DeepPocketsItems.miningPack);
	}
}
