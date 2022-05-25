package com.mawuote.client.modules.movement;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.player.EventPush;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

    public Velocity() {
        super("Velocity", "Velocity", "Stops knockback", ModuleCategory.MOVEMENT);
    }

    public static ValueBoolean noPush = new ValueBoolean("NoPush", "NoPush", "", false);
    public static ValueNumber horizontal = new ValueNumber("Horizontal","Horizontal","",0,0,100);
    public static ValueNumber vertical = new ValueNumber("Vertical","Vertical","",0,0,100);

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity sPacketEntityVelocity = (SPacketEntityVelocity) event.getPacket();

            if (sPacketEntityVelocity.getEntityID() == mc.player.entityId) {
                if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                    event.setCancelled(true);
                } else {
                    sPacketEntityVelocity.motionX *= horizontal.getValue().floatValue();
                    sPacketEntityVelocity.motionY *= vertical.getValue().floatValue();
                    sPacketEntityVelocity.motionZ *= horizontal.getValue().floatValue();
                }
            }
        }

        if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion sPacketExplosion = (SPacketExplosion) event.getPacket();
            if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                event.setCancelled(true);
            } else {
                sPacketExplosion.motionX *= horizontal.getValue().floatValue();
                sPacketExplosion.motionY *= vertical.getValue().floatValue();
                sPacketExplosion.motionZ *= horizontal.getValue().floatValue();
            }
        }
    }

    @SubscribeEvent
    public void onPush(EventPush event) {
        if(noPush.getValue()) {
            event.setCancelled(true);
        }
    }
}
