package com.firstarchon.arcana.creativetab;

import com.firstarchon.arcana.init.ModItems;
import com.firstarchon.arcana.referance.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabArcanaItems {
	public static final CreativeTabs CreativeTabArcanaItems = new CreativeTabs(Reference.MOD_ID)
	   {
	     @Override
     public Item getTabIconItem()
	      {
	       
	    	 return Reference.GetItemIcon();
	    	 
	    
	    	 
	     }

	       @Override
	       public String getTranslatedTabLabel()
        {
	          return "Arcana Items";
        }
	   };
	}