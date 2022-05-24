package com.mawuote.api.manager.event.impl.render;

import com.mawuote.api.manager.event.Event;

public class EventRenderPutColorMultiplier extends Event
{
    private float _opacity;

    public void setOpacity(float opacity)
    {
        _opacity = opacity;
    }

    public float getOpacity()
    {
        return _opacity;
    }
}
