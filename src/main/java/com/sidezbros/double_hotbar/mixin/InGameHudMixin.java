package com.sidezbros.double_hotbar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper{

	
	private final int shift = 21;
	private boolean onScreen = false;
	@Shadow private int scaledHeight;
	@Shadow private int scaledWidth;
	@Shadow abstract PlayerEntity getCameraPlayer();
	@Shadow abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
	private void renderHotbarFrame(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		this.drawTexture(matrices, this.scaledWidth / 2 - 91, this.scaledHeight - 22 - this.shift, 0, 0, 182, 22);
		this.onScreen = true;
	}
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
	private void renderHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		int m = 1;
		for (int n2 = 0; n2 < 9; ++n2) {
	            int o = this.scaledWidth / 2 - 90 + n2 * 20 + 2;
	            int p = this.scaledHeight - 16 - 3;
	            this.renderHotbarItem(o, p-this.shift, tickDelta, getCameraPlayer(), getCameraPlayer().getInventory().main.get(n2+27), m++);
	    }
	}
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasStatusBars()Z"))
	public void shiftStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
		if(this.onScreen) {
			matrices.translate(0, -this.shift, 0);
		}
	}
	

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getSleepTimer()I", ordinal = 0))
	public void returnStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
		if(this.onScreen) {
			matrices.translate(0, this.shift, 0);
		}
		this.onScreen = false;
	}

}
