package com.mawuote.api.utilities.world;

import com.mawuote.api.utilities.IMinecraft;
import com.mawuote.api.utilities.entity.EntityUtils;
import com.mawuote.api.utilities.math.MathUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockUtils implements IMinecraft {
    public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
    public static final List<Block> shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);

    public BlockUtils() {

    }

    public static BlockPos[] SURROUND = {
            new BlockPos(0, -1, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, -1)
    };

    public static boolean canBeClicked(BlockPos var0) {
        return mc.world.getBlockState(var0).getBlock().canCollideCheck(mc.world.getBlockState(var0), false);
    }

    private static float[] getLegitRotations(Vec3d var0) {
        Vec3d var1 = new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
        double var2 = var0.x - var1.x;
        double var4 = var0.y - var1.y;
        double var6 = var0.z - var1.z;
        double var8 = Math.sqrt(var2 * var2 + var6 * var6);
        float var10 = (float)Math.toDegrees(Math.atan2(var6, var2)) - 90.0F;
        float var11 = (float)(-Math.toDegrees(Math.atan2(var4, var8)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(var10 - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(var11 - mc.player.rotationPitch)};
    }

    public static void faceVectorPacketInstant(Vec3d var0) {
        float[] var1 = getLegitRotations(var0);
        mc.player.connection.sendPacket(new net.minecraft.network.play.client.CPacketPlayer.Rotation(var1[0], var1[1], mc.player.onGround));
    }

    public static boolean placeBlockOnHole(BlockPos pos, boolean swing) {
        EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return false;
        }

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (!mc.player.isSneaking()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.player.setSneaking(true);
        }

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        if(swing)
            mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        return true;
    }

    public static
    boolean canPlaceCrystal ( BlockPos blockPos ) {
        BlockPos boost = blockPos.add ( 0 , 1 , 0 );
        BlockPos boost2 = blockPos.add ( 0 , 2 , 0 );
        try {
            return ( BlockUtils.mc.world.getBlockState ( blockPos ).getBlock ( ) == Blocks.BEDROCK || BlockUtils.mc.world.getBlockState ( blockPos ).getBlock ( ) == Blocks.OBSIDIAN ) && BlockUtils.mc.world.getBlockState ( boost ).getBlock ( ) == Blocks.AIR && BlockUtils.mc.world.getBlockState ( boost2 ).getBlock ( ) == Blocks.AIR && BlockUtils.mc.world.getEntitiesWithinAABB ( Entity.class , new AxisAlignedBB ( boost ) ).isEmpty ( ) && BlockUtils.mc.world.getEntitiesWithinAABB ( Entity.class , new AxisAlignedBB ( boost2 ) ).isEmpty ( );
        } catch ( Exception e ) {
            return false;
        }
    }

    public static
    boolean canPlaceCrystal ( BlockPos blockPos , boolean specialEntityCheck , boolean oneDot15 ) {
        BlockPos boost = blockPos.add ( 0 , 1 , 0 );
        BlockPos boost2 = blockPos.add ( 0 , 2 , 0 );
        try {
            if ( BlockUtils.mc.world.getBlockState ( blockPos ).getBlock ( ) != Blocks.BEDROCK && BlockUtils.mc.world.getBlockState ( blockPos ).getBlock ( ) != Blocks.OBSIDIAN ) {
                return false;
            }
            if ( ! oneDot15 && BlockUtils.mc.world.getBlockState ( boost2 ).getBlock ( ) != Blocks.AIR || BlockUtils.mc.world.getBlockState ( boost ).getBlock ( ) != Blocks.AIR ) {
                return false;
            }
            for (Entity entity : BlockUtils.mc.world.getEntitiesWithinAABB ( Entity.class , new AxisAlignedBB ( boost ) )) {
                if ( entity.isDead || specialEntityCheck && entity instanceof EntityEnderCrystal ) continue;
                return false;
            }
            if ( ! oneDot15 ) {
                for (Entity entity : BlockUtils.mc.world.getEntitiesWithinAABB ( Entity.class , new AxisAlignedBB ( boost2 ) )) {
                    if ( entity.isDead || specialEntityCheck && entity instanceof EntityEnderCrystal ) continue;
                    return false;
                }
            }
        } catch ( Exception ignored ) {
            return false;
        }
        return true;
    }

    public static
    boolean rayTracePlaceCheck ( BlockPos pos , boolean shouldCheck , float height ) {
        return ! shouldCheck || BlockUtils.mc.world.rayTraceBlocks ( new Vec3d ( BlockUtils.mc.player.posX , BlockUtils.mc.player.posY + (double) BlockUtils.mc.player.getEyeHeight ( ) , BlockUtils.mc.player.posZ ) , new Vec3d ( pos.getX ( ) , (float) pos.getY ( ) + height , pos.getZ ( ) ) , false , true , false ) == null;
    }

    public static
    boolean rayTracePlaceCheck ( BlockPos pos , boolean shouldCheck ) {
        return BlockUtils.rayTracePlaceCheck ( pos , shouldCheck , 1.0f );
    }

    public static
    boolean rayTracePlaceCheck ( BlockPos pos ) {
        return BlockUtils.rayTracePlaceCheck ( pos , true );
    }



    public static EnumFacing getFirstFacing(BlockPos pos) {
        for (EnumFacing facing : getPossibleSides(pos)) {
            return facing;
        }
        return null;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        List<EnumFacing> facings = new ArrayList<>();
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            if(mc.world.getBlockState(neighbour) == null)return facings;
            if(mc.world.getBlockState(neighbour).getBlock() == null)return facings;
            if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }

    public static List<BlockPos> getNearbyBlocks(EntityPlayer player, double blockRange, boolean motion) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();
        int range = (int) MathUtils.roundToPlaces(blockRange, 0);

        if (motion)
            player.getPosition().add(new Vec3i(player.motionX, player.motionY, player.motionZ));

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range - (range / 2); y++)
                for (int z = -range; z <= range; z++)
                    nearbyBlocks.add(player.getPosition().add(x, y, z));

        return nearbyBlocks;
    }

    public static void placeBlock(BlockPos blockPos, boolean swing, boolean packet) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (!mc.world.getBlockState(blockPos.offset(enumFacing)).getBlock().equals(Blocks.AIR)) {
                if(!isIntercepted(blockPos)) {
                    if(packet) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos.offset(enumFacing), enumFacing.getOpposite(), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
                    } else {
                        mc.playerController.processRightClickBlock(mc.player, mc.world, blockPos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(blockPos), EnumHand.MAIN_HAND);
                    }
                    if(swing) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    return;
                }
            }
        }
    }

    public static void placeBlock(final BlockPos position, final EnumHand hand, final boolean packet){
        if (!mc.world.getBlockState(position).getBlock().isReplaceable(mc.world, position)) return;
        if (getPlaceableSide(position) == null) return;

        clickBlock(position, getPlaceableSide(position), hand, packet);
        mc.player.connection.sendPacket(new CPacketAnimation(hand));
    }

    public static void clickBlock(final BlockPos position, final EnumFacing side, final EnumHand hand, final boolean packet){
        if (packet) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(position.offset(side), side.getOpposite(), hand, 0.5f, 0.5f, 0.5f));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, position.offset(side), side.getOpposite(), new Vec3d(position), hand);
        }
    }

    public static boolean isIntercepted(BlockPos blockPos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) continue;
            if (entity instanceof EntityEnderCrystal) continue;
            if (new AxisAlignedBB(blockPos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPositionPlaceable(BlockPos position, boolean entityCheck, boolean sideCheck) {
        if (!mc.world.getBlockState(position).getBlock().isReplaceable(mc.world, position)) return false;

        if (entityCheck) {
            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                return false;
            }
        }

        if (sideCheck) {
            return getPlaceableSide(position) != null;
        }

        return true;
    }

    public static
    List < BlockPos > getSphere ( BlockPos pos , float r , int h , boolean hollow , boolean sphere , int plus_y ) {
        ArrayList < BlockPos > circleblocks = new ArrayList <> ( );
        int cx = pos.getX ( );
        int cy = pos.getY ( );
        int cz = pos.getZ ( );
        int x = cx - (int) r;
        while ( (float) x <= (float) cx + r ) {
            int z = cz - (int) r;
            while ( (float) z <= (float) cz + r ) {
                int y = sphere ? cy - (int) r : cy;
                while ( true ) {
                    float f = y;
                    float f2 = sphere ? (float) cy + r : (float) ( cy + h );
                    if ( ! ( f < f2 ) ) break;
                    double dist = ( cx - x ) * ( cx - x ) + ( cz - z ) * ( cz - z ) + ( sphere ? ( cy - y ) * ( cy - y ) : 0 );
                    if ( ! ( ! ( dist < (double) ( r * r ) ) || hollow && dist < (double) ( ( r - 1.0f ) * ( r - 1.0f ) ) ) ) {
                        BlockPos l = new BlockPos ( x , y + plus_y , z );
                        circleblocks.add ( l );
                    }
                    ++ y;
                }
                ++ z;
            }
            ++ x;
        }
        return circleblocks;
    }

    public static boolean isPositionPlaceable(BlockPos position, boolean entityCheck, boolean sideCheck, boolean ignoreCrystals) {
        if (!mc.world.getBlockState(position).getBlock().isReplaceable(mc.world, position)) return false;

        if (entityCheck) {
            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                if (entity instanceof EntityEnderCrystal && ignoreCrystals) continue;
                return false;
            }
        }

        if (sideCheck) {
            return getPlaceableSide(position) != null;
        }

        return true;
    }

    public static boolean isPositionPlaceable(BlockPos pos, boolean entityCheck, double distance) {
        Block block = BlockUtils.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if(mc.player.getDistance(entity) > distance) continue;
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                return false;
            }
        }
        return true;
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {

        for (EnumFacing side : EnumFacing.values()) {

            BlockPos neighbour = pos.offset(side);

            if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                continue;
            }

            IBlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) {
                return side;
            }
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    public static BlockResistance getBlockResistance(BlockPos block) {
        if (mc.world.isAirBlock(block))
            return BlockResistance.Blank;

        else if (mc.world.getBlockState(block).getBlock().getBlockHardness(mc.world.getBlockState(block), mc.world, block) != -1 && !(mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST)))
            return BlockResistance.Breakable;

        else if (mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST))
            return BlockResistance.Resistant;

        else if (mc.world.getBlockState(block).getBlock().equals(Blocks.BEDROCK))
            return BlockResistance.Unbreakable;

        return null;
    }

    public enum BlockResistance {
        Blank,
        Breakable,
        Resistant,
        Unbreakable
    }

}
