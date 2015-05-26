package com.firstarchon.arcana.creativetab;

import com.firstarchon.arcana.init.ModItems;
import com.firstarchon.arcana.referance.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabArcanaBlocks {
	public static final CreativeTabs CreativeTabArcanaBlocks = new CreativeTabs(Reference.MOD_ID)
	   {
	     @Override
     public Item getTabIconItem()
	      {
	           return ModItems.ItemDerrpybook;
	     }

	       @Override
	       public String getTranslatedTabLabel()
        {
	          return "Arcana Blocks";
        }
	   };
	}
