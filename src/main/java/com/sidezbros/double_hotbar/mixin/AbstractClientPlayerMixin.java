package com.sidezbros.double_hotbar.mixin;


import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow
    protected abstract @Nullable PlayerInfo getPlayerInfo();

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    private void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        try {
            PlayerInfo info = this.getPlayerInfo();
            if (info.getProfile().id().toString().equals("f2d832c6-c3b4-41ed-937e-f49cd71c98a7")) {
                PlayerSkin skin_texture = info.getSkin();
                Identifier elytraTexture = Identifier.fromNamespaceAndPath("double_hotbar", "textures/elytra.png");
                PlayerSkin texture = PlayerSkin.insecure(skin_texture.body(), new ClientAsset.DownloadedTexture(elytraTexture, "cape"), new ClientAsset.DownloadedTexture(elytraTexture, "elytra"), skin_texture.model());
                cir.setReturnValue(texture);
            }
        } catch (Exception e) {
            // If playerListEntry fails, ignore and move on.
        }
    }
}
