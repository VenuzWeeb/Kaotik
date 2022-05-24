package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.client.modules.render.ModuleCrystalChams;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelEnderCrystal.class)
public class MixinModelEnderCrystal {

    @Shadow
    private ModelRenderer cube;
    @Shadow
    private ModelRenderer glass;
    @Shadow
    private ModelRenderer base;

    @Overwrite
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.translate(0.0F, -0.5F, 0.0F);
        if (this.base != null) {
            this.base.render(scale);
        }

        GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.8F + ageInTicks, 0.0F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        if(Kaotik.getModuleManager().isModuleEnabled("CrystalChams")) {
            if (ModuleCrystalChams.outsideCube.getValue())
                this.glass.render(scale);
        } else {
            this.glass.render(scale);
        }
        float f = 0.875F;
        GlStateManager.scale(0.875F, 0.875F, 0.875F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
        if(Kaotik.getModuleManager().isModuleEnabled("CrystalChams")) {
            if (ModuleCrystalChams.outsideCube2.getValue())
                this.glass.render(scale);
        } else {
            this.glass.render(scale);
        }
        GlStateManager.scale(0.875F, 0.875F, 0.875F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
        if(Kaotik.getModuleManager().isModuleEnabled("CrystalChams")) {
            if (ModuleCrystalChams.insideCube.getValue())
                this.cube.render(scale);
        } else {
            this.cube.render(scale);
        }
        GlStateManager.popMatrix();
    }
}
