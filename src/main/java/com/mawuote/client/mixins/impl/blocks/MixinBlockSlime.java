package com.mawuote.client.mixins.impl.blocks;

import com.mawuote.api.manager.event.impl.render.EventBlockGetRenderLayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlime;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockSlime.class)
public class MixinBlockSlime
{
   @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
   public void getRenderLayer(CallbackInfoReturnable<BlockRenderLayer> callback)
   {
       EventBlockGetRenderLayer event = new EventBlockGetRenderLayer((Block) (Object) this);
       MinecraftForge.EVENT_BUS.post(event);

       if (event.isCancelled())                                                               
       {
           callback.cancel();
           callback.setReturnValue(event.getBlockRenderLayer());
       }
   }
}
