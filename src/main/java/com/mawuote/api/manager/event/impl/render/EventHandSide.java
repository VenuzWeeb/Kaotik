package com.mawuote.api.manager.event.impl.render;

import com.mawuote.api.manager.event.Event;
import net.minecraft.util.EnumHandSide;

public class EventHandSide extends Event {
    private final EnumHandSide handSide;

    public EventHandSide(EnumHandSide handSide) {
        this.handSide = handSide;
    }

    public EnumHandSide getHandSide() {
        return handSide;
    }
}