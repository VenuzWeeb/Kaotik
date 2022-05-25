package com.mawuote.client.modules.movement;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraftforge.fml.common.Mod;

public class FastFall extends Module {

    public FastFall() {
        super("FastFall", "Fast Fall", "falls fast", ModuleCategory.MOVEMENT);
    }

    public static ValueNumber speed = new ValueNumber("Speed","Speed","speed",1.0,0.1,5.0);



    @Override
    public void onMotionUpdate() {
        if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder()) {
            return;
        }
        if (mc.player.onGround) {
            mc.player.motionY -= speed.getValue().floatValue();
        }
    }
}
