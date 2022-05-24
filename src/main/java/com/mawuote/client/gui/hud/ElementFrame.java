package com.mawuote.client.gui.hud;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.utilities.IMinecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class ElementFrame implements IMinecraft {
    private final Element element;

    private float x;
    private float y;
    private float width;
    private float height;

    private float dragX;
    private float dragY;

    private boolean dragging;
    private boolean visible;

    private HudEditorScreen parent;

    public ElementFrame(final Element element, final float x, final float y, final float width, final float height, final HudEditorScreen parent){
        this.element = element;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;

        dragging = false;
        visible = true;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        if (element != null && Kaotik.ELEMENT_MANAGER.isElementEnabled(element.getName())){
            if (dragging){
                x = dragX + mouseX;
                y = dragY + mouseY;

                final ScaledResolution resolution = new ScaledResolution(mc);

                if (x < 0.0) x = 0.0f;
                if (y < 0.0) y = 0.0f;

                if (x > resolution.getScaledWidth() - width) {
                    x = resolution.getScaledWidth() - width;
                }

                if (y > resolution.getScaledHeight() - height){
                    y = resolution.getScaledHeight() - height;
                }
            }

            if (dragging) {
                Gui.drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 100).getRGB());
            } else {
                Gui.drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 100).getRGB());
            }

            element.onRender2D(new EventRender2D(partialTicks));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton){
        if (mouseButton == 0 && isHovering(mouseX, mouseY)){
            dragX = x - mouseX;
            dragY = y - mouseY;
            dragging = true;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state){
        dragging = false;
    }

    public boolean isHovering(int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public Element getElement() {
        return element;
    }

    public HudEditorScreen getParent() {
        return parent;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
