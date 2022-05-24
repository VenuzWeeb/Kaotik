package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.client.modules.client.ModuleColor;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glEnable;

public class ColorComponent extends Component {
    ValueColor setting;
    public boolean open = false;
    ResourceLocation alphaBG = new ResourceLocation("mawuote:alpha_texture.png");

    //hue
    boolean hueDragging;
    float hueWidth;

    //saturation
    boolean saturationDragging;
    float satWidth;

    //brightness
    boolean brightnessDragging;
    float briWidth;

    //alpha
    boolean alphaDragging;
    float alphaWidth;

    public ColorComponent(final ValueColor setting, final ModuleComponent parent, final int offset) {
        super(parent.getParent().getX(), parent.getParent().getY() + offset, parent.getParent());
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        float[] hsb = Color.RGBtoHSB(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), null);
        Color color = Color.getHSBColor(hsb[0], 1, 1);

        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + 14, new Color(10, 10, 10).getRGB());
        Gui.drawRect(getX() + getWidth() - 12, getY() + 2, getX() + getWidth() - 2, getY() + 12, setting.getValue().getRGB());
        RenderUtils.drawOutline(getX() + getWidth() - 12, getY() + 2, getX() + getWidth() - 2, getY() + 12, 0.5f, new Color(99, 57, 8).getRGB());

        if (open) {
            Gui.drawRect(getX(), getY() + 14, getX() + getWidth(), getY() + 28, new Color(10, 10, 10).getRGB());
            for (float i = 0; i + 1 < 96.0f; i += 0.45f) {
                RenderUtils.drawRecta((getX() + 2 + i), getY() + 16, 1, 11, Color.getHSBColor(i / 96.0f, 1f, 1f).getRGB());
            }
            RenderUtils.drawOutline(getX() + 2, getY() + 16, getX() + 2 + getWidth() - 4, getY() + 27, 0.5f, new Color(99, 57, 8).getRGB());
            RenderUtils.drawRecta(getX() + 2 + hueWidth, getY() + 16, 1, 11, new Color(99, 57, 8).getRGB());

            Gui.drawRect(getX(), getY() + 28, getX() + getWidth(), getY() + 42, new Color(10, 10, 10).getRGB());
            RenderUtils.drawSidewaysGradient(getX() + 2, getY() + 29, getWidth() - 4, 11, new Color(99, 57, 8), color, 255, 255);
            RenderUtils.drawOutline(getX() + 2, getY() + 29, getX() + 2 + getWidth() - 4, getY() + 40, 0.5f, new Color(99, 57, 8).getRGB());
            RenderUtils.drawRecta(getX() + 2 + satWidth, getY() + 29, 1, 11, new Color(99, 57, 8).getRGB());

            Gui.drawRect(getX(), getY() + 42, getX() + getWidth(), getY() + 56, new Color(10, 10, 10).getRGB());
            RenderUtils.drawSidewaysGradient(getX() + 2, getY() + 42, getWidth() - 4, 11, new Color(99, 57, 8), color, 255, 255);
            RenderUtils.drawOutline(getX() + 2, getY() + 42, getX() + 2 + getWidth() - 4, getY() + 53, 0.5f, new Color(99, 57, 8).getRGB());
            RenderUtils.drawRecta(getX() + 2 + briWidth, getY() + 42, 1, 11, new Color(99, 57, 8).getRGB());

            Gui.drawRect(getX(), getY() + 56, getX() + getWidth(), getY() + 70, new Color(10, 10, 10).getRGB());
            this.renderAlphaBG(getX() + 2, getY() + 55, alphaBG);
            RenderUtils.drawSidewaysGradient(getX() + 2, getY() + 55, getWidth() - 4, 11, new Color(0, 0, 0), color, 0, 255);
            RenderUtils.drawOutline(getX() + 2, getY() + 55, getX() + 2 + getWidth() - 4, getY() + 66, 0.5f, new Color(10, 10, 10).getRGB());
            RenderUtils.drawRecta(getX() + 2 + alphaWidth, getY() + 55, 1, 11, new Color(99, 57, 8).getRGB());

            Gui.drawRect(getX(), getY() + 70, getX() + getWidth(), getY() + 84, new Color(10, 10, 10).getRGB());
            Kaotik.FONT_MANAGER.drawString("Rainbow", getX() + 3, getY() + 78 - (Kaotik.FONT_MANAGER.getHeight()/2f), Color.WHITE);
            Gui.drawRect(getX() + getWidth() - 12, getY() + 72, getX() + getWidth() - 2, getY() + 82, new Color(10, 10, 10).getRGB());
            if(this.setting.getRainbow()) {
                RenderUtils.prepareGL();
                GL11.glShadeModel(GL11.GL_SMOOTH);
                glEnable(GL_LINE_SMOOTH);
                GL11.glLineWidth(2.5f);
                GL11.glBegin(1);
                GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
                GL11.glVertex2d(getX() + getWidth() - 8, getY() + 80);
                GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
                GL11.glVertex2d(getX() + getWidth() - 8 + 4, getY() + 74);
                GL11.glEnd();
                GL11.glBegin(1);
                GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
                GL11.glVertex2d(getX() + getWidth() - 8, getY() + 80);
                GL11.glColor3f(ModuleColor.getActualColor().getRed()/255f, ModuleColor.getActualColor().getGreen()/255f, ModuleColor.getActualColor().getBlue()/255f);
                GL11.glVertex2d(getX() + getWidth() - 10, getY() + 77);
                GL11.glEnd();
                RenderUtils.releaseGL();
            }

        }

        Gui.drawRect(getX() - 1, getY(), getX(), getY() + 84, new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + 84, new Color(47, 8, 8).getRGB());
        Kaotik.FONT_MANAGER.drawString(setting.getName(), getX() + 3, getY() + 3, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton){
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight() && mouseButton == 1) {
            open = !open;
        }

        if (this.isMouseOnHue(mouseX, mouseY) && mouseButton == 0 && this.open) {
            this.hueDragging = true;
        } else if (this.isMouseOnSat(mouseX, mouseY) && mouseButton == 0 && this.open) {
            this.saturationDragging = true;
        } else if (this.isMouseOnBri(mouseX, mouseY) && mouseButton == 0 && this.open) {
            this.brightnessDragging = true;
        } else if (this.isMouseOnAlpha(mouseX, mouseY) && mouseButton == 0 && this.open) {
            this.alphaDragging = true;
        } else if (this.isMouseOnRainbow(mouseX, mouseY) && mouseButton == 0 && this.open) {
            this.setting.setRainbow(!this.setting.getRainbow());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.hueDragging = false;
        this.saturationDragging = false;
        this.brightnessDragging = false;
        this.alphaDragging = false;
    }

    public void renderAlphaBG(int x, int y, ResourceLocation texture){
        mc.getTextureManager().bindTexture(texture);
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        Gui.drawScaledCustomSizeModalRect(x,y,0,0,104,16,getWidth() - 4,11,104,16);
        GL11.glPopMatrix();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
    }

    public boolean isMouseOnHue(final int x, final int y) {
        return x > getX() + 2 && x < getX() + 2 + getWidth() - 4 && y > getY() + 16 && y < getY() + 27;
    }

    public boolean isMouseOnSat(final int x, final int y) {
        return x > getX() + 2 && x < getX() + 2 + getWidth() - 4 && y > getY() + 29 && y < getY() + 40;
    }

    public boolean isMouseOnBri(final int x, final int y) {
        return x > getX() + 2 && x < getX() + 2 + getWidth() - 4 && y > getY() + 42 && y < getY() + 53;
    }

    public boolean isMouseOnAlpha(final int x, final int y) {
        return x > getX() + 2 && x < getX() + 2 + getWidth() - 4 && y > getY() + 55 && y < getY() + 66;
    }

    public boolean isMouseOnRainbow(final int x, final int y) {
        return x > getX() + getWidth() - 12 && x < getX() + getWidth() - 2 && y > getY() + 72 && y < getY() + 82;
    }

    @Override
    public void update(final int mouseX, final int mouseY) {
        super.update(mouseX, mouseY);
        float[] hsb = Color.RGBtoHSB(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), null);

        final double difference = Math.min(95, Math.max(0, mouseX - getX()));
        hueWidth = 95.5f * ((hsb[0] * 360) / 360);
        satWidth = 94.5f * ((hsb[1] * 360) / 360);
        briWidth = 94.5f * ((hsb[2] * 360) / 360);
        alphaWidth = 94.5f * ((setting.getValue().getAlpha()) / 255.0f);

        changeColor(difference, new Color(Color.HSBtoRGB((float) ((difference / 95 * 360) / 360), hsb[1], hsb[2])), new Color(Color.HSBtoRGB(0.0f, hsb[1], hsb[2])), hueDragging);
        changeColor(difference, new Color(Color.HSBtoRGB(hsb[0], (float) ((difference / 95 * 360) / 360), hsb[2])), new Color(Color.HSBtoRGB(hsb[0], 0.0f, hsb[2])), saturationDragging);
        changeColor(difference, new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float) ((difference / 95 * 360) / 360))), new Color(Color.HSBtoRGB(hsb[0], hsb[1], 0.0f)), brightnessDragging);
        changeAlpha(difference, (float) ((difference / 95 * 255) / 255), alphaDragging);
    }

    private void changeColor(double difference, Color color, Color zeroColor, boolean dragging) {
        if (dragging) {
            if (difference == 0.0) {
                setting.setValue(new Color(zeroColor.getRed(), zeroColor.getGreen(), zeroColor.getBlue(), setting.getValue().getAlpha()));
            } else {
                setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), setting.getValue().getAlpha()));
            }
        }
    }

    private void changeAlpha(double difference, float alpha, boolean dragging) {
        if (dragging) {
            if (difference == 0.0) {
                setting.setValue(new Color(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), 0));
            } else {
                setting.setValue(new Color(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), (int) (alpha * 255)));
            }
        }
    }
}