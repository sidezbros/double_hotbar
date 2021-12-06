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
	@Shadow private int scaledHeight;
	@Shadow private int scaledWidth;
	@Shadow abstract PlayerEntity getCameraPlayer();
	@Shadow abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
	private void renderHotbarFrame(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		this.drawTexture(matrices, this.scaledWidth / 2 - 91, this.scaledHeight - 22 - this.shift, 0, 0, 182, 22);
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
	
	@Inject(method = "renderStatusBars", at = @At(value = "HEAD"))
	private void shiftStatusBars(MatrixStack matrices, CallbackInfo info) {
		this.scaledHeight -= this.shift;
	}
	
	@Inject(method = "renderStatusBars", at = @At(value = "TAIL"))
	private void returnStatusBars(MatrixStack matrices, CallbackInfo info) {
		this.scaledHeight += this.shift;
	}
	
	@Inject(method = "renderExperienceBar", at = @At(value = "HEAD"))
	private void shiftExperienceBar(MatrixStack matrices, int x, CallbackInfo info) {
		this.scaledHeight -= this.shift;
	}
	
	@Inject(method = "renderExperienceBar", at = @At(value = "TAIL"))
	private void returnExperienceBar(MatrixStack matrices, int x, CallbackInfo info) {
		this.scaledHeight += this.shift;
	}
	
	@Inject(method = "renderMountJumpBar", at = @At(value = "HEAD"))
	private void shiftMountJumpBar(MatrixStack matrices, int x, CallbackInfo info) {
		this.scaledHeight -= this.shift;
	}
	
	@Inject(method = "renderMountJumpBar", at = @At(value = "TAIL"))
	private void returnMountJumpBar(MatrixStack matrices, int x, CallbackInfo info) {
		this.scaledHeight += this.shift;
	}
	
	@Inject(method = "renderMountHealth", at = @At(value = "HEAD"))
	private void shiftMountHealth(MatrixStack matrices, CallbackInfo info) {
		this.scaledHeight -= this.shift;
	}
	
	@Inject(method = "renderMountHealth", at = @At(value = "TAIL"))
	private void returnMountHealth(MatrixStack matrices, CallbackInfo info) {
		this.scaledHeight += this.shift;
	}
	
	@Inject(method = "renderHeldItemTooltip", at = @At(value = "HEAD"))
	private void shiftHeldItemTooltip(MatrixStack matrices, CallbackInfo info) {
		this.scaledHeight -= this.shift;
	}
	
	@Inject(method = "renderHeldItemTooltip", at = @At(value = "TAIL"))
	private void returnHeldItemTooltip(MatrixStack matrices, CallbackInfo info) {
		this.scaledHeight += this.shift;
	}

}
