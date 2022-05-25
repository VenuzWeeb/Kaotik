package com.mawuote.client.modules.render;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.utilities.entity.EntityUtils;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleESP extends Module {
    public ModuleESP(){super("ESP", "ESP", "Draws boxes around certain entities or events.", ModuleCategory.RENDER);}

    public static ValueBoolean players = new ValueBoolean("Players", "Players", "", false);
    public static ValueBoolean mobs = new ValueBoolean("Mobs", "Mobs", "", false);
    public static ValueBoolean animals = new ValueBoolean("Animals", "Animals", "", false);
    public static ValueBoolean others = new ValueBoolean("Others", "Others", "", false);
    public static ValueBoolean chorus = new ValueBoolean("Chorus", "Chorus", "", false);
    public static ValueNumber chorusFadeSpeed = new ValueNumber("ChorusFadeSpeed", "ChorusFadeSpeed", "", 200, 10, 500);

    public static ValueBoolean items = new ValueBoolean("Items", "Items", "", false);
    public static ValueBoolean itemNames = new ValueBoolean("ItemNames", "ItemNames", "", false);
    public static ValueBoolean xpbottles = new ValueBoolean("Xp", "Xp", "", false);
    public static ValueBoolean xporbs = new ValueBoolean("XpOrbs", "XpOrbs", "", false);
    public static ValueBoolean pearls = new ValueBoolean("Pearls", "Pearls", "", false);
    public static ValueNumber itemAlpha = new ValueNumber("ItemAlpha", "ItemAlpha", "", 180, 0, 255);
    public static ValueNumber width = new ValueNumber("Width", "Width", "", 3.0, 0.5, 5.0);
    public static ValueBoolean syncColor = new ValueBoolean("SyncColor", "SyncColor", "", true);
    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(255, 255, 255, 255));

    HashMap<BlockPos, Sound> chorusPositions = new HashMap();
    List<BlockPos> eatingPositions = new ArrayList<>();


    public static Color color = daColor.getValue();
    public void onUpdate() {
        if(syncColor.getValue()) {
            color = this.globalColor(255);
        } else {
            color = daColor.getValue();
        }

        for(EntityPlayer player : mc.world.playerEntities) {
            if(player.getHeldItemMainhand().getItem() instanceof ItemChorusFruit && player.isHandActive()) {
                BlockPos pos = new BlockPos(Math.floor(player.posX), player.posY, Math.floor(player.posZ));
                eatingPositions.add(pos);
            }
        }
    }

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if(event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            if (packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT || packet.getSound() == SoundEvents.ENTITY_ENDERMEN_TELEPORT) {
                BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                chorusPositions.put(pos, new Sound(pos));
                chorusPositions.get(pos).starTime = System.currentTimeMillis();
            }
        }
    }

    public static void drawText(BlockPos pos, String text, int alpha) {
        if (pos == null || text == null) {
            return;
        }
        GlStateManager.pushMatrix();
        RenderUtils.glBillboardDistanceScaled((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, mc.player, 0.7f);
        GlStateManager.disableDepth();
        GlStateManager.translate((-((double) Kaotik.FONT_MANAGER.getStringWidth( text) / 2.0)), (double)0.0, (double)0.0);
        Kaotik.FONT_MANAGER.drawString(text, 0, 0, new Color(195, 54, 252, alpha));
        GlStateManager.popMatrix();
    }

    public void onRender3D(EventRender3D event) {
        if(mc.player == null || mc.world == null)
            return;

        for (final HashMap.Entry<BlockPos, Sound> entry : chorusPositions.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue().starTime < (chorusFadeSpeed.getValue().intValue() * 10)) {
                int opacity = 5;

                long time = System.currentTimeMillis();
                long duration = time - entry.getValue().starTime;

                int minusO = MathHelper.clamp((int) (((float) duration / (float) (chorusFadeSpeed.getValue().intValue() * 10)) * 255f), 0, 250);

                if (duration < (chorusFadeSpeed.getValue().intValue() * 10)) {
                    opacity = 255 - minusO;
                }

                drawText(entry.getValue().pos, "Player teleports", opacity);
            }
        }

        /*for(BlockPos pos : eatingPositions) {
            if(pos != null) {
                drawText(pos, "Player eating chorus...");
            }
        }*/

        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityItem && mc.player.getDistanceSq(entity) < 2500.0) {
                if (itemNames.getValue())
                    this.drawText(entity);
                if (this.items.getValue()) {
                    int i = 0;
                    final Vec3d interp = EntityUtils.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = new Color(color.getRed(), color.getGreen(), color.getBlue());
                    final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.itemAlpha.getValue().intValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtils.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), 255), 1.0f);

                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xporbs.getValue()) {
            int i = 0;
            for (final Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityXPOrb && mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interp = EntityUtils.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = new Color(color.getRed(), color.getGreen(), color.getBlue());
                    Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.itemAlpha.getValue().intValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtils.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), 255), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.pearls.getValue()) {
            int i = 0;
            for (final Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderPearl && mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interp = EntityUtils.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = new Color(color.getRed(), color.getGreen(), color.getBlue());
                    final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.itemAlpha.getValue().intValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtils.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), 255), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xpbottles.getValue()) {
            int i = 0;
            for (final Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityExpBottle && mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interp = EntityUtils.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = new Color(color.getRed(), color.getGreen(), color.getBlue());
                    final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.itemAlpha.getValue().intValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtils.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), 255), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
    }

    private void drawText(final Entity entityIn) {
        if(mc.player == null || mc.world == null || mc.getRenderManager().options == null)
            return;
        GlStateManager.pushMatrix();
        final double scale = 1.0;
        final String name = (entityIn instanceof EntityItem) ? ((EntityItem) entityIn).getItem().getDisplayName() : ((entityIn instanceof EntityEnderPearl) ? "Thrown Ender Pearl" : ((entityIn instanceof EntityExpBottle) ? "Thrown Exp Bottle" : "null"));
        final Vec3d interp = EntityUtils.getInterpolatedRenderPos(entityIn, mc.getRenderPartialTicks());
        final float yAdd = entityIn.height / 2.0f + 0.5f;
        final double x = interp.x;
        final double y = interp.y + yAdd;
        final double z = interp.z;
        final float viewerYaw = mc.getRenderManager().playerViewY;
        final float viewerPitch = mc.getRenderManager().playerViewX;
        final boolean isThirdPersonFrontal = mc.getRenderManager().options.thirdPersonView == 2;
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        final float f = mc.player.getDistance(entityIn);
        final float m = f / 8.0f * (float) Math.pow(1.258925437927246, scale);
        GlStateManager.scale(m, m, m);
        final FontRenderer fontRendererIn = mc.fontRenderer;
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        final String str = name + ((entityIn instanceof EntityItem) ? (" x" + ((EntityItem) entityIn).getItem().getCount()) : "");
        final int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        if (Kaotik.getModuleManager().isModuleEnabled("Font")) {
            Kaotik.FONT_MANAGER.drawString(str, -i, 9.0f, Color.WHITE);
        } else {
            GlStateManager.enableTexture2D();
            fontRendererIn.drawStringWithShadow(str, (float) (-i), 9.0f, Color.WHITE.getRGB());
            GlStateManager.disableTexture2D();
        }
        GlStateManager.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.popMatrix();

    }

    public class Sound
    {
        public BlockPos pos;
        public long starTime;

        public Sound(final BlockPos pos) {
            this.pos = pos;
            this.starTime = 0;
        }
    }
}
