package com.firstarchon.arcana.item.food;

import com.firstarchon.arcana.referance.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemModFood extends ItemFood{
	private  PotionEffect[] effects;
	
	ItemModFood(String unlocalizedName, int healAmount, double d, boolean wolvesFavorite, PotionEffect...effects){
		super(healAmount,wolvesFavorite);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(Reference.MOD_ID + ':' + unlocalizedName);
		this.setCreativeTab(CreativeTabs.tabFood);
		this.effects = effects;
	}
		
		@Override
		protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
			super.onFoodEaten(stack, world, player);

			for (int i = 0; i < this.effects.length; i++) {
				if (!world.isRemote && this.effects[i] != null && this.effects[i].getPotionID() > 0)
					player.addPotionEffect(new PotionEffect(this.effects[i].getPotionID(), this.effects[i].getDuration(), this.effects[i].getAmplifier(), this.effects[i].getIsAmbient()));
			}
		
	}
}
