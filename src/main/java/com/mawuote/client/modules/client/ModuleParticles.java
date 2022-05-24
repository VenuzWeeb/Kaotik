package com.mawuote.client.modules.client;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.client.gui.special.particles.ParticleSystem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ModuleParticles extends Module<B> {
    public ModuleParticles(){super("Particles", "Particles", "Renders fancy particles on the screen.", ModuleCategory.CLIENT);}

    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(255, 255, 255));
    public static ValueNumber lineWidth = new ValueNumber("LineWidth", "LineWidth", "", 2.0, 1.0, 3.0);
    public static ValueNumber amount = new ValueNumber("Population", "Amounts", "", 100, 50, 400);
    public static ValueNumber radius = new ValueNumber("Radius", "Radius", "", 100, 50, 300);
    public static ValueNumber speed = new ValueNumber("Speed", "Speed", "", 0.1f, 0.1f, 10f);
    public static ValueNumber delta = new ValueNumber("Delta", "Delta", "", 1, 1, 10);

    boolean changeAmount = false;

    private ParticleSystem ps;
    public void onEnable() {
        ps = new ParticleSystem(amount.getValue().intValue(), radius.getValue().intValue());
    }

    @SubscribeEvent
    public void onSetting(EventClient event) {
        if(mc.player == null || mc.world == null)
            return;
        if(event.getSetting() == amount ) {
            changeAmount = true;
        }
    }

    public void onUpdate(){
        if(changeAmount) {
            ps.changeParticles(amount.getValue().intValue());
            changeAmount = false;
        }

        ps.tick(delta.getValue().intValue());
        ps.dist = radius.getValue().intValue();
        ps.SPEED = (float)speed.getValue().doubleValue();
    }

    public void onRender2D(EventRender2D event) {
        if (mc.ingameGUI.getChatGUI().getChatOpen() || mc.currentScreen == Kaotik.CLICK_GUI || mc.currentScreen instanceof GuiContainer) {
            ps.render();
        }
    }
}
