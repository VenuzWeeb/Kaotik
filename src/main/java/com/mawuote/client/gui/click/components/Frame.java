package com.mawuote.client.gui.click.components;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.client.gui.click.components.impl.ColorComponent;
import com.mawuote.client.gui.click.components.impl.ModuleComponent;
import com.mawuote.client.gui.click.ClickGuiScreen;
import com.mawuote.client.gui.click.components.impl.PreviewComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class Frame {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    ArrayList<Component> buttons;

    private String tab;
    int x, y, dragX, dragY, width, height;
    boolean open = true, dragging;

    public Frame(ModuleCategory category, int x, int y) {
        this.tab = category.getName();
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 0;
        this.dragX = 0;
        this.dragY = 0;
        this.dragging = false;

        buttons = new ArrayList<>();
        int offset = 14;
        for (Module module : Kaotik.MODULE_MANAGER.getModules(category)) {
            buttons.add(new ModuleComponent(module, x, y + offset, this));
            offset += 14;
        }

        this.height = offset;
        refreshPosition();
    }

    public Frame(int x, int y) {
        this.tab = "HUD";
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 0;
        this.dragX = 0;
        this.dragY = 0;
        this.dragging = false;

        buttons = new ArrayList<>();
        int offset = 14;
        for (Element element : Kaotik.ELEMENT_MANAGER.getElements()) {
            buttons.add(new ModuleComponent(element, x, y + offset, this));
            offset += 14;
        }

        this.height = offset;
        refreshPosition();
    }

    public void drawScreen(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + 14, new Color(47, 8, 8).getRGB());
        Gui.drawRect(x - 1, y, x, y + 14, new Color(47, 8, 8).getRGB());
        Gui.drawRect(x + width, y, x + width + 1, y + 14, new Color(47, 8, 8).getRGB());
        if (open) {
            Gui.drawRect(x - 1, y + height, x + width + 1, y + height + 1, new Color(30, 29, 29).getRGB());
        }

        Kaotik.FONT_MANAGER.drawString(ClickGuiScreen.capitalize(tab), x + /* (ClickGuiModule.categoryPosition.equals("Center") ? ((width / 2.0f) - (FontManager.getStringWidth(ClickGuiScreen.capitalize(category.getName())) / 2.0f)) : ClickGuiModule.categoryPosition.equals("Right") ? width - 3 - FontManager.getStringWidth(ClickGuiScreen.capitalize(category.getName())) : 3) */ 3, y + 3, Color.WHITE);
        if (open) {
            for (Component button : buttons) {
                button.drawScreen(mouseX, mouseY);
                button.update(mouseX, mouseY);

                if (button instanceof ModuleComponent) {
                    ModuleComponent moduleButton = (ModuleComponent) button;
                    if (moduleButton.isOpen()) {
                        for (Component settingButton : moduleButton.getSubButtons()) {
                            settingButton.drawScreen(mouseX, mouseY);
                            settingButton.update(mouseX, mouseY);
                        }
                    }
                }
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOnHeader(mouseX, mouseY) && mouseButton == 0) {
            setDragging(true);
            dragX = mouseX - getX();
            dragY = mouseY - getY();
        }

        if (mouseButton == 1 && isMouseOnHeader(mouseX, mouseY)) {
            open = !open;
            if (open) {
                int offset = 14;
                for (Component ignored : buttons) {
                    offset += 14;
                }
                height = offset;
            } else {
                height = 14;
            }
        }

        for (Component button : buttons) {
            button.mouseClicked(mouseX, mouseY, mouseButton);

            if (button instanceof ModuleComponent) {
                ModuleComponent moduleButton = (ModuleComponent) button;
                if (moduleButton.isOpen()) {
                    for (Component settingButton : moduleButton.getSubButtons()) {
                        settingButton.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, final int state) {
        setDragging(false);

        for (Component button : buttons) {
            button.mouseReleased(mouseX, mouseY, state);

            if (button instanceof ModuleComponent) {
                ModuleComponent moduleButton = (ModuleComponent) button;
                if (moduleButton.isOpen()) {
                    for (Component settingButton : moduleButton.getSubButtons()) {
                        settingButton.mouseReleased(mouseX, mouseY, state);
                    }
                }
            }
        }
    }

    public void keyTyped(char typedChar, int key) {
        for (Component button : buttons) {
            button.keyTyped(typedChar, key);

            if (button instanceof ModuleComponent) {
                ModuleComponent moduleButton = (ModuleComponent) button;
                if (moduleButton.isOpen()) {
                    for (Component settingButton : moduleButton.getSubButtons()) {
                        settingButton.keyTyped(typedChar, key);
                    }
                }
            }
        }
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (dragging) {
            setX(mouseX - dragX);
            setY(mouseY - dragY);
        }
    }

    public void refreshPosition() {
        int offset = 14;
        for (final Component button : buttons) {
            button.setX(this.x);
            button.setY(this.y + offset);
            offset += 14;

            if (button instanceof ModuleComponent) {
                ModuleComponent moduleButton = (ModuleComponent) button;
                if (moduleButton.isOpen()) {
                    for (final Component settingButton : moduleButton.getSubButtons()) {
                        settingButton.setX(this.x);
                        settingButton.setY(this.y + offset);

                        if (settingButton instanceof ColorComponent) {
                            if (((ColorComponent) settingButton).open) {
                                offset += 84;
                            } else {
                                offset += 14;
                            }
                        } else if (settingButton instanceof PreviewComponent) {
                            if (((PreviewComponent) settingButton).open) {
                                offset += 100;
                            } else {
                                offset += 14;
                            }
                        } else {
                            offset += 14;
                        }
                    }
                }
            }
        }
        this.height = offset;
    }

    public boolean isMouseOnHeader(final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 14;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
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
}
