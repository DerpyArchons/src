package com.firstarchon.arcana.item;

import com.firstarchon.arcana.referance.Reference;

import net.minecraft.item.Item;

public class ItemArcana extends Item{
	ItemArcana(String baseName) {
		setUnlocalizedName(Reference.MOD_ID + ":" + baseName);
	    setTextureName(Reference.MOD_ID + ":" + baseName);
	    

 
	}
}
