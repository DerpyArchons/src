package com.firstarchon.arcana.block;

import com.firstarchon.arcana.entities.TileEntityArcana;
import com.firstarchon.arcana.entities.TileEntitySpiritExtracter;
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


public class BlockSpiritExtracter extends BlockArcana { 

	public IIcon[] icons = new IIcon[6];
	
    public BlockSpiritExtracter() 
    { 
        super("BlockSpiritExtracter", Material.iron);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setStepSound(soundTypeMetal);
    }
    @Override
    
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 6; i ++) {
            this.icons[i] = reg.registerIcon(this.textureName + "Off" + "_" + i);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[side];
    }

}