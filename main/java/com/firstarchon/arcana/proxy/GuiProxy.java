package com.firstarchon.arcana.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import  com.firstarchon.arcana.gui.*;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

        public void registerKeyBindings () {}

        @Override
        public Object getServerGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
                return null;
        }

        @Override
        public Object getClientGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
                if ( ID == GuiBasic.GUI_ID )
                        return new GuiBasic();
                
                return null;
        }
}
