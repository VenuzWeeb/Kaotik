package com.mawuote.api.manager.event.impl.render;

import com.mawuote.api.manager.event.Event;

/**
 * @author SrRina
 * @since 10/10/2020 at 17:04
 */
public class EventRender2D extends Event {
    private float partialTicks;

    public EventRender2D(float partialTicks) {
        super();

        this.partialTicks = partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
