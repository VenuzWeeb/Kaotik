package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.render.OutlineUtils;
import com.mawuote.client.gui.click.components.impl.PreviewComponent;
import com.mawuote.client.modules.render.ModuleCrystalChams;
import com.mawuote.client.modules.render.ModuleESP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin({RenderEnderCrystal.class})
public abstract class MixinRenderEnderCrystal {
    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    @Shadow
    public abstract void doRender(EntityEnderCrystal var1, double var2, double var4, double var6, float var8, float var9);

    @Redirect(
            method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"
            )
    )
    private void render1(ModelBase var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
        if (!(Kaotik.getModuleManager().isModuleEnabled("CrystalChams"))) {
            var1.render(var2, var3, var4, var5, var6, var7, var8);
        }
    }

    @Redirect(
            method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
                    ordinal = 1
            )
    )
    private void render2(ModelBase var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
        if (!(Kaotik.getModuleManager().isModuleEnabled("CrystalChams"))) {
            var1.render(var2, var3, var4, var5, var6, var7, var8);
        }
    }

    @Inject(
            method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"},
            at = {@At("RETURN")},
            cancellable = true
    )
    public void IdoRender(EntityEnderCrystal var1, double var2, double var4, double var6, float var8, float var9, CallbackInfo var10) {

        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.fancyGraphics = false;

        if(Kaotik.getModuleManager().isModuleEnabled("ESP")) {
            if(ModuleESP.others.getValue()) {
                float var13;
                float var14;

                var13 = (float) var1.innerRotation + var9;
                GlStateManager.pushMatrix();
                GlStateManager.translate(var2, var4, var6);
                Minecraft.getMinecraft().renderManager.renderEngine.bindTexture(ENDER_CRYSTAL_TEXTURES);
                var14 = MathHelper.sin(var13 * 0.2F) / 2.0F + 0.5F;
                var14 += var14 * var14;
                GL11.glLineWidth(5.0F);
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                } else {
                    this.modelEnderCrystalNoBase.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                }

                OutlineUtils.renderOne((float) ModuleESP.width.getValue().doubleValue());
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                } else {
                    this.modelEnderCrystalNoBase.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                }

                OutlineUtils.renderTwo();
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                } else {
                    this.modelEnderCrystalNoBase.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                }
                final Color rainbowColor1 = new Color(ModuleESP.color.getRed(), ModuleESP.color.getGreen(), ModuleESP.color.getBlue());
                final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                Color n = new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
                OutlineUtils.renderThree();
                OutlineUtils.renderFour();
                OutlineUtils.setColor(n);
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                } else {
                    this.modelEnderCrystalNoBase.render(var1, 0.0F, var13 * 3.0F, var14 * 0.2F, 0.0F, 0.0F, 0.0625F);
                }

                OutlineUtils.renderFive();
                GlStateManager.popMatrix();
            }
        }
        if (Kaotik.getModuleManager().isModuleEnabled("CrystalChams")) {
            float var14;
            GL11.glPushMatrix();
            var14 = (float) var1.innerRotation + var9;
            GlStateManager.translate(var2, var4, var6);
            if (var1 == PreviewComponent.entityEnderCrystal) {
                GlStateManager.scale(1, 1, 1);
            } else {
                GlStateManager.scale(ModuleCrystalChams.size.getValue().floatValue(), ModuleCrystalChams.size.getValue().floatValue(), ModuleCrystalChams.size.getValue().floatValue());
            }
            GlStateManager.scale(ModuleCrystalChams.size.getValue().floatValue(), ModuleCrystalChams.size.getValue().floatValue(), ModuleCrystalChams.size.getValue().floatValue());
            Minecraft.getMinecraft().renderManager.renderEngine.bindTexture(ENDER_CRYSTAL_TEXTURES);
            float var15 = MathHelper.sin(var14 * 0.2F) / 2.0F + 0.5F;
            var15 += var15 * var15;
            float spinSpeed = ModuleCrystalChams.crystalSpeed.getValue().floatValue();
            float bounceSpeed = ModuleCrystalChams.crystalBounce.getValue().floatValue();

            if(ModuleCrystalChams.texture.getValue()) {
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                } else {
                    this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                }
            }

            GL11.glPushAttrib(1048575);
            if(ModuleCrystalChams.mode.getValue().equals(ModuleCrystalChams.modes.Wireframe)) {
                GL11.glPolygonMode(1032, 6913);
            }
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(10754);
            if(ModuleCrystalChams.lifetimeColor.getValue()) {
                if (ModuleCrystalChams.thingers.containsKey(var1.getUniqueID()))
                    GL11.glColor4f(ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getRed() / 255.f, ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getGreen() / 255.f, ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getBlue() / 255.f, ModuleCrystalChams.pulse.getValue() ? ModuleCrystalChams.pulseAlpha/255f : ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getAlpha() / 255.f);
                else
                    GL11.glColor4f(ModuleCrystalChams.lifeStart.getValue().getRed() / 255f, ModuleCrystalChams.lifeStart.getValue().getGreen() / 255f, ModuleCrystalChams.lifeStart.getValue().getBlue() / 255f, ModuleCrystalChams.pulse.getValue() ? ModuleCrystalChams.pulseAlpha/255f : ModuleCrystalChams.lifeStart.getValue().getAlpha() / 255f);
            } else {
                if (ModuleCrystalChams.hiddenSync.getValue()) {
                    GL11.glColor4f(ModuleCrystalChams.color.getRed() / 255.0F, ModuleCrystalChams.color.getGreen() / 255.0F, ModuleCrystalChams.color.getBlue() / 255.0F, ModuleCrystalChams.pulse.getValue() ? ModuleCrystalChams.pulseAlpha/255f : ModuleCrystalChams.daColor.getValue().getAlpha() / 255.0f);
                } else {
                    GL11.glColor4f(ModuleCrystalChams.hideColor.getRed() / 255.0F, ModuleCrystalChams.hideColor.getGreen() / 255.0F, ModuleCrystalChams.hideColor.getBlue() / 255.0F, ModuleCrystalChams.pulse.getValue() ? ModuleCrystalChams.pulseAlpha/255f : ModuleCrystalChams.hiddenColor.getValue().getAlpha() / 255.0f);
                }
            }
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
            } else {
                this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
            }
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            if(ModuleCrystalChams.lifetimeColor.getValue()) {
                if (ModuleCrystalChams.thingers.containsKey(var1.getUniqueID()))
                    GL11.glColor4f(ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getRed() / 255.f, ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getGreen() / 255.f, ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getBlue() / 255.f, ModuleCrystalChams.pulse.getValue() ? ModuleCrystalChams.pulseAlpha/255f : ModuleCrystalChams.thingers.get(var1.getUniqueID()).color.getAlpha() / 255.f);
                else
                    GL11.glColor4f(ModuleCrystalChams.lifeStart.getValue().getRed() / 255f, ModuleCrystalChams.lifeStart.getValue().getGreen() / 255f, ModuleCrystalChams.lifeStart.getValue().getBlue() / 255f, ModuleCrystalChams.pulse.getValue() ? ModuleCrystalChams.pulseAlpha/255f : ModuleCrystalChams.lifeStart.getValue().getAlpha() / 255f);
            } else {
                GL11.glColor4f(ModuleCrystalChams.color.getRed() / 255.0F, ModuleCrystalChams.color.getGreen() / 255.0F, ModuleCrystalChams.color.getBlue() / 255.0F, ModuleCrystalChams.pulse.getValue() ? ModuleCrystalChams.pulseAlpha/255f : ModuleCrystalChams.daColor.getValue().getAlpha() / 255.0f);
            }
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
            } else {
                this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
            }
            if (ModuleCrystalChams.enchanted.getValue()) {
                mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
                GL11.glTexCoord3d(1.0, 1.0, 1.0);
                GL11.glEnable(3553);
                GL11.glBlendFunc(768, 771);
                GL11.glColor4f(ModuleCrystalChams.enchantedColor.getValue().getRed() / 255.0F, ModuleCrystalChams.enchantedColor.getValue().getGreen() / 255.0F, ModuleCrystalChams.enchantedColor.getValue().getBlue() / 255.0F, ModuleCrystalChams.enchantedColor.getValue().getAlpha() / 255.0f);

                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                } else {
                    this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                }
                GL11.glColor4f(1, 1, 1, 1);
            }
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();

            if(ModuleCrystalChams.outline.getValue()) {
                if (ModuleCrystalChams.outlineMode.getValue().equals(ModuleCrystalChams.outlineModes.Wire)) {
                    GL11.glPushAttrib(1048575);
                    GL11.glPolygonMode(1032, 6913);
                    GL11.glDisable(3008);
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glLineWidth(ModuleCrystalChams.width.getValue().floatValue());
                    GL11.glEnable(2960);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GL11.glEnable(10754);
                    GL11.glColor4f(ModuleCrystalChams.outColor.getRed() / 255.0F, ModuleCrystalChams.outColor.getGreen() / 255.0F, ModuleCrystalChams.outColor.getBlue() / 255.0F, 1);
                    if (var1.shouldShowBottom()) {
                        this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    } else {
                        this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    }
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                    if (var1.shouldShowBottom()) {
                        this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    } else {
                        this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    }
                    GL11.glEnable(3042);
                    GL11.glEnable(2896);
                    GL11.glEnable(3553);
                    GL11.glEnable(3008);
                    GL11.glPopAttrib();
                } else {
                    OutlineUtils.setColor(ModuleCrystalChams.outColor);
                    OutlineUtils.renderOne(ModuleCrystalChams.width.getValue().floatValue());
                    if (var1.shouldShowBottom()) {
                        this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    } else {
                        this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    }
                    OutlineUtils.renderTwo();
                    if (var1.shouldShowBottom()) {
                        this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    } else {
                        this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    }
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour();
                    OutlineUtils.setColor(ModuleCrystalChams.outColor);
                    if (var1.shouldShowBottom()) {
                        this.modelEnderCrystal.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    } else {
                        this.modelEnderCrystalNoBase.render(var1, 0.0F, var14 * spinSpeed, var15 * bounceSpeed, 0.0F, 0.0F, 0.0625F);
                    }
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor(Color.WHITE);
                }
            }
            GL11.glPopMatrix();
        }
    }
}

