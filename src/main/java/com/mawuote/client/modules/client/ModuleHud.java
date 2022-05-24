package com.mawuote.client.modules.client;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.utilities.math.AnimationUtils;
import com.mawuote.api.utilities.render.GifLocation;
import com.mawuote.api.utilities.render.RainbowUtils;
import com.mawuote.api.utilities.math.TimerUtils;
import com.mawuote.api.utilities.math.TPSUtils;
import com.mawuote.api.manager.value.impl.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Objects;


//TODO
public class ModuleHud extends Module<B> {
    public ModuleHud() {
        super("Hud", "Hud", "", ModuleCategory.CLIENT);
    }

    public enum effectHudModes {
        Hide, Keep, Move;
    }

    public enum infoColors {
        Gray, White;
    }

    public enum waterModes {
        Default, Custom;
    }

    //color
    public static ValueEnum colorMode = new ValueEnum("ColorMode", "ColorMode", "", colorModes.Wave);
    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(70, 10, 10));
    public static ValueColor gradient1 = new ValueColor("Gradient1", "Gradient1", "", new Color(255, 0, 0));
    public static ValueColor gradient2 = new ValueColor("Gradient2", "Gradient2", "", new Color(0, 0, 0));
    public static ValueNumber rainbowOffset = new ValueNumber("RainbowOffset", "RainbowOffset", "", 255, 0, 255);
    public static ValueNumber rainbowSat = new ValueNumber("RainbowSaturation", "RainbowSat", "", 255, 0, 255);
    public static ValueNumber rainbowBri = new ValueNumber("RainbowBrightness", "RainbowSat", "", 255, 0, 255);
    public static ValueEnum infoColor = new ValueEnum("InfoColor", "InfoColor", "", infoColors.Gray);

    public static ValueEnum waterMode = new ValueEnum("WaterMode", "WaterMode", "", waterModes.Default);
    public static ValueString waterString = new ValueString("WaterString", "WaterStrig", "", "Kaotik");
    public static ValueBoolean watermark = new ValueBoolean("Watermark", "Watermark", "", true);
    public static ValueBoolean watermarkVersion = new ValueBoolean("WatermarkVersion", "WatermarkVersion", "", true);
    public static ValueBoolean welcomer = new ValueBoolean("Welcomer", "Welcomer", "", false);
    public static ValueBoolean fps = new ValueBoolean("FPS", "FPS", "", true);
    public static ValueBoolean tps = new ValueBoolean("TPS", "TPS", "", true);
    public static ValueBoolean ping = new ValueBoolean("Ping", "Ping", "", true);
    public static ValueBoolean packetPS = new ValueBoolean("Packets", "Packets", "", false);
    public static ValueBoolean speed = new ValueBoolean("Speed", "Speed", "", true);
    public static ValueBoolean brand = new ValueBoolean("ServerBrand", "ServerBrand", "", true);
    public static ValueEnum effectHud = new ValueEnum("EffectHud", "EffectHud", "", effectHudModes.Move);
    public static ValueBoolean potionEffects = new ValueBoolean("Effects", "Effect", "", true);
    public static ValueBoolean potionSync = new ValueBoolean("EffectSync", "EffectSync", "", false);
    public static ValueBoolean coords = new ValueBoolean("Coords", "Coords", "", true);
    public static ValueBoolean netherCoords = new ValueBoolean("NetherCoords", "NetherCoords", "", true);
    public static ValueBoolean direction = new ValueBoolean("Direction", "Direction", "", true);
    public static ValueBoolean lagNotifier = new ValueBoolean("LagNotifier", "LagNotifier", "", false);
    public static ValueBoolean rubberNotifier = new ValueBoolean("RubberNotifier", "RubberNotifier", "", false);
    public static ValueBoolean armor = new ValueBoolean("Armor", "Armor", "", false);

    public enum colorModes {
        Static, Wave, Gradient, Rainbow;
    }

    public enum renderingModes {
        Up, Down;
    }

    public enum orderModes {
        ABC, Length;
    }

    public static ValueBoolean arrayList = new ValueBoolean("ArrayList", "ArrayList", "", true);
    public static ValueEnum arrayRendering = new ValueEnum("Rendering", "Rendering", "", renderingModes.Up);
    public static ValueEnum ordering = new ValueEnum("Ordering", "Ordering", "", orderModes.Length);

    private int components;
    private int leftComponents;
    private int packets;
    AnimationUtils anim = new AnimationUtils(500, 1, 100);
    TimerUtils packetTimer = new TimerUtils();
    private Color colorHud;
    private boolean rubberbanded;
    TimerUtils serverTimer = new TimerUtils();
    TimerUtils rubberTimer = new TimerUtils();
    private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

    public void renderGif() {
        GifLocation gif = new GifLocation("clientname/ban", 44, 1);

        gif.update();

        mc.getTextureManager().bindTexture(gif.getTexture());
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 498, 494, 498, 494);
    }

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        serverTimer.reset();
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            rubberbanded = true;
            rubberTimer.reset();
        }
    }

    @SubscribeEvent
    public void onSend(EventPacket.Send event) {
        packets += 1;
    }

    public void onUpdate() {
        if(packetTimer.hasReached(1000)) {
            packets = 0;
            packetTimer.reset();
        }
    }

    public void onRender2D(EventRender2D event) {
        if(mc.player == null || mc.world == null)
            return;

        /*drawStringWithShadow(anim.getValue() + "", 20, 20, -1);

        drawStringWithShadow("HELLOO", (int)anim.getValue(), 20, -1);


        if(anim.isDone()) {
            drawStringWithShadow("DONE", 50, 50, Color.GREEN.getRGB());
            anim.reset();
        }*/

        Color color = new Color(daColor.getValue().getRed(), daColor.getValue().getGreen(), daColor.getValue().getBlue());
        Color color1 = new Color(gradient1.getValue().getRed(), gradient1.getValue().getGreen(), gradient1.getValue().getBlue());
        Color color2 = new Color(gradient2.getValue().getRed(), gradient2.getValue().getGreen(), gradient2.getValue().getBlue());

        colorHud = color;

        if(welcomer.getValue()) {
            Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((leftComponents * 2) + 10) * 2.0F) % 2.0F - 1.0F));
            Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (leftComponents * 2) + 10);
            String stringW = "Hello " + mc.player.getName() + ", Welcome To Kaotik";

            if (colorMode.getValue().equals(colorModes.Rainbow)) {
                drawRainbowString(stringW, new ScaledResolution(mc).getScaledWidth() / 2 - Kaotik.FONT_MANAGER.getStringWidth( stringW) / 2, 2, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            } else {
                drawStringWithShadow(stringW, new ScaledResolution(mc).getScaledWidth() / 2 - Kaotik.FONT_MANAGER.getStringWidth(stringW) / 2, 2, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            }
        }

        if(lagNotifier.getValue() && serverTimer.hasReached(1000)) {
            drawStringWithShadow(ChatFormatting.RED + "Server has not responded for \247r" + new DecimalFormat("0.0").format((double) serverTimer.time() / 1000) + "s", new ScaledResolution(mc).getScaledWidth() / 2 - Kaotik.FONT_MANAGER.getStringWidth( "Server has not responded for " + new DecimalFormat("0.0").format(serverTimer.time() / 1000) + "s") / 2, welcomer.getValue() ? 12 : 2, -1);
        }

        if(rubberNotifier.getValue() && rubberbanded) {
            drawStringWithShadow(ChatFormatting.RED + "Rubberband detected \247r" + new DecimalFormat("0.0").format((double) rubberTimer.time() / 1000) + "s", new ScaledResolution(mc).getScaledWidth() / 2 - Kaotik.FONT_MANAGER.getStringWidth( (ChatFormatting.RED + "Rubberband detected \247r" + new DecimalFormat("0.0").format((double) rubberTimer.time() / 1000) + "s")) / 2, 0 + (welcomer.getValue() ? 12 : 0) + (lagNotifier.getValue() ? 12 : 0), -1);
            if(rubberTimer.hasReached(3500)) {
                rubberbanded = false;
            }
        }

        if (armor.getValue()) {

            GlStateManager.enableTexture2D();

            ScaledResolution resolution = new ScaledResolution(mc);
            int i = resolution.getScaledWidth() / 2;
            int iteration = 0;
            int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
            for (ItemStack is : mc.player.inventory.armorInventory) {
                iteration++;
                if (is.isEmpty()) continue;
                int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();

                itemRender.zLevel = 200F;
                itemRender.renderItemAndEffectIntoGUI(is, x, y);
                itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
                itemRender.zLevel = 0F;

                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();

                String s = is.getCount() > 1 ? is.getCount() + "" : "";
                mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, 0xffffff);
                float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                float red = 1 - green;
                int dmg = 100 - (int) (red * 100);
                drawStringWithShadow(dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, new Color(red, green, 0).getRGB());
            }

            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }

        if(arrayRendering.getValue().equals(renderingModes.Up)) {
            this.components = 1;
        } else {
            this.components = 0;
        }
        this.leftComponents = 0;

        ScaledResolution scaledRes = new ScaledResolution(mc);

        if(watermark.getValue()) {
            Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((leftComponents * 2) + 10) * 2.0F) % 2.0F - 1.0F));
            Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (leftComponents * 2) + 10);
            String stringW;
            if(waterMode.getValue().equals(waterModes.Default)) {
                stringW = "Kaotik" + (watermarkVersion.getValue() ? " " + Kaotik.VERSION : "");
            } else {
                stringW = waterString.getValue() + (watermarkVersion.getValue() ? " " + Kaotik.VERSION : "");
            }
            if (colorMode.getValue().equals(colorModes.Rainbow)) {
                drawRainbowString(stringW, 2, 2, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            } else {
                drawStringWithShadow(stringW, 2, 2, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            }
        }

        if(coords.getValue()) {
            Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((leftComponents * 2) + 10) * 2.0F) % 2.0F - 1.0F));
            Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (leftComponents * 2) + 10);

            final boolean inHell = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell");
            final String posX = String.format("%.1f", mc.player.posX);
            final String posY = String.format("%.1f", mc.player.posY);
            final String posZ = String.format("%.1f", mc.player.posZ);
            final float nether = inHell ? 8.0f : 0.125f;
            final String hposX = String.format("%.1f", (mc.player.posX * nether));
            final String hposZ = String.format("%.1f", (mc.player.posZ * nether));
            if (colorMode.getValue().equals(colorModes.Rainbow)) {
                drawRainbowString("\u00a7+" + "XYZ " + "\u00a7r" + getInfoColor() + posX + ", " + posY + ", " + posZ + " " + (netherCoords.getValue() ? "[" + hposX + ", " + hposZ + "]" : ""), 2, scaledRes.getScaledHeight() - 2 - (mc.ingameGUI.getChatGUI().getChatOpen() ? Kaotik.FONT_MANAGER.getHeight() + 14 : Kaotik.FONT_MANAGER.getHeight()), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            } else {
                drawStringWithShadow("XYZ " + getInfoColor() + posX + ", " + posY + ", " + posZ + " " + (netherCoords.getValue() ? "[" + hposX + ", " + hposZ + "]" : ""), 2, scaledRes.getScaledHeight() - 2 - (mc.ingameGUI.getChatGUI().getChatOpen() ? Kaotik.FONT_MANAGER.getHeight() + 14 : Kaotik.FONT_MANAGER.getHeight()), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            }
            leftComponents++;
        }

        if(direction.getValue()) {
            Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((leftComponents * 2) + 10) * 2.0F) % 2.0F - 1.0F));
            Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (leftComponents * 2) + 10);
            if (colorMode.getValue().equals(colorModes.Rainbow)) {
                drawRainbowString("\u00a7+" + getFacing() + "\u00a7r" + getInfoColor() + " [" + getTowards() + "]", 2, scaledRes.getScaledHeight() - 2 - (mc.ingameGUI.getChatGUI().getChatOpen() ? Kaotik.FONT_MANAGER.getHeight() + 14 : Kaotik.FONT_MANAGER.getHeight() + 2) - (leftComponents * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            } else {
                drawStringWithShadow(getFacing() + getInfoColor() + " [" + getTowards() + "]", 2, scaledRes.getScaledHeight() - 2 - (mc.ingameGUI.getChatGUI().getChatOpen() ? Kaotik.FONT_MANAGER.getHeight() + 14 : Kaotik.FONT_MANAGER.getHeight() + 2) - (leftComponents * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
            }
        }

        if(arrayRendering.getValue().equals(renderingModes.Up)) {
            if(potionEffects.getValue()) {
                final int[] potCount = {0};
                try {
                    mc.player.getActivePotionEffects().forEach(effect -> {
                        Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                        Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);
                        String name = I18n.format(effect.getPotion().getName());
                        double duration = effect.getDuration() / 19.99f;
                        int amplifier = effect.getAmplifier() + 1;
                        int potionColor = effect.getPotion().getLiquidColor();
                        double p1 = duration % 60f;
                        DecimalFormat format2 = new DecimalFormat("00");
                        String seconds = format2.format(p1);
                        String s = name + " " + amplifier + getInfoColor() + " " +  (int) duration / 60 + ":" + seconds;
                        String sR = "\u00a7+" + name + " " + amplifier + "\u00a7r" + getInfoColor() + " " +  (int) duration / 60 + ":" + seconds;
                        if (colorMode.getValue().equals(colorModes.Rainbow) && potionSync.getValue()) {
                            drawRainbowString(sR, scaledRes.getScaledWidth() - 2 - getStringWidth(s), scaledRes.getScaledHeight() + (potCount[0] * -10) - 10 - 2, potionSync.getValue() ? colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()) : potionColor);
                        } else {
                            drawStringWithShadow(s, scaledRes.getScaledWidth() - 2 - getStringWidth(s), scaledRes.getScaledHeight() + (potCount[0] * -10) - 10 - 2, potionSync.getValue() ? colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()) : potionColor);
                        }
                        potCount[0]++;
                        components++;
                    });
                } catch(NullPointerException e){e.printStackTrace();}
            }
            if(brand.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = getServerBrand();
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(speed.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                final DecimalFormat df = new DecimalFormat("#.#");

                final double deltaX = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
                final double deltaZ = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
                final float tickRate = (Minecraft.getMinecraft().timer.tickLength / 1000.0f);
                final String BPSText = df.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate)*3.6);

                String string = "Speed " + getInfoColor() + BPSText + "km/h";
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "Speed " + "\u00a7r" + getInfoColor() + BPSText + "km/h", scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(tps.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "TPS " + getInfoColor() + String.format("%.2f", (double) TPSUtils.getTickRate());
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "TPS " + "\u00a7r" + getInfoColor() + String.format("%.2f", (double) TPSUtils.getTickRate()), scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(fps.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "FPS " + getInfoColor() + Minecraft.debugFPS;
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "FPS " + "\u00a7r" + getInfoColor() + Minecraft.debugFPS, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(ping.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "Ping " + getInfoColor() + getPing();
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "Ping " + "\u00a7r" + getInfoColor() + getPing(), scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(packetPS.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "Packets " + getInfoColor() + this.packets;
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "Packets " + "\u00a7r" + getInfoColor() + this.packets, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() - 2 - (components * 10), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
            }

            if(arrayList.getValue()) {
                final int[] mods = {0};
                if(ordering.getValue().equals(orderModes.Length)) {
                    Kaotik.getModuleManager().getModules().stream().filter(Module::isToggled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> getStringWidth(module.getTag() + module.getHudInfo()) * (-1))).forEach(m -> {
                        String string = m.getTag() + ChatFormatting.GRAY + m.getHudInfo();
                        Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((mods[0] * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                        Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (mods[0] * 2) + 10);
                        if (colorMode.getValue().equals(colorModes.Rainbow)) {
                            drawRainbowString("\u00a7+" + m.getTag() + "\u00a7r" + ChatFormatting.GRAY + m.getHudInfo(), scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (mods[0] * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        } else {
                            drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (mods[0] * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        }
                        mods[0]++;
                    });
                } else {
                    Kaotik.getModuleManager().getModules().stream().filter(Module::isToggled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> module.getTag())).forEach(m -> {
                        String string = m.getTag() + ChatFormatting.GRAY + m.getHudInfo();
                        Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((mods[0] * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                        Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (mods[0] * 2) + 10);
                        if (colorMode.getValue().equals(colorModes.Rainbow)) {
                            drawRainbowString("\u00a7+" + m.getTag() + "\u00a7r" + ChatFormatting.GRAY + m.getHudInfo(), scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (mods[0] * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        } else {
                            drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (mods[0] * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        }
                        mods[0]++;
                    });
                }
            }
        } else {
            if(potionEffects.getValue()) {
                final int[] potCount = {0};
                try {
                    mc.player.getActivePotionEffects().forEach(effect -> {
                        Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                        Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);
                        String name = I18n.format(effect.getPotion().getName());
                        double duration = effect.getDuration() / 19.99f;
                        int amplifier = effect.getAmplifier() + 1;
                        int potionColor = effect.getPotion().getLiquidColor();
                        double p1 = duration % 60f;
                        DecimalFormat format2 = new DecimalFormat("00");
                        String seconds = format2.format(p1);
                        String s = name + " " + amplifier + getInfoColor() + " " +  (int) duration / 60 + ":" + seconds;
                        String sR = "\u00a7+" + name + " " + amplifier + "\u00a7r" + getInfoColor() + " " +  (int) duration / 60 + ":" + seconds;
                        if (colorMode.getValue().equals(colorModes.Rainbow)) {
                            drawStringWithShadow(sR, scaledRes.getScaledWidth() - 2 - getStringWidth(s), 2 + (potCount[0] * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), potionSync.getValue() ? colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()) : potionColor);
                        } else {
                            drawStringWithShadow(s, scaledRes.getScaledWidth() - 2 - getStringWidth(s), 2 + (potCount[0] * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), potionSync.getValue() ? colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()) : potionColor);
                        }
                        potCount[0]++;
                        components++;
                    });
                } catch(NullPointerException e){e.printStackTrace();}
            }
            if(brand.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = getServerBrand();
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(speed.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                final DecimalFormat df = new DecimalFormat("#.#");

                final double deltaX = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
                final double deltaZ = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
                final float tickRate = (Minecraft.getMinecraft().timer.tickLength / 1000.0f);
                final String BPSText = df.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate)*3.6);

                String string = "Speed " + getInfoColor() + BPSText + "km/h";
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "Speed " + "\u00a7r" + getInfoColor() + BPSText + "km/h", scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(tps.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "TPS " + getInfoColor() + String.format("%.2f", (double) TPSUtils.getTickRate());
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "TPS " + "\u00a7r" + getInfoColor() + String.format("%.2f", (double) TPSUtils.getTickRate()), scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(fps.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "FPS " + getInfoColor() + Minecraft.debugFPS;
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "FPS " + "\u00a7r" + getInfoColor() + Minecraft.debugFPS, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(ping.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "Ping " + getInfoColor() + getPing();
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "Ping " + "\u00a7r" + getInfoColor() + getPing(), scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
                components++;
            }
            if(packetPS.getValue()) {
                Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((components * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (components * 2) + 10);

                String string = "Packets " + getInfoColor() + this.packets;
                if (colorMode.getValue().equals(colorModes.Rainbow)) {
                    drawRainbowString("\u00a7+" + "Packets " + "\u00a7r" + getInfoColor() + this.packets, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                } else {
                    drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), 2 + (components * 10) + ((effectHud.getValue().equals(effectHudModes.Move) && !mc.player.getActivePotionEffects().isEmpty()) ? 25 : 0), colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                }
            }

            if(arrayList.getValue()) {
                final int[] mods = {0};
                if(ordering.getValue().equals(orderModes.Length)) {
                    Kaotik.getModuleManager().getModules().stream().filter(Module::isToggled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> getStringWidth(module.getTag() + module.getHudInfo()) * (-1))).forEach(m -> {
                        String string = m.getTag() + ChatFormatting.GRAY + m.getHudInfo();
                        Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((mods[0] * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                        Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (mods[0] * 2) + 10);

                        if (colorMode.getValue().equals(colorModes.Rainbow)) {
                            drawStringWithShadow("\u00a7+" + m.getTag() + "\u00a7r" + ChatFormatting.GRAY + m.getHudInfo(), scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() + (mods[0] * -10) - 12, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        } else {
                            drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() + (mods[0] * -10) - 12, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        }
                        mods[0]++;
                    });
                } else {
                    Kaotik.getModuleManager().getModules().stream().filter(Module::isToggled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> module.getTag())).forEach(m -> {
                        String string = m.getTag() + ChatFormatting.GRAY + m.getHudInfo();
                        Color gradientColor = RainbowUtils.getGradientOffset(color1, color2, Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((mods[0] * 2) + 10) * 2.0F) % 2.0F - 1.0F));
                        Color waveColor = RainbowUtils.astolfoRainbow(color, 50, (mods[0] * 2) + 10);

                        if (colorMode.getValue().equals(colorModes.Rainbow)) {
                            drawStringWithShadow("\u00a7+" + m.getTag() + "\u00a7r" + ChatFormatting.GRAY + m.getHudInfo(), scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() + (mods[0] * -10) - 12, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        } else {
                            drawStringWithShadow(string, scaledRes.getScaledWidth() - 2 - getStringWidth(string), scaledRes.getScaledHeight() + (mods[0] * -10) - 12, colorMode.getValue().equals(colorModes.Static) ? colorHud.getRGB() : (colorMode.getValue().equals(colorModes.Wave) ? waveColor.getRGB() : gradientColor.getRGB()));
                        }
                        mods[0]++;
                    });
                }
            }
        }

    }

    public ChatFormatting getInfoColor() {
        if(infoColor.getValue().equals(infoColors.Gray)) {
            return ChatFormatting.GRAY;
        } else {
            return ChatFormatting.WHITE;
        }
    }

    private void drawStringWithShadow (String text,float x, float y, int color) {
        Kaotik.FONT_MANAGER.drawString(text, x, y, new Color(color));
    }

    public float getStringWidth(String text) {
        return Kaotik.FONT_MANAGER.getStringWidth( text);
    }

    public void drawRainbowString(String text, float x, float y, int coloring) {
        int currentWidth = 0;
        boolean shouldRainbow = true;
        boolean shouldContinue = false;
        int[] counterChing = {1};
        for (int i = 0; i < text.length(); ++i) {
            Color color;
            color = RainbowUtils.anyRainbowColor(counterChing[0] * rainbowOffset.getValue().intValue(), rainbowSat.getValue().intValue(), rainbowBri.getValue().intValue());

            char currentChar = text.charAt(i);
            char nextChar = text.charAt(MathHelper.clamp(i + 1, 0, text.length() - 1));
            if ((String.valueOf(currentChar) + nextChar).equals("\u00a7r")) {
                shouldRainbow = false;
            } else if ((String.valueOf(currentChar) + nextChar).equals("\u00a7+")) {
                shouldRainbow = true;
            }
            if (shouldContinue) {
                shouldContinue = false;
                continue;
            }
            if ((String.valueOf(currentChar) + nextChar).equals("\u00a7r")) {
                String escapeString = text.substring(i);
                Kaotik.FONT_MANAGER.drawString(escapeString, x + (float) currentWidth, y, Color.WHITE);
                break;
            }
            Kaotik.FONT_MANAGER.drawString(String.valueOf(currentChar).equals("\u00a7") ? "" : String.valueOf(currentChar), x + (float) currentWidth, y, shouldRainbow ? color : Color.WHITE);
            if (String.valueOf(currentChar).equals("\u00a7")) {
                shouldContinue = true;
            }
            currentWidth += this.getStringWidth(String.valueOf(currentChar));
            if (String.valueOf(currentChar).equals(" ")) continue;
            counterChing[0]++;
        }
    }

    public static String getFacing()
    {
        switch (MathHelper.floor((double) (mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7)
        {
            case 0:
                return "South";
            case 1:
                return "South";
            case 2:
                return "West";
            case 3:
                return "West";
            case 4:
                return "North";
            case 5:
                return "North";
            case 6:
                return "East";
            case 7:
                return "East";
        }
        return "Invalid";
    }

    public static String getTowards()
    {
        switch (MathHelper.floor((double) (mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7)
        {
            case 0:
                return "+Z";
            case 1:
                return "+Z";
            case 2:
                return "-X";
            case 3:
                return "-X";
            case 4:
                return "-Z";
            case 5:
                return "-Z";
            case 6:
                return "+X";
            case 7:
                return "+X";
        }
        return "Invalid";
    }

    public static int getPing() {
        int p;
        if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
            p = -1;
        } else {
            mc.player.getName();
            p = Objects.requireNonNull(mc.getConnection().getPlayerInfo(mc.player.getName())).getResponseTime();
        }
        return p;
    }

    public static final String getServerBrand() {
        String s;
        if (mc.getCurrentServerData() == null) {
            s = "Vanilla";
        }
        else {
            final EntityPlayerSP it = mc.player;
            final int n = 0;
            final String getServerBrand = mc.player.getServerBrand();
            s = ((getServerBrand == null) ? "Vanilla" : getServerBrand);
        }
        return s;
    }
}
