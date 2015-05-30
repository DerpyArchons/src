package com.firstarchon.arcana.handler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.java.games.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

public class KeyHandlerTutorial // note that before we extended KeyHandler, but that class no longer exists
{
	/** Key index for easy handling */
	public static final int CUSTOM_INV = 0;
	/** Key descriptions; use a language file to localize the description later */
	private static final String[] desc = {"key.tut_inventory.desc"};
	/** Default key values */
	//private static final int[] keyValues = {Keyboard.KEY_P};
	private final KeyBinding[] keys;
	
	public KeyHandlerTutorial()
	{
		keys = new KeyBinding[desc.length];
		for (int i = 0; i < desc.length; ++i) 
		{
			//keys[i] = new KeyBinding(desc[i], keyValues[i], "key.tutorial.category");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}
	/**
	 * KeyInputEvent is in the FML package, so we must register to the FML event bus
	 */
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) 
	{
		// FMLClientHandler.instance().getClient().inGameHasFocus
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) 
		{
			if (keys[CUSTOM_INV].isPressed())
			{
			//	TutorialMain.packetPipeline.sendToServer(new OpenGuiPacket(TutorialMain.GUI_CUSTOM_INV));

			}

		}

	}
}