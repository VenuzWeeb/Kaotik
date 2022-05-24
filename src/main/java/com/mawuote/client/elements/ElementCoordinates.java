package com.mawuote.client.elements;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.client.modules.client.ModuleColor;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiChat;

import java.text.DecimalFormat;

public class ElementCoordinates extends Element {
    public ElementCoordinates() {
        super("Coordinates", "Renders your coordinates and direction on screen.");
    }

    DecimalFormat format = new DecimalFormat("#.#");

    public static ValueEnum color = new ValueEnum("Color", "Color", "The color of the Coordinates.", Colors.Gray);
    public static ValueEnum direction = new ValueEnum("Direction", "Direction", "Renders the direction you are facing on screen.", Directions.Normal);
    public static ValueBoolean nether = new ValueBoolean("Opposite", "Opposite", "Renders the coordinates of the opposite dimension that you are currently in.", true);
    public static ValueString firstSymbol = new ValueString("FirstSymbol", "FirstSymbol", "The first character to be rendered on the coordinates.", "[");
    public static ValueString secondSymbol = new ValueString("SecondSymbol", "SecondSymbol", "The first character to be rendered on the coordinates.", "]");
    public static ValueBoolean chatMove = new ValueBoolean("ChatMove", "ChatMove", "Moves the coordinates above the chat textbox when it is opened.", true);

    @Override
    public void onRender2D(EventRender2D event){
        super.onRender2D(event);

        frame.setWidth(Kaotik.FONT_MANAGER.getStringWidth(getCoordinatesText()));
        frame.setHeight(direction.getValue().equals(Directions.Normal) ? Kaotik.FONT_MANAGER.getHeight() * 2 + 1 : Kaotik.FONT_MANAGER.getHeight());

        if (direction.getValue().equals(Directions.Normal)) Kaotik.FONT_MANAGER.drawString(getDirectionName() + " " + firstSymbol.getValue() + getColor() + getFacing(mc.player.getHorizontalFacing().getName()) + ChatFormatting.RESET + secondSymbol.getValue(), frame.getX(), frame.getY() - (chatMove.getValue() && mc.currentScreen instanceof GuiChat ? 11 : 0), ModuleColor.getActualColor());
        Kaotik.FONT_MANAGER.drawString(getCoordinatesText(), frame.getX(), direction.getValue().equals(Directions.Normal) ? (frame.getY() + Kaotik.FONT_MANAGER.getHeight() + 1) - (chatMove.getValue() && mc.currentScreen instanceof GuiChat ? 11 : 0) : frame.getY() - (chatMove.getValue() && mc.currentScreen instanceof GuiChat ? 12 : 0), ModuleColor.getActualColor());
    }

    public String getCoordinatesText(){
        return (direction.getValue().equals(Directions.Merged) ? getDirectionName() + " " + firstSymbol.getValue() + getColor() + getFacing(mc.player.getHorizontalFacing().getName()) + ChatFormatting.RESET + secondSymbol.getValue() + " " : "") + "X: " + getColor() + format.format(mc.player.posX) + ChatFormatting.RESET + " Y: " + getColor() + format.format(mc.player.posY) + ChatFormatting.RESET + " Z: " + getColor() + format.format(mc.player.posZ) + ChatFormatting.RESET + (nether.getValue() ? " " + firstSymbol.getValue() + getColor() + format.format(mc.player.dimension == -1 ? mc.player.posX * 8 : mc.player.posX / 8) + ChatFormatting.RESET + ", " + getColor() + format.format(mc.player.dimension == -1 ? mc.player.posX * 8 : mc.player.posX / 8) + ChatFormatting.RESET + secondSymbol.getValue() : "");
    }

    private String getDirectionName(){
        return mc.player.getHorizontalFacing().getName().substring(0, 1).toUpperCase() + mc.player.getHorizontalFacing().getName().substring(1).toLowerCase();
    }

    public ChatFormatting getColor(){
        if (color.getValue().equals(Colors.White)){
            return ChatFormatting.WHITE;
        } else if (color.getValue().equals(Colors.Gray)){
            return ChatFormatting.GRAY;
        } else {
            return ChatFormatting.RESET;
        }
    }

    private String getFacing(String input) {
        switch (input.toLowerCase()) {
            case "north":
                return "-Z";
            case "east":
                return "+X";
            case "south":
                return "+Z";
            default:
                return "-X";
        }
    }

    public enum Colors { Normal, White, Gray }
    public enum Directions { None, Normal, Merged }
}
