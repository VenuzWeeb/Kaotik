package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.value.Value;
import com.mawuote.api.manager.value.impl.*;
import com.mawuote.client.modules.client.ModuleColor;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class ModuleComponent extends com.mawuote.client.gui.click.components.Component {
    Module<B> module;
    private final ArrayList<com.mawuote.client.gui.click.components.Component> subButtons;
    boolean open;

    public ModuleComponent(Module<B> module, int x, int y, com.mawuote.client.gui.click.components.Frame parent) {
        super(x, y, parent);
        this.module = module;
        this.subButtons = new ArrayList<>();
        this.open = false;
        int tempY = y + 14;

        if (module.getValues() != null && !module.getValues().isEmpty()) {
            for (final Value setting : module.getValues()) {
                if (setting instanceof ValueBoolean) {
                    subButtons.add(new BooleanComponent((ValueBoolean) setting, this, tempY));
                    tempY += 14;
                }

                if (setting instanceof ValueNumber){
                    subButtons.add(new NumberComponent((ValueNumber) setting, this, tempY));
                    tempY += 14;
                }

                if (setting instanceof ValueEnum) {
                    subButtons.add(new ModeComponent((ValueEnum) setting, this, tempY));
                    tempY += 14;
                }

                if (setting instanceof ValueString) {
                    subButtons.add(new StringComponent((ValueString) setting, this, tempY));
                    tempY += 14;
                }

                if (setting instanceof ValueColor){
                    subButtons.add(new ColorComponent((ValueColor) setting, this, tempY));
                    tempY += 100;
                }

                if(setting instanceof ValuePreview) {
                    subButtons.add(new PreviewComponent((ValuePreview) setting, this, tempY));
                    tempY += 70;
                }

                if (setting instanceof ValueBind) {
                    subButtons.add(new BindComponent((ValueBind) setting, this, tempY));
                    tempY += 14;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + 14, module.isToggled() ? ModuleColor.getColor() : new Color(42, 42, 42).getRGB());
        Gui.drawRect(getX() - 1, getY(), getX(), getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + 14, new Color(30, 30, 30).getRGB());
        Kaotik.FONT_MANAGER.drawString(module.getTag(), getX() + 3, getY() + 3, Color.WHITE);
        if (open && !getSubButtons().isEmpty()) {
            for (final com.mawuote.client.gui.click.components.Component button : getSubButtons()) {
                button.drawScreen(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()) {
            if (mouseButton == 0) {
                module.toggle();
            } else if (mouseButton == 1) {
                open = !open;
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public ArrayList<Component> getSubButtons() {
        return subButtons;
    }
}