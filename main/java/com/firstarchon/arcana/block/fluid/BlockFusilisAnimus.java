package com.firstarchon.arcana.block.fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import com.firstarchon.arcana.block.fluid.*;
import com.firstarchon.arcana.referance.Reference;


public class BlockFusilisAnimus extends BlockFluidClassic

{

 private String name = "FusilisAnimus";

 public BlockFusilisAnimus(Fluid fluid, Material material) 

 {

 super(fluid, material);

 setCreativeTab(CreativeTabs.tabMisc);

 setBlockName(Reference.MOD_ID + "_" + name);

 }

 

 @SideOnly(Side.CLIENT)

 protected IIcon stillIcon;

 @SideOnly(Side.CLIENT)

 protected IIcon flowingIcon;

 

 @Override

 public IIcon getIcon(int side, int meta) {

 return (side == 0 || side == 1)? stillIcon : flowingIcon;

 }

 

 @SideOnly(Side.CLIENT)

 @Override

 public void registerBlockIcons(IIconRegister register) {

 stillIcon = register.registerIcon(Reference.MOD_ID + ":" + name + "_still");

 flowingIcon = register.registerIcon(Reference.MOD_ID + ":" + name + "_flowing");

 }

 

 @Override

 public boolean canDisplace(IBlockAccess world, int x, int y, int z) {

 if (world.getBlock(x, y, z).getMaterial().isLiquid()) return false;

 return super.canDisplace(world, x, y, z);

 }

 

 @Override

 public boolean displaceIfPossible(World world, int x, int y, int z) {

 if (world.getBlock(x, y, z).getMaterial().isLiquid()) return false;

 return super.displaceIfPossible(world, x, y, z);

 }

}
