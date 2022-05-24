package com.mawuote.client.modules.render;

import com.mawuote.api.manager.event.impl.render.EventBossBar;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleNoRender extends Module<B> {

    public ModuleNoRender() {
        super("NoRender", "No Render", "Prevents certain things from rendering.", ModuleCategory.RENDER);
    }

    public static ValueBoolean fire = new ValueBoolean("Fire","fire","fire",false);
    public static ValueBoolean armor = new ValueBoolean("Armor", "Armor", "", false);
    public static ValueBoolean totemPop = new ValueBoolean("TotemPop", "TotemPop", "", false);
    public static ValueBoolean pumpkin = new ValueBoolean("Pumpkin", "Pumpkin", "", false);
    public static ValueBoolean hurtCam = new ValueBoolean("HurtCam", "HurtCam", "", false);
    public static ValueBoolean bossBar = new ValueBoolean("BossBar", "BossBar", "", false);
    public static ValueBoolean suffocation = new ValueBoolean("Suffocation", "Suffocation", "", false);

    @SubscribeEvent
    public void onRenderBlock(RenderBlockOverlayEvent event) {
        if (fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBossBar(EventBossBar event) {
        if (bossBar.getValue()) {
            event.setCancelled(true);
        }
    }
}
