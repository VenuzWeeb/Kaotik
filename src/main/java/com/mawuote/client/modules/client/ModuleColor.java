package com.mawuote.client.modules.client;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.*;

public class ModuleColor extends Module {

    public ModuleColor() {
        super("Color", "Color", "Allows you to customize the client's main colors.", ModuleCategory.CLIENT, true);
    }

    public enum prefixModes {
        Rainbow, Gradient, Static;
    }

    public static ValueBoolean prefix = new ValueBoolean("Prefix", "Prefix", "", false);
    public static ValueEnum textColour = new ValueEnum("TextColour","textcolour","", colourModes.Blue);
    public static ValueEnum bracketColour = new ValueEnum("BracketColour","bracketcolour","", bracketModes.Gray);
    public static ValueEnum prefixMode = new ValueEnum("PrefixMode", "PrefixMode", "", prefixModes.Static);

    public static ValueColor prefixStart = new ValueColor("PrefixStart", "PrefixStart", "", new Color(255, 0, 0, 255));
    public static ValueColor prefixEnd = new ValueColor("PrefixEnd", "PrefixEnd", "", new Color(0, 0, 0, 255));

    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(82, 1, 1, 255));

    public static Color getActualColor() {
        return new Color(daColor.getValue().getRed(), daColor.getValue().getGreen(), daColor.getValue().getBlue());
    }

    public static int getColor() {
        return new Color(daColor.getValue().getRed(), daColor.getValue().getGreen(), daColor.getValue().getBlue()).getRGB();
    }

    public enum colourModes {
        Black,
        Blue,
        DarkBlue,
        Green,
        DarkGreen,
        Red,
        DarkRed,
        Gold,
        Gray,
        DarkGray,
        Yellow,
        DarkAqua,
        DarkPurple,
        Aqua,
        LightPurple,
        White
    }

    public enum bracketModes {
        Black,
        Blue,
        DarkBlue,
        Green,
        DarkGreen,
        Red,
        DarkRed,
        Gold,
        Gray,
        DarkGray,
        Yellow,
        DarkAqua,
        DarkPurple,
        Aqua,
        LightPurple,
        White
    }

    public static ChatFormatting getTextColor(){
        switch ((colourModes) textColour.getValue()) {
            case Red: {
                return ChatFormatting.RED;
            }
            case Aqua: {
                return ChatFormatting.AQUA;
            }
            case Blue: {
                return ChatFormatting.BLUE;
            }
            case Gold: {
                return ChatFormatting.GOLD;
            }
            case Gray: {
                return ChatFormatting.GRAY;
            }
            case Black: {
                return ChatFormatting.BLACK;
            }
            case Green: {
                return ChatFormatting.GREEN;
            }
            case White: {
                return ChatFormatting.WHITE;
            }
            case Yellow: {
                return ChatFormatting.YELLOW;
            }
            case DarkRed: {
                return ChatFormatting.DARK_RED;
            }
            case DarkAqua: {
                return ChatFormatting.DARK_AQUA;
            }
            case DarkBlue: {
                return ChatFormatting.DARK_BLUE;
            }
            case DarkGray: {
                return ChatFormatting.DARK_GRAY;
            }
            case DarkGreen: {
                return ChatFormatting.DARK_GREEN;
            }
            case DarkPurple: {
                return ChatFormatting.DARK_PURPLE;
            }
            case LightPurple: {
                return ChatFormatting.LIGHT_PURPLE;
            }
        }
        return null;
    }

    public static ChatFormatting getBracketColour(){

        switch ((bracketModes) bracketColour.getValue()) {
            case Red: {
                return ChatFormatting.RED;
            }
            case Aqua: {
                return ChatFormatting.AQUA;
            }
            case Blue: {
                return ChatFormatting.BLUE;
            }
            case Gold: {
                return ChatFormatting.GOLD;
            }
            case Gray: {
                return ChatFormatting.GRAY;
            }
            case Black: {
                return ChatFormatting.BLACK;
            }
            case Green: {
                return ChatFormatting.GREEN;
            }
            case White: {
                return ChatFormatting.WHITE;
            }
            case Yellow: {
                return ChatFormatting.YELLOW;
            }
            case DarkRed: {
                return ChatFormatting.DARK_RED;
            }
            case DarkAqua: {
                return ChatFormatting.DARK_AQUA;
            }
            case DarkBlue: {
                return ChatFormatting.DARK_BLUE;
            }
            case DarkGray: {
                return ChatFormatting.DARK_GRAY;
            }
            case DarkGreen: {
                return ChatFormatting.DARK_GREEN;
            }
            case DarkPurple: {
                return ChatFormatting.DARK_PURPLE;
            }
            case LightPurple: {
                return ChatFormatting.LIGHT_PURPLE;
            }
        }
        return null;
    }

}
