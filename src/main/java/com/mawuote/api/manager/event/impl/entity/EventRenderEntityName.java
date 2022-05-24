package com.mawuote.api.manager.event.impl.entity;

import com.mawuote.api.manager.event.Event;
import net.minecraft.client.entity.AbstractClientPlayer;

public class EventRenderEntityName extends Event
{
    public AbstractClientPlayer Entity;
    public double X;
    public double Y;
    public double Z;
    public String Name;
    public double DistanceSq;

    public EventRenderEntityName(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq)
    {
        Entity = entityIn;
        x = X;
        y = Y;
        z = Z;
        Name = name;
        DistanceSq = distanceSq;
    }

}
