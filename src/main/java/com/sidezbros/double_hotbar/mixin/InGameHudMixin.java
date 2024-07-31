package com.sidezbros.double_hotbar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sidezbros.double_hotbar.DHModConfig;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin{

	private boolean onScreen = false;
	@Shadow private static Identifier HOTBAR_TEXTURE;
	@Shadow protected abstract PlayerEntity getCameraPlayer();
	@Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
	private void renderHotbarFrame(DrawContext context, float tickDelta, CallbackInfo info) {
		if(DHModConfig.INSTANCE.displayDoubleHotbar && !DHModConfig.INSTANCE.disableMod) {
			context.drawGuiTexture(HOTBAR_TEXTURE, context.getScaledWindowWidth() / 2 - 91, context.getScaledWindowHeight() - 22 - DHModConfig.INSTANCE.shift, 182, 22-DHModConfig.INSTANCE.renderCrop);
			this.onScreen = true;
		}
		
	}
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
	private void shiftHotbarSelector(DrawContext context, float tickDelta, CallbackInfo info) {
		if(DHModConfig.INSTANCE.displayDoubleHotbar && DHModConfig.INSTANCE.reverseBars && !DHModConfig.INSTANCE.disableMod) {
			context.getMatrices().translate(0, -DHModConfig.INSTANCE.shift, 0);
		}
		
	}
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
	private void returnHotbarSelector(DrawContext context, float tickDelta, CallbackInfo info) {
		if(DHModConfig.INSTANCE.displayDoubleHotbar && DHModConfig.INSTANCE.reverseBars && !DHModConfig.INSTANCE.disableMod) {
			context.getMatrices().translate(0, DHModConfig.INSTANCE.shift, 0);
		}
	}
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"))
	private void shiftHotbarItems(DrawContext context, float tickDelta, CallbackInfo info) {
		if(DHModConfig.INSTANCE.displayDoubleHotbar && DHModConfig.INSTANCE.reverseBars && !DHModConfig.INSTANCE.disableMod) {
			context.getMatrices().translate(0, -DHModConfig.INSTANCE.shift, 0);
		}
	}
	
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
	private void renderHotbarItems(DrawContext context, float tickDelta, CallbackInfo info) {
		if(DHModConfig.INSTANCE.displayDoubleHotbar && !DHModConfig.INSTANCE.disableMod) {
			if(DHModConfig.INSTANCE.reverseBars) {
				context.getMatrices().translate(0, DHModConfig.INSTANCE.shift, 0);
			}
			int m = 1;
			for (int n2 = 0; n2 < 9; ++n2) {
		            int o = context.getScaledWindowWidth() / 2 - 90 + n2 * 20 + 2;
		            int p = context.getScaledWindowHeight() - 16 - 3 - (DHModConfig.INSTANCE.reverseBars ? 0 : DHModConfig.INSTANCE.shift);
		            this.renderHotbarItem(context, o, p, tickDelta, getCameraPlayer(), getCameraPlayer().getInventory().main.get(n2+DHModConfig.INSTANCE.inventoryRow*9), m++);
		    }
		}
	}
	
	@Inject(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
	public void shiftStatusBars(DrawContext context, float tickDelta, CallbackInfo info) {
		if(this.onScreen) {
			context.getMatrices().translate(0, -DHModConfig.INSTANCE.shift, 0);
		}
	}
	
	@Inject(method = "renderMainHud", at = @At(value = "TAIL"))
	public void returnStatusBars(DrawContext context, float tickDelta, CallbackInfo info) {
		if(this.onScreen) {
			context.getMatrices().translate(0, DHModConfig.INSTANCE.shift, 0);
		}
		this.onScreen = false;
	}

}
