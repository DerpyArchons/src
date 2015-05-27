package com.firstarchon.arcana.item.food;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemCookedBacon extends ItemModFood {


	public ItemCookedBacon() {
		
		
		super("ItemCookedBacon", 10 , 1, true, new PotionEffect(Potion.regeneration.id, 1200, 0),  new PotionEffect(Potion.field_76444_x.id, 1200, 0));
		this.setAlwaysEdible();
	}

}
