package com.rebelkeithy.deeppockets;

import com.rebelkeithy.deeppockets.Items.ItemMiningPack;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class PackModel implements ItemMeshDefinition  {

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack){ 	
		
			ItemMiningPack pack = (ItemMiningPack)stack.getItem();
			
			int items = pack.getTotalItems(stack);
			int i = Math.min(1+items/128, 4);			
			return new ModelResourceLocation(stack.getItem().getRegistryName()+""+ i, "inventory");
		
	}

}
