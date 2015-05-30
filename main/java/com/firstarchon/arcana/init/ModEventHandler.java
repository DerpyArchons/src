package com.firstarchon.arcana.init;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

import com.firstarchon.arcana.block.fluid.BlockFusilisAnimus;
import com.firstarchon.arcana.handler.*;
import com.firstarchon.arcana.init.ModFluids;

public class ModEventHandler {

	static ArcanaEventhandler handler = new ArcanaEventhandler();
	static BucketHandler buckethandler = new BucketHandler();
	
	public static void init()
	{
		GameRegistry.registerWorldGenerator(handler, 0);
		MinecraftForge.EVENT_BUS.register(new ModDropHandler());
		
		//FMLCommonHandler.instance().bus().register(new KeyHandlerTutorial());
	
	}
	
}
