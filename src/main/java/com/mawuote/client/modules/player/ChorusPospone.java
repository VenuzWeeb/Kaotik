package com.mawuote.client.modules.player;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.player.EventPlayerTravel;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
import java.util.Queue;

public class ChorusPospone extends Module {
    public ChorusPospone() {
        super("ChorusPostpone", "ChorusPostpone", "", ModuleCategory.PLAYER);
    }

    ValueNumber sDelay = new ValueNumber("Delay", "Delay", "", 18, 0, 500);

    int delay = 0;
    int delay2 = 0;
    boolean ateChorus = false;
    boolean hackPacket = false;
    boolean posTp = false;
    double posX;
    double posY;
    double posZ;

    Queue<CPacketPlayer> packets = new LinkedList<>();
    Queue<CPacketConfirmTeleport> packetss = new LinkedList<>();


    public void onEnable() {
        ateChorus = false;
        hackPacket = false;
        posTp = false;
    }

    public void onUpdate() {
        if (ateChorus) {
            delay++;
            delay2++;
            if (!mc.player.getPosition().equals(new BlockPos(posX, posY, posZ)) && !posTp) {
                if (mc.player.getDistance(posX, posY, posZ) > 1) {
                    mc.player.setPosition(posX, posY, posZ);
                    posTp = true;
                }
            }
        }
        if (ateChorus && delay2 > sDelay.getValue().intValue()) {
            ateChorus = false;
            delay = 0;
            hackPacket = true;
            delay2 = 0;
            sendPackets();
        }
    }

    public void sendPackets() {
        while (!packets.isEmpty()) {
            mc.player.connection.sendPacket(packets.poll());
        }
        while (!packetss.isEmpty()) {
            mc.player.connection.sendPacket(packetss.poll());
        }
        hackPacket = false;
        delay2 = 0;
        ateChorus = false;
    }

    @SubscribeEvent
    public void Event(EventPlayerTravel eventPlayerTravel) {

    }

    @SubscribeEvent
    public void finishEating(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() == mc.player) {
            if (event.getResultStack().getItem().equals(Items.CHORUS_FRUIT)) {
                posX = mc.player.posX;
                posY = mc.player.posY;
                posZ = mc.player.posZ;
                posTp = false;
                ateChorus = true;
            }
        }
    }

    @SubscribeEvent
    public void finishEating(LivingEntityUseItemEvent.Start event) {

    }



    @SubscribeEvent
    public void onSend(EventPacket.Send event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            if (ateChorus && delay2 < sDelay.getValue().intValue()) {
                packetss.add((CPacketConfirmTeleport) event.getPacket());
                event.setCancelled(true);
            }

        }
        if (event.getPacket() instanceof CPacketPlayer) {
            if (ateChorus && delay2 < sDelay.getValue().intValue()) {
                packets.add((CPacketPlayer) event.getPacket());
                event.setCancelled(true);
            }
        }
    }
}
