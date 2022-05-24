package com.mawuote.client.modules.render;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ModuleBreakESP extends Module {
    public ModuleBreakESP(){
        super("BreakESP", "Break ESP", "Highlights blocks which are being mined by yourself or others.", ModuleCategory.RENDER);
        this.percentMap.put(0, 10);
        this.percentMap.put(1, 20);
        this.percentMap.put(2, 30);
        this.percentMap.put(3, 40);
        this.percentMap.put(4, 50);
        this.percentMap.put(5, 60);
        this.percentMap.put(6, 70);
        this.percentMap.put(7, 80);
        this.percentMap.put(8, 90);
        this.percentMap.put(9, 100);
    }

    private Map percentMap = new HashMap();

    public static ValueBoolean name = new ValueBoolean("Name", "Name", "", false);
    public static ValueBoolean percent = new ValueBoolean("Percent", "Percent", "", false);
    public static ValueBoolean outline = new ValueBoolean("Outline", "Outline", "", false);
    public static ValueNumber red = new ValueNumber("Red", "Red", "", 255, 0, 255);
    public static ValueNumber green = new ValueNumber("Green", "Green", "", 0, 0, 255);
    public static ValueNumber blue = new ValueNumber("Blue", "Blue", "", 0, 0, 255);
    public static ValueNumber alpha = new ValueNumber("Alpha", "Alpha", "", 180, 0, 255);

    public void onRender3D(EventRender3D event) {
        mc.renderGlobal.damagedBlocks.forEach((breakingGuy, breakBlock) -> {
            if(breakingGuy == null)
                return;
            RenderUtils.drawBoxESP(breakBlock.getPosition(), new Color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue()), 1.0f, outline.getValue(), true, alpha.getValue().intValue(), true, 0.0, false);
            if (percent.getValue()) {
                drawText(breakBlock.getPosition(), this.percentMap.get(breakBlock.getPartialBlockDamage()).toString() + "%" + (name.getValue() ? " " + mc.world.getEntityByID(breakingGuy).getName() : ""), Kaotik.getModuleManager().isModuleEnabled("Font"));
            }
        });
    }

    public static void drawText(final BlockPos pos, final String text, boolean font) {
        if(font) {
            GlStateManager.pushMatrix();
            RenderUtils.glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (EntityPlayer) mc.player, 1.0f);
            GlStateManager.disableDepth();
            GlStateManager.translate(-(Kaotik.FONT_MANAGER.getStringWidth(text) / 2.0), 0.0, 0.0);
            Kaotik.FONT_MANAGER.drawString(text, 0.0f, 0.0f, Color.WHITE);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.pushMatrix();
            RenderUtils.glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (EntityPlayer) mc.player, 1.0f);
            GlStateManager.disableDepth();
            GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
            mc.fontRenderer.drawStringWithShadow(text, 0.0f, 0.0f, -1);
            GlStateManager.popMatrix();
        }
    }

}
