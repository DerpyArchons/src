package com.firstarchon.arcana.init;

import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.block.BlockContainer;
import com.firstarchon.arcana.block.*;
import com.firstarchon.arcana.block.ore.*;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
public static final BlockTestBlock BlockTestBlock = new BlockTestBlock();
public static final BlockSpiritShardOre BlockSpiritShardOre = new BlockSpiritShardOre();
public static final BlockMysticPedestal BlockMysticPedistal = new BlockMysticPedestal();
public static final BlockArcanaTE BlockArcanaTE = new BlockArcanaTE();
public static final BlockInfusionGemOre BlockInfusionGemOre = new BlockInfusionGemOre();
public static final BlockProjectionGemOre BlockProjectionGemOre = new BlockProjectionGemOre();
public static final BlockSpiritExtracter BlockSpiritExtracter = new BlockSpiritExtracter();

public static void init()
{
	GameRegistry.registerBlock(BlockTestBlock, "BlockTestBlock");
	GameRegistry.registerBlock(BlockSpiritShardOre, "BlockSpiritShardOre");
	GameRegistry.registerBlock(BlockMysticPedistal, "BlockMysticPedistal");
	GameRegistry.registerBlock(BlockArcanaTE, "BlockArcanaTE");
	GameRegistry.registerBlock(BlockInfusionGemOre, "BlockInfusionGemOre");
	GameRegistry.registerBlock(BlockProjectionGemOre, "BlockProjectionGemOre");
	GameRegistry.registerBlock(BlockSpiritExtracter, "BlockSpiritExtracter");
	
	OreDictionary.registerOre("oreSpirit",BlockSpiritShardOre );
	
}
}

