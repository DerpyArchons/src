package com.firstarchon.arcana.block;

import com.firstarchon.arcana.entities.MysticPedistalTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.client.model.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockMysticPedestal extends BlockArcana 
{
	public BlockMysticPedestal()
	{
		super("BlockMysticPedistal", Material.rock );
		this.setBlockName("BlockMysticPedistal");
		//this.setRenderBounds(.5F, .5F, .5F, .5F, .5F, .5F);
		
	}
		
	//This will tell minecraft not to render any side of our cube.
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
	   return false;
	}

	//And this tell it that you can see through this block, and neighbor blocks should be rendered.
	public boolean isOpaqueCube()
	{
	   return false;
	}
		  
	public TileEntity createTileEntity(World world, int metadata)
	{
	   return new MysticPedistalTileEntity();
	}
	
}


