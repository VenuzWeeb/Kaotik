package com.mawuote.client.mixins.impl.blocks;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.render.EventBlockGetRenderLayer;
import com.mawuote.client.modules.render.ModuleWallhack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock
{

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side,
            CallbackInfoReturnable<Boolean> callback)
    {
        if (Kaotik.getModuleManager().isModuleEnabled("Wallhack"))
            ModuleWallhack.processShouldSideBeRendered((Block)(Object)this, blockState, blockAccess, pos, side, callback);
    }
    
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

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLightValue(CallbackInfoReturnable<Integer> callback)
    {
        if (Kaotik.getModuleManager().isModuleEnabled("Wallhack"))
            ModuleWallhack.processGetLightValue((Block)(Object)this, callback);
    }
}
