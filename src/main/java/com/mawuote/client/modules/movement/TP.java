package com.mawuote.client.modules.movement;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;

import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.utilities.math.TimerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import java.util.LinkedList;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;

public class TP extends Module {
    public TP(){super("teleportationhack", "TP", "", ModuleCategory.MOVEMENT);}

    ValueNumber toggleDelay= new ValueNumber("ToggleDelay", "ToggleDelay", "", 500, 0, 2000);

    Queue<CPacketPlayer> packets = new LinkedList<CPacketPlayer>();;
    private EntityOtherPlayerMP clonedPlayer;
    private TimerUtils timer = new TimerUtils();

    @SubscribeEvent
    public void onPacket(EventPacket.Send event) {
        if (this.isToggled() && event.getPacket() instanceof CPacketPlayer) {
            event.setCancelled(true);
            this.packets.add((CPacketPlayer)event.getPacket());
        }
    }

    public void onUpdate() {
        if(timer.hasReached(toggleDelay.getValue().intValue())) {
            this.toggle();
        }
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            timer.reset();
            (this.clonedPlayer = new EntityOtherPlayerMP((World)mc.world, mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)mc.player);
            this.clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
            mc.world.addEntityToWorld(-100, (Entity)this.clonedPlayer);
        }
    }

    @Override
    public void onDisable() {
        while (!this.packets.isEmpty()) {
            mc.player.connection.sendPacket((Packet)this.packets.poll());
        }
        final EntityPlayer localPlayer = (EntityPlayer)mc.player;
        if (localPlayer != null) {
            mc.world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
        }
    }
}
