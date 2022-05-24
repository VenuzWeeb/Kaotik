package com.mawuote.api.manager.misc;

import com.mawuote.api.utilities.render.RainbowUtils;
import com.mawuote.client.modules.client.ModuleColor;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;

/**
 * @author SrRina
 * @since 09/10/2020 at 16:22
 */
public class ChatManager {
    private Minecraft mc = Minecraft.getMinecraft();


    private GuiNewChat gameChatGUI;

    public static String prefix = ModuleColor.getBracketColour() + " [" + ModuleColor.getTextColor() + "Kaotik" + ModuleColor.getBracketColour() + "] " + ModuleColor.getTextColor();

    public static ChatManager INSTANCE;

    public ChatManager() {
        INSTANCE = this;
    }

    public void printChatMessage(String message) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }

        if (gameChatGUI == null) {
            gameChatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        }

        gameChatGUI.printChatMessage(new TextComponentString(message));
    }

    public void sendChatMessage(String message) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }

        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage(message));
    }

    public static void sendRawMessage(String message){
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
    }

    public static void printChatNotifyClient(String message) {
        if (ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Rainbow) || ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Gradient)) {
            INSTANCE.printChatMessage((ModuleColor.prefix.getValue() ? "\u00a7+" + ChatFormatting.GRAY + " [" + ModuleColor.getTextColor() + "Kaotik" + ChatFormatting.GRAY + "] " : "") + "\u00a7r" + ChatFormatting.RESET + message);
        } else {
            INSTANCE.printChatMessage((ModuleColor.prefix.getValue() ? ChatFormatting.GRAY + " [" + ModuleColor.getTextColor() + "Kaotik" + ChatFormatting.GRAY + "] " : "") + ChatFormatting.RESET + message);
        }
    }

    public static void printTextComponentMessage(TextComponentString message) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }

        if (INSTANCE.gameChatGUI == null) {
            INSTANCE.gameChatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        }

        INSTANCE.gameChatGUI.printChatMessage(message);
    }

    public static void sendClientMessage(String string, int id) {
        if (Minecraft.getMinecraft().player == null)
            return;

        final ITextComponent component;

        if(ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Rainbow) || ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Gradient)) {
            component = new TextComponentString((ModuleColor.prefix.getValue() ? "\u00a7+" + ChatFormatting.GRAY + " [" + ModuleColor.getTextColor() + "Kaotik" + ChatFormatting.GRAY + "] " : "") + "\u00a7r" + ChatFormatting.RESET + string);
        } else {
            component = new TextComponentString((ModuleColor.prefix.getValue() ? ModuleColor.getBracketColour() + " [" + ModuleColor.getTextColor() + "Kaotik" + ModuleColor.getBracketColour() + "] " : "") + ChatFormatting.RESET + string);
        }
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, id);
    }

    public void drawRainbowString(String text, float x, float y, boolean shadow) {
        int currentWidth = 0;
        boolean shouldRainbow = true;
        boolean shouldContinue = false;
        int[] counterChing = {1};
        for (int i = 0; i < text.length(); ++i) {
            Color color;
            if(ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Rainbow)) {
                color = RainbowUtils.anyRainbowColor(counterChing[0] * 150, 180, 255);
            } else {
                color = RainbowUtils.getGradientOffset(new Color(ModuleColor.prefixStart.getValue().getRed(), ModuleColor.prefixStart.getValue().getGreen(), ModuleColor.prefixStart.getValue().getBlue()), new Color(ModuleColor.prefixEnd.getValue().getRed(), ModuleColor.prefixEnd.getValue().getGreen(), ModuleColor.prefixEnd.getValue().getBlue()), Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) 20 / (float) ((counterChing[0] * 2) + 10) * 2.0F) % 2.0F - 1.0F));
            }

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
                this.drawString(escapeString, x + (float) currentWidth, y, Color.WHITE.getRGB(), shadow);
                break;
            }
            this.drawString(String.valueOf(currentChar).equals("\u00a7") ? "" : String.valueOf(currentChar), x + (float) currentWidth, y, shouldRainbow ? color.getRGB() : Color.WHITE.getRGB(), shadow);
            if (String.valueOf(currentChar).equals("\u00a7")) {
                shouldContinue = true;
            }
            currentWidth += this.getStringWidth(String.valueOf(currentChar));
            if (String.valueOf(currentChar).equals(" ")) continue;
            counterChing[0]++;
        }
    }

    public int getStringWidth(String text) {
        return mc.fontRenderer.getStringWidth(text);
    }

    public float drawString(String text, float x, float y, int color, boolean shadow) {
        mc.fontRenderer.drawString(text, x, y, color, shadow);
        return x;
    }
}
