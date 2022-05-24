package com.mawuote.client.gui.hud;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.client.modules.client.ModuleGUI;
import com.mawuote.client.modules.client.ModuleHUDEditor;
import com.mawuote.client.gui.click.components.Frame;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class HudEditorScreen extends GuiScreen {
    private final ArrayList<ElementFrame> elementFrames;
    private final Frame frame;

    public HudEditorScreen(){
        elementFrames = new ArrayList<>();
        this.frame = new Frame(20, 20);

        for (Element element : Kaotik.ELEMENT_MANAGER.getElements()){
            addElement(element);
            element.setFrame(getFrame(element));
        }
    }

    public void addElement(final Element element){
        elementFrames.add(new ElementFrame(element, 10.0f, 10.0f, 80.0f, 15.0f, this));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        frame.drawScreen(mouseX, mouseY);
        frame.updatePosition(mouseX, mouseY);
        frame.refreshPosition();
        mouseScroll();

        for (ElementFrame frame : elementFrames){
            frame.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frame.mouseClicked(mouseX, mouseY, mouseButton);

        for (ElementFrame frame : elementFrames){
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        frame.mouseReleased(mouseX, mouseY, state);

        for (ElementFrame frame : elementFrames){
            frame.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        frame.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        if (ModuleHUDEditor.INSTANCE != null){
            ModuleHUDEditor.INSTANCE.disable();
        }
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }

    public void mouseScroll(){
        int scroll = Mouse.getDWheel();
        if (scroll < 0){
            frame.setY(frame.getY() - ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
        } else if (scroll > 0){
            frame.setY(frame.getY() + ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
        }
    }

    public Frame getFrame() {
        return frame;
    }

    public ArrayList<ElementFrame> getElementFrames() {
        return elementFrames;
    }

    public ElementFrame getFrame(final Element element){
        for (ElementFrame frame : elementFrames){
            if (!frame.getElement().equals(element)) continue;
            return frame;
        }

        return null;
    }
}