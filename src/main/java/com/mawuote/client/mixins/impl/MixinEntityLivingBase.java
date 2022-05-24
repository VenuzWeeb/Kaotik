package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.client.modules.render.ModuleAnimations;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=EntityLivingBase.class, priority = Integer.MIN_VALUE)
public class MixinEntityLivingBase {

    @Inject(method={"getArmSwingAnimationEnd"}, at={@At(value="HEAD")}, cancellable = true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        if (Kaotik.MODULE_MANAGER.isModuleEnabled("Animations") && ModuleAnimations.changeSwing.getValue()){
            info.setReturnValue(ModuleAnimations.swingDelay.getValue().intValue());
        }
    }
}
