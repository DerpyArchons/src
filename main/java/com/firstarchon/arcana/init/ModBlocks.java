package com.firstarchon.arcana.init;

import com.firstarchon.arcana.block.*;
//import com.firstarchon.arcana.block.BlockTestBlock;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
public static final BlockArcana BlockTestBlock = new BlockTestBlock();
public static final BlockArcana BlockSpiritShardOre = new BlockSpiritShardOre();


public static void init()
{
	GameRegistry.registerBlock(BlockTestBlock, "BlockTestBlock");
	GameRegistry.registerBlock(BlockSpiritShardOre, "BlockSpiritShardOre");
}
}

