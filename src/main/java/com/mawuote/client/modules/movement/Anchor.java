package com.mawuote.client.modules.movement;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.utilities.world.HoleUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Anchor extends Module<B> {
    public Anchor(){super("Anchor", "Anchor", "Automatically stops player movement when hovering a hole.", ModuleCategory.MOVEMENT);}

    ValueNumber height = new ValueNumber("Height", "Height", "", 3, 1, 5);
    ValueBoolean doubles = new ValueBoolean("Doubles", "Doubles", "", false);
    ValueBoolean drop = new ValueBoolean("Drop", "Drop", "", false);
    ValueNumber speed = new ValueNumber("DropSpeed","DropSpeed","",1.0,0.1,5.0);
    ValueBoolean pitchD = new ValueBoolean("PitchDepend", "PitchDepend", "", false);
    ValueNumber pitch = new ValueNumber("Pitch", "Pitch", "", 60, 0, 90);

    private Vec3d Center = Vec3d.ZERO;

    public void onMotionUpdate() {
        if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder()) {
            return;
        }

        for (int i = 0; i < height.getValue().intValue(); i++) {
            if (pitchD.getValue() && !(mc.player.rotationPitch >= pitch.getValue().intValue())) {
                return;
            }
            if (HoleUtils.isHole(getPlayerPos().down(i)) || (HoleUtils.isDoubleHole(getPlayerPos().down(i)) && doubles.getValue())) {
                Center = getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);
                double XDiff = Math.abs(Center.x - mc.player.posX);
                double ZDiff = Math.abs(Center.z - mc.player.posZ);

                if (XDiff <= 0.1 && ZDiff <= 0.1) {
                    Center = Vec3d.ZERO;
                } else {
                    double MotionX = Center.x - mc.player.posX;
                    double MotionZ = Center.z - mc.player.posZ;

                    mc.player.motionX = MotionX / 2;
                    mc.player.motionZ = MotionZ / 2;
                    if(drop.getValue()) {
                        mc.player.motionY -= speed.getValue().floatValue();
                    }
                }
            }
        }
    }

    public Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D ;

        return new Vec3d(x, y, z);
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }
}
