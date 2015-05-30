package com.firstarchon.arcana.item;

import net.minecraft.item.ItemStack;


public class ItemChargedSpiritCore extends ItemArcana 
{
		public ItemChargedSpiritCore()
		{
			super("itemChargedSpiritCore");
			this.setMaxStackSize(16);
		}
		
		@Override
		public boolean hasEffect(ItemStack par1ItemStack)
		{
			return true;
		}
}