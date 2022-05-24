package com.mawuote.api.manager.value.impl;

import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.value.Value;
import net.minecraftforge.common.MinecraftForge;

public class ValueString extends Value {
    private String value;

    public ValueString(String name, String tag, String description, String value) {
        super(name, tag, description);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        MinecraftForge.EVENT_BUS.post(new EventClient(this));
        this.value = value;
    }
}
