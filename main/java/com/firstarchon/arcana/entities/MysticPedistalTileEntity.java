package com.firstarchon.arcana.entities;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
/*
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
*/
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MysticPedistalTileEntity extends TileEntity{
   
	public int Field1;
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
	   super.writeToNBT(par1);
	   par1.setInteger("testField", Field1);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
	   super.readFromNBT(par1);
	   this.Field1 = par1.getInteger("testField");
	}

/*
	public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
        }

        public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        readFromNBT(packet.customParam1);
        }
*/
}
