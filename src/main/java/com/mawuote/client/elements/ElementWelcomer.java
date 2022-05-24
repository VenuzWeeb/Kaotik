package com.mawuote.client.elements;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.client.modules.client.ModuleColor;
import com.mawuote.client.modules.client.ModuleStreamerMode;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class ElementWelcomer extends Element {
    public ElementWelcomer() {
        super("Welcomer", "Renders a nice greeting message.");
    }

    public static ValueEnum mode = new ValueEnum("Mode", "Mode", "The mode for the Welcomer.", Modes.Shorter);
    public static ValueBoolean center = new ValueBoolean("Center", "Center", "Makes the Welcomer be positioned to the center.", true);
    public static ValueString customValue = new ValueString("CustomValue", "CustomValue", "The value for the Custom mode.", "Hello, <player>!");
    public static ValueEnum nameColor = new ValueEnum("NameColor", "NameColor", "The color for thet player's name.", NameColors.Normal);

    public static ValueBoolean emoji = new ValueBoolean("Emoji", "Emoji", "Renders a nice face after the welcomer text.", true);
    public static ValueString emojiValue = new ValueString("EmojiValue", "EmojiValue", "The value for the Emoji.", ">:)");

    @Override
    public void onRender2D(EventRender2D event){
        frame.setWidth(Kaotik.FONT_MANAGER.getStringWidth(getText()));
        frame.setHeight(Kaotik.FONT_MANAGER.getHeight());

        ScaledResolution resolution = new ScaledResolution(mc);
        Kaotik.FONT_MANAGER.drawString(getText(), center.getValue() ? (resolution.getScaledWidth() / 2.0f) - (Kaotik.FONT_MANAGER.getStringWidth(getText()) / 2.0f) : frame.getX(), frame.getY(), ModuleColor.getActualColor());
    }

    public String getText(){
        return getWelcomeMessage() + (emoji.getValue() ? " " + emojiValue.getValue() : "");
    }

    public String getWelcomeMessage(){
        switch ((Modes) mode.getValue()){
            case Short: {
                return "Greetings, " + getNameColor() + getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Time:{
                return getTimeOfDay() + ", " + getNameColor() + getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Holiday:{
                return getHoliday() + ", " + getNameColor() + getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Hebrew:{
                return "Shalom, " + getNameColor() + getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Long:{
                return "Welcome to Kaotik, " + getNameColor() + getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Custom:{
                return customValue.getValue().replaceAll("<player>", getNameColor() + getPlayerName() + ChatFormatting.RESET);
            }
            default: {
                return "Hello, " + getNameColor() + getPlayerName() + ChatFormatting.RESET + "!";
            }
        }
    }

    private ChatFormatting getNameColor(){
        if (nameColor.getValue().equals(NameColors.White)){
            return ChatFormatting.WHITE;
        } else if (nameColor.getValue().equals(NameColors.Gray)){
            return ChatFormatting.GRAY;
        } else {
            return ChatFormatting.RESET;
        }
    }

    public String getPlayerName(){
        if (Kaotik.MODULE_MANAGER.isModuleEnabled("StreamerMode") && ModuleStreamerMode.hideYou.getValue()){
            return ModuleStreamerMode.yourName.getValue();
        } else {
            return mc.player.getName();
        }
    }

    public String getTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            return "Good morning";
        } else if (timeOfDay < 16) {
            return "Good afternoon";
        } else if (timeOfDay < 21) {
            return "Good evening";
        } else {
            return "Good night";
        }
    }

    public String getHoliday() {
        final int month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        final int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        switch (month) {
            case 1: {
                if (day == 1) {
                    return "Happy New Years";
                }
            }
            case 2: {
                if (day == 14) {
                    return "Happy Valentines Day";
                }
                break;
            }
            case 10: {
                if (day == 31) {
                    return "Happy Halloween";
                }
                break;
            }
            case 11: {
                final LocalDate thanksGiving = Year.of(Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))).atMonth(Month.NOVEMBER).atDay(1).with(TemporalAdjusters.lastInMonth(DayOfWeek.WEDNESDAY));
                if (thanksGiving.getDayOfMonth() == day) {
                    return "Happy Thanksgiving";
                }
            }
            case 12: {
                if (day == 25) {
                    return "Merry Christmas";
                }
                break;
            }
        }
        return "No holiday is currently going on";
    }

    public enum Modes { Shorter, Short, Holiday, Time, Hebrew, Long, Custom }
    public enum NameColors { Normal, White, Gray }
}
