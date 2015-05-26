package com.firstarchon.arcana.handler;

import java.io.File;

import com.firstarchon.arcana.referance.Reference;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	public static Configuration configuration;
	public static boolean testValue = false;
	
	public static void init(File configFile)
	{
		if (configuration == null)
		{
			configuration = new Configuration(configFile);
			loadConfiguration();
		
		}
		
	}
	@SubscribeEvent
	public void onconfingurationChangedEvent(OnConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
        	loadConfiguration();
			
		}
	}
private static void loadConfiguration()
{
	testValue = configuration.getBoolean("configValeu", configuration.CATEGORY_GENERAL, false, "this is an example config vale");
	if (configuration.hasChanged())
	{
		configuration.save();
	}
}
           
}
