package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.client.modules.client.ModuleStreamerMode;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ GuiPlayerTabOverlay.class })
public class MixinGuiPlayerTabOverlay
{

    @Inject(method = { "getPlayerName" }, at = { @At("HEAD") }, cancellable = true)
    public void getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn, final CallbackInfoReturnable returnable) {
        if (Kaotik.getModuleManager().isModuleEnabled("StreamerMode") && ModuleStreamerMode.hideYou.getValue()) {
            returnable.cancel();
            returnable.setReturnValue(ModuleStreamerMode.getPlayerName(networkPlayerInfoIn));
        }
    }
}