package com.mawuote.client.modules.render;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.entity.EventRenderEntityName;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.utilities.entity.DamageUtils;
import com.mawuote.api.utilities.entity.EntityUtils;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.client.modules.client.ModuleStreamerMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

/**
 * author zoom8
 *
 * I used wurst plus 2 nametags as base but made them better :D
 */

public class ModuleNametags extends Module<B> {
    public ModuleNametags(){super("NameTags", "Name Tags", "Draws nametags on other players and yourself.", ModuleCategory.RENDER);}
    public static ValueBoolean show_armor = new ValueBoolean("Armor", "ShowArmor", "", false);
    public static ValueBoolean percent = new ValueBoolean("Percent", "Percent", "", false);
    public static ValueBoolean show_health = new ValueBoolean("Health", "show_health", "", false);
    public static ValueBoolean show_ping = new ValueBoolean("Ping", "show_ping", "", false);
    public static ValueBoolean show_totems = new ValueBoolean("Pops", "show_totems", "", false);
    public static ValueBoolean show_invis = new ValueBoolean("Invis", "show_invis", "", false);
    public static ValueBoolean gamemode = new ValueBoolean("Gamemode", "gamemode", "", false);
    public static ValueBoolean entityID = new ValueBoolean("EntityID", "EntityID", "", false);
    public static ValueBoolean simplify = new ValueBoolean("Simple", "simplify", "", false);
    public static ValueNumber m_scale = new ValueNumber("Scale", "Scale", "", 4, 1, 15);
    public static ValueNumber width = new ValueNumber("Width", "Width", "", 1.5, 0.1, 3.0);
    public static ValueBoolean outline = new ValueBoolean("Outline", "Outline", "", true);
    public static ValueBoolean syncColor = new ValueBoolean("SyncColor", "SyncColor", "", true);
    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(255, 255, 255, 255));
    public static ValueNumber a = new ValueNumber("Alpha", "Alpha", "", 255, 0, 255);

    Color color;

    int enchantIgriega;

    @SubscribeEvent
    public void onRenderName(EventRenderEntityName event) {
        event.setCancelled(true);
    }

    @Override
    public void onRender3D(EventRender3D event) {
        if(syncColor.getValue()) {
            color = this.globalColor(255);
        } else {
            color = daColor.getValue();
        }
        for (final EntityPlayer player : mc.world.playerEntities) {
            if (player != null && !player.equals((Object)mc.player) && player.isEntityAlive() && (!player.isInvisible() || this.show_invis.getValue())) {
                final double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - mc.getRenderManager().renderPosX;
                final double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - mc.getRenderManager().renderPosY;
                final double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - mc.getRenderManager().renderPosZ;
                this.renderNameTag(player, x, y, z, event.getPartialTicks());
            }
        }
    }

    private void renderNameTag(final EntityPlayer player, final double x, final double y, final double z, final float delta) {
        double tempY = y;
        tempY += (player.isSneaking() ? 0.5 : 0.7);
        final Entity camera = mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        final String displayTag = this.getDisplayTag(player);
        final double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        final int width = (int) Kaotik.FONT_MANAGER.getStringWidth( displayTag) / 2;
        double scale = (0.0018 + m_scale.getValue().intValue() * (distance * 0.3)) / 1000.0;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.4f, (float)z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        RenderUtils.drawRectrgb((float)(-width - 2)-1, (float)(-(Kaotik.FONT_MANAGER.getHeight() + 1))-1, width + 3f, 2.5f + (Kaotik.getModuleManager().isModuleEnabled("Font") ? 0.5f : 0), 0, 0, 0, a.getValue().intValue());
        if(outline.getValue()) {
            /*if(rainbowOutline.getValue()) {
                Color rainbowCol = UtilRainbow.anyRainbowColor(1 * 50, sat.getValue().intValue(), bri.getValue().intValue());
                Color rainbowCol2 = UtilRainbow.anyRainbowColor(50 * 50, sat.getValue().intValue(), bri.getValue().intValue());

                RenderUtil.prepareGL();
                GL11.glShadeModel(GL11.GL_SMOOTH);
                glEnable(GL_LINE_SMOOTH);
                GL11.glDisable((int)3553);
                GL11.glLineWidth((float) this.width.getValue().doubleValue());
                GL11.glBegin(1);
                GL11.glColor3f(rainbowCol.getRed()/255.0f, rainbowCol.getGreen()/255.0f, rainbowCol.getBlue()/255.0f);
                GL11.glVertex2d((float) (-width - 2) - 1, (float) (-(RenderUtils.getStringHeight() + 1)) - 1);
                GL11.glColor3f(rainbowCol2.getRed()/255.0f, rainbowCol2.getGreen()/255.0f, rainbowCol2.getBlue()/255.0f);
                GL11.glVertex2d(width + 3f, (float) (-(RenderUtils.getStringHeight() + 1)) - 1);
                GL11.glEnd();
                GL11.glBegin(1);
                GL11.glColor3f(rainbowCol.getRed()/255.0f, rainbowCol.getGreen()/255.0f, rainbowCol.getBlue()/255.0f);
                GL11.glVertex2d((float) (-width - 2) - 1, (float) (-(RenderUtils.getStringHeight() + 1)) - 1);
                GL11.glColor3f(rainbowCol2.getRed()/255.0f, rainbowCol2.getGreen()/255.0f, rainbowCol2.getBlue()/255.0f);
                GL11.glVertex2d((float) (-width - 2) - 1, (2.5f + (Kaotik.getModuleManager().isModuleEnabled("CustomFont") ? 0.5f : 0)));
                GL11.glEnd();
                GL11.glBegin(1);
                GL11.glColor3f(rainbowCol2.getRed()/255.0f, rainbowCol2.getGreen()/255.0f, rainbowCol2.getBlue()/255.0f);
                GL11.glVertex2d(width + 3f, (float) (-(RenderUtils.getStringHeight() + 1)) - 1);
                GL11.glColor3f(rainbowCol.getRed()/255.0f, rainbowCol.getGreen()/255.0f, rainbowCol.getBlue()/255.0f);
                GL11.glVertex2d(width + 3f, (2.5f + (Kaotik.getModuleManager().isModuleEnabled("CustomFont") ? 0.5f : 0)));
                GL11.glEnd();
                GL11.glBegin(1);
                GL11.glColor3f(rainbowCol2.getRed()/255.0f, rainbowCol2.getGreen()/255.0f, rainbowCol2.getBlue()/255.0f);
                GL11.glVertex2d((float) (-width - 2) - 1, (2.5f + (Kaotik.getModuleManager().isModuleEnabled("CustomFont") ? 0.5f : 0)));
                GL11.glColor3f(rainbowCol.getRed()/255.0f, rainbowCol.getGreen()/255.0f, rainbowCol.getBlue()/255.0f);
                GL11.glVertex2d(width + 3f, (2.5f + (Kaotik.getModuleManager().isModuleEnabled("CustomFont") ? 0.5f : 0)));
                GL11.glEnd();
                GL11.glEnable((int)3553);
                RenderUtil.releaseGL();
                //RenderUtil.rainbowOutline((-width - 2)-2, (-(RenderUtils.getStringHeight() + 1))-2, ((width + 3f) * 2) + 1, (2.5f * 3) + (Kaotik.getModuleManager().isModuleEnabled("CustomFont") ? 6 : 6), 1, sat.getValue().intValue(), bri.getValue().intValue());
            } else {*/
                RenderUtils.drawOutlineLine((float) (-width - 2) - 1, (float) (-(Kaotik.FONT_MANAGER.getHeight() + 1)) - 1, width + 3f, 2.5f + (Kaotik.getModuleManager().isModuleEnabled("Font") ? 0.5f : 0), (float) this.width.getValue().doubleValue(), color.getRGB());
            //}
        }
        GlStateManager.disableBlend();
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
        }
        if (!renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
            final String stackName = renderMainHand.getDisplayName();
            final int stackNameWidth = (int) Kaotik.FONT_MANAGER.getStringWidth( stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75f, 0.75f, 0.0f);
            Kaotik.FONT_MANAGER.drawString(stackName, (-stackNameWidth), show_armor.getValue() ? (int)-(this.getBiggestArmorTag(player) + 18.0f) : (percent.getValue() ? -36 : -26), Color.WHITE);
            GL11.glScalef(1.5f, 1.5f, 1.0f);
            GL11.glPopMatrix();
        }
        //if (show_armor.getValue()) {
            GlStateManager.pushMatrix();
            int xOffset = -8;
            for (final ItemStack stack : player.inventory.armorInventory) {
                if (stack != null) {
                    xOffset -= 8;
                }
            }
            xOffset -= 8;
            final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
            if (renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
                renderOffhand.stackSize = 1;
            }
            if (show_armor.getValue()) {
                this.renderItemStack(renderOffhand, xOffset);
            }
            xOffset += 16;
            for (int i = player.inventory.armorInventory.size(); i > 0; i--) {
                final ItemStack stack2 = player.inventory.armorInventory.get(i - 1);
                final ItemStack armourStack = stack2.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }
                if (show_armor.getValue()) {
                    this.renderItemStack(armourStack, xOffset);
                }
                if(percent.getValue()) {
                    this.renderPercent(armourStack, xOffset);
                }
                xOffset += 16;
            }
            if (show_armor.getValue()) {
                this.renderItemStack(renderMainHand, xOffset);
            }
            GlStateManager.popMatrix();
        //}
        Kaotik.FONT_MANAGER.drawString(displayTag, (float)(-width), (float)(-(Kaotik.FONT_MANAGER.getHeight() - 1)) + (float)(Kaotik.getModuleManager().isModuleEnabled("Font") ? -0.5 : 0), new Color(this.getDisplayColour(player)));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private void renderPercent(final ItemStack stack, final int x) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        if (DamageUtils.hasDurability(stack)) {
            final int percent = DamageUtils.getRoundedDamage(stack);
            String color;
            if (percent >= 60) {
                color = section_sign() + "a";
            } else if (percent >= 25) {
                color = section_sign() + "e";
            } else {
                color = section_sign() + "c";
            }
            Kaotik.FONT_MANAGER.drawString(color + percent + "%", (float) (x * 2), show_armor.getValue() ? ((enchantIgriega < -62) ? (float) enchantIgriega : -62) : (-36), Color.WHITE);
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    private void renderItemStack(final ItemStack stack, final int x) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, -29);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, -29);
        mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x);
        /*if (UtilDamage.hasDurability(stack)) {
            final int percent = UtilDamage.getRoundedDamage(stack);
            String color;
            if (percent >= 60) {
                color = section_sign() + "a";
            }
            else if (percent >= 25) {
                color = section_sign() + "e";
            }
            else {
                color = section_sign() + "c";
            }
            FontUtils.drawDoubleStringWithShadow(Kaotik.getModuleManager().isModuleEnabled("CustomFont"), color + percent + "%", (float)(x * 2), (enchantIgriega < -62) ? (float)enchantIgriega : -62 , -1);
        }*/
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(final ItemStack stack, final int x) {
        int enchantmentY = -37;
        final NBTTagList enchants = stack.getEnchantmentTagList();
        if (enchants.tagCount() > 2 && simplify.getValue()) {
            Kaotik.FONT_MANAGER.drawString("god", (x * 2), enchantmentY, new Color(-3977919));
            enchantmentY -= 8;
        } else {
            for (int index = 0; index < enchants.tagCount(); ++index) {
                final short id = enchants.getCompoundTagAt(index).getShort("id");
                final short level = enchants.getCompoundTagAt(index).getShort("lvl");
                final Enchantment enc = Enchantment.getEnchantmentByID(id);
                if (enc != null) {
                    String encName = enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName(level).substring(11).substring(0, 1).toLowerCase()) : enc.getTranslatedName(level).substring(0, 1).toLowerCase();
                    encName += level;
                    Kaotik.FONT_MANAGER.drawString(encName, (x * 2), enchantmentY, Color.WHITE);
                    enchantmentY -= 8;
                }
            }
        }
        enchantIgriega = enchantmentY;
    }

    private float getBiggestArmorTag(final EntityPlayer player) {
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (final ItemStack stack : player.inventory.armorInventory) {
            float encY = 0.0f;
            if (stack != null) {
                final NBTTagList enchants = stack.getEnchantmentTagList();
                for (int index = 0; index < enchants.tagCount(); ++index) {
                    final short id = enchants.getCompoundTagAt(index).getShort("id");
                    final Enchantment enc = Enchantment.getEnchantmentByID(id);
                    if (enc != null) {
                        encY += 8.0f;
                        arm = true;
                    }
                }
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect()) {
            float encY2 = 0.0f;
            final NBTTagList enchants2 = renderMainHand.getEnchantmentTagList();
            for (int index2 = 0; index2 < enchants2.tagCount(); ++index2) {
                final short id2 = enchants2.getCompoundTagAt(index2).getShort("id");
                final Enchantment enc2 = Enchantment.getEnchantmentByID(id2);
                if (enc2 != null) {
                    encY2 += 8.0f;
                    arm = true;
                }
            }
            if (encY2 > enchantmentY) {
                enchantmentY = encY2;
            }
        }
        final ItemStack renderOffHand = player.getHeldItemOffhand().copy();
        if (renderOffHand.hasEffect()) {
            float encY = 0.0f;
            final NBTTagList enchants = renderOffHand.getEnchantmentTagList();
            for (int index = 0; index < enchants.tagCount(); ++index) {
                final short id = enchants.getCompoundTagAt(index).getShort("id");
                final Enchantment enc = Enchantment.getEnchantmentByID(id);
                if (enc != null) {
                    encY += 8.0f;
                    arm = true;
                }
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        return (arm ? 0 : 20) + enchantmentY;
    }

    private String getDisplayTag(final EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(mc.getSession().getUsername())) {
            name = "You";
        }
        if(Kaotik.getModuleManager().isModuleEnabled("StreamerMode") && ModuleStreamerMode.hideName.getValue()) {
            if(Kaotik.FRIEND_MANAGER.isFriend(player.getName())) {
                name = "Friend";
            } else {
                name = ModuleStreamerMode.otherName.getValue();
            }
        }
        if (!this.show_health.getValue()) {
            return name;
        }
        final float health = EntityUtils.getHealth((Entity)player);
        String color;
        if (health > 18.0f) {
            color = TextFormatting.GREEN.toString();
        }
        else if (health > 16.0f) {
            color = TextFormatting.DARK_GREEN.toString();
        }
        else if (health > 12.0f) {
            color = TextFormatting.YELLOW.toString();
        }
        else if (health > 8.0f) {
            color = TextFormatting.GOLD.toString();
        }
        else if (health > 5.0f) {
            color = TextFormatting.RED.toString();
        }
        else {
            color = TextFormatting.DARK_RED.toString();
        }
        String pingStr = "";
        if (this.show_ping.getValue()) {
            try {
                final int responseTime = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                pingStr = " " + pingStr + responseTime + "ms";
            }
            catch (Exception ex) {}
        }

        String idString = "";
        if (entityID.getValue()) {
            idString = idString + " ID: " + player.getEntityId() + " ";
        }

        String gameModeStr = "";
        if (this.gamemode.getValue()) {
            if (player.isCreative()) {
                gameModeStr += " [C]";
            }
            else if (player.isSpectator() || player.isInvisible()) {
                gameModeStr += " [I]";
            }
            else {
                gameModeStr += " [S]";
            }
        }
        String healthStr = "";
        if(this.show_health.getValue()) {
            if (Math.floor(health) == health) {
                healthStr = color + " " + ((health > 0.0f) ? Integer.valueOf((int) Math.floor(health)) : "dead");
            } else {
                healthStr = color + " " + ((health > 0.0f) ? Integer.valueOf((int)health) : "dead");
            }
        }
        String popcolor = "";

        String popStr = "";
        return name + idString + gameModeStr + pingStr + healthStr + popStr;
    }

    private int getDisplayColour(final EntityPlayer player) {
        int colour = -1;
        if (Kaotik.FRIEND_MANAGER.isFriend(player.getName())) {
            return -11157267;
        }
        else if (player.isSneaking()) {
            colour = color.getRGB();
        }
        return colour;
    }

    private double interpolate(final double previous, final double current, final float delta) {
        return previous + (current - previous) * delta;
    }

    public String section_sign() {
        return "\u00A7";
    }

}
