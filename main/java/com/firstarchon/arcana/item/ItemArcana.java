package com.firstarchon.arcana.item;

import com.firstarchon.arcana.referance.Reference;

import net.minecraft.item.Item;
//base item arcana item class. handles setting of unlocalized name and texture name.
public class ItemArcana extends Item{
	protected ItemArcana(String baseName) {
		//generates a unlocalized name from baseName. basename is declaired in the item class and passed to this method via the super() constructor.
		setUnlocalizedName(Reference.MOD_ID + ":" + baseName);
		//sets texture name. texturenames should be "basename.png" I think the "MOD_ID +:" part tells it where to look in the filpath for the .png
	    setTextureName(Reference.MOD_ID + ":" + baseName);
	    //sets all items to ITEM_TAB
	   this.setCreativeTab(Reference.ITEM_TAB);

 
	}
}
