package com.mawuote.api.manager.value.impl;

import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.value.Value;
import net.minecraftforge.common.MinecraftForge;

public class ValueNumber extends Value {
    public static final int INTEGER = 0x01;
    public static final int DOUBLE  = 0x02;
    public static final int FLOAT   = 0x03;

    private Number value;
    private Number minimum;
    private Number maximum;

    public ValueNumber(String name, String tag, String description, Number value, Number minimum, Number maximum) {
        super(name, tag, description);
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        MinecraftForge.EVENT_BUS.post(new EventClient(this));
        this.value = value;
    }

    public Number getMinimum() {
        return minimum;
    }

    public Number getMaximum() {
        return maximum;
    }

    public int getType() {
        if (this.value.getClass() == Integer.class) {
            return ValueNumber.INTEGER;
        } else if (this.value.getClass() == Double.class) {
            return ValueNumber.DOUBLE;
        } else if (this.value.getClass() == Float.class) {
            return ValueNumber.FLOAT;
        }

        return -1;
    }
}
