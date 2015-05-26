package com.firstarchon.arcana.init;

import cpw.mods.fml.common.registry.GameRegistry;
import block.BlockArcanea;
import block.BlockTestBlock;

public class ModBlocks {
public static final BlockArcanea BlockTestBlock = new BlockTestBlock();


public static void init()
{
	GameRegistry.registerBlock(BlockTestBlock, "BlockTestBlock");
}
}

