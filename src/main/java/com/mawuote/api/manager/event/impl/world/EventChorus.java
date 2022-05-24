package com.mawuote.api.manager.event.impl.world;

import com.mawuote.api.manager.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventChorus extends Event
{
    final EntityLivingBase entityLivingBase;
    double x;
    double y;
    double z;
    boolean successful;

    public EventChorus(final EntityLivingBase entityLivingBase, final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityLivingBase = entityLivingBase;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public void setSuccessful(final boolean successful) {
        this.successful = successful;
    }

    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
    }

    public boolean isCancelable() {
        return true;
    }
}
