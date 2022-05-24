package com.mawuote.api.manager.value.impl;

import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.value.Value;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;

public class ValueColor extends Value {
    private Color value;
    private Boolean rainbow;

    public ValueColor(String name, String tag, String description, Color value) {
        super(name, tag, description);
        this.value = value;
        this.rainbow = false;
    }

    public Color getValue() {
        this.doRainbow();
        return value;
    }

    public void setValue(Color value) {
        MinecraftForge.EVENT_BUS.post(new EventClient(this));
        this.value = value;
    }

    private void doRainbow() {
        if (rainbow) {
            float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);
            double rainbowState = Math.ceil((System.currentTimeMillis()) / 20.0);
            rainbowState %= 360.0;
            Color c = this.HSBAlpha(Color.getHSBColor((float)(rainbowState/360.0), hsb[1], hsb[2]), value.getAlpha());
            setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
        }
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public Boolean getRainbow() {
        return rainbow;
    }

    public Color HSBAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
