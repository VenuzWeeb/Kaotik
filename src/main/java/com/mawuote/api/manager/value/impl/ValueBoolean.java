package com.mawuote.api.manager.value.impl;

import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.value.Value;
import net.minecraftforge.common.MinecraftForge;

public class ValueBoolean extends Value {
    private boolean value;

    public ValueBoolean(String name, String tag, String description, boolean value) {
        super(name, tag, description);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        MinecraftForge.EVENT_BUS.post(new EventClient(this));
        this.value = value;
    }
}
