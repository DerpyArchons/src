package com.firstarchon.arcana.init;

import net.minecraft.item.Item;

//import com.firstarchon.arcana.item.Itemderpybook;

import com.firstarchon.arcana.item.*;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static final Itemderpybook ItemDerrpybook = new Itemderpybook();
	public static final ItemPortaAstriae ItemPortaAstriae = new ItemPortaAstriae();
	protected static final Item Itemderrpybook = null;
	public static final Item ItemMundusAnimorum = new ItemMundusAnimorum();
	public static final Item ItemViaInfernae = new ItemViaInfernae();
	public static final Item ItemTerminusUltimorum = new ItemTerminusUltimorum();
	public static final Item ItemSpiritShard = new ItemSpiritShard();
	
public static void init()
{
	GameRegistry.registerItem(ItemDerrpybook, "ItemDerpybook");
	GameRegistry.registerItem(ItemPortaAstriae, "ItemPorta Astriae");
	GameRegistry.registerItem(ItemMundusAnimorum, "ItemMundusAnimorum");
	GameRegistry.registerItem(ItemViaInfernae, "ItemViaInfernae");
	GameRegistry.registerItem(ItemTerminusUltimorum, "ItemTerminusUltimorum");
	GameRegistry.registerItem(ItemSpiritShard, "IremSpiritShard");
}
}
