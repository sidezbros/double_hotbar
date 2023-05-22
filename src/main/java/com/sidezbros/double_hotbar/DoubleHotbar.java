package com.sidezbros.double_hotbar;

import java.time.Instant;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
	private boolean[] hotbarKeys = new boolean[10];
	private long[] timer = new long[10];
	private boolean alreadySwapped = false;

	@Override
	public void onInitializeClient() {
		DHModConfig.init();
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.double_hotbar.swap", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R, "category.double_hotbar.keybinds"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (DHModConfig.INSTANCE.holdToSwap) {
				if (keyBinding.isPressed() != this.hotbarKeys[9]) {
					this.hotbarKeys[9] = keyBinding.isPressed();
					if (keyBinding.isPressed()) {
						timer[9] = Instant.now().toEpochMilli();
					} else {
						if (Instant.now().toEpochMilli() - timer[9] < DHModConfig.INSTANCE.holdTime) {
							this.swapStack(client.player, !DHModConfig.INSTANCE.holdToSwapBar, client.player.getInventory().selectedSlot);
						} else {
							this.alreadySwapped = false;
						}
					}
				}
				if (!this.alreadySwapped && keyBinding.isPressed()
						&& Instant.now().toEpochMilli() - timer[9] > DHModConfig.INSTANCE.holdTime) {
					this.swapStack(client.player, DHModConfig.INSTANCE.holdToSwapBar, client.player.getInventory().selectedSlot);
					this.alreadySwapped = true;
				}
			} else {
				while (keyBinding.wasPressed()) {
					this.swapStack(client.player, true, 0);
				}
			}
			if (DHModConfig.INSTANCE.allowDoubleTap) {
				for (int i = 0; i < 9; i++) {
					if (client.options.hotbarKeys[i].isPressed() != this.hotbarKeys[i]) {
						this.hotbarKeys[i] = client.options.hotbarKeys[i].isPressed();
						if (client.options.hotbarKeys[i].isPressed()) {
							if (Instant.now().toEpochMilli() - timer[i] < DHModConfig.INSTANCE.doubleTapWindow) {
								this.swapStack(client.player, false, i);
								timer[i] = 0;
							} else {
								timer[i] = Instant.now().toEpochMilli();
							}
						}
					}
				}
			}
		});
	}

	public void swapStack(PlayerEntity player, boolean fullRow, int slot) {
		@SuppressWarnings("resource")
		ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
		if (interactionManager == null || DHModConfig.INSTANCE.disableMod) {
			return;
		}
		if (fullRow) {
			for (int i = 0; i < 9; i++) {
				interactionManager.clickSlot(player.playerScreenHandler.syncId,
						DHModConfig.INSTANCE.inventoryRow * 9 + i, i, SlotActionType.SWAP, player);
			}
			playWoosh();
		} else {
			interactionManager.clickSlot(player.playerScreenHandler.syncId,
					DHModConfig.INSTANCE.inventoryRow * 9 + slot, slot, SlotActionType.SWAP, player);
			playWoosh();
		}
	}

	public void playWoosh() {
		private Clip woosh1;
		private Clip woosh2;
		
		try {
			if (DHModConfig.INSTANCE.woosh == 1) {
				AudioInputStream woosh1AIS = AudioSystem.getAudioInputStream(this.getClass().getResource("/assets/double_hotbar/woosh1.wav"));
				woosh1 = AudioSystem.getClip();
				woosh1.open(woosh1AIS);
				woosh1.start();
			}
			else if (DHModConfig.INSTANCE.woosh == 2) {
				AudioInputStream woosh2AIS = AudioSystem.getAudioInputStream(this.getClass().getResource("/assets/double_hotbar/woosh2.wav"));
				woosh2 = AudioSystem.getClip();
				woosh2.open(woosh2AIS);
				woosh2.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
