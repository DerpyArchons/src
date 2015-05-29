package com.firstarchon.arcana.block;

import com.firstarchon.arcana.entities.TileEntityArcana;
import com.firstarchon.arcana.referance.Reference;
import com.firstarchon.arcana.creativetab.CreativeTabArcanaBlocks;
import com.firstarchon.arcana.referance.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockArcanaTE extends BlockContainer{
	
	IIcon[] icons = new IIcon[10];
	private String name = "BlockArcanaTE";
	
	public BlockArcanaTE()
	{
		super( Material.rock );
		this.setBlockName((Reference.MOD_ID + ":" + name));
		this.setHardness(2F);
		this.setResistance(5F);
		this.setStepSound(soundTypeStone);
		this.setHarvestLevel("Pickaxe", 2);
		this.setCreativeTab(Reference.BLOCK_TAB);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void registerBlockIcons(IIconRegister part1IconRegister)
	{
		//IIcon[] icons = new IIcon[10];
		
		for (int i = 0; i< icons.length; i++)
		{
			icons[i] = part1IconRegister.registerIcon(Reference.MOD_ID + ":" + "BlockArcanaTE" + i);
		}
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return icons[par2];
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		// TODO Auto-generated method stub
		return new TileEntityArcana();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if(entity != null)
		{
			if (entity instanceof TileEntityArcana)
			{
				TileEntityArcana arcanaEntity = (TileEntityArcana) entity;
				
				if(!world.isRemote)
				{
					arcanaEntity.editCounter();
					
					return true;
				}
			}
		}
		
		return false;
	
	}

	
}
