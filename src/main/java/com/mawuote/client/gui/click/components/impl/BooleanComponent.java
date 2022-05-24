package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.client.modules.client.ModuleColor;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glEnable;

public class BooleanComponent extends Component {
    ValueBoolean setting;

    public BooleanComponent(final ValueBoolean setting, final ModuleComponent parent, final int offset) {
        super(parent.getParent().getX(), parent.getParent().getY() + offset, parent.getParent());
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + 14, new Color(40, 40, 40).getRGB());
        Gui.drawRect(getX() + getWidth() - 12, getY() + 2, getX() + getWidth() - 2, getY() + 12, new Color(30, 30, 30).getRGB());

        if(this.setting.getValue()) {
            RenderUtils.prepareGL();
            GL11.glShadeModel(GL11.GL_SMOOTH);
            glEnable(GL_LINE_SMOOTH);
            GL11.glLineWidth(2.5f);
            GL11.glBegin(1);
            GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
            GL11.glVertex2d(getX() + getWidth() - 8, getY() + 10);
            GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
            GL11.glVertex2d(getX() + getWidth() - 8 + 4, getY() + 4);
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
            GL11.glVertex2d(getX() + getWidth() - 8, getY() + 10);
            GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
            GL11.glVertex2d(getX() + getWidth() - 10, getY() + 7);
            GL11.glEnd();
            RenderUtils.releaseGL();
        }

        Gui.drawRect(getX() - 1, getY(), getX(), getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + 14, new Color(30, 30, 30).getRGB());
        Kaotik.FONT_MANAGER.drawString(setting.getName(), getX() + 3, getY() + 3, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()) {
            setting.setValue(!setting.getValue());
        }
    }
}
