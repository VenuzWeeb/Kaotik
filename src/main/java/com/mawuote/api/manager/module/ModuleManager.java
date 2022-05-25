package com.mawuote.api.manager.module;

import com.mawuote.api.manager.event.impl.player.EventMotionUpdate;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.value.Value;
import com.mawuote.client.modules.client.*;
import com.mawuote.client.modules.combat.*;
import com.mawuote.client.modules.misc.Announcer;
import com.mawuote.client.modules.misc.ChatMod;
import com.mawuote.client.modules.misc.Killsay;
import com.mawuote.client.modules.misc.hihowareyou;
import com.mawuote.client.modules.movement.*;
import com.mawuote.client.modules.player.ChorusPospone;
import com.mawuote.client.modules.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ModuleManager {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Module> modules;

    public ModuleManager(){
        MinecraftForge.EVENT_BUS.register(this);
        this.modules = new ArrayList<>();

        // Combat
        addModule(new AutoCrystalHack());
        addModule(new AutoTrap());
        addModule(new AutoFillHoleChing());
        addModule(new Surroundhack());
        addModule(new FeetPlace());


        // Player
        addModule(new ChorusPospone());


        // Misc

        addModule(new ModuleDiscordPresence());
        addModule(new Announcer());
        addModule(new hihowareyou());
        addModule(new Killsay());
        addModule(new ChatMod());


        // Movement
        addModule(new Anchor());
        addModule(new Speedy());
        addModule(new Strafe());
        addModule(new Step());
        addModule(new NoSlowDown());
        addModule(new FastFall());
        addModule(new TP());
        addModule(new Sprint());
        addModule(new Velocity());


        // Render
        addModule(new ModuleAnimations());
        addModule(new ModuleHoleESP());
        addModule(new ModuleShaderChams());
        addModule(new ModulePopChams());
        addModule(new ModuleCrosshair());
        addModule(new ModuleWallhack());
        addModule(new ModuleCrystalChams());
        addModule(new ModulePlayerChams());
        addModule(new ModuleESP());
        addModule(new ModuleNoRender());
        addModule(new ModuleSkeleton());
        addModule(new ModuleNametags());
        addModule(new ModuleLogoutSpots());


        // Client
        addModule(new ModuleMiddleClick());
        addModule(new ModuleNotifications());
        addModule(new ModuleHud());
        addModule(new ModuleGUI());
        addModule(new ModuleStreamerMode());
        addModule(new ModuleColor());
        addModule(new ModuleFont());
        addModule(new ModuleParticles());
        addModule(new ModuleHUDEditor());

        modules.sort(Comparator.comparing(Module::getName));
    }

    public void addModule(Module module){
        try {
            for (Field field : module.getClass().getDeclaredFields()){
                if (Value.class.isAssignableFrom(field.getType())){
                    if (!field.isAccessible()) field.setAccessible(true);
                    module.addValue((Value) field.get(module));
                }
            }

            module.addValue(module.tag);
            module.addValue(module.chatNotify);
            module.addValue(module.drawn);
            module.addValue(module.bind);

            modules.add(module);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Module> getModules(){
        return modules;
    }

    public ArrayList<Module> getModules(ModuleCategory category){
        return (ArrayList<Module>) modules.stream().filter(m -> m.getCategory().equals(category)).collect(Collectors.toList());
    }

    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }

        return null;
    }

    public boolean isModuleEnabled(String name){
        Module module = modules.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
        if (module != null){
            return module.isToggled();
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (mc.player != null && mc.world != null){
            modules.stream().filter(Module::isToggled).forEach(Module::onUpdate);
        }
    }

    @SubscribeEvent
    public void onUpdate(EventMotionUpdate event){
        if (mc.player != null && mc.world != null){
            modules.stream().filter(Module::isToggled).forEach(Module::onMotionUpdate);
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Post event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            modules.stream().filter(Module::isToggled).forEach(m -> m.onRender2D(new EventRender2D(event.getPartialTicks())));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event){
        mc.profiler.startSection("mawuote");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);

        EventRender3D renderEvent = new EventRender3D(event.getPartialTicks());
        modules.stream().filter(Module::isToggled).forEach(mm -> mm.onRender3D(renderEvent));

        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        mc.profiler.endSection();
    }

    @SubscribeEvent
    public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event){
        modules.stream().filter(Module::isToggled).forEach(Module::onLogin);
    }

    @SubscribeEvent
    public void onLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        modules.stream().filter(Module::isToggled).forEach(Module::onLogout);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event){
        if (Keyboard.getEventKeyState()){
            if (Keyboard.getEventKey() == Keyboard.KEY_NONE) return;
            for (Module module : modules){
                if (module.getBind() == Keyboard.getEventKey()) module.toggle();
            }
        }
    }
}
