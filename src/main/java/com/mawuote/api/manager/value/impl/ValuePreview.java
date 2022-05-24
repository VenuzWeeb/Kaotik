package com.mawuote.api.manager.value.impl;

import com.mawuote.api.manager.value.Value;
import net.minecraft.entity.Entity;

public class ValuePreview extends Value {
    private Entity entity;

    public ValuePreview(String name, String tag, String description, Entity entity) {
        super(name, tag, description);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
