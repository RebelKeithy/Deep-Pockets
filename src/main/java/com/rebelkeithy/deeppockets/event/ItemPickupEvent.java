package com.rebelkeithy.deeppockets.event;

import java.util.Random;

import com.rebelkeithy.deeppockets.DeepPocketsConfig;
import com.rebelkeithy.deeppockets.Items.ItemMiningPack;
import com.rebelkeithy.deeppockets.Items.ItemMiningPack.StoredOre;
import com.rebelkeithy.deeppockets.proxy.compatability.Compatability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemPickupEvent 
{
	@SubscribeEvent
	public void pickupItemEvent(EntityItemPickupEvent event)
	{
		if(event.getEntityPlayer() != null)
		{
			String oreName = DeepPocketsConfig.isOre(event.getItem().getEntityItem());
			if(oreName != null)
			{
				EntityPlayer player = event.getEntityPlayer();
				for(ItemStack stack : Compatability.getInventory(player.inventory))
				{
					if(stack.getItem() instanceof ItemMiningPack)
					{		
						ItemMiningPack pack = (ItemMiningPack)stack.getItem();

						if(pack.numStoredOres(stack) < pack.getMaxSlots() || pack.containsOre(stack, oreName))
						{
							StoredOre ore = pack.getOre(stack, oreName);
							ore.amount += Compatability.getStackCount(event.getItem().getEntityItem());
							ore.displayName = event.getItem().getEntityItem().getDisplayName();
							if(ore.registryName == null)
							{
								ore.registryName = event.getItem().getEntityItem().getItem().getRegistryName().toString();
								ore.meta = event.getItem().getEntityItem().getItemDamage();
							}
							pack.setOre(stack, oreName, ore);

							Random rand = new Random();
							float f = ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F;
							player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.AMBIENT, 0.2f, f);
							
							Compatability.setCount(event.getItem().getEntityItem(), 0);
							event.setCanceled(true);					    
							return;
						}
					}
				}
			}
		}
	}
}
