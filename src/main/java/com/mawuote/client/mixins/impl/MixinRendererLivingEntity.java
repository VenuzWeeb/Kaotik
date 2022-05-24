/*package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.utilities.render.OutlineUtils;
import com.mawuote.client.modules.render.ModulePlayerChams;
import com.mawuote.client.modules.render.ModulePopChams;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(value = RenderLivingBase.class, priority = 999)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    @Shadow
    protected ModelBase mainModel;

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    protected MixinRendererLivingEntity() {
        super(null);
    }


                GL11.glPushAttrib(1048575);
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
                if(Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName()) && !ModulePlayerChams.syncFriend.getValue()) {
                    if (ModulePlayerChams.fhiddenSync.getValue()) {
                        GL11.glColor4f(ModulePlayerChams.fcolor.getRed() / 255.0F, ModulePlayerChams.fcolor.getGreen() / 255.0F, ModulePlayerChams.fcolor.getBlue() / 255.0F, ModulePlayerChams.friendPulse.getValue() ? ModulePlayerChams.pulseAlpha/255f : ModulePlayerChams.fdaColor.getValue().getAlpha() / 255.0F);
                    } else {
                        GL11.glColor4f(ModulePlayerChams.fhideColor.getRed() / 255.0F, ModulePlayerChams.fhideColor.getGreen() / 255.0F, ModulePlayerChams.fhideColor.getBlue() / 255.0F, ModulePlayerChams.friendPulse.getValue() ? ModulePlayerChams.pulseAlpha/255f : ModulePlayerChams.fhiddenColor.getValue().getAlpha() / 255.0F);
                    }
                } else {
                    if (ModulePlayerChams.hiddenSync.getValue()) {
                        GL11.glColor4f(ModulePlayerChams.color.getRed() / 255.0F, ModulePlayerChams.color.getGreen() / 255.0F, ModulePlayerChams.color.getBlue() / 255.0F, ModulePlayerChams.pulse.getValue() ? ModulePlayerChams.pulseAlpha/255f : ModulePlayerChams.daColor.getValue().getAlpha() / 255.0F);
                    } else {
                        GL11.glColor4f(ModulePlayerChams.hideColor.getRed() / 255.0F, ModulePlayerChams.hideColor.getGreen() / 255.0F, ModulePlayerChams.hideColor.getBlue() / 255.0F, ModulePlayerChams.pulse.getValue() ? ModulePlayerChams.pulseAlpha/255f : ModulePlayerChams.hiddenColor.getValue().getAlpha() / 255.0F);
                    }
                }
                if(Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName())) {
                    if(ModulePlayerChams.fhidden.getValue()) {
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                } else {
                    if(ModulePlayerChams.hidden.getValue()) {
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                }
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                if(Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName()) && !ModulePlayerChams.syncFriend.getValue()) {
                    GL11.glColor4f(ModulePlayerChams.fcolor.getRed() / 255.0F, ModulePlayerChams.fcolor.getGreen() / 255.0F, ModulePlayerChams.fcolor.getBlue() / 255.0F, ModulePlayerChams.friendPulse.getValue() ? ModulePlayerChams.pulseAlpha/255f : ModulePlayerChams.fdaColor.getValue().getAlpha() / 255.0F);
                } else {
                    GL11.glColor4f(ModulePlayerChams.color.getRed() / 255.0F, ModulePlayerChams.color.getGreen() / 255.0F, ModulePlayerChams.color.getBlue() / 255.0F, ModulePlayerChams.pulse.getValue() ? ModulePlayerChams.pulseAlpha/255f : ModulePlayerChams.daColor.getValue().getAlpha() / 255.0F);
                }
                if(Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName())) {
                    if(ModulePlayerChams.fvisible.getValue()) {
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                } else {
                    if(ModulePlayerChams.visible.getValue()) {
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                }
                if (ModulePlayerChams.enchanted.getValue()) {
                    mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
                    GL11.glTexCoord3d(1.0, 1.0, 1.0);
                    GL11.glEnable(3553);
                    GL11.glBlendFunc(768, 771);
                    GL11.glColor4f(ModulePlayerChams.enchantedColor.getValue().getRed() / 255.0F, ModulePlayerChams.enchantedColor.getValue().getGreen() / 255.0F, ModulePlayerChams.enchantedColor.getValue().getBlue() / 255.0F, ModulePlayerChams.enchantedColor.getValue().getAlpha() / 255.0f);

                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);

                    GL11.glColor4f(1, 1, 1, 1);
                }
                GL11.glEnable(3042);
                GL11.glEnable(2896);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
                GL11.glPopAttrib();
                if(ModulePlayerChams.outline.getValue()) {
                    if (ModulePlayerChams.outlineMode.getValue().equals(ModulePlayerChams.outlineModes.Wire)) {
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3008);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glLineWidth(ModulePlayerChams.width.getValue().floatValue());
                        GL11.glEnable(2960);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        GL11.glEnable(10754);
                        if (Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName()) && !ModulePlayerChams.syncFriend.getValue()) {
                            GL11.glColor4f(ModulePlayerChams.foutColor.getRed() / 255.0F, ModulePlayerChams.foutColor.getGreen() / 255.0F, ModulePlayerChams.foutColor.getBlue() / 255.0F, 1);
                        } else {
                            GL11.glColor4f(ModulePlayerChams.outColor.getRed() / 255.0F, ModulePlayerChams.outColor.getGreen() / 255.0F, ModulePlayerChams.outColor.getBlue() / 255.0F, 1);
                        }
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glEnable(3042);
                        GL11.glEnable(2896);
                        GL11.glEnable(3553);
                        GL11.glEnable(3008);
                        GL11.glPopAttrib();
                    } else {
                        if (Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName()) && !ModulePlayerChams.syncFriend.getValue()) {
                            OutlineUtils.setColor(ModulePlayerChams.foutColor);
                        } else {
                            OutlineUtils.setColor(ModulePlayerChams.outColor);
                        }
                        OutlineUtils.renderOne(ModulePlayerChams.width.getValue().floatValue());
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderTwo();
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderThree();
                        OutlineUtils.renderFour();
                        if (Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName()) && !ModulePlayerChams.syncFriend.getValue()) {
                            OutlineUtils.setColor(ModulePlayerChams.foutColor);
                        } else {
                            OutlineUtils.setColor(ModulePlayerChams.outColor);
                        }
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                    }
                }
                if(Kaotik.FRIEND_MANAGER.isFriend(entitylivingbaseIn.getName())) {
                    if(!ModulePlayerChams.fvisible.getValue()) {
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                } else {
                    if(!ModulePlayerChams.visible.getValue()) {
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                }

            }
        }

        if(entitylivingbaseIn == ModulePopChams.player) {
            if (ModulePopChams.player != null) {
                GL11.glPushAttrib(1048575);
                RenderUtils.prepareGL();
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                GL11.glColor4f(ModulePopChams.color.getRed() / 255f, ModulePopChams.color.getGreen() / 255f, ModulePopChams.color.getBlue() / 255f, ModulePopChams.opacity);
                GL11.glPolygonMode(1032, 6914);
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                GL11.glColor4f(ModulePopChams.outlineColor.getRed() / 255f, ModulePopChams.outlineColor.getGreen() / 255f, ModulePopChams.outlineColor.getBlue() / 255f, ModulePopChams.opacity);
                GL11.glPolygonMode(1032, 6913);
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                RenderUtils.releaseGL();
            }
        }

        if(entitylivingbaseIn == ModuleKillEffects.player) {
            if (ModuleKillEffects.player != null) {
                GL11.glPushAttrib(1048575);
                RenderUtils.prepareGL();
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                GL11.glColor4f(ModuleKillEffects.chamColor.getValue().getRed() / 255f, ModuleKillEffects.chamColor.getValue().getGreen() / 255f, ModuleKillEffects.chamColor.getValue().getBlue() / 255f, ModuleKillEffects.opacity);
                GL11.glPolygonMode(1032, 6914);
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                GL11.glColor4f(ModuleKillEffects.chamColor.getValue().getRed() / 255f, ModuleKillEffects.chamColor.getValue().getGreen() / 255f, ModuleKillEffects.chamColor.getValue().getBlue() / 255f, ModuleKillEffects.opacity);
                GL11.glPolygonMode(1032, 6913);
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                RenderUtils.releaseGL();
            }
        }

        if (!Kaotik.getModuleManager().isModuleEnabled("PlayerChams") || !isPlayer) {
            if (!(entitylivingbaseIn == ModulePopChams.player)) {
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            }
        }
    }
}
*/