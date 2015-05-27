package com.firstarchon.arcana.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipies {
	public static void init()
	{
		//SpiritCore Recipe
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (ModItems.ItemSpiritCore),
				"sgs",
				"gdg", 
				"sgs",
				's', new ItemStack(ModItems.ItemSpiritShard),
				'g', "blockGlass",
				'd', new ItemStack(Items.diamond)));
		
	}

}
