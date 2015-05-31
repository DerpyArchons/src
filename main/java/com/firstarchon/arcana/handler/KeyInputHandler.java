package com.firstarchon.arcana.handler;

import org.lwjgl.input.Keyboard;

import com.firstarchon.arcana.keybindings.ArcanaKeybindings;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;



public class KeyInputHandler extends ArcanaKeybindings {
	


    public static final ArcanaKeybindings ArcanaKeybindings = new ArcanaKeybindings();
	
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(KeyBinding.ping.isPressed())
            System.out.println("ping");
        if(KeyBinding.pong.isPressed())
            System.out.println("pong");
    }

}