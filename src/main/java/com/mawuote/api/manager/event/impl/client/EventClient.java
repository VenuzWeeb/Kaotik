package com.mawuote.api.manager.event.impl.client;

import com.mawuote.api.manager.event.Event;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.value.Value;

public class EventClient extends Event {

    private Module module;
    private Value setting;
    private int stage;

    public EventClient(Value setting) {
        this.setting = setting;
    }

    public EventClient(int stage) {
        this.stage = stage;
    }

    public Module getModule() {
        return module;
    }

    public Value getSetting() {
        return setting;
    }
}
