package com.firstarchon.arcana.init;

import com.firstarchon.arcana.block.BlockArcanea;
import com.firstarchon.arcana.block.BlockTestBlock;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
public static final BlockArcanea BlockTestBlock = new BlockTestBlock();


public static void init()
{
	GameRegistry.registerBlock(BlockTestBlock, "BlockTestBlock");
}
}

