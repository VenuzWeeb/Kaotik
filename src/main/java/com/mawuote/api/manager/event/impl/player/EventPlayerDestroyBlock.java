package com.mawuote.api.manager.event.impl.player;

import com.mawuote.api.manager.event.Event;
import net.minecraft.util.math.BlockPos;

public class EventPlayerDestroyBlock extends Event {

    BlockPos pos;

    public EventPlayerDestroyBlock(BlockPos blockPos){
        super();
        pos = blockPos;
    }

    public BlockPos getBlockPos(){
        return pos;
    }

    public void setPos(BlockPos pos){
        this.pos = pos;
    }
}
