package com.mawuote.api.manager.value.impl;

import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.value.Value;
import net.minecraftforge.common.MinecraftForge;

public class ValueBind extends Value {
    private int value;

    public ValueBind(String name, String tag, String description, int value) {
        super(name, tag, description);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        MinecraftForge.EVENT_BUS.post(new EventClient(this));
        this.value = value;
    }
}
