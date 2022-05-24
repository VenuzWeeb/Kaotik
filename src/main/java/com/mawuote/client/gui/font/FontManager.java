package com.mawuote.client.gui.font;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.IMinecraft;

import java.awt.*;
import java.io.InputStream;

public class FontManager implements IMinecraft {
    public FontRenderer FONT_RENDERER;

    public void load(){
        FONT_RENDERER = new FontRenderer(getFont("Lato-Medium.ttf", 40.0f));
    }

    public float drawString(final String text, final float x, final float y, final Color color){
        if (Kaotik.MODULE_MANAGER.isModuleEnabled("Font")) {
            return FONT_RENDERER.drawStringWithShadow(text, x, y, color.getRGB());
        } else {
            return mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
        }
    }

    public float getStringWidth(final String text){
        if (Kaotik.MODULE_MANAGER.isModuleEnabled("Font")) {
            return FONT_RENDERER.getStringWidth(text);
        } else {
            return mc.fontRenderer.getStringWidth(text);
        }
    }

    public float getHeight() {
        if (Kaotik.MODULE_MANAGER.isModuleEnabled("Font")) {
            return FONT_RENDERER.getHeight();
        } else {
            return mc.fontRenderer.FONT_HEIGHT;
        }
    }

    private static Font getFont(final String fontName, final float size) {
        try {
            final InputStream inputStream = FontManager.class.getResourceAsStream("/assets/mawuote/fonts/" + fontName);
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            inputStream.close();
            return awtClientFont;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Font("default", 0, (int)size);
        }
    }
}
