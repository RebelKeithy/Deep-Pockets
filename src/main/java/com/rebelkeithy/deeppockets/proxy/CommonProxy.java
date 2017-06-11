package com.rebelkeithy.deeppockets.proxy;

import com.rebelkeithy.deeppockets.Items.DeepPocketsItems;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy 
{
	 public void preinit(FMLPreInitializationEvent e){
		 DeepPocketsItems.commonPreInit();
	 }
	 public void init(FMLInitializationEvent e){
		 
	 }
	    
	    
}
