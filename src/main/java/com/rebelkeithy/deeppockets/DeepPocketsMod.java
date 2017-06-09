package com.rebelkeithy.deeppockets;

import com.rebelkeithy.deeppockets.event.ItemPickupEvent;
import com.rebelkeithy.deeppockets.proxy.CommonProxy;
import com.rebelkeithy.deeppockets.proxy.compatability.Compatability;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DeepPocketsMod.MODID, version = DeepPocketsMod.VERSION, acceptedMinecraftVersions = "[1.10,1.12)")
public class DeepPocketsMod
{
    public static final String MODID = "deeppockets";
    public static final String VERSION = "0.1";

	@Mod.Instance(MODID)
	public static DeepPocketsMod instance;
	
	@SidedProxy(serverSide = "com.rebelkeithy.deeppockets.proxy.ServerProxy", clientSide = "com.rebelkeithy.deeppockets.proxy.ClientProxy")
	public static CommonProxy proxy;
	
    @EventHandler
    public void preinit(FMLPreInitializationEvent e){
    	proxy.preinit(e);
    
    	Compatability.init();    	
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		DeepPocketsConfig.init(config);		
		  	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e){
    	proxy.init(e);    
    	Recipies.registerRecipies();    	
    	MinecraftForge.EVENT_BUS.register(new ItemPickupEvent());
    }   
}
