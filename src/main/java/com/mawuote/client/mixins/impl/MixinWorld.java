package com.mawuote.client.mixins.impl;

import com.mawuote.api.manager.event.impl.player.EventPush;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={World.class})
public class MixinWorld {

    @Redirect(method={"handleMaterialAcceleration"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
    public boolean isPushedbyWaterHook(Entity entity) {
        EventPush event = new EventPush(entity);
        MinecraftForge.EVENT_BUS.post(event);
        return entity.isPushedByWater() && !event.isCancelled();
    }
}
