package com.firstarchon.arcana.block;

import com.firstarchon.arcana.creativetab.CreativeTabArcanaBlocks;
import com.firstarchon.arcana.referance.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockArcana extends Block
{
	public BlockArcana(String baseName ,Material material) {
		  super(material);
		setBlockName(Reference.MOD_ID + ":" + baseName);
	    setBlockTextureName(Reference.MOD_ID + ":" + baseName);
	    this.setCreativeTab(Reference.BLOCK_TAB);
	   	}

}
