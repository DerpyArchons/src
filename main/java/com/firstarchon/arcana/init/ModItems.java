package com.firstarchon.arcana.init;
import com.firstarchon.arcana.item.food.ItemRawBacon;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;



//import com.firstarchon.arcana.item.Itemderpybook;
import com.firstarchon.arcana.item.food.*;
import com.firstarchon.arcana.item.*;
import com.firstarchon.arcana.item.books.ItemMundusAnimorum;
import com.firstarchon.arcana.item.books.ItemPortaAstriae;
import com.firstarchon.arcana.item.books.ItemTerminusUltimorum;
import com.firstarchon.arcana.item.books.ItemViaInfernae;
import com.firstarchon.arcana.item.ItemBucketOfFusilisAnimus;
import com.firstarchon.arcana.block.fluid.*;
import com.firstarchon.arcana.init.ModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static final Itemderpybook ItemDerrpybook = new Itemderpybook();
	public static final ItemPortaAstriae ItemPortaAstriae = new ItemPortaAstriae();
	public static final ItemMundusAnimorum ItemMundusAnimorum = new ItemMundusAnimorum();
	public static final ItemViaInfernae ItemViaInfernae = new ItemViaInfernae();
	public static final ItemTerminusUltimorum ItemTerminusUltimorum = new ItemTerminusUltimorum();
	public static final ItemSpiritShard ItemSpiritShard = new ItemSpiritShard();
	public static final ItemSpiritCore ItemSpiritCore = new ItemSpiritCore();
	public static final ItemRawBacon ItemRawBacon = new ItemRawBacon();
	public static final ItemCookedBacon ItemCookedBacon = new ItemCookedBacon();
	public static final ItemBlankCircuit ItemBlankCircuit = new ItemBlankCircuit();
	public static final ItemSpiritCircuit ItemSpiritCircuit = new ItemSpiritCircuit();
	public static final ItemInfusionCircuit ItemInfusionCircuit = new ItemInfusionCircuit();
	public static final ItemProjectionCircuit ItemProjectionCircuit = new ItemProjectionCircuit();
	public static final ItemInfusionGem ItemInfusionGem = new ItemInfusionGem();
	public static final ItemProjectionGem ItemProjectionGem = new ItemProjectionGem();
	public static final ItemChargedSpiritCore ItemChargedSpiritCore = new ItemChargedSpiritCore();
	
	public static void init()
	{
		GameRegistry.registerItem(ItemDerrpybook, "ItemDerpybook");
		GameRegistry.registerItem(ItemPortaAstriae, "ItemPorta Astriae");
		GameRegistry.registerItem(ItemMundusAnimorum, "ItemMundusAnimorum");
		GameRegistry.registerItem(ItemViaInfernae, "ItemViaInfernae");
		GameRegistry.registerItem(ItemTerminusUltimorum, "ItemTerminusUltimorum");
		GameRegistry.registerItem(ItemSpiritShard, "ItemSpiritShard");
		GameRegistry.registerItem(ItemSpiritCore, "ItemSpiritCore");
		GameRegistry.registerItem(ItemRawBacon, "ItemRawBacon");
		GameRegistry.registerItem(ItemCookedBacon, "ItemCoockedBacon");
		GameRegistry.registerItem(ItemBlankCircuit, "ItemBlankCircuit");
		GameRegistry.registerItem(ItemSpiritCircuit, "ItemSpiritCircuit");
		GameRegistry.registerItem(ItemInfusionCircuit, "ItemInfusionCircuit");
		GameRegistry.registerItem(ItemProjectionCircuit, "ItemProjectionCircuit");
		GameRegistry.registerItem(ItemInfusionGem, "ItemInfusionGem");
		GameRegistry.registerItem(ItemProjectionGem, "ItemProjectionGem");
		GameRegistry.registerItem(ItemChargedSpiritCore, "ItemChargedSpiritCore");
		
	}
}
