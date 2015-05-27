package com.firstarchon.arcana.referance;
import com.firstarchon.arcana.block.BlockArcana;
import com.firstarchon.arcana.block.ore.BlockSpiritShardOre;
import com.firstarchon.arcana.creativetab.CreativeTabArcanaBlocks;
import com.firstarchon.arcana.creativetab.CreativeTabArcanaItems;
import com.firstarchon.arcana.init.ModBlocks;
import com.firstarchon.arcana.init.ModItems;
import com.firstarchon.arcana.item.ItemArcana;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Reference {
public static final String MOD_ID = "arcana";
public static final String MOD_NAME = "Arcana";
public static final String VERSION = "1.7.10-0.1";
public static final String CLIENT_PROXY_CLASS = "com.firstarchon." + MOD_ID + ".proxy.ClientProxy";
public static final String SERVER_PROXY_CLASS = "com.firstarchon." + MOD_ID + ".proxy.ServerProxy";
public static final String GUI_FACTORY_CLASS = "com.firstarchon." + MOD_ID + ".client.gui.GuiFactory";
public static final CreativeTabs ITEM_TAB = CreativeTabArcanaItems.CreativeTabArcanaItems;
public static final CreativeTabs BLOCK_TAB = CreativeTabArcanaBlocks.CreativeTabArcanaBlocks;
//sets item tab icon
public static Item GetItemIcon() {
	return ModItems.ItemSpiritCore;
}
//sets block icon
public static Item GetBlockIcon() {
	return   Item.getItemFromBlock(ModBlocks.BlockSpiritShardOre);
}

}
