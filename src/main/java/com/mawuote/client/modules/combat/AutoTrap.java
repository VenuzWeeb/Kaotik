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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class AutoTrap extends Module<B> {
    public AutoTrap() {
        super("AutoTrap", "Auto Trap", "traps run ez", ModuleCategory.COMBAT);
    }

    public static EntityPlayer target = null;
    private int placements;

    private final Vec3d[] offsets = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, 0.0)};

    public static ValueEnum item = new ValueEnum("Item", "Item", "The item for block placing.", InventoryUtils.Items.Obsidian);
    public static ValueEnum switchMode = new ValueEnum("Switch", "Switch", "The mode for switching.", InventoryUtils.SwitchModes.Normal);
    public static ValueNumber blocks = new ValueNumber("Blocks", "Blocks", "The amount of blocks that can be placed per tick.", 8, 1, 40);
    public static ValueNumber range = new ValueNumber("Range", "Range", "The maximum range that the block can be away.", 4.0f, 0.0f, 8.0f);
    public static ValueNumber targetRange = new ValueNumber("TargetRange", "TargetRange", "The range for the targeting.", 6.0f, 0.0f, 8.0f);
    public static ValueBoolean selfDisable = new ValueBoolean("SelfDisable", "SelfDisable", "Automatically disables when there are no more holes.", true);

    public void onMotionUpdate(){
        target = CrystalUtils.getTarget(targetRange.getValue().floatValue());
        if (target == null) return;

        int slot = InventoryUtils.getCombatBlock(item.getValue().toString());
        int lastSlot = mc.player.inventory.currentItem;

        if (slot == -1){
            ChatManager.sendClientMessage("No blocks could be found.", 256);
            disable();
            return;
        }

        placements = 0;

        if (!getPlaceableBlocks(target).isEmpty()){
            InventoryUtils.switchSlot(slot, switchMode.getValue().equals(InventoryUtils.SwitchModes.Silent));

            for (BlockPos position : getPlaceableBlocks(target)) placeBlock(position);

            if (!switchMode.getValue().equals(InventoryUtils.SwitchModes.Strict)) InventoryUtils.switchSlot(lastSlot, switchMode.getValue().equals(InventoryUtils.SwitchModes.Silent));
        }

        if (selfDisable.getValue() && getPlaceableBlocks(target).isEmpty()){
            disable();
        }
    }

    public void placeBlock(BlockPos position){
        if (BlockUtils.isPositionPlaceable(position, true, true, false)) {
            if (placements < blocks.getValue().intValue()) {
                BlockUtils.placeBlock(position, EnumHand.MAIN_HAND, true);
                placements++;
            }
        }
    }

    public List<BlockPos> getPlaceableBlocks(EntityPlayer player){
        List<BlockPos> positions = new ArrayList<>();

        for (Vec3d vec3d : offsets){
            BlockPos position = new BlockPos(vec3d.add(player.getPositionVector()));
            if (mc.world.getBlockState(position).getBlock().isReplaceable(mc.world, position)) positions.add(position);
        }

        return positions;
    }
}