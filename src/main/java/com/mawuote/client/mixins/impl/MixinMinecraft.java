package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.client.gui.special.EuropaMainMenu;
import com.mawuote.client.modules.client.ModuleGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = Minecraft.class, priority = 999)
public abstract class MixinMinecraft {

    private long lastFrame = this.getTime();
    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Shadow
    public abstract void displayGuiScreen(@Nullable GuiScreen var1);

    @Inject(method={"runTick()V"}, at={@At(value="RETURN")})
    private void runTick(CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && ModuleGUI.customMenu.getValue()) {
            Minecraft.getMinecraft().displayGuiScreen(new EuropaMainMenu());
        }
    }

    @Inject(method={"runGameLoop"}, at={@At(value="HEAD")})
    private void onRunGameLoopPre(CallbackInfo ci) {
        long currentTime = this.getTime();
        int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        RenderUtils.deltaTime = deltaTime;
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="HEAD")})
    private void displayGuiScreen(GuiScreen screen, CallbackInfo ci) {
        if (screen instanceof GuiMainMenu) {
            this.displayGuiScreen(new EuropaMainMenu());
        }
    }

    @Redirect(method={"sendClickBlockToController"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    private boolean isHandActiveWrapper(EntityPlayerSP playerSP) {
        return !Kaotik.getModuleManager().isModuleEnabled("MultiTask") && playerSP.isHandActive();
    }

    @Redirect(method={"rightClickMouse"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z", ordinal=0))
    private boolean isHittingBlockHook(PlayerControllerMP playerControllerMP) {
        return !Kaotik.getModuleManager().isModuleEnabled("MultiTask") && playerControllerMP.getIsHittingBlock();
    }

    @Redirect(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"))
    private void createDisplay(String title) {
        Display.setTitle("Kaotik " + Kaotik.VERSION + " | evolving to kaotik");
    }
}
