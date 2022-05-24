package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.value.impl.ValueBind;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class BindComponent extends Component {
    private boolean binding;
    ValueBind setting;

    public BindComponent(final ValueBind setting, final ModuleComponent parent, final int offset) {
        super(parent.getParent().getX(), parent.getParent().getY() + offset, parent.getParent());
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + 14, new Color(40, 40, 40).getRGB());
        Gui.drawRect(getX() - 1, getY(), getX(), getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + 13, new Color(30, 30, 30).getRGB());
        Kaotik.FONT_MANAGER.drawString(setting.getName(), getX() + 3, getY() + 3, Color.WHITE);
        Kaotik.FONT_MANAGER.drawString(binding ? "[...]" : "[" + Keyboard.getKeyName(setting.getValue()).toUpperCase() + "]", getX() + getWidth() - 3 - Kaotik.FONT_MANAGER.getStringWidth(binding ? "[...]" : "[" + Keyboard.getKeyName(setting.getValue()).toUpperCase() + "]"), getY() + 3, Color.LIGHT_GRAY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()) {
            binding = !binding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (binding) {
            if (keyCode == 211) {
                setting.setValue(Keyboard.KEY_NONE);
            } else {
                if (keyCode != Keyboard.KEY_ESCAPE)  {
                    setting.setValue(keyCode);
                }
            }

            binding = false;
        }
    }
}