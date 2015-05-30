package com.firstarchon.arcana.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.firstarchon.arcana.block.fluid.*;
import com.firstarchon.arcana.init.*;
import com.firstarchon.arcana.referance.Reference;

public class ItemBucketOfFusilisAnimus extends ItemBucket{

	public static Block isFull;
	private String name = "ItemBucketOfFusilisAnimus";	
	ItemStack itemstack = new ItemStack(Items.bucket);
	
	public ItemBucketOfFusilisAnimus()
	{
		super(isFull);
		setUnlocalizedName(Reference.MOD_ID + ":" + name);
		setContainerItem(Items.bucket);
		setCreativeTab(CreativeTabs.tabMisc);
		setTextureName(Reference.MOD_ID + ":" + name);
		
	}

	/*
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, World world, MovingObjectPosition pos)
	  {
		 return itemstack;
		 

	  }
*/
	
}
