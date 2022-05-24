package com.mawuote.api.utilities.crystal;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.utilities.math.MathUtils;
import com.mawuote.api.utilities.IMinecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CrystalUtils implements IMinecraft {
    public static EntityPlayer getTarget(float range){
        EntityPlayer targetPlayer = null;

        for (EntityPlayer player : new ArrayList<>(mc.world.playerEntities)){
            if (mc.player.getDistanceSq(player) > MathUtils.square(range)) continue;
            if (player == mc.player) continue;
            if (Kaotik.FRIEND_MANAGER.isFriend(player.getName())) continue;
            if (player.isDead) continue;
            if (player.getHealth() <= 0) continue;

            if (targetPlayer == null){
                targetPlayer = player;
                continue;
            }

            if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(targetPlayer)){
                targetPlayer = player;
            }
        }

        return targetPlayer;
    }

    public static List<BlockPos> getSphere(final float range, final boolean sphere, final boolean hollow) {
        final List<BlockPos> blocks = new ArrayList<>();

        for (int x = mc.player.getPosition().getX() - (int) range; x <= mc.player.getPosition().getX() + range; ++x) {
            for (int z = mc.player.getPosition().getZ() - (int) range; z <= mc.player.getPosition().getZ() + range; ++z) {
                for (int y = sphere ? (mc.player.getPosition().getY() - (int) range) : mc.player.getPosition().getY(); y < ((mc.player.getPosition().getY() + range)); ++y) {
                    final double distance = (mc.player.getPosition().getX() - x) * (mc.player.getPosition().getX() - x) + (mc.player.getPosition().getZ() - z) * (mc.player.getPosition().getZ() - z) + (sphere ? ((mc.player.getPosition().getY() - y) * (mc.player.getPosition().getY() - y)) : 0);
                    if (distance < range * range && (!hollow || distance >= (range - 1.0) * (range - 1.0))) blocks.add(new BlockPos(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean canPlaceCrystal(BlockPos position, boolean placeUnderBlock, boolean multiPlace, boolean holePlace){
        if (mc.world.getBlockState(position).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(position).getBlock() != Blocks.OBSIDIAN) return false;
        if (mc.world.getBlockState(position.add(0, 1, 0)).getBlock() != Blocks.AIR || (!placeUnderBlock && mc.world.getBlockState(position.add(0, 2, 0)).getBlock() != Blocks.AIR)) return false;
        if (multiPlace) return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 1, 0))).isEmpty() && (!placeUnderBlock && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 2, 0))).isEmpty());
        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 1, 0)))){
            if (entity instanceof EntityEnderCrystal) continue;
            return false;
        }

        if (!placeUnderBlock){
            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 2, 0)))){
                if (entity instanceof EntityEnderCrystal || (holePlace && entity instanceof EntityPlayer)) continue;
                return false;
            }
        }

        return true;
    }

    public static void drawText(BlockPos pos, String text) {
        GlStateManager.pushMatrix();
        RenderUtils.glBillboardDistanceScaled((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate((-((double) Kaotik.FONT_MANAGER.getStringWidth(text) / 2.0)), 0.0, 0.0);
        Kaotik.FONT_MANAGER.drawString(text, 0, 0, Color.WHITE);
        GlStateManager.popMatrix();
    }

    public static boolean isEntityMoving(EntityLivingBase entity){
        return entity.motionX > 2 || entity.motionY > 2 || entity.motionZ > 2;
    }

    public static boolean canSeePos(BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
    }
}
