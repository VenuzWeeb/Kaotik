package com.mawuote.client.modules.combat;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.utilities.crystal.CrystalUtils;
import com.mawuote.api.utilities.entity.InventoryUtils;
import com.mawuote.api.utilities.math.MathUtils;
import com.mawuote.api.utilities.world.BlockUtils;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;

public class FeetPlace extends Module {
    public FeetPlace(){super("FeetPlace", "FeetPlace", "Automatically places obsidian at the enemy's feet.", ModuleCategory.COMBAT);}

    public static ValueNumber enemyRange = new ValueNumber("EnemyRange", "EnemyRange", "", 5, 2, 8);
    public static ValueNumber palceReach = new ValueNumber("PlaceReach", "PlaceReach", "", 5.0, 2.0, 6.0);
    public static ValueNumber blockDistance = new ValueNumber("BlockDistance", "BlockDistance", "BlockDistance", 4, 2, 6);

    EntityPlayer target;
    boolean placed = false;
    int oldSlot = -1;
    BlockPos currentPos = null;

    public void onMotionUpdate() {
        if(mc.player == null || mc.world == null)
            return;

        this.oldSlot = mc.player.inventory.currentItem;

        target = (EntityPlayer) getClosest();

        int obiSlot = InventoryUtils.getHotbarBlockSlot(Blocks.OBSIDIAN);


        if(target != null) {
            BlockPos playerPos = new BlockPos(Math.floor(target.posX), target.posY, Math.floor(target.posZ));

            if (placed == false) {
                if (AutoCrystalHack.renderPosition != null) return;

                BlockPos pos = getPos(target);

                if (pos != null) {
                    if (obiSlot != -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(obiSlot));
                    }
                    BlockUtils.placeBlock(pos, true, false);
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                    placed = true;
                    currentPos = pos;
                }
            }

            if(currentPos != null && placed != false) {
                if (mc.player.getDistance(target) > enemyRange.getValue().doubleValue() || mc.player.getDistanceSq(currentPos) > MathUtils.square(blockDistance.getValue().doubleValue()) || playerPos.y <= currentPos.y || BlockUtils.isIntercepted(currentPos.up()) || !(mc.world.getBlockState(currentPos.up()).getBlock() == Blocks.AIR)) {
                    placed = false;
                }
            }
        }
    }


    public BlockPos getPos(EntityPlayer target) {
        BlockPos placePos = null;
        BlockPos playerPos = new BlockPos(Math.floor(target.posX), target.posY, Math.floor(target.posZ));
        double dist = MathUtils.square(palceReach.getValue().doubleValue());

        for(BlockPos pos : CrystalUtils.getSphere(palceReach.getValue().floatValue(), true, false)) {
            if (pos.getY() >= playerPos.getY()) continue;

            if (pos == playerPos) continue;

            if (!canPlace(pos, true, true)) {
                continue;
            }

            if(!(mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR)) continue;

            if(BlockUtils.isIntercepted(pos.up()) || BlockUtils.isIntercepted(pos.up())) continue;
            double pDist = target.getDistanceSq(pos);
            if (pDist < dist) {
                dist = pDist;
                placePos = pos;
            }
        }

        return placePos;
    }

    public Entity getClosest() {
        Entity returnEntity = null;
        double dist = enemyRange.getValue().doubleValue();
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity instanceof EntityPlayer) {
                if(entity != null) {
                    if (mc.player.getDistance(entity) > dist)
                        continue;
                    if (entity == mc.player)
                        continue;
                    double pDist = mc.player.getDistance(entity);
                    if (pDist < dist) {
                        dist = pDist;
                        returnEntity = entity;
                    }
                }
            }
        }
        return returnEntity;
    }

    public boolean canPlace(BlockPos pos, boolean obsidian, boolean bedrock) {
        Block block = BlockUtils.mc.world.getBlockState(pos).getBlock();
        if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow || (block instanceof BlockObsidian && obsidian) || (block == Blocks.BEDROCK && bedrock)) {
            return true;
        }
        return false;
    }

    public void onDisable() {
        placed = false;
    }
}
