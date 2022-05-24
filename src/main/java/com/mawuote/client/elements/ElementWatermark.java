package com.mawuote.client.elements;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.client.modules.client.ModuleColor;
import com.mojang.realmsclient.gui.ChatFormatting;

public class ElementWatermark extends Element {
    public ElementWatermark() {
        super("Watermark", "The client's watermark.");
    }

    public static ValueEnum mode = new ValueEnum("Mode", "Mode", "The mode for the watermark.", Modes.Normal);
    public static ValueString customValue = new ValueString("CustomValue", "CustomValue", "The value for the Custom Watermark.", "Kaotik");

    public static ValueEnum version = new ValueEnum("Version", "Version", "Renders the Version on the watermark.", Versions.Normal);
    public static ValueEnum versionColor = new ValueEnum("VersionColor", "VersionColor", "The color for the version.", VersionColors.Normal);

    @Override
    public void onRender2D(EventRender2D event){
        super.onRender2D(event);

        frame.setWidth(Kaotik.FONT_MANAGER.getStringWidth(getText()));
        frame.setHeight(Kaotik.FONT_MANAGER.getHeight());

        Kaotik.FONT_MANAGER.drawString(getText(), frame.getX(), frame.getY(), ModuleColor.getActualColor());
    }

    private String getText(){
        return (mode.getValue().equals(Modes.Custom) ? customValue.getValue() : "Kaotik") + (!version.getValue().equals(Versions.None) ? " " + getVersionColor() + (version.getValue().equals(Versions.Normal) ? "v" : "") + Kaotik.VERSION : "");
    }

    private ChatFormatting getVersionColor(){
        if (versionColor.getValue().equals(VersionColors.White)){
            return ChatFormatting.WHITE;
        } else if (versionColor.getValue().equals(VersionColors.Gray)){
            return ChatFormatting.GRAY;
        } else {
            return ChatFormatting.RESET;
        }
    }

    public enum Modes { Normal, Custom }
    public enum Versions { None, Simple, Normal }
    public enum VersionColors { Normal, White, Gray }
}