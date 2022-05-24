package com.mawuote.client.gui.click.components;

import net.minecraft.client.Minecraft;

public class Component {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    int x, y, width, height;
    Frame parent;

    public Component(int x, int y, Frame parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.width = parent.width;
        this.height = 14;
    }

    public void drawScreen(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void update(int mouseX, int mouseY) {

    }

    public Frame getParent() {
        return parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}