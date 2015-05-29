package com.firstarchon.arcana.init;

import net.minecraft.world.World;

import com.firstarchon.arcana.entities.*;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModEntities 
{
/*
 * int x, y, z;
 
	MysticPedistalTileEntity tile = (MysticPedistalTileEntity) World.getTileEntity(x, y, z );
	{
		if (tile != null)
		{
			int l = tile.Field1;
		}
	}
	*/
	public static void init()
	{
		GameRegistry.registerTileEntity(TileEntityArcana.class, "TE_arcanaTE");
	}
	
}


