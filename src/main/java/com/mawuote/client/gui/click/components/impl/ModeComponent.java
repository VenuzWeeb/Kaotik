package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.client.modules.client.ModuleColor;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModeComponent extends Component {
    ValueEnum setting;
    private int enumSize;

    public ModeComponent(final ValueEnum setting, final ModuleComponent parent, final int offset) {
        super(parent.getParent().getX(), parent.getParent().getY() + offset, parent.getParent());
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + 14, new Color(40, 40, 40).getRGB());
        Gui.drawRect(getX() - 1, getY(), getX(), getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + 14, new Color(70, 10, 10).getRGB());
        Kaotik.FONT_MANAGER.drawString(setting.getName(), getX() + 3, getY() + 3, Color.WHITE);
        Kaotik.FONT_MANAGER.drawString(setting.getValue().toString(), getX() + getWidth() - 3 - Kaotik.FONT_MANAGER.getStringWidth(setting.getValue().toString()), getY() + 3, ModuleColor.getActualColor());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()) {
            if (mouseButton == 0) {
                final int maxIndex = this.setting.getValues().size() - 1;
                ++this.enumSize;
                if (this.enumSize > maxIndex) {
                    this.enumSize = 0;
                }
                this.setting.setValue(this.setting.getValues().get(this.enumSize));
            } else if (mouseButton == 1) {
                final int maxIndex = this.setting.getValues().size() - 1;
                --this.enumSize;
                if (this.enumSize < 0) {
                    this.enumSize = maxIndex;
                }
                this.setting.setValue(this.setting.getValues().get(this.enumSize));
            }
        }
    }
}