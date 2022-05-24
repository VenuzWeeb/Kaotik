package com.mawuote.client.modules.render;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.entity.player.EntityPlayer;

public class ModuleAnimations extends Module<B> {
    public ModuleAnimations() {
        super("Animations", "Animations", "Let's you change swing speed and other related things.", ModuleCategory.RENDER);
    }

    public static ValueBoolean playersDisableAnimations = new ValueBoolean("DisableAnimations", "DisableAnimations", "Disables player animations.", false);

    public static ValueBoolean changeMainhand = new ValueBoolean("ChangeMainhand", "ChangeMainhand", "Changes the hand progress of Mainhand.", true);
    public static ValueNumber mainhand = new ValueNumber("Mainhand", "Mainhand", "The hand progress for Mainhand.", 1.0f, 0.0f, 1.0f);

    public static ValueBoolean changeOffhand = new ValueBoolean("ChangeOffhand", "ChangeOffhand", "Changes the hand progress of Offhand.", true);
    public static ValueNumber offhand = new ValueNumber("Offhand", "Offhand", "The hand progress for Offhand.", 1.0f, 0.0f, 1.0f);

    public static ValueBoolean changeSwing = new ValueBoolean("ChangeSwing", "ChangeSwing", "Changes the swing speed.", false);
    public static ValueNumber swingDelay = new ValueNumber("SwingDelay", "SwingDelay", "The delay for swinging.", 6, 1, 20);

    public void onUpdate(){
        if (playersDisableAnimations.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                player.limbSwing = 0;
                player.limbSwingAmount = 0;
                player.prevLimbSwingAmount = 0;
            }
        }

        if (changeMainhand.getValue() && mc.entityRenderer.itemRenderer.equippedProgressMainHand != mainhand.getValue().floatValue()) {
            mc.entityRenderer.itemRenderer.equippedProgressMainHand = mainhand.getValue().floatValue();
            mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
        }

        if (changeOffhand.getValue() && mc.entityRenderer.itemRenderer.equippedProgressOffHand != offhand.getValue().floatValue()) {
            mc.entityRenderer.itemRenderer.equippedProgressOffHand = offhand.getValue().floatValue();
            mc.entityRenderer.itemRenderer.itemStackOffHand = mc.player.getHeldItemOffhand();
        }
    }
}