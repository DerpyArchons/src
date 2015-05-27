package com.firstarchon.arcana.init;

import net.minecraftforge.oredict.OreDictionary;

import com.firstarchon.arcana.block.*;
import com.firstarchon.arcana.block.ore.BlockSpiritShardOre;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
public static final BlockTestBlock BlockTestBlock = new BlockTestBlock();
public static final BlockSpiritShardOre BlockSpiritShardOre = new BlockSpiritShardOre();


public static void init()
{
	GameRegistry.registerBlock(BlockTestBlock, "BlockTestBlock");
	GameRegistry.registerBlock(BlockSpiritShardOre, "BlockSpiritShardOre");
	
	//adds spirit ore to ore dictionary.
	OreDictionary.registerOre("oreSpirit",BlockSpiritShardOre );
	
}
}

