package com.sidezbros.double_hotbar.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;


@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
	
	@Shadow protected abstract PlayerListEntry getPlayerListEntry();
	
	@Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
	private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
		try {
			PlayerListEntry playerListEntry = this.getPlayerListEntry();
			if(playerListEntry.getProfile().getId().toString().equals("f2d832c6-c3b4-41ed-937e-f49cd71c98a7")) {
				SkinTextures skin_texture = playerListEntry.getSkinTextures();
				Identifier elytraTexture = Identifier.of("double_hotbar", "textures/elytra.png");
				SkinTextures texture = new SkinTextures(skin_texture.texture(), skin_texture.textureUrl(), elytraTexture, elytraTexture, skin_texture.model(), skin_texture.secure());
				cir.setReturnValue(texture);
			}
		} catch(Exception e) {
			// If playerListEntry fails, ignore and move on.
		}
	}
}
