/*package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.client.modules.render.ModuleGlintModify;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{

    @Shadow
    private void renderModel(IBakedModel model, int color, ItemStack stack) {}

    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderEffect(final int oldValue) {
        return Kaotik.getModuleManager().isModuleEnabled("GlintModify") ? ModuleGlintModify.getColor().getRGB() : oldValue;
    }

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"))
    private void POOOOOP(RenderItem renderItem, IBakedModel model, ItemStack stack) {
    }

    @Redirect(
            method = {"renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"
            )
    )
    private void renderItem(final float colorRed, final float colorGreen, final float colorBlue, final float alpha){

    }
}

 */
