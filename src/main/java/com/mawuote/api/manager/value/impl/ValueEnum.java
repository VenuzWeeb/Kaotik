package com.mawuote.api.manager.value.impl;

import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.value.Value;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class ValueEnum extends Value {
    private Enum value;

    public ValueEnum(String name, String tag, String description, Enum value) {
        super(name, tag, description);
        this.value = value;
    }

    public Enum getValue() {
        return value;
    }

    public void setValue(Enum value) {
        MinecraftForge.EVENT_BUS.post(new EventClient(this));
        this.value = value;
    }

    public Enum getEnumByName(String name) {
        Enum enumRequested = null;

        for (Enum enums : getValues()) {
            if (enums.name().equals(name)) {
                enumRequested = enums;

                break;
            }
        }

        return enumRequested;
    }

    public ArrayList<Enum> getValues() {
        ArrayList<Enum> enumList = new ArrayList<>();

        for (Enum enums : this.value.getClass().getEnumConstants()) {
            enumList.add(enums);
        }

        return enumList;
    }
}
