package com.mawuote.client.modules.render;

import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.event.impl.render.EventRenderEntityLayer;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.utilities.math.MathUtils;
import com.mawuote.api.utilities.shader.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;

public class ModuleShaderChams extends Module {
    public ModuleShaderChams(){
        super("ShaderChams", "Shader Chams", "Draws a shader on players to make them look amazing.", ModuleCategory.RENDER);
    }

    public enum modes {
        Smoke, Aqua, Flow, Red, Outline;
    }

    ValueEnum mode = new ValueEnum("Mode", "Mode", "", modes.Smoke);

    public static boolean renderNameTags;

    public void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        FramebufferShader framebufferShader = null;
        switch((modes)mode.getValue()) {
            case Smoke: {
                framebufferShader = SmokeShader.SMOKE_SHADER;
                break;
            }
            case Aqua: {
                framebufferShader = AquaShader.AQUA_SHADER;
                break;
            }
            case Flow: {
                framebufferShader = FlowShader.FLOW_SHADER;
                break;
            }
            case Red: {
                framebufferShader = RedShader.RED_SHADER;
                break;
            }
            case Outline: {
                framebufferShader = GlowShader.GLOW_SHADER;
            }
        }
        final FramebufferShader framebufferShader2 = framebufferShader;
        if (framebufferShader2 == null) {
            return;
        }
        final FramebufferShader shader = framebufferShader2;

        GlStateManager.matrixMode(GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.pushMatrix();


        shader.startDraw(event.getPartialTicks());
        renderNameTags = false;

        try {
            for (final Entity entity : mc.world.loadedEntityList) {
                if (!(entity == mc.player) && !(entity == mc.getRenderViewEntity())) {
                    if (!(entity instanceof EntityPlayer)) {
                        continue;
                    }
                    final Render getEntityRenderObject = mc.getRenderManager().getEntityRenderObject(entity);
                    if (getEntityRenderObject == null) {
                        continue;
                    }
                    final Render entityRenderObject = getEntityRenderObject;
                    final Vec3d vector = MathUtils.getInterpolatedRenderPos(entity, event.getPartialTicks());

                    entityRenderObject.doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
                }
            }
        } catch (Exception ex) {
        }
        renderNameTags = true;
        Float n;
        final float n2 = 0.5f;
        final Float value3 = 3.0f;
        n = n2 + value3.floatValue();
        final Float radius = n;
        final FramebufferShader framebufferShader3 = shader;
        final float red = (float) 255;
        final float green = (float) 0;
        final float blue = (float) 255;
        final float alpha = (float) 255;

        framebufferShader3.stopDraw(red, green, blue, alpha, radius, 1.0f);

        GlStateManager.color(1.0f, 1.0f, 1.0f);

        GlStateManager.matrixMode(GL_PROJECTION);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderEntityLayer(EventRenderEntityLayer event) {
        if (!renderNameTags) {
            event.setCancelled(true);
        }
    }
}
