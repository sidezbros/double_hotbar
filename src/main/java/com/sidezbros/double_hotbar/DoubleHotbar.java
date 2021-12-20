package com.sidezbros.double_hotbar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;

public class DoubleHotbar implements ClientModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("double_hotbar");
	private static KeyBinding keyBinding;
	
	@Override
	public void onInitializeClient() {
		DHModConfig.init();
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			    "key.double_hotbar.swap",
			    InputUtil.Type.KEYSYM,
			    GLFW.GLFW_KEY_R,
			    "category.double_hotbar.keybinds"
			));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    while (keyBinding.wasPressed()) {
		    	this.swapRows(client.player);
		    }
		});
	}
	
    public void swapRows(PlayerEntity player) {
    	@SuppressWarnings("resource")
		ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
    	if (interactionManager == null) {
    		return;
    	}
    	for(int i = 0; i < 9; i++) {
    		interactionManager.clickSlot(player.playerScreenHandler.syncId, DHModConfig.INSTANCE.inventoryRow*9+i, i, SlotActionType.SWAP, player);
    	}
    }
}
