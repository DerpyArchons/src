package com.firstarchon.arcana.keybindings;

import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
//import net.java.games.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
 
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;

public class ArcanaKeybindings 
{

    // Declare two KeyBindings, ping and pong
    public static  KeyBinding ping;
    public static  KeyBinding pong;

    public static void init() 
    {
        // Define the "ping" binding, with (unlocalized) name "key.ping" and
        // the category with (unlocalized) name "key.categories.mymod" and
        // key code 24 ("O", LWJGL constant: Keyboard.KEY_O)
        ping = new KeyBinding("key.ping", Keyboard.KEY_O, "key.categories.inventory");
        // Define the "pong" binding, with (unlocalized) name "key.pong" and
        // the category with (unlocalized) name "key.categories.mymod" and
        // key code 25 ("P", LWJGL constant: Keyboard.KEY_P)
        pong = new KeyBinding("key.pong", Keyboard.KEY_P, "key.categories.inventory");

        // Register both KeyBindings to the ClientRegistry
        ClientRegistry.registerKeyBinding(ping);
        ClientRegistry.registerKeyBinding(pong);
    }
}    

