package com.mawuote.api.manager.event.impl.network;

import com.mawuote.api.manager.event.Event;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class EventPlayerLeave extends Event {

    private final String name;
    private final UUID uuid;
    private final EntityPlayer entity;

    public EventPlayerLeave(String n, UUID id, EntityPlayer ent){
        super();
        name = n;
        uuid = id;
        entity = ent;
    }

    public String getName(){
        return name;
    }

    public EntityPlayer getEntity() {
        /* 37 */     return entity;
        /*    */   }

    public UUID getUuid() {return uuid;}
}
