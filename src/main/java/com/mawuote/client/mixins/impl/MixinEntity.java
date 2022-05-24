package com.mawuote.client.mixins.impl;

import com.mawuote.api.manager.event.impl.player.EventPush;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class MixinEntity
{
    public MixinEntity() {
        super();
    }

    @Shadow
    public double motionX;

    @Shadow
    public double motionY;

    @Shadow
    public double motionZ;

    @Shadow
    public void move(final MoverType type, final double x, final double y, final double z) {
    }

    /*@Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void applyCollisionEvent(final Entity entity, final double x, final double y, final double z) {
        final EventEntity.EventColision event = new EventEntity.EventColision(entity, x, y, z);
        Kaotik.getPomeloEventManager().dispatchEvent(event);
        if (event.isCancelled()) {
            return;
        }
        entity.motionX += x;
        entity.motionY += y;
        entity.motionZ += z;
        entity.isAirBorne = true;
    }*/

    @Redirect(method={"applyEntityCollision"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(Entity entity, double x, double y, double z) {
        EventPush event = new EventPush(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCancelled()) {
            entity.motionX += event.x;
            entity.motionY += event.y;
            entity.motionZ += event.z;
            entity.isAirBorne = event.airbone;
        }
    }
}
