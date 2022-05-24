package com.mawuote.api.manager.event.impl.player;

import com.mawuote.api.manager.event.Event;

public class EventMotionUpdate extends Event {

    public int stage;

    public EventMotionUpdate(int stage) {
        super();
        this.stage = stage;
    }
}
