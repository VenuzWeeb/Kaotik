package com.mawuote.api.manager.event.impl.render;

import com.mawuote.api.manager.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

public class EventBlockGetRenderLayer extends Event
{
    private BlockRenderLayer _layer;
    private Block _block;

    public EventBlockGetRenderLayer(Block block)
    {
        _block = block;
    }

    public Block getBlock()
    {
        return _block;
    }

    public void setLayer(BlockRenderLayer layer)
    {
        _layer = layer;
    }

    public BlockRenderLayer getBlockRenderLayer()
    {
        return _layer;
    }

}
