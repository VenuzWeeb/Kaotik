package com.mawuote.api.utilities.math;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class TPSUtils {
    private static final float[] tickRates = new float[20];
    private int nextIndex;
    private long timeLastTimeUpdate;

    public TPSUtils(){
        nextIndex = 0;
        timeLastTimeUpdate = -1L;
        Arrays.fill(tickRates, 0.0F);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static float getTickRate() {
        float numTicks = 0.0F;
        float sumTickRates = 0.0F;
        for (float tickRate : tickRates) {
            if (tickRate > 0.0F) {
                sumTickRates += tickRate;
                numTicks += 1.0F;
            }
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0F, 20.0F);
    }

    public static float getTpsFactor() {
        float TPS = getTickRate();
        return 20.0f / TPS;
    }

    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            float timeElapsed = (float) (System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;
            tickRates[(this.nextIndex % tickRates.length)] = MathHelper.clamp(20.0F / timeElapsed, 0.0F, 20.0F);
            this.nextIndex += 1;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onUpdate(EventPacket.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            onTimeUpdate();
        }
    }
}


