package com.mawuote.client.modules.movement;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.player.EventPlayerMove;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.utilities.entity.EntityUtils;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Strafe extends Module {
    public Strafe(){super("Strafe", "Strafe", "", ModuleCategory.MOVEMENT);}

    private int stage = 1;
    private double moveSpeed;
    private double lastDist;
    private int cooldownHops = 0;
    private float timerSpeed;
    private int acceleration;
    private int baseSpeed;

    ValueBoolean timer = new ValueBoolean("UseTimer", "UseTimer", "", true);
    ValueBoolean inWater = new ValueBoolean("SpeedInWater", "SpeedInWater", "", true);
    ValueBoolean step = new ValueBoolean("CancelStep", "CancelStep", "", true);

    public void onEnable() {
        timerSpeed = 1.0f;
        this.moveSpeed = getBaseMoveSpeed();
    }

    public void onDisable() {
        mc.timer.tickLength = 50.f;
        this.moveSpeed = 0.0;
        this.stage = 2;
    }

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if(timer.getValue()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                timerSpeed = 1.0f;
            }
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.baseSpeed = 100;
            this.acceleration = 2149;
        }
    }

    public void onMotionUpdate() {
        this.lastDist = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
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
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.rotationYaw;
        if (Strafe.mc.player.isSneaking() || Strafe.mc.player.isOnLadder() || Strafe.mc.player.isInWeb || Strafe.mc.player.capabilities.isFlying) {
            return;
        }
        else if ((mc.player.isInWater() || mc.player.isInLava()) && !inWater.getValue()) {
            return;
        }
        if (this.stage == 1 && EntityUtils.isMoving()) {
            this.stage = 2;
            this.moveSpeed = this.getMultiplier() * getBaseMoveSpeed() - 0.01;
        } else if (this.stage == 2) {
            this.stage = 3;
            if (EntityUtils.isMoving()) {
                event.setY(mc.player.motionY = 0.4);
                if (this.cooldownHops > 0) {
                    --this.cooldownHops;
                }
                if (mc.player.isPotionActive(MobEffects.SPEED)) {
                    acceleration = 2260;
                } else {
                    acceleration = 2149;
                }
                this.moveSpeed *= acceleration / 1000.0;
            }
        } else if (this.stage == 3) {
            this.stage = 4;
            final double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        } else {
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically) {
                this.stage = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
            this.moveSpeed = 0.0;
        } else if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                moveStrafe = 0.0f;
            } else if (moveStrafe <= -1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
        if (this.cooldownHops == 0) {
            event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
            event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
        }
        if (this.step.getValue()) {
            mc.player.stepHeight = 0.6f;
        }
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        event.setCancelled(true);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if(mc.player != null) {
            if (mc.player.isPotionActive(MobEffects.SPEED)) {
                final int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
                baseSpeed *= 1.0 + 0.2 * amplifier;
            }
        }
        return baseSpeed;
    }

    private float getMultiplier() {
        this.baseSpeed = 100;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            this.baseSpeed = 122;
        }
        return this.baseSpeed / 100.0f;
    }
}
