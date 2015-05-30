package com.firstarchon.arcana.handler;

import java.util.Random;

import com.firstarchon.arcana.block.ore.BlockProjectionGemOre;
import com.firstarchon.arcana.init.ModBlocks;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class ArcanaEventhandler implements IWorldGenerator {

	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
	
		switch(world.provider.dimensionId)
		{
		case -1: generateNether (world, random, chunkX * 16 ,chunkZ * 16);
		case 0: generateSurface (world, random, chunkX * 16 , chunkZ * 16);
		case 1: generateEnd (world, random, chunkX * 16, chunkZ * 16);
		}
		
	}

	private void generateEnd (World world, Random random, int x, int z)
	{
		
	}
	
	private void generateSurface (World world, Random random, int x ,int z)
	{
	
		addOreSpawn(ModBlocks.BlockSpiritShardOre, 0, Blocks.stone, world, random, x, z, 16, 16, 5 + random.nextInt(5), 20, 20, 60);
	}
	
	private void generateNether (World world, Random random, int x ,int z)
	{
		addOreSpawn(ModBlocks.BlockInfusionGemOre, 0, Blocks.netherrack, world, random, x, z, 16, 16, 5 + random.nextInt(5), 20, 20, 60);
		addOreSpawn(ModBlocks.BlockProjectionGemOre, 0, Blocks.netherrack, world, random, x, z, 16, 16, 5 + random.nextInt(5), 20, 20, 60);
	}

/**
 * Adds an Ore Spawn to Minecraft. Simply register all Ores to spawn with this method in your generation method in your IWorldGeneration extending class. 
 * 
 * @param The Block to spawn
 * @param The metadata of the Block
 * @param The Block where to generate in
 * @param A Random object for retrieving random positions within the world to spawn the block
 * @param An int for passing the X-Coordinate for the generation method
 * @param An int for passing the Y-Coordinate for the generation method
 * @param An int for setting the maximum X-Coordinate values for spawning on the X-Axis on a Per-Chunk basis
 * @param An int for setting the maximum X-Coordinate values for spawning on the Z-Axis on a Per-Chunk basis 
 * @param An int for setting the maximum size of a vein
 * @param An int for setting the number of chances available for  the Block to spawn per-chunk
 * @param An int for the minimum Y coordinate height at which this block may spawn
 * @param An int for the maximum Y coordinate height at which this block may spawn
 */
	public void addOreSpawn(Block block, int metadata, Block target, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chancesToSpawn, int minY, int maxY)
	{
		int maxPossY = minY + (maxY -1);
		assert maxY > minY: "The maximum Y must be greater than the Minimum Y";
		assert maxX > 0 && maxX <=16:"addOreSpawn: The maximum X must be greater than 0 and less than 16";
		assert minY >0:"addOreSpawn: The Minimum Y must be grater than 0";
		assert maxY < 256 && maxY > 0:"addOreSpawn: The Maximum Y must be less than 256 but greater than 0";
		assert maxZ >0 && maxZ <= 16:"addOreSpawn: The Maximum Z must be greater than 0 and less than 16";
		
		
		int diffBtwnMinMaxY = maxY - minY;
		for(int x=0; x < chancesToSpawn; x++)
		{
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			(new WorldGenMinable(block, metadata, maxVeinSize, target)).generate(world, random, posX, posY, posZ);
		}
	}



}
