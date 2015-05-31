package com.firstarchon.arcana.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.firstarchon.arcana.referance.*;
import com.firstarchon.arcana.block.fluid.*;
import com.firstarchon.arcana.handler.BucketHandler;
import com.firstarchon.arcana.item.ItemBucketOfFusilisAnimus;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModFluids 

{

	public static Fluid FusilisAnimus;

	public static Block BlockFusilisAnimus;
	public static final ItemBucketOfFusilisAnimus ItemBucketOfFusilisAnimus = new ItemBucketOfFusilisAnimus();


	public static void init()
 
	{

		FusilisAnimus = new Fluid("FusilisAnimus");

		FluidRegistry.registerFluid(FusilisAnimus);

		FusilisAnimus.setViscosity(500);
		FusilisAnimus.setLuminosity(15);
		FusilisAnimus.isGaseous();
		FusilisAnimus.setUnlocalizedName("FusilisAnimus");

 

		BlockFusilisAnimus = new BlockFusilisAnimus(FusilisAnimus, Material.water);

		GameRegistry.registerBlock(ModFluids.BlockFusilisAnimus,Reference.MOD_ID + "_" +   "FusilisAnimus");

		FluidContainerRegistry.registerFluidContainer(FusilisAnimus, new ItemStack(ItemBucketOfFusilisAnimus), new ItemStack(Items.bucket));
	
		GameRegistry.registerItem(ItemBucketOfFusilisAnimus, "ItemBucketOfFusilisAnimus");
 
		BucketHandler.INSTANCE.buckets.put(BlockFusilisAnimus, ItemBucketOfFusilisAnimus);
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	}

}
