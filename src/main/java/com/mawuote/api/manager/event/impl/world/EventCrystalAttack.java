package com.mawuote.api.manager.event.impl.world;

import com.mawuote.api.manager.event.Event;

public class EventCrystalAttack extends Event {
    private final int entityId;

    public EventCrystalAttack(final int entityId) {
        this.entityId = entityId;
    }

    public int getEntityID(){
        return entityId;
    }
}
