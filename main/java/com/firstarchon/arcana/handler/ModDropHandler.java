package com.firstarchon.arcana.handler;

import java.util.Random;

import com.firstarchon.arcana.init.ModItems;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityPig;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class ModDropHandler {
 public static double rand;
 public Random r = new Random();

 @SubscribeEvent 
  public void onEntityDrop(LivingDropsEvent event) {
   if(event.entityLiving instanceof EntityPig) {
    if(r.nextInt(100)<75){
     
    
    event.entityLiving.dropItem(ModItems.ItemRawBacon, r.nextInt(2));
    }
   }
  }
  
 }
