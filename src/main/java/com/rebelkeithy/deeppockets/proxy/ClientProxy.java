package com.rebelkeithy.deeppockets.proxy;

import com.rebelkeithy.deeppockets.Items.DeepPocketsItems;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
	
	public void preinit(FMLPreInitializationEvent e){
		 super.preinit(e);
		 DeepPocketsItems.clientpreInit();
	 }
	 public void init(FMLInitializationEvent e){
		 super.init(e);		 
		 
	 }
	
}
