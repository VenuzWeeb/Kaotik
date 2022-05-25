package com.mawuote.client.modules.movement;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.player.EventKey;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown extends Module {
    public NoSlowDown(){super("NoSlowDown", "No Slow", "", ModuleCategory.MOVEMENT);}

    public static ValueBoolean guiMove = new ValueBoolean("GuiMove", "GuiMove", "", true);
    public static ValueBoolean noSlow = new ValueBoolean("NoSlow", "NoSlow", "", true);

    private boolean sneaking = false;
    private static KeyBinding[] keys = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};


    @Override
    public void onMotionUpdate() {
        if (this.guiMove.getValue()) {
            if (mc.currentScreen instanceof GuiOptions || mc.currentScreen instanceof GuiVideoSettings || mc.currentScreen instanceof GuiScreenOptionsSounds || mc.currentScreen instanceof GuiContainer || mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen == Kaotik.CLICK_GUI) {
                for (KeyBinding bind : keys) {
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)Keyboard.isKeyDown((int)bind.getKeyCode()));
                }
            } else if (mc.currentScreen == null) {
                for (KeyBinding bind : keys) {
                    if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)false);
                }
            }
        }
        Item item = mc.player.getActiveItemStack().getItem();
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (this.noSlow.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5.0f;
            event.getMovementInput().moveForward *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onKeyEvent(EventKey event) {
        if (this.guiMove.getValue() && !(mc.currentScreen instanceof GuiChat)) {
            event.info = event.pressed;
        }
    }
}
