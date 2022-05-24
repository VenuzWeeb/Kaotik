package com.mawuote.client.modules.combat;

import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.utilities.entity.InventoryUtils;
import com.mawuote.api.utilities.math.MathUtils;
import com.mawuote.api.utilities.world.BlockUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Surroundhack extends Module<B> {
    public Surroundhack() {
        super("SurroundHack", "Surround", "Places blocks around your feet to protect you from crystals.", ModuleCategory.COMBAT);
    }

    private int placements;
    private BlockPos startPosition;
    private int tries;

    ValueEnum mode = new ValueEnum("Mode", "Mode", "The mode for the Surround.", Modes.Normal);
    ValueEnum item = new ValueEnum("Item", "Item", "The item for block placing.", InventoryUtils.Items.Obsidian);
    ValueEnum switchMode = new ValueEnum("Switch", "Switch", "The mode for switching.", InventoryUtils.SwitchModes.Normal);
    ValueNumber blocks = new ValueNumber("Blocks", "Blocks", "The amount of blocks that can be placed per tick.", 8, 1, 40);
    ValueEnum supportBlocks = new ValueEnum("SupportBlocks", "SupportBlocks", "The support blocks for placing.", Supports.Dynamic);
    ValueNumber retries = new ValueNumber("Retries", "Retries", "The amount of retries that can happen before stopping the crystal ignore.", 5, 0, 20);
    ValueBoolean dynamic = new ValueBoolean("Dynamic", "Dynamic", "Makes the surround place dynamically.", true);
    ValueBoolean center = new ValueBoolean("Center", "Center", "Positions the player to the center.", false);
    ValueBoolean rotate = new ValueBoolean("Rotate", "Rotate", "Rotates to the block when placing.", false);
    ValueBoolean floor = new ValueBoolean("Floor", "Floor", "Places blocks at the floor.", false);

    public void onMotionUpdate(){
        if (startPosition.getY() != MathUtils.roundToPlaces(mc.player.posY, 0) && mode.getValue().equals(Modes.Normal)){
            disable();
            return;
        }

        int slot = InventoryUtils.getCombatBlock(item.getValue().toString());
        int lastSlot = mc.player.inventory.currentItem;

        if (slot == -1){
            ChatManager.sendClientMessage("No blocks could be found.", 256);
            disable();
            return;
        }

        if (!getUnsafeBlocks().isEmpty()){
            InventoryUtils.switchSlot(slot, switchMode.getValue().equals(InventoryUtils.SwitchModes.Silent));

            for (BlockPos position : getUnsafeBlocks()){
                if (!supportBlocks.getValue().equals(Supports.None)){
                    if (BlockUtils.getPlaceableSide(position) == null || supportBlocks.getValue().equals(Supports.Static)) {
                        if (BlockUtils.isPositionPlaceable(position.down(), true, true)) {
                            placeBlock(position.down());
                        }
                    }
                }

                if (BlockUtils.isPositionPlaceable(position, true, true, tries <= retries.getValue().intValue())) {
                    placeBlock(position);
                    tries++;
                }
            }

            if (!switchMode.getValue().equals(InventoryUtils.SwitchModes.Strict)) InventoryUtils.switchSlot(lastSlot, switchMode.getValue().equals(InventoryUtils.SwitchModes.Silent));
        }

        placements = 0;

        if (getUnsafeBlocks().isEmpty()) {
            tries = 0;
            if (mode.getValue().equals(Modes.Toggle)) {
                disable();
            }
        }
    }

    private List<BlockPos> getOffsets(){
        final List<BlockPos> offsets = new ArrayList<>();

        if (dynamic.getValue()) {
            final double decimalX = Math.abs(mc.player.posX) - Math.floor(Math.abs(mc.player.posX));
            final double decimalZ = Math.abs(mc.player.posZ) - Math.floor(Math.abs(mc.player.posZ));

            final int lengthX = calculateLength(decimalX, false);
            final int negativeLengthX = calculateLength(decimalX, true);
            final int lengthZ = calculateLength(decimalZ, false);
            final int negativeLengthZ = calculateLength(decimalZ, true);

            final List<BlockPos> tempOffsets = new ArrayList<>();
            offsets.addAll(getOverlapPositions());

            for (int x = 1; x < lengthX + 1; ++x) {
                tempOffsets.add(addToPosition(getPlayerPosition(), x, 1 + lengthZ));
                tempOffsets.add(addToPosition(getPlayerPosition(), x, -(1 + negativeLengthZ)));
            }

            for (int x = 0; x <= negativeLengthX; ++x) {
                tempOffsets.add(addToPosition(getPlayerPosition(), -x, 1 + lengthZ));
                tempOffsets.add(addToPosition(getPlayerPosition(), -x, -(1 + negativeLengthZ)));
            }

            for (int z = 1; z < lengthZ + 1; ++z) {
                tempOffsets.add(addToPosition(getPlayerPosition(), 1 + lengthX, z));
                tempOffsets.add(addToPosition(getPlayerPosition(), -(1 + negativeLengthX), z));
            }

            for (int z = 0; z <= negativeLengthZ; ++z) {
                tempOffsets.add(addToPosition(getPlayerPosition(), 1 + lengthX, -z));
                tempOffsets.add(addToPosition(getPlayerPosition(), -(1 + negativeLengthX), -z));
            }

            offsets.addAll(tempOffsets);
        } else {
            for (final EnumFacing side : EnumFacing.HORIZONTALS) {
                offsets.add(getPlayerPosition().add(side.getXOffset(), 0, side.getZOffset()));
            }
        }

        return offsets;
    }

    private BlockPos getPlayerPosition() {
        return new BlockPos(mc.player.posX, ((mc.player.posY - Math.floor(mc.player.posY)) > 0.8) ? (Math.floor(mc.player.posY) + 1.0) : Math.floor(mc.player.posY), mc.player.posZ);
    }

    private List<BlockPos> getOverlapPositions() {
        final List<BlockPos> positions = new ArrayList<>();

        final int offsetX = calculateOffset(mc.player.posX - Math.floor(mc.player.posX));
        final int offsetZ = calculateOffset(mc.player.posZ - Math.floor(mc.player.posZ));
        positions.add(getPlayerPosition());

        for (int x = 0; x <= Math.abs(offsetX); ++x) {
            for (int z = 0; z <= Math.abs(offsetZ); ++z) {
                final int properX = x * offsetX;
                final int properZ = z * offsetZ;
                positions.add(getPlayerPosition().add(properX, -1, properZ));
            }
        }

        return positions;
    }

    private BlockPos addToPosition(final BlockPos position, double x, double z) {
        if (position.getX() < 0) x = -x;
        if (position.getZ() < 0) z = -z;
        return position.add(x, 0.0, z);
    }

    private int calculateOffset(final double dec) {
        return (dec >= 0.7) ? 1 : ((dec <= 0.3) ? -1 : 0);
    }

    private int calculateLength(final double decimal, final boolean negative) {
        if (negative) return (decimal <= 0.3) ? 1 : 0;
        return (decimal >= 0.7) ? 1 : 0;
    }

    @Override
    public void onEnable(){
        super.onEnable();

        if (mc.player == null || mc.world == null){
            disable();
            return;
        }

        startPosition = new BlockPos(MathUtils.roundVector(mc.player.getPositionVector(), 0));
    }

    public void placeBlock(BlockPos position){
        if (placements < blocks.getValue().intValue()){
            BlockUtils.placeBlock(position, EnumHand.MAIN_HAND, true);
            placements++;
        }
    }

    public List<BlockPos> getUnsafeBlocks(){
        List<BlockPos> positions = new ArrayList<>();
        for (BlockPos position : getOffsets()) if (!isSafe(position)) positions.add(position);
        return positions;
    }

    public boolean isSafe(BlockPos position){
        return !mc.world.getBlockState(position).getBlock().isReplaceable(mc.world, position);
    }

    public String getHudInfo(){
        return " " + tries + "/" + retries.getValue().intValue();
    }

    public enum Modes { Normal, Persistent, Toggle, Shift }
    public enum Supports { None, Dynamic, Static }
}
