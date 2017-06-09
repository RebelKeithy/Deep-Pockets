package com.rebelkeithy.deeppockets.Items;

import java.util.ArrayList;
import java.util.List;

import com.rebelkeithy.deeppockets.DeepPocketsMod;
import com.rebelkeithy.deeppockets.PackModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DeepPocketsItems 
{
	public static Item miningPack;
	public static Item advancedMiningPack;
	
	public static final List<Item> items = new ArrayList<Item>();	
	
	public static final void commonPreInit(){
		miningPack = registerItem(new ItemMiningPack(5), "mining_pack").setCreativeTab(CreativeTabs.TOOLS);
		advancedMiningPack = registerItem(new ItemMiningPack(10), "advanced_mining_pack").setCreativeTab(CreativeTabs.TOOLS);
	}
	
	public static final void clientpreInit(){
		for(Item item:items){
			registerRender(item);
		}
	}
	
	private static final Item registerItem(Item item,String name){
		GameRegistry.register(item, new ResourceLocation(DeepPocketsMod.MODID, name));
		item.setUnlocalizedName(name);
		items.add(item);
		return item;
	}
	@SideOnly(value = Side.CLIENT) 
	private static final void registerRender(Item item){
		List<ModelResourceLocation> packModels = new ArrayList<ModelResourceLocation>();		
		for(int i = 1; i <= 4; i++)
		{
			packModels.add(new ModelResourceLocation(item.getRegistryName()+""+ i, "inventory"));			
		}
		
		ModelLoader.registerItemVariants(item, packModels.toArray(new ModelResourceLocation[packModels.size()]));		
		ModelLoader.setCustomMeshDefinition(item, new PackModel());		
	}
	
}
