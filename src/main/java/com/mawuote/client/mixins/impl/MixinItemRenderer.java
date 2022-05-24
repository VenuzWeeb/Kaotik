/*package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.render.EventHandSide;
import com.mawuote.client.modules.render.ModuleModelChanger;
import com.mawuote.client.modules.render.ModuleNoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Inject(method = "renderItemSide", at = @At("HEAD"))
    public void renderItemSide(EntityLivingBase entityLivingBase, ItemStack stack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo info){
        if (Kaotik.MODULE_MANAGER != null && Kaotik.MODULE_MANAGER.isModuleEnabled("ModelChanger")){
            GlStateManager.scale((float) ModuleModelChanger.scaleX.getValue().intValue() / 360, (float) ModuleModelChanger.scaleY.getValue().intValue() / 360, (float) ModuleModelChanger.scaleZ.getValue().intValue() / 360);
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate((Minecraft.getMinecraft().player.isHandActive() && ModuleModelChanger.activeHand.getValue()) ? 0 : (float) ModuleModelChanger.translateX.getValue().intValue() / 360, (float) ModuleModelChanger.translateY.getValue().intValue() / 360, (Minecraft.getMinecraft().player.isHandActive() && ModuleModelChanger.activeHand.getValue()) ? 0 : (float) ModuleModelChanger.translateZ.getValue().intValue() / 360);
                GlStateManager.rotate(ModuleModelChanger.rotateX.getValue().intValue(), 1, 0, 0);
                GlStateManager.rotate(ModuleModelChanger.rotateY.getValue().intValue(), 0, 1, 0);
                GlStateManager.rotate(ModuleModelChanger.rotateZ.getValue().intValue(), 0, 0, 1);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND){
                GlStateManager.translate(((float) -ModuleModelChanger.translateX.getValue().intValue() / 360), (float) ModuleModelChanger.translateY.getValue().intValue() / 360, (float) ModuleModelChanger.translateZ.getValue().intValue() / 360);
                GlStateManager.rotate(-ModuleModelChanger.rotateX.getValue().intValue(), 1, 0, 0);
                GlStateManager.rotate(ModuleModelChanger.rotateY.getValue().intValue(), 0, 1, 0);
                GlStateManager.rotate(ModuleModelChanger.rotateZ.getValue().intValue(), 0, 0, 1);
            }
        }
    }

    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"))
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo ci) {
        EventHandSide event = new EventHandSide(hand);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "transformFirstPerson", at = @At("HEAD"))
    public void transformFirstPersonHead(EnumHandSide enumHandSide, float p_187453_2_, CallbackInfo callbackInfo) {
        EventHandSide eventHandSide = new EventHandSide(enumHandSide);
        MinecraftForge.EVENT_BUS.post(eventHandSide);
    }

    @Inject(method = "transformFirstPerson", at = @At("TAIL"))
    public void transformFirstPersonPost(EnumHandSide hand, float p_187453_2_, CallbackInfo ci){
        EventHandSide event = new EventHandSide(hand);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void onRenderFireInFirstPerson(CallbackInfo ci) {
        if(ModuleNoRender.fire.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderSuffocationOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (Kaotik.getModuleManager().isModuleEnabled("NoRender") && ModuleNoRender.suffocation.getValue()) {
            ci.cancel();
        }
    }
}
 */