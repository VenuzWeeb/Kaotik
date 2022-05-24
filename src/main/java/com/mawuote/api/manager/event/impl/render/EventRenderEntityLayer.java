package com.mawuote.api.manager.event.impl.render;

import com.mawuote.api.manager.event.Event;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntityLayer extends Event {

    private EntityLivingBase entity;

    private LayerRenderer<?> layer;

    public EventRenderEntityLayer(EntityLivingBase entity, LayerRenderer<?> layer) {
        this.entity = entity;
        this.layer = layer;
    }

    public final EntityLivingBase getEntity() {
        return this.entity;
    }

    public final void setEntity(EntityLivingBase entityLivingBase) {
        this.entity = entityLivingBase;
    }

    public final LayerRenderer<?> getLayer() {
        return this.layer;
    }

    public final void setLayer(LayerRenderer<?> layerRenderer) {
        this.layer = layerRenderer;
    }

    public final EntityLivingBase component1() {
        return this.entity;
    }

    public final LayerRenderer<?> component2() {
        return this.layer;
    }

    public final EventRenderEntityLayer copy( EntityLivingBase entity,  LayerRenderer<?> layer) {
        return new EventRenderEntityLayer(entity, layer);
    }

    public static EventRenderEntityLayer copy$default(EventRenderEntityLayer renderEntityLayerEvent, EntityLivingBase entityLivingBase, LayerRenderer layerRenderer, int n, Object object) {
        if ((n & 1) != 0) {
            entityLivingBase = renderEntityLayerEvent.entity;
        }
        if ((n & 2) != 0) {
            layerRenderer = renderEntityLayerEvent.layer;
        }
        return renderEntityLayerEvent.copy(entityLivingBase, layerRenderer);
    }

    public String toString() {
        return "RenderEntityLayerEvent(entity=" + (Object)this.entity + ", layer=" + this.layer + ')';
    }

    public int hashCode() {
        int result2 = this.entity.hashCode();
        result2 = result2 * 31 + this.layer.hashCode();
        return result2;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EventRenderEntityLayer)) {
            return false;
        }
        EventRenderEntityLayer renderEntityLayerEvent = (EventRenderEntityLayer)other;
        if(!(this.entity == renderEntityLayerEvent.entity)) {
            return false;
        }
        return(this.layer == renderEntityLayerEvent.layer);
    }
}
