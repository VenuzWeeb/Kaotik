package com.mawuote.client.modules.render;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModulePopChams extends Module<B> {
    public ModulePopChams() {
        super("PopChams", "Pop Chams", "Renders a cham which fades out when a player pops.", ModuleCategory.RENDER);
    }

    public static ValueBoolean angel = new ValueBoolean("Angel", "Angel", "", false);
    public static ValueNumber angelSpeed = new ValueNumber("AngelSpeed", "AngelSpeed", "", 150, 10, 500);
    public static ValueNumber fadeSpeed = new ValueNumber("FadeSpeed", "FadeSpeed", "", 200, 10, 500);

    public static ValueBoolean outline = new ValueBoolean("Outline", "Outline", "", false);
    public static ValueNumber width = new ValueNumber("Width", "Width", "", 3.0, 0.5, 5.0);
    public static ValueBoolean syncColor = new ValueBoolean("SyncColor", "SyncColor", "", true);
    public static ValueColor fillColor = new ValueColor("FillColor", "FillColor", "", new Color(255, 255, 255, 180));
    public static ValueColor outColor = new ValueColor("OutlineColor", "OutlineColor", "", new Color(255, 255, 255, 180));

    public static Color color;
    public static Color outlineColor;

    public static EntityOtherPlayerMP player;
    public static EntityPlayer entity;
    long startTime;
    public static float opacity;

    public static long time;
    public static long duration;
    public static float startAlpha;

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if(mc.player == null || mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getEntity(mc.world) instanceof EntityPlayer) {
                entity = (EntityPlayer) packet.getEntity(mc.world);
                if (packet.getOpCode() == 35 && entity != null && entity != mc.player) {
                    GameProfile profile = new GameProfile(mc.player.getUniqueID(), "");
                    player = new EntityOtherPlayerMP(mc.world, profile);
                    player.copyLocationAndAnglesFrom(packet.getEntity(mc.world));
                    player.rotationYaw = entity.rotationYaw;
                    player.rotationYawHead = entity.rotationYawHead;
                    player.rotationPitch = entity.rotationPitch;
                    player.prevRotationPitch = entity.prevRotationPitch;
                    player.prevRotationYaw = entity.prevRotationYaw;
                    player.renderYawOffset = entity.renderYawOffset;
                    startTime = System.currentTimeMillis();
                }
            }
        }
    }

    public void onRender3D(EventRender3D event) {
        if(mc.player == null || mc.world == null) {
            return;
        }
        if(syncColor.getValue()) {
            color = this.globalColor(255);
            outlineColor = this.globalColor(255);
        } else {
            color = fillColor.getValue();
            outlineColor = outColor.getValue();
        }

        opacity = 0;

        time = System.currentTimeMillis();
        duration = time - startTime;
        startAlpha = (fillColor.getValue().getAlpha()/255f);

        if(player != null && entity != null) {
            if (duration < (fadeSpeed.getValue().intValue()*10)) {
                opacity = startAlpha - ((float) duration / (float) (fadeSpeed.getValue().intValue()*10));
            }
            if(duration < (fadeSpeed.getValue().intValue()*10)) {
                GL11.glPushMatrix();
                if (ModulePopChams.angel.getValue())
                    GlStateManager.translate(0.0f, ((float) ModulePopChams.duration / (angelSpeed.getValue().intValue()*10)), 0.0f);
                mc.renderManager.renderEntityStatic(player, 1f, false);
                GlStateManager.translate(0.0f, 0.0f, 0.0f);
                GL11.glPopMatrix();
            }
        }
    }
}
