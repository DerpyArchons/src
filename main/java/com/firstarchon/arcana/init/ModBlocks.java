package com.firstarchon.arcana.init;

import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.block.BlockContainer;
import com.firstarchon.arcana.block.*;
import com.firstarchon.arcana.block.ore.BlockSpiritShardOre;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
public static final BlockTestBlock BlockTestBlock = new BlockTestBlock();
public static final BlockSpiritShardOre BlockSpiritShardOre = new BlockSpiritShardOre();
public static final BlockMysticPedestal BlockMysticPedistal = new BlockMysticPedestal();
public static final BlockArcanaTE BlockArcanaTE = new BlockArcanaTE();

public static void init()
{
	GameRegistry.registerBlock(BlockTestBlock, "BlockTestBlock");
	GameRegistry.registerBlock(BlockSpiritShardOre, "BlockSpiritShardOre");
	GameRegistry.registerBlock(BlockMysticPedistal, "BlockMysticPedistal");
	GameRegistry.registerBlock(BlockArcanaTE, "BlockArcanaTE");
	//adds spirit ore to ore dictionary.
	OreDictionary.registerOre("oreSpirit",BlockSpiritShardOre );
	
}
}

