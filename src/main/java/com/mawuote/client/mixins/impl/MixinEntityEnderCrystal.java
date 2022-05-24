package com.mawuote.client.mixins.impl;

import com.mawuote.api.manager.event.impl.world.EventCrystalAttack;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityEnderCrystal.class)
public class MixinEntityEnderCrystal {
    @Inject(method = "attackEntityFrom", at = @At("RETURN"), cancellable = true)
    public void attackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (source.getTrueSource() != null) {
            EventCrystalAttack event = new EventCrystalAttack(source.getTrueSource().entityId);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                info.cancel();
            }
        }
    }
}
