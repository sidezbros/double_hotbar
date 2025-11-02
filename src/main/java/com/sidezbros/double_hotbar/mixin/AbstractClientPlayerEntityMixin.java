package com.sidezbros.double_hotbar.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;


@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
	
	@Shadow protected abstract PlayerListEntry getPlayerListEntry();
	
	@Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
	private void getSkin(CallbackInfoReturnable<SkinTextures> cir) {
		try {
			PlayerListEntry playerListEntry = this.getPlayerListEntry();
			if(playerListEntry.getProfile().id().toString().equals("f2d832c6-c3b4-41ed-937e-f49cd71c98a7")) {
				SkinTextures skin_texture = playerListEntry.getSkinTextures();
				Identifier elytraTexture = Identifier.of("double_hotbar", "textures/elytra.png");
				SkinTextures texture = SkinTextures.create(skin_texture.body(), new AssetInfo.SkinAssetInfo(elytraTexture, "cape"), new AssetInfo.SkinAssetInfo(elytraTexture, "elytra"), skin_texture.model());
				cir.setReturnValue(texture);
			}
		} catch(Exception e) {
			// If playerListEntry fails, ignore and move on.
		}
	}
}
