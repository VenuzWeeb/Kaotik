package com.mawuote.client.modules.movement;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Step extends Module<B> {
    public Step() {
        super("Stepy", "Step", "Increases your vanilla step height.", ModuleCategory.MOVEMENT);
    }

    private float oldHeight = -1.0f;
    private float timerSpeed;

    public static ValueNumber height = new ValueNumber("Height", "Height", "", 2.0, 0.0, 2.5);
    public static ValueBoolean timer = new ValueBoolean("UseTimer", "UseTimer", "", false);

    public void onUpdate() {
        if (mc.player != null) {
            if (Kaotik.getModuleManager().isModuleEnabled("Speed")) {
                mc.player.stepHeight = oldHeight;
                return;
            }

            mc.player.stepHeight = (float) height.getValue().doubleValue();
        }
    }

    public void onMotionUpdate() {
        if (mc.player != null) {
            if (timer.getValue()) {
                if (this.mc.player.moveForward != 0.0 || this.mc.player.moveStrafing != 0.0) {
                    timerSpeed = Math.min(timerSpeed + 0.006f, 1.1f);
                } else {
                    timerSpeed = 1.0f;
                }
                mc.timer.tickLength = 50.0f / timerSpeed;
            }
        }
    }

    public void onEnable() {
        timerSpeed = 1.0f;
        if (mc.player != null) {
            oldHeight = mc.player.stepHeight;
        }
    }

    public void onDisable() {
        mc.player.stepHeight = oldHeight;
        oldHeight = -1.0f;
        mc.timer.tickLength = 50.f;
    }

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if(timer.getValue()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                timerSpeed = 1.0f;
            }
        }
    }

    public void onLogout() {
        disable();
    }

    public void onDeath() {
        disable();
    }
}
