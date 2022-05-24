package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.api.utilities.math.TimerUtils;
import com.mawuote.client.modules.client.ModuleColor;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class StringComponent extends Component {
    ValueString setting;

    private boolean listening;
    private String currentString = "";

    private final TimerUtils timer = new TimerUtils();
    private final TimerUtils backTimer = new TimerUtils();
    private final TimerUtils deleteTimer = new TimerUtils();
    private boolean selecting = false;
    private boolean undoing = false;

    public StringComponent(final ValueString setting, final ModuleComponent parent, final int offset) {
        super(parent.getParent().getX(), parent.getParent().getY() + offset, parent.getParent());
        this.setting = setting;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        if (timer.hasReached(400L)){
            undoing = !undoing;
            timer.reset();
        }

        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + 14, new Color(40, 40, 40).getRGB());
        Gui.drawRect(getX() - 1, getY(), getX(), getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + 13, new Color(30, 30, 30).getRGB());

        if (selecting) Gui.drawRect(getX() + 3, getY() + 3, (int) (getX() + 3 + Kaotik.FONT_MANAGER.getStringWidth(currentString)), (int) (getY() + Kaotik.FONT_MANAGER.getHeight() + 3), new Color(ModuleColor.getActualColor().getRed(), ModuleColor.getActualColor().getGreen(), ModuleColor.getActualColor().getBlue(), 100).getRGB());

        if (listening) {
            Kaotik.FONT_MANAGER.drawString(currentString + (selecting ? "" : (undoing ? (Kaotik.MODULE_MANAGER.isModuleEnabled("Font") ? "|" : "\u23d0") : "")), getX() + 3, getY() + 3, Color.LIGHT_GRAY);
        } else {
            Kaotik.FONT_MANAGER.drawString(setting.getValue(), getX() + 3, getY() + 3, Color.LIGHT_GRAY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()) {
            listening = !listening;
            currentString = setting.getValue();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        backTimer.reset();

        if (listening){
            if (keyCode == 1) {
                selecting = false;
                return;
            } else if (keyCode == 28){
                updateString();
                selecting = false;
                listening = false;
            } else if (keyCode == 14){
                currentString = selecting ? "" : removeLastCharacter(currentString);
                selecting = false;
            } else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                try {
                    currentString = currentString + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception exception){
                    exception.printStackTrace();
                }
            } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)){
                currentString = selecting ? "" + typedChar : currentString + typedChar;
                selecting = false;
            }

            if (keyCode == 30 && Keyboard.isKeyDown(29)) {
                selecting = true;
            }
        }
    }

    private void updateString(){
        if (currentString.length() > 0) setting.setValue(currentString);
        currentString = "";
    }

    private String removeLastCharacter(String input){
        if (input.length() > 0) {
            return input.substring(0, input.length() - 1);
        } else {
            return input;
        }
    }
}