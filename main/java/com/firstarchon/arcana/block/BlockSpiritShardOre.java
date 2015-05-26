package com.firstarchon.arcana.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import com.firstarchon.arcana.handler.ArcanaEventhandler;
import com.firstarchon.arcana.creativetab.*;
import com.firstarchon.arcana.init.ModItems;
import com.firstarchon.arcana.init.ModEventHandler;

public class BlockSpiritShardOre extends BlockArcana
{
	private static final int least_quantity = 1;
	private static final int most_quantity = 4;
	Random random = new Random();
	int maxX = 16; //minX is one, so setting this to 16 says all of the x ranges can be used
	int maxZ = 16; //minY is one, so setting this to 16 says all of the y ranges can be used
	int maxVeinSize = 4 + random.nextInt(5); //this means each patch of our blocks will be between 4 and 8 large
	int chancesToSpawn = 50; //How many times our ore can spawn in one chunk
	int minY = 15; //how low the ore can spawn
	int maxY = 80; //how high the ore can spawn
	public BlockSpiritShardOre()
	{
		super("BlockSpiritShardOre", Material.rock );
		this.setBlockName("BlockSpiritShardOre");
		this.setCreativeTab(CreativeTabArcanaBlocks.CreativeTabArcanaBlocks);
		setHardness(2F);
		setResistance(5F);
		setStepSound(soundTypeStone);
		setHarvestLevel("Pickaxe", 2);
		//this.addOreSpawn()
	}

	public Item getItemDropped(int meta, Random rand, int fortune)
	{
		return ModItems.ItemSpiritShard;
		
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
	    if (this.least_quantity >= this.most_quantity)
	        return this.least_quantity;
	    return this.least_quantity + random.nextInt(this.most_quantity - this.least_quantity + fortune + 1);
	}
}

