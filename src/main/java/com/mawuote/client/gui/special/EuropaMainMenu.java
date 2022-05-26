package com.mawuote.client.gui.special;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class EuropaMainMenu
        extends GuiScreen {
    private ResourceLocation resourceLocation = new ResourceLocation("mawuote:mainmenu.png");
    private int y;
    private int x;
    private int singleplayerWidth;
    private int multiplayerWidth;
    private int settingsWidth;
    private int exitWidth;
    private int textHeight;
    private float xOffset;
    private float yOffset;

    public void initGui() {
        this.x = this.width / 2;
        this.y = this.height / 4 + 48;
        this.buttonList.add(new TextButton(0, this.x, this.y + 22, "Solo"));
        this.buttonList.add(new TextButton(1, this.x, this.y + 44, "RollPacks"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 66, "Options"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 88, "Close Kaotik"));
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel((int)7425);
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (EuropaMainMenu.isHovered(this.x - (int)Kaotik.FONT_MANAGER.getStringWidth( "Singleplayer") / 2, this.y + 20, (int)Kaotik.FONT_MANAGER.getStringWidth( "Singleplayer"), (int)Kaotik.FONT_MANAGER.getHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
        } else if (EuropaMainMenu.isHovered(this.x - (int)Kaotik.FONT_MANAGER.getStringWidth( "Multiplayer") / 2, this.y + 44, (int)Kaotik.FONT_MANAGER.getStringWidth( "Multiplayer"), (int)Kaotik.FONT_MANAGER.getHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        } else if (EuropaMainMenu.isHovered(this.x - (int)Kaotik.FONT_MANAGER.getStringWidth( "Options") / 2, this.y + 66, (int)Kaotik.FONT_MANAGER.getStringWidth( "Options"), (int)Kaotik.FONT_MANAGER.getHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        } else if (EuropaMainMenu.isHovered(this.x - (int)Kaotik.FONT_MANAGER.getStringWidth( "Quit") / 2, this.y + 88, (int)Kaotik.FONT_MANAGER.getStringWidth( "Quit"), (int)Kaotik.FONT_MANAGER.getHeight(), mouseX, mouseY)) {
            this.mc.shutdown();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.xOffset = -1.0f * (((float)mouseX - (float)this.width / 2.0f) / ((float)this.width / 32.0f));
        this.yOffset = -1.0f * (((float)mouseY - (float)this.height / 2.0f) / ((float)this.height / 18.0f));
        this.x = this.width / 2;
        this.y = this.height / 4 + 48;
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        this.mc.getTextureManager().bindTexture(this.resourceLocation);
        EuropaMainMenu.drawCompleteImage(-16.0f + this.xOffset, -9.0f + this.yOffset, this.width + 32, this.height + 18);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)posX, (float)posY, (float)0.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3f((float)0.0f, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3f((float)width, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3f((float)width, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public BufferedImage parseBackground(BufferedImage background) {
        int height;
        int width = 1920;
        int srcWidth = background.getWidth();
        int srcHeight = background.getHeight();
        for (height = 1080; width < srcWidth || height < srcHeight; width *= 2, height *= 2) {
        }
        BufferedImage imgNew = new BufferedImage(width, height, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.dispose();
        return imgNew;
    }

    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }

    private static class TextButton
            extends GuiButton {
        public TextButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, (int)Kaotik.FONT_MANAGER.getStringWidth( buttonText), (int)Kaotik.FONT_MANAGER.getHeight(), buttonText);
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = (float)mouseX >= (float)this.x - (float)Kaotik.FONT_MANAGER.getStringWidth( this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                drawGradientRect(this.x - 40 + (this.hovered ? - 2 : 0), this.y - 5 + (this.hovered ? - 2 : 0), this.x + 40 + (this.hovered ? 2 : 0), (int) (this.y + Kaotik.FONT_MANAGER.getHeight() + 5 + (this.hovered ? 2 : 0)), new Color(227, 126, 95).getRGB(), new Color(48, 150, 196).getRGB());
                RenderUtils.drawOutlineLine(this.x - 40 + (this.hovered ? - 2 : 0), this.y - 5 + (this.hovered ? - 2 : 0), this.x + 40 + (this.hovered ? 2 : 0), this.y + Kaotik.FONT_MANAGER.getHeight() + 5 + (this.hovered ? 2 : 0), 2, new Color(0, 0, 0).getRGB());
                Kaotik.FONT_MANAGER.drawString(this.displayString, (int)this.x - (int)Kaotik.FONT_MANAGER.getStringWidth( this.displayString) / (int)2.0f, this.y, Color.WHITE);
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return this.enabled && this.visible && (float)mouseX >= (float)this.x - (float)Kaotik.FONT_MANAGER.getStringWidth( this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        }
    }

    public static boolean isCustomFont() {
        if(Kaotik.getModuleManager().isModuleEnabled("Font")) {
            return true;
        } else {
            return false;
        }
    }
}
