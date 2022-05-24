package com.mawuote.client.gui.click;

import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.client.modules.client.ModuleGUI;
import com.mawuote.client.gui.click.components.Frame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiScreen extends GuiScreen {
    public static ClickGuiScreen INSTANCE = new ClickGuiScreen();
    public ArrayList<Frame> frames;

    public ClickGuiScreen() {
        frames = new ArrayList<>();
        int offset = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            if (category == ModuleCategory.HUD) continue;
            frames.add(new Frame(category, 10 + offset, 20));
            offset += 124;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution resolution = new ScaledResolution(mc);

        for (Frame panel : frames) {
            panel.drawScreen(mouseX, mouseY);
            panel.updatePosition(mouseX, mouseY);
            panel.refreshPosition();
            mouseScroll();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Frame panel : frames) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (Frame panel : frames) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) throws IOException {
        super.keyTyped(typedChar, key);

        for (Frame panel : frames) {
            panel.keyTyped(typedChar, key);
        }
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        ModuleGUI.INSTANCE.disable();
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }

    public void mouseScroll() {
        int scroll = Mouse.getDWheel();
        for (final Frame panel : frames) {
            if (scroll < 0) {
                panel.setY(panel.getY() - ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
            } else if (scroll > 0) {
                panel.setY(panel.getY() + ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
            }
        }
    }

    public static String capitalize(String input) {
        /*if (ClickGuiModule.names.equals("Lowercase")) {
            return input.toLowerCase();
        } else if (ClickGuiModule.names.equals("Uppercase")) {
            return input.toUpperCase();
        } else {*/
            return input;
        //}
    }
}
