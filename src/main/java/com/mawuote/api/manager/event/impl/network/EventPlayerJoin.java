package com.mawuote.api.manager.event.impl.network;

import com.mawuote.api.manager.event.Event;

import java.util.UUID;

public class EventPlayerJoin extends Event {
    private final String name;
    private final UUID uuid;

    public EventPlayerJoin(String n, UUID id){
        super();
        name = n;
        uuid = id;
    }

    public String getName(){
        return name;
    }

    public UUID getUuid() {return uuid;}
}