package com.mawuote.client.modules.movement;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.utilities.entity.EntityUtils;
import com.mawuote.api.utilities.math.MathUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint extends Module<B> {
    public Sprint() {
        super("Sprint", "Sprint", "Automatically sprints for you.", ModuleCategory.MOVEMENT);
    }

    public enum modes {
        Rage, Legit
    }

    ValueEnum mode = new ValueEnum("Mode", "Mode", "Mode", modes.Rage);

    @Override
    public void onUpdate() {
        if (mode.getValue().equals(modes.Legit)) {
            if (mc.player.moveForward > 0 && !mc.player.collidedHorizontally) {
                mc.player.setSprinting(true);
            }
        } else {
            if (EntityUtils.isMoving()) {
                mc.player.setSprinting(true);
                if (mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown()) {
                    final double[] dir = MathUtils.directionSpeed(.18);
                    mc.player.motionX = dir[0];
                    mc.player.motionZ = dir[1];
                }
            }
        }
    }
}
