package com.mawuote.client.modules.movement;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.player.EventPlayerMove;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speedy extends Module<B> {
    public Speedy(){super("Speedy", "Speedy", "Fast", ModuleCategory.MOVEMENT);}

    public enum modes {
        Strafe;
    }

    public static ValueEnum mode = new ValueEnum("Mode", "Mode", "", modes.Strafe);
    public static ValueBoolean timer = new ValueBoolean("UseTimer", "UseTimer", "", false);
    public static ValueBoolean inWater = new ValueBoolean("SpeedInWater", "SpeedInWater", "", false);

    /**
     * spaghetti code by zoom :D
     */

    private double speed;
    private float timerSpeed;

    private void manageY(EventPlayerMove event) {
        double jump = 0.40123128D;
        if ((mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) && mc.player.onGround) {
            if (mc.player.isPotionActive(MobEffects.JUMP_BOOST))
                jump += ((mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
            event.setY(mc.player.motionY = jump);
            this.speed *= 2.149D;
        }
    }

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if(timer.getValue()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                timerSpeed = 1.0f;
            }
        }
    }

    public void onMotionUpdate() {
        if (timer.getValue()) {
            if (this.mc.player.moveForward != 0.0 || this.mc.player.moveStrafing != 0.0) {
                timerSpeed = Math.min(timerSpeed + 0.006f, 1.1f);
            } else {
                timerSpeed = 1.0f;
            }
            mc.timer.tickLength = 50.0f / timerSpeed;
        }
    }

    @SubscribeEvent
    public void onMove(EventPlayerMove event) {

        float rotationYaw = mc.player.rotationYaw;

        speed = 0.2873f;
        if(Speedy.mc.player == null || Speedy.mc.world == null) {
            return;
        }
        if (Speedy.mc.player.isSneaking() || Speedy.mc.player.isOnLadder() || Speedy.mc.player.isInWeb || Speedy.mc.player.capabilities.isFlying) {
            return;
        }
        else if ((mc.player.isInWater() || mc.player.isInLava()) && !inWater.getValue()) {
            return;
        }
        if(Speedy.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amp = Speedy.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            this.speed *= 1.0 + 0.2 * (amp + 1);
        }

        double moveForward = Speedy.mc.player.movementInput.moveForward;
        double moveStrafe = Speedy.mc.player.movementInput.moveStrafe;

        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += moveForward > 0.0f ? -45.0f : 45.0f;
                moveStrafe = 0.0f;
            } else if (moveStrafe <= -1.0f) {
                rotationYaw += moveForward > 0.0f ? 45.0f : -45.0f;
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }

        manageY(event);

        double toRadiansX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        double toRadiansZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));

        event.setX(moveForward * speed * toRadiansX + moveStrafe * speed * toRadiansZ);
        event.setZ(moveForward * speed * toRadiansZ - moveStrafe * speed * toRadiansX);
        event.setCancelled(true);
    }

    @Override
    public String getHudInfo(){
        String t = "";
        t = " [" + ChatFormatting.WHITE + mode.getValue() + ChatFormatting.GRAY + "]";
        return t;
    }

    public void onEnable() {
        timerSpeed = 1.0f;
        MinecraftForge.EVENT_BUS.register(this);

    }

    public void onDisable() {
        mc.timer.tickLength = 50.f;
        MinecraftForge.EVENT_BUS.unregister(this);

    }
}

