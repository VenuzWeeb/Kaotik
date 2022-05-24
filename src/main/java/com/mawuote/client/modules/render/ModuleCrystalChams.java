package com.mawuote.client.modules.render;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.*;
import com.mawuote.api.utilities.math.TimerUtils;
import com.mawuote.api.utilities.render.RainbowUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class ModuleCrystalChams extends Module<B> {
    public ModuleCrystalChams(){super("CrystalChams", "Crystal Chams", "Renders chams over crystals to make them look better.", ModuleCategory.RENDER);}

    public enum modes {
        Fill, Wireframe;
    }

    public enum outlineModes {
        Wire, Flat;
    }

    ValuePreview preview = new ValuePreview("CrystalPreview", "CrystalPreview", "", new EntityEnderCrystal(mc.world));
    public static ValueEnum mode = new ValueEnum("Mode", "Mode", "", modes.Fill);
    public static ValueNumber size = new ValueNumber("CrystalSize", "CrystalSize", "", 0.5f, 0.1f, 10f);
    public static ValueNumber crystalSpeed = new ValueNumber("CrystalSpeed", "CrystalSpeed", "", 3.0f, 0.1f, 50.0f);
    public static ValueNumber crystalBounce = new ValueNumber("CrystalBounce", "CrystalBounce", "", 0.2f, 0.0f, 10.0f);

    public static ValueBoolean insideCube = new ValueBoolean("InsideCube", "InsideCube", "", true);
    public static ValueBoolean outsideCube = new ValueBoolean("OutsideCube", "OutsideCube", "", true);
    public static ValueBoolean outsideCube2 = new ValueBoolean("OutsideCube2", "OutsideCube2", "", true);

    public static ValueBoolean pulse = new ValueBoolean("Pulse", "Pulse", "", false);
    ValueNumber pulseMax = new ValueNumber("PulseMax", "PulseMax", "", 255, 0, 255);
    ValueNumber pulseMin = new ValueNumber("PulseMin", "PulseMin", "", 100, 0, 255);
    ValueNumber pulseChangeAmt = new ValueNumber("PulseChangeAmt", "PulseChangeAmt", "", 5, 1, 50);

    public static ValueBoolean texture = new ValueBoolean("Texture", "Texture", "", false);
    public static ValueBoolean enchanted = new ValueBoolean("Glint", "Glint", "", false);
    public static ValueColor enchantedColor = new ValueColor("GlintColor", "GlintColor", "", new Color(0, 255, 120, 255));
    public static ValueBoolean outline = new ValueBoolean("Outline", "Outline", "", false);
    public static ValueEnum outlineMode = new ValueEnum("OutlineMode", "OutlineMode", "", outlineModes.Wire);
    public static ValueNumber width = new ValueNumber("Width", "Width", "", 3.0, 0.5, 5.0);
    public static ValueBoolean syncColor = new ValueBoolean("SyncColor", "SyncColor", "", true);
    public static ValueColor daColor = new ValueColor("Color", "Color", "", new Color(0, 255, 120, 255));
    public static ValueColor hiddenColor = new ValueColor("HiddenColor", "HiddenColor", "", new Color(0, 100, 255, 255));
    public static ValueBoolean hiddenSync = new ValueBoolean("HiddenSync", "HiddenSync", "", true);
    public static ValueColor outlineColor = new ValueColor("OutColor", "OutColor", "", new Color(255, 255, 255));

    public static ValueBoolean lifetimeColor = new ValueBoolean("Lifetime", "Lifetime", "", false);
    public static ValueNumber lifeTime = new ValueNumber("LifeSpeed", "LifeSpeed", "", 400, 50, 500);
    public static ValueColor lifeStart = new ValueColor("LifeColorStart", "LifeColorStart", "", new Color(255, 0, 0));
    public static ValueColor lifeEnd = new ValueColor("LifeColorEnd", "LifeColorEnd", "", new Color(0, 0, 255));

    public static Color color;
    public static Color outColor;
    public static Color hideColor;

    public static HashMap<UUID, Thingering> thingers = new HashMap();

    public static int pulseAlpha = 1;
    private boolean isReversing = false;
    private TimerUtils timer = new TimerUtils();
    int maxvalue;
    int minvalue;

    public void onUpdate() {
        maxvalue = pulseMax.getValue().intValue();
        minvalue = pulseMin.getValue().intValue();

        if(pulseAlpha < minvalue) {
            pulseAlpha = minvalue;
        }

        if (timer.hasReached(5)) {
            pulseAlpha += isReversing ? -pulseChangeAmt.getValue().intValue() : pulseChangeAmt.getValue().intValue();

            if (isReversing && pulseAlpha <= minvalue) {
                isReversing = false;
            } else if (!isReversing && pulseAlpha >= maxvalue) {
                isReversing = true;
            }
            timer.reset();
        }

        if(syncColor.getValue()) {
            color = this.globalColor(255);
            outColor = this.globalColor(255);
            hideColor = this.globalColor(255);
        } else {
            color = daColor.getValue();
            outColor = outlineColor.getValue();
            hideColor = hiddenColor.getValue();
        }

        for(Entity entity : mc.world.loadedEntityList) {
            if(entity instanceof EntityEnderCrystal) {
                if(!thingers.containsKey(entity.getUniqueID())) {
                    thingers.put(entity.getUniqueID(), new Thingering(entity));
                    thingers.get(entity.getUniqueID()).starTime = System.currentTimeMillis();
                }
            }
        }

        if(mc.player == null || mc.world == null)
            return;

        int others = (lifeTime.getValue().intValue()*10);

        for (final HashMap.Entry<UUID, Thingering> entry : thingers.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue().starTime < others) {
                float offset = 0;

                long time = System.currentTimeMillis();
                long duration = time - entry.getValue().starTime;

                if (duration < (lifeTime.getValue().intValue()*10)) {
                    offset = 1f - ((float) duration / (float) others);
                }
                entry.getValue().color = RainbowUtils.getGradientAlpha(lifeEnd.getValue(), lifeStart.getValue(), offset);
            }
        }
    }

    public class Thingering
    {
        public Entity entity;
        public long starTime;
        public Color color;

        public Thingering(final Entity entity) {
            this.entity = entity;
            this.starTime = 0;
        }
    }
}
