package com.firstarchon.arcana.init;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class SmeltingRecipies {

	public static void init()
	{
		GameRegistry.addSmelting(ModItems.ItemRawBacon, new ItemStack( ModItems.ItemCookedBacon), .1F);
	}
	
}
