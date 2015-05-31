package com.firstarchon.arcana;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.firstarchon.arcana.block.ore.*;
import com.firstarchon.arcana.handler.ConfigurationHandler;
import com.firstarchon.arcana.handler.KeyInputHandler;
import com.firstarchon.arcana.init.ModBlocks;
import com.firstarchon.arcana.init.ModEntities;
import com.firstarchon.arcana.init.ModEventHandler;
import com.firstarchon.arcana.init.ModFluids;
import com.firstarchon.arcana.init.ModItems;
import com.firstarchon.arcana.init.Recipies;
import com.firstarchon.arcana.init.SmeltingRecipies;
import com.firstarchon.arcana.keybindings.ArcanaKeybindings;
import com.firstarchon.arcana.proxy.IProxy;
import com.firstarchon.arcana.referance.Reference;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class Arcana {
	
	@Mod.Instance(Reference.MOD_ID)
	public static Arcana instance;
	
	@SidedProxy(clientSide =Reference.CLIENT_PROXY_CLASS , serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;
	
	
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		ModItems.init();
		ModBlocks.init();
		ModEventHandler.init();
		ModEntities.init();
		ModFluids.init();
		ArcanaKeybindings.init();
		FMLCommonHandler.instance().bus().register(new KeyInputHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		Recipies.init();
		SmeltingRecipies.init();
	}
	

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		
	}
}
