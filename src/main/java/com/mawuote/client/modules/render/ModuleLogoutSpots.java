package com.mawuote.client.modules.render;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.manager.event.impl.network.EventPlayerJoin;
import com.mawuote.api.manager.event.impl.network.EventPlayerLeave;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.utilities.math.MathUtils;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.client.modules.client.ModuleColor;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleLogoutSpots extends Module {
    public ModuleLogoutSpots(){
        super("LogoutSpots", "Logout Spots", "Draws an ESP on logout spots from other players.", ModuleCategory.RENDER);
        spots = new CopyOnWriteArrayList<LogoutPos>();

    }

    List<LogoutPos> spots;
    AxisAlignedBB bb;
    double x;
    double y;
    double z;

    public static ValueNumber range = new ValueNumber("Range", "Range", "", 300.0f, 50.0f, 500.0f);
    public static ValueBoolean coords = new ValueBoolean("Coords", "Coords", "", true);
    public static ValueBoolean message = new ValueBoolean("Message", "Message", "", false);
    public static ValueBoolean syncColor = new ValueBoolean("SyncColor", "SyncColor", "", true);
    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(255, 255, 255, 255));

    Color color;

    @Override
    public void onDisable() {
        this.spots.clear();
    }
    @Override
    public void onRender3D(final EventRender3D event) {
        if(syncColor.getValue()) {
            color = this.globalColor(255);
        } else {
            color = daColor.getValue();
        }
        if (!this.spots.isEmpty()) {
            synchronized (this.spots) {
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        bb = RenderUtils.interpolateAxis(spot.getEntity().getEntityBoundingBox());
                        RenderUtils.drawBlockOutline(bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), 255), 1.0f);
                        x = this.interpolate(spot.getEntity().lastTickPosX, spot.getEntity().posX, event.getPartialTicks()) - mc.getRenderManager().renderPosX;
                        y = this.interpolate(spot.getEntity().lastTickPosY, spot.getEntity().posY, event.getPartialTicks()) - mc.getRenderManager().renderPosY;
                        z = this.interpolate(spot.getEntity().lastTickPosZ, spot.getEntity().posZ, event.getPartialTicks()) - mc.getRenderManager().renderPosZ;
                        this.renderNameTag(spot.getName(), x, y, z, event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ());
                    }
                });
            }
        }
    }

    @Override
    public void onUpdate() {
        this.spots.removeIf(spot -> mc.player.getDistanceSq((Entity)spot.getEntity()) >= MathUtils.square(this.range.getValue().floatValue()));
    }

    @SubscribeEvent
    public void onJoin(EventPlayerJoin event) {
        final UUID uuid = event.getUuid();
        final EntityPlayer entity = mc.world.getPlayerEntityByUUID(uuid);
        if (entity != null && this.message.getValue()) {
            ChatManager.printChatNotifyClient(ChatFormatting.GOLD + entity.getName() + ChatFormatting.GREEN + " logged in" + ChatFormatting.WHITE + (this.coords.getValue() ? (" at [" + (int) entity.posX + ", " + (int) entity.posY + ", " + (int) entity.posZ + "]!") : "!"));
        }
        this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
    }

    @SubscribeEvent
    public void onLeave(EventPlayerLeave event) {
        final EntityPlayer entity2 = event.getEntity();
        final UUID uuid2 = event.getUuid();
        final String name = event.getName();
        if (this.message.getValue()) {
            ChatManager.printChatNotifyClient(ChatFormatting.GOLD + event.getName() + ChatFormatting.RED + " logged out" + ChatFormatting.WHITE + (this.coords.getValue() ? (" at [" + (int) entity2.posX + ", " + (int) entity2.posY + ", " + (int) entity2.posZ + "]!") : "!"));
        }
        if (name != null && entity2 != null && uuid2 != null) {
            this.spots.add(new LogoutPos(name, uuid2, entity2));
        }
    }

    private void renderNameTag(final String name, final double x, final double yi, final double z, final float delta, final double xPos, final double yPos, final double zPos) {
        final double y = yi + 0.7;
        final Entity camera = mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        final String displayTag = name + "_logout_spot XYZ " + String.format("%.1f", xPos) + " " + String.format("%.1f", yPos) + " " + String.format("%.1f", zPos);
        final double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        final int width = (int) Kaotik.FONT_MANAGER.getStringWidth( displayTag) / 2;
        double scale = (0.0018 + ModuleNametags.m_scale.getValue().intValue() * (distance * 0.3)) / 1000.0;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y + 1.4f, (float)z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        RenderUtils.drawRectrgb((float)(-width - 2), (float)(-(Kaotik.FONT_MANAGER.getHeight() + 1)), width + 3f, 2.5f, 0, 0, 0, ModuleNametags.a.getValue().intValue());
        if(ModuleNametags.outline.getValue()) {
            RenderUtils.drawOutlineLine((float) (-width - 2), (float) (-(Kaotik.FONT_MANAGER.getHeight() + 1)), width + 3f, 2.5f, (float) ModuleNametags.width.getValue().doubleValue(), ModuleColor.getColor());
        }
        //drawBorderedRectReliant((float)(-width - 2), (float)(-(FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 1)), width + 2.0f, 1.5f, 1.8f,1426064384, 855638016);
        GlStateManager.disableBlend();
        Kaotik.FONT_MANAGER.drawString(displayTag, (-width), (-(Kaotik.FONT_MANAGER.getHeight() - 1)), new Color(-5592406));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private double interpolate(final double previous, final double current, final float delta) {
        return previous + (current - previous) * delta;
    }

    private static class LogoutPos
    {
        private final String name;
        private final UUID uuid;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;

        public LogoutPos(final String name, final UUID uuid, final EntityPlayer entity) {
            this.name = name;
            this.uuid = uuid;
            this.entity = entity;
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
        }

        public String getName() {
            return this.name;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public EntityPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }
}
