package com.mawuote.api.utilities.world;

import com.mawuote.api.utilities.IMinecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import com.mawuote.api.utilities.world.BlockUtils.BlockResistance;

public class HoleUtils implements IMinecraft {

    public static boolean isBedrockHole(BlockPos pos) {
        boolean retVal = false;
        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
            if (mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR))
                if (mc.world.getBlockState(pos.up().up()).getBlock().equals(Blocks.AIR))
                    if (mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.BEDROCK))
                        if (mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.BEDROCK))
                            if (mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.BEDROCK))
                                if (mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.BEDROCK))
                                    if (mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.BEDROCK))
                                        retVal = true;
        return retVal;

    }

    public static boolean isObiHole(BlockPos pos) {
        boolean retVal = false;
        int obiCount = 0;
        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
            if (mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR)) {
                if (mc.world.getBlockState(pos.up().up()).getBlock().equals(Blocks.AIR)) {
                    if (mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.OBSIDIAN)) {
                        if (mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.OBSIDIAN)) {
                            obiCount++;
                        }
                        if (mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.OBSIDIAN)) {
                            if (mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.OBSIDIAN)) {
                                obiCount++;
                            }
                            if (mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.OBSIDIAN)) {
                                if (mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.OBSIDIAN)) {
                                    obiCount++;
                                }
                                if (mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.OBSIDIAN)) {
                                    if (mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.OBSIDIAN)) {
                                        obiCount++;
                                    }
                                    if (mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.OBSIDIAN)) {
                                        if (mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.OBSIDIAN)) {
                                            obiCount++;
                                        }
                                        if (obiCount >= 1) {
                                            retVal = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return retVal;

    }


    public static boolean isDoubleHole(final BlockPos pos) {
        for (final EnumFacing f : EnumFacing.HORIZONTALS) {
            final int offX = f.getXOffset();
            final int offZ = f.getZOffset();
            if ((mc.world.getBlockState(pos.add(offX, 0, offZ)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.add(offX, 0, offZ)).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.add(offX * -2, 0, offZ * -2)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.add(offX * -2, 0, offZ * -2)).getBlock() == Blocks.BEDROCK) && mc.world.getBlockState(pos.add(offX * -1, 0, offZ * -1)).getBlock() == Blocks.AIR && isSafeBlock(pos.add(0, -1, 0)) && isSafeBlock(pos.add(offX * -1, -1, offZ * -1))) {
                if (offZ == 0 && isSafeBlock(pos.add(0, 0, 1)) && isSafeBlock(pos.add(0, 0, -1)) && isSafeBlock(pos.add(offX * -1, 0, 1)) && isSafeBlock(pos.add(offX * -1, 0, -1))) {
                    return true;
                }
                if (offX == 0 && isSafeBlock(pos.add(1, 0, 0)) && isSafeBlock(pos.add(-1, 0, 0)) && isSafeBlock(pos.add(1, 0, offZ * -1)) && isSafeBlock(pos.add(-1, 0, offZ * -1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isHole(BlockPos pos) {
        boolean retVal = false;
        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
            if (mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR))
                if (mc.world.getBlockState(pos.up().up()).getBlock().equals(Blocks.AIR))
                    if (mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.OBSIDIAN))
                        if (mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.OBSIDIAN))
                            if (mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.OBSIDIAN))
                                if (mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.OBSIDIAN))
                                    if (mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.OBSIDIAN))
                                        retVal = true;

        return retVal;
    }

    public static boolean isPlayerInHole() {
        boolean retVal = false;
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
            if (mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR))
                if (mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.OBSIDIAN))
                    if (mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.OBSIDIAN))
                        if (mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.OBSIDIAN))
                            if (mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.OBSIDIAN))
                                if (mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.OBSIDIAN))
                                    retVal = true;

        return retVal;
    }

    public static boolean isEntityInHole(Entity entity) {
        boolean retVal = false;
        BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
            if (mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR))
                if (mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.OBSIDIAN))
                    if (mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.OBSIDIAN))
                        if (mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.OBSIDIAN))
                            if (mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.OBSIDIAN))
                                if (mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.OBSIDIAN))
                                    retVal = true;

        return retVal;
    }

    public static boolean isPlayerTrapped() {
        boolean retVal = false;
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ);
        if (!(mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR)))
            retVal = true;

        return retVal;
    }

    public static boolean isEntityTrapped(Entity entity) {
        boolean retVal = false;
        BlockPos pos = new BlockPos(entity.posX, entity.posY + 1, entity.posZ);
        if (!(mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR)))
            retVal = true;

        return retVal;
    }

    public static boolean isInHole(EntityPlayer player) {
        boolean retVal = false;
        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
            if (mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR))
                if (mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.down()).getBlock().equals(Blocks.OBSIDIAN))
                    if (mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.east()).getBlock().equals(Blocks.OBSIDIAN))
                        if (mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.west()).getBlock().equals(Blocks.OBSIDIAN))
                            if (mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.south()).getBlock().equals(Blocks.OBSIDIAN))
                                if (mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos.north()).getBlock().equals(Blocks.OBSIDIAN))
                                    retVal = true;

        return retVal;
    }

    static boolean isSafeBlock(final BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    public static Vec3d centerPos(double posX, double posY, double posZ) {
        return new Vec3d(Math.floor(posX) + 0.5D, Math.floor(posY), Math.floor(posZ) + 0.5D);
    }

    public static boolean isDoubleBedrockHoleX(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            IBlockState iBlockState = mc.world.getBlockState(blockPos2);

            if (iBlockState.getBlock() != Blocks.AIR && (iBlockState.getBlock() == Blocks.BEDROCK))
                continue;

            return false;
        }

        return true;
    }

    public static boolean isDoubleBedrockHoleZ(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            IBlockState iBlockState = mc.world.getBlockState(blockPos2);

            if (iBlockState.getBlock() != Blocks.AIR && (iBlockState.getBlock() == Blocks.BEDROCK))
                continue;

            return false;
        }

        return true;
    }

    public static boolean isDoubleObsidianHoleX(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            if (BlockUtils.getBlockResistance(blockPos2) == BlockResistance.Resistant || BlockUtils.getBlockResistance(blockPos2) == BlockResistance.Unbreakable)
                continue;

            return false;
        }

        return true;
    }

    public static boolean isDoubleObsidianHoleZ(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            if (BlockUtils.getBlockResistance(blockPos2) == BlockResistance.Resistant || BlockUtils.getBlockResistance(blockPos2) == BlockResistance.Unbreakable)
                continue;

            return false;
        }

        return true;
    }
}
