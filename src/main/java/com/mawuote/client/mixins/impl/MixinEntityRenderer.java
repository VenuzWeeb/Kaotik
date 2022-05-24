package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.client.modules.render.ModuleNoRender;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value=EntityRenderer.class, priority = Integer.MIN_VALUE)
public class MixinEntityRenderer {

    @Shadow
    public ItemStack itemActivationItem;

    @Inject(method={"renderItemActivation"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemActivationHook(CallbackInfo info) {
        if (this.itemActivationItem != null && Kaotik.getModuleManager().isModuleEnabled("NoRender") && ModuleNoRender.totemPop.getValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }
}




