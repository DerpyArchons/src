package com.firstarchon.arcana.item.food;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemFood;

public class ItemRawBacon extends ItemModFood {


	public ItemRawBacon() {
		  super("ItemRawBacon", 3 , 0.3, true, new PotionEffect(Potion.hunger .id, 1200, 0),
		  new PotionEffect(Potion.poison.id, 300, 0));
		  this.setAlwaysEdible();
	}

}
