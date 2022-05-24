package com.mawuote.client.modules.render;

import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModuleCrosshair extends Module<B> {
    public ModuleCrosshair(){super("Crosshair", "Crosshair", "Renders a better looking crosshair over the vanilla Minecraft one.", ModuleCategory.RENDER);}

    public static ValueBoolean dynamic = new ValueBoolean("Dynamic", "Dynamic", "", false);
    public static ValueBoolean attackIndicator = new ValueBoolean("AttackIndicator", "AttackIndicator", "", false);
    public static ValueBoolean outline = new ValueBoolean("Outline", "Outline", "", false);
    public static ValueNumber lineWidth = new ValueNumber("OutlineWidth", "OutlineWidth", "", 1.0, 0.0, 5.0);
    public static ValueNumber length = new ValueNumber("Length", "Length", "", 10.0, 0.0, 20.0);
    public static ValueNumber thick = new ValueNumber("Thick", "Thick", "", 10.0, 0.0, 20.0);
    public static ValueNumber gap = new ValueNumber("Gap", "Gap", "", 10.0, 0.0, 20.0);
    public static ValueNumber opacity = new ValueNumber("Alpha", "Alpha", "", 255, 0, 255);
    public static ValueBoolean syncColor = new ValueBoolean("SyncColor", "SyncColor", "", true);
    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(0, 255, 0, 255));

    Color color;

    public void onEnable() {
        GuiIngameForge.renderCrosshairs = false;
    }

    public void onDisable() {
        GuiIngameForge.renderCrosshairs = true;
    }

    public void onRender2D(EventRender2D event) {
        if(syncColor.getValue()) {
            color = this.globalColor(255);
        } else {
            color = daColor.getValue();
        }

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = opacity.getValue().intValue();

        int color = new Color(red, green, blue, alpha).getRGB();
        int black = new Color(0, 0, 0, 255).getRGB();

        ScaledResolution resolution = new ScaledResolution(mc);

        RenderUtils.drawRecta((resolution.getScaledWidth()/2) - gap.getValue().floatValue() - length.getValue().floatValue() - (moving() ? 2 : 0), (resolution.getScaledHeight()/2) - (thick.getValue().floatValue()/2), length.getValue().floatValue(), thick.getValue().floatValue(), color);
        RenderUtils.drawRecta((resolution.getScaledWidth()/2) + gap.getValue().floatValue() + (moving() ? 2 : 0), (resolution.getScaledHeight()/2) - (thick.getValue().floatValue()/2), length.getValue().floatValue(), thick.getValue().floatValue(), color);
        RenderUtils.drawRecta((resolution.getScaledWidth()/2) - (thick.getValue().floatValue()/2), (resolution.getScaledHeight()/2) - gap.getValue().floatValue() - length.getValue().floatValue() - (moving() ? 2 : 0), thick.getValue().floatValue(), length.getValue().floatValue(), color);
        RenderUtils.drawRecta((resolution.getScaledWidth()/2) - (thick.getValue().floatValue()/2), (resolution.getScaledHeight()/2) + gap.getValue().floatValue() + (moving() ? 2 : 0), thick.getValue().floatValue(), length.getValue().floatValue(), color);

        if(outline.getValue()) {
            RenderUtils.drawOutlineLine((resolution.getScaledWidth()/2) - gap.getValue().floatValue() - length.getValue().floatValue() - (moving() ? 2 : 0), (resolution.getScaledHeight()/2) - (thick.getValue().floatValue()/2), (resolution.getScaledWidth()/2) - gap.getValue().floatValue() - (moving() ? 2 : 0), (resolution.getScaledHeight()/2) + (thick.getValue().floatValue()/2), lineWidth.getValue().floatValue(), black);
            RenderUtils.drawOutlineLine((resolution.getScaledWidth()/2) + gap.getValue().floatValue() + (moving() ? 2 : 0), (resolution.getScaledHeight()/2) - (thick.getValue().floatValue()/2), (resolution.getScaledWidth()/2) + length.getValue().floatValue() + gap.getValue().floatValue() + (moving() ? 2 : 0), (resolution.getScaledHeight()/2) + (thick.getValue().floatValue()/2), lineWidth.getValue().floatValue(), black);
            RenderUtils.drawOutlineLine((resolution.getScaledWidth()/2) - (thick.getValue().floatValue()/2), (resolution.getScaledHeight()/2) - gap.getValue().floatValue() - length.getValue().floatValue() - (moving() ? 2 : 0), (resolution.getScaledWidth()/2) + (thick.getValue().floatValue()/2), (resolution.getScaledHeight()/2) - gap.getValue().floatValue() - (moving() ? 2 : 0), lineWidth.getValue().floatValue(), black);
            RenderUtils.drawOutlineLine((resolution.getScaledWidth()/2) - (thick.getValue().floatValue()/2), (resolution.getScaledHeight()/2) + gap.getValue().floatValue() + (moving() ? 2 : 0), (resolution.getScaledWidth()/2) + (thick.getValue().floatValue()/2), (resolution.getScaledHeight()/2) + length.getValue().floatValue() + gap.getValue().floatValue() + (moving() ? 2 : 0), lineWidth.getValue().floatValue(), black);

        }

        if(attackIndicator.getValue()) {
            float f = mc.player.getCooledAttackStrength(0.0F);
            if(f < 1.0F) {
                int k = (int)(f * 20.0F);
                RenderUtils.drawRecta((resolution.getScaledWidth() / 2) - (10), (resolution.getScaledHeight() / 2) + gap.getValue().floatValue() + length.getValue().floatValue() + (moving() ? 2 : 0) + 2, k, 2, color);
                RenderUtils.drawOutlineLine((resolution.getScaledWidth() / 2) - (10), (resolution.getScaledHeight() / 2) + gap.getValue().floatValue() + length.getValue().floatValue() + (moving() ? 2 : 0) + 2, (resolution.getScaledWidth() / 2) - (10) + k, (resolution.getScaledHeight() / 2) + gap.getValue().floatValue() + length.getValue().floatValue() + (moving() ? 2 : 0) + 4, 1.0f, black);
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public boolean moving() {
        if((mc.player.isSneaking() || mc.player.moveStrafing != 0 || mc.player.moveForward != 0 || !mc.player.onGround) && dynamic.getValue()) {
            return true;
        }
        return false;
    }
}