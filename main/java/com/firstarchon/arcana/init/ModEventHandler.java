package com.firstarchon.arcana.init;

import cpw.mods.fml.common.registry.GameRegistry;

import com.firstarchon.arcana.handler.*;

public class ModEventHandler {

	static ArcanaEventhandler handler = new ArcanaEventhandler();
	
	public static void init()
	{
		GameRegistry.registerWorldGenerator(handler, 0);
	}
	
}
