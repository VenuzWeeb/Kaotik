package com.mawuote.api.manager.event.impl.player;

import com.mawuote.api.manager.event.Event;
import net.minecraft.entity.Entity;

public class EventPush extends Event {
    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;

    public EventPush(Entity entity, double x, double y, double z, boolean airbone) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airbone = airbone;
    }

    public EventPush() {
    }

    public EventPush(Entity entity) {
        this.entity = entity;
    }
}


