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
		
		GameRegistry.addRecipe(new ItemStack (ModBlocks.BlockArcanaTE),
				"sgs",
				"grg",
				"sgs",
				's', new ItemStack(ModItems.ItemSpiritCore),
				'g', Blocks.glass,
				'r', Blocks.redstone_block);
		
		GameRegistry.addRecipe(new ItemStack (ModItems.ItemBlankCircuit),
				"rbr",
				"ggg",
				"iri",
				'r', new ItemStack(Items.redstone),
				'g', new ItemStack(Items.dye, 1, 2),
				'i', new ItemStack(Items.iron_ingot),
				'b', new ItemStack(Blocks.wool,1,15));
		
		
		  GameRegistry.addRecipe(new ItemStack (ModItems.ItemSpiritCircuit),
		 
				"rcr",
				"sfs",
				"rsr",
				'r', new ItemStack(Items.redstone),
				'c', new ItemStack(ModItems.ItemSpiritCore),
				's', new ItemStack(ModItems.ItemSpiritShard),
				'f', new ItemStack(ModItems.ItemBlankCircuit));
		  GameRegistry.addRecipe(new ItemStack(ModItems.ItemInfusionCircuit),
				  "rir",
				  "ici",
				  "rir",
				  'r', new ItemStack(Items.redstone),
				  'i', new ItemStack(ModItems.ItemInfusionGem),
				  'c', new ItemStack(ModItems.ItemSpiritCircuit));
		  
		  GameRegistry.addRecipe(new ItemStack(ModItems.ItemProjectionCircuit),
				  "rir",
				  "ici",
				  "rir",
				  'r', new ItemStack(Items.redstone),
				  'i', new ItemStack(ModItems.ItemProjectionGem),
				  'c', new ItemStack(ModItems.ItemSpiritCircuit));
	}

}
