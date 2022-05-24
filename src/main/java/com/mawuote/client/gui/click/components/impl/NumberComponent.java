package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.utilities.math.AnimationUtils;
import com.mawuote.api.utilities.math.MathUtils;
import com.mawuote.api.utilities.math.TimerUtils;
import com.mawuote.client.modules.client.ModuleColor;
import net.minecraft.client.Minecraft;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

import static org.lwjgl.opengl.GL11.glScissor;

public class NumberComponent extends Component {
    ValueNumber setting;
    private double sliderWidth;
    private boolean dragging;
    private boolean typing;
    private String currentString = "";
    private boolean selecting = false;
    private final TimerUtils backTimer = new TimerUtils();
    private boolean undoing = false;
    private final TimerUtils timer = new TimerUtils();
    AnimationUtils animationUtils;
    boolean animated = false;


    public NumberComponent(final ValueNumber setting, final ModuleComponent parent, final int offset) {
        super(parent.getParent().getX(), parent.getParent().getY() + offset, parent.getParent());
        this.setting = setting;
        this.dragging = false;
        this.typing = false;
        this.animationUtils = new AnimationUtils(500, 0, this.getWidth() - 1);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        if (timer.hasReached(400L)){
            undoing = !undoing;
            timer.reset();
        }

        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + 14, new Color(40, 40, 40).getRGB());
        Gui.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + 13, new Color(50, 50, 50).getRGB());
        if(!typing) {
            Gui.drawRect(getX() + 1, getY() + 1, (int) (getX() + 1 + sliderWidth), getY() + 13, ModuleColor.getColor());
        }
        Gui.drawRect(getX() - 1, getY(), getX(), getY() + 14, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + 14, new Color(30, 30, 30).getRGB());
        if (!typing) {
            Kaotik.FONT_MANAGER.drawString(setting.getName(), getX() + 3, getY() + 3, Color.WHITE);
            Kaotik.FONT_MANAGER.drawString(setting.getValue() + (setting.getType() == ValueNumber.INTEGER ? ".0" : ""), getX() + getWidth() - 3 - Kaotik.FONT_MANAGER.getStringWidth(setting.getValue() + (setting.getType() == ValueNumber.INTEGER ? ".0" : "")), getY() + 3, Color.WHITE);
            animated = false;
        } else {
            if(this.animated == false) {
                animationUtils.reset();
            }
            if(!animationUtils.isDone()) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                this.scissor(getX() + 1, getY() + 1, this.getWidth(), 13);
                this.drawRect(getX() + 1, getY() + 1, (getX() + 1 + (float)sliderWidth) - animationUtils.getValue(), getY() + 13, ModuleColor.getColor());
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
            animated = true;
            Kaotik.FONT_MANAGER.drawString(currentString + (selecting ? "" : (undoing ? (Kaotik.MODULE_MANAGER.isModuleEnabled("Font") ? "|" : "\u23d0") : "")), getX() + ((getWidth() - 1)/2f) - (Kaotik.FONT_MANAGER.getStringWidth(currentString)/2f), getY() + 3, Color.WHITE);
        }
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= (getY() + 2) && mouseY <= (getY() + getHeight() - 2)) {
            dragging = true;
        } else if (mouseButton == 1 && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= (getY() + 2) && mouseY <= (getY() + getHeight() - 2)) {
            typing = !typing;
            currentString = setting.getValue().toString();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        final double difference = Math.min(98, Math.max(0, mouseX - getX()));

        if (setting.getType() == ValueNumber.INTEGER) {
            sliderWidth = 98.0f * (setting.getValue().intValue() - setting.getMinimum().intValue()) / (setting.getMaximum().intValue() - setting.getMinimum().intValue());
            if (dragging) {
                if (difference == 0.0) {
                    setting.setValue(setting.getMinimum());
                } else {
                    final int value = (int) MathUtils.roundToPlaces(difference / 98 * (setting.getMaximum().intValue() - setting.getMinimum().intValue()) + setting.getMinimum().intValue(), 0);
                    setting.setValue(value);
                }
            }
        } else if (setting.getType() == ValueNumber.DOUBLE) {
            sliderWidth = 98.0f * (setting.getValue().doubleValue() - setting.getMinimum().doubleValue()) / (setting.getMaximum().doubleValue() - setting.getMinimum().doubleValue());
            if (dragging) {
                if (difference == 0.0) {
                    setting.setValue(setting.getMinimum());
                } else {
                    final double value = MathUtils.roundToPlaces(difference / 98 * (setting.getMaximum().doubleValue() - setting.getMinimum().doubleValue()) + setting.getMinimum().doubleValue(), 2);
                    setting.setValue(value);
                }
            }
        } else if (setting.getType() == ValueNumber.FLOAT){
            sliderWidth = 98.0f * (setting.getValue().floatValue() - setting.getMinimum().floatValue()) / (setting.getMaximum().floatValue() - setting.getMinimum().floatValue());
            if (dragging) {
                if (difference == 0.0) {
                    setting.setValue(setting.getMinimum());
                } else {
                    final float value = (float) MathUtils.roundToPlaces(difference / 98 * (setting.getMaximum().floatValue() - setting.getMinimum().floatValue()) + setting.getMinimum().floatValue(), 2);
                    setting.setValue(value);
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        backTimer.reset();

        if (typing){
            if (keyCode == 1) {
                selecting = false;
                return;
            } else if (keyCode == 28){
                updateString();
                selecting = false;
                typing = false;
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
        if (currentString.length() > 0) {
            if (setting.getType() == ValueNumber.INTEGER) {
                try {
                    if (!(Integer.parseInt(currentString) > setting.getMaximum().intValue() || Integer.parseInt(currentString) < setting.getMinimum().intValue())) {
                        setting.setValue(Integer.parseInt(currentString));
                    } else {
                        setting.setValue(setting.getValue());
                    }
                } catch (NumberFormatException e) {
                    setting.setValue(setting.getValue());
                }
            } else if (setting.getType() == ValueNumber.FLOAT) {
                try {
                    if (!(Float.parseFloat(currentString) > setting.getMaximum().floatValue() || Float.parseFloat(currentString) < setting.getMinimum().floatValue())) {
                        setting.setValue(Float.parseFloat(currentString));
                    } else {
                        setting.setValue(setting.getValue());
                    }
                } catch (NumberFormatException e) {
                    setting.setValue(setting.getValue());
                }
            } else if (setting.getType() == ValueNumber.DOUBLE) {
                try {
                    if (!(Double.parseDouble(currentString) > setting.getMaximum().doubleValue() || Double.parseDouble(currentString) < setting.getMinimum().doubleValue())) {
                        setting.setValue(Double.parseDouble(currentString));
                    } else {
                        setting.setValue(setting.getValue());
                    }
                } catch (NumberFormatException e) {
                    setting.setValue(setting.getValue());
                }
            }
        }
        currentString = "";
    }

    private String removeLastCharacter(String input){
        if (input.length() > 0) {
            return input.substring(0, input.length() - 1);
        } else {
            return input;
        }
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        float j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }
}
