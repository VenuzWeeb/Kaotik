package com.mawuote.api.manager.event.impl.network;

import com.mawuote.api.manager.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public class EventDeath extends Event {
    public EntityPlayer player;

    public EventDeath(EntityPlayer player) {
        this.player = player;
    }
}
