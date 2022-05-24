package com.mawuote.client.mixins.impl;

import com.mawuote.api.manager.event.impl.player.EventPlayerDestroyBlock;
import com.mawuote.api.manager.event.impl.world.EventBlock;
import com.mawuote.api.manager.event.impl.world.EventClickBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    /**
     **Reach made by zoom bc ezpz :D
     **/

    @Shadow
    public GameType currentGameType;

    public Minecraft mc;

    @Inject(method={"clickBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
        EventClickBlock event = new EventClickBlock(pos, face);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method={"onPlayerDamageBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
        EventBlock event = new EventBlock(pos, face);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCancelled()){
            info.cancel();
        }
    }

    @Inject(method = "onPlayerDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V"), cancellable = true)
    private void onPlayerDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info){
        MinecraftForge.EVENT_BUS.post(new EventPlayerDestroyBlock(pos));
    }
}
