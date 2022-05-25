package com.mawuote.client.modules.combat;

import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.utilities.crystal.CrystalUtils;
import com.mawuote.api.utilities.entity.InventoryUtils;
import com.mawuote.api.utilities.world.BlockUtils;
import com.mawuote.api.utilities.world.HoleUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class AutoFillHoleChing extends Module {
    public AutoFillHoleChing() {
        super("AutoFillHoleChing", "Auto Fill Hole Ching", "Automatically fills holes with selected blocks.", ModuleCategory.COMBAT);
    }

    private int placements = 0;

    public static ValueEnum mode = new ValueEnum("Mode", "Mode", "The mode for the HoleFill.", Modes.Normal);
    public static ValueEnum item = new ValueEnum("Item", "Item", "The item for block placing.", InventoryUtils.Items.Obsidian);
    public static ValueEnum switchMode = new ValueEnum("Switch", "Switch", "The mode for switching.", InventoryUtils.SwitchModes.Normal);
    ValueNumber blocks = new ValueNumber("Blocks", "Blocks", "The amount of blocks that can be placed per tick.", 8, 1, 40);
    public static ValueNumber range = new ValueNumber("Range", "Range", "The maximum range that the block can be away.", 4.0f, 0.0f, 8.0f);
    public static ValueNumber targetRange = new ValueNumber("TargetRange", "TargetRange", "The range for the targeting.", 6.0f, 0.0f, 8.0f);
    public static ValueBoolean doubles = new ValueBoolean("Doubles", "Doubles", "Fills in double holes too.", true);
    public static ValueBoolean targetDisable = new ValueBoolean("TargetDisable", "TargetDisable", "Automatically disables when there is no target.", true);
    public static ValueBoolean selfDisable = new ValueBoolean("SelfDisable", "SelfDisable", "Automatically disables when there are no more holes.", true);

    @Override
    public void onMotionUpdate(){
        super.onMotionUpdate();

        EntityPlayer target = CrystalUtils.getTarget(targetRange.getValue().floatValue());
        if (mode.getValue().equals(Modes.Smart) && target == null) {
            if (targetDisable.getValue()) disable();
            return;
        }

        placements = 0;

        int slot = InventoryUtils.getCombatBlock(item.getValue().toString());
        int lastSlot = mc.player.inventory.currentItem;

        if (slot == -1){
            ChatManager.sendClientMessage("No blocks could be found.", 256);
            disable();
            return;
        }

        ArrayList<BlockPos> holes = getHoles();

        if (!holes.isEmpty()){
            InventoryUtils.switchSlot(slot, switchMode.getValue().equals(InventoryUtils.SwitchModes.Silent));

            for (BlockPos position : holes) {
                placeBlock(position);
            }

            if (!switchMode.getValue().equals(InventoryUtils.SwitchModes.Strict)) InventoryUtils.switchSlot(lastSlot, switchMode.getValue().equals(InventoryUtils.SwitchModes.Silent));
        }

        if (selfDisable.getValue() && holes.isEmpty()){
            disable();
        }
    }

    public ArrayList<BlockPos> getHoles(){
        ArrayList<BlockPos> holes = new ArrayList<>();

        for (BlockPos position : CrystalUtils.getSphere(range.getValue().floatValue(), true, false)){
            if (HoleUtils.isHole(position)) {
                if (!BlockUtils.isPositionPlaceable(position, true, true, false)) continue;
                holes.add(position);
            } else if (HoleUtils.isDoubleHole(position) && doubles.getValue()) {
                if (!(mc.world.getBlockState(position).getBlock() == Blocks.AIR)) continue;
                if (!(mc.world.getBlockState(position.up()).getBlock() == Blocks.AIR)) continue;
                if (!(mc.world.getBlockState(position.up().up()).getBlock() == Blocks.AIR)) continue;
                if (!BlockUtils.isPositionPlaceable(position, true, true, false)) continue;
                holes.add(position);
            }
        }

        return holes;
    }

    public void placeBlock(BlockPos position){
        if (BlockUtils.isPositionPlaceable(position, true, true, false)) {
            if (placements < blocks.getValue().intValue()) {
                BlockUtils.placeBlock(position, EnumHand.MAIN_HAND, true);
                placements++;
            }
        }
    }

    public enum Modes { Normal, Smart }
}
