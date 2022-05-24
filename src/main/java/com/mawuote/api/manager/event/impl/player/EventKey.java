package com.mawuote.api.manager.event.impl.player;

import com.mawuote.api.manager.event.Event;

public class EventKey extends Event {
    public boolean info;
    public boolean pressed;

    public EventKey(boolean info, boolean pressed) {
        this.info = info;
        this.pressed = pressed;
    }
}


