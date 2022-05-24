package com.mawuote.client.elements;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.client.modules.client.ModuleColor;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ElementArmor extends Element {
    public ElementArmor() {
        super("Armor", "Renders the status of your armor on screen.");
    }

    public static ValueBoolean percentage = new ValueBoolean("Percentage", "Percentage", "Renders the percentage that the armor's durability is at.", true);
    public static ValueEnum percentageColor = new ValueEnum("PercentageColor", "PercentageColor", "The color for the percentage.", PercentageColors.Damage);

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        GlStateManager.enableTexture2D();

        frame.setWidth(90);
        frame.setHeight(15);

        int index = 0;
        for (ItemStack stack : mc.player.inventory.armorInventory) {
            index++;
            if (stack.isEmpty()) continue;
            GlStateManager.enableDepth();

            mc.getRenderItem().zLevel = 200.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) (frame.getX() - 90 + (9 - index) * 20 + 2), (int) (frame.getY()));
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, (int) (frame.getX() - 90 + (9 - index) * 20 + 2), (int) (frame.getY()), "");
            mc.getRenderItem().zLevel = 0.0f;

            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            String s = stack.getCount() > 1 ? stack.getCount() + "" : "";
            mc.fontRenderer.drawStringWithShadow(s, (frame.getX() - 90 + (9 - index) * 20 + 2) + 19 - 2 - mc.fontRenderer.getStringWidth(s), frame.getY() + 9, 0xffffff);

            if (percentage.getValue()) {
                float green = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
                float red = 1 - green;
                int dmg = 100 - (int) (red * 100);
                Kaotik.FONT_MANAGER.drawString(getPercentageColor() + "" + dmg + "", (frame.getX() - 90 + (9 - index) * 20 + 2) + 8 - Kaotik.FONT_MANAGER.getStringWidth(dmg + "") / 2, frame.getY() - 11, percentageColor.getValue().equals(PercentageColors.Damage) ? new Color(red, green, 0) : ModuleColor.getActualColor());
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    private ChatFormatting getPercentageColor(){
        if (percentageColor.getValue().equals(PercentageColors.White)){
            return ChatFormatting.WHITE;
        } else if (percentageColor.getValue().equals(PercentageColors.Gray)){
            return ChatFormatting.GRAY;
        } else {
            return ChatFormatting.RESET;
        }
    }

    public enum PercentageColors {
        Normal, Gray, White, Damage
    }
}
