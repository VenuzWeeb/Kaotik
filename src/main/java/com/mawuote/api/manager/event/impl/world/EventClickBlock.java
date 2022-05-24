package com.mawuote.api.manager.event.impl.world;

import com.mawuote.api.manager.event.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventClickBlock extends Event {
    public BlockPos pos;
    public EnumFacing facing;

    public EventClickBlock(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getPos(){return this.pos;}
}
