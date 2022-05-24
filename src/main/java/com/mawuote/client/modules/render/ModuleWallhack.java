package com.mawuote.client.modules.render;

import com.mawuote.api.manager.event.impl.client.EventClient;
import com.mawuote.api.manager.event.impl.render.EventBlockGetRenderLayer;
import com.mawuote.api.manager.event.impl.render.EventRenderPutColorMultiplier;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

public class ModuleWallhack extends Module<B> {
    public ModuleWallhack(){
        super("WallHack", "Wall Hack", "Makes blocks transparent so that you can find target blocks easily.", ModuleCategory.RENDER);
        blocks = new ArrayList<>();
        blocks.add(Blocks.GOLD_ORE);
        blocks.add(Blocks.IRON_ORE);
        blocks.add(Blocks.COAL_ORE);
        blocks.add(Blocks.LAPIS_ORE);
        blocks.add(Blocks.DIAMOND_ORE);
        blocks.add(Blocks.REDSTONE_ORE);
        blocks.add(Blocks.LIT_REDSTONE_ORE);
        blocks.add(Blocks.TNT);
        blocks.add(Blocks.EMERALD_ORE);
        blocks.add(Blocks.FURNACE);
        blocks.add(Blocks.LIT_FURNACE);
        blocks.add(Blocks.DIAMOND_BLOCK);
        blocks.add(Blocks.IRON_BLOCK);
        blocks.add(Blocks.GOLD_BLOCK);
        blocks.add(Blocks.QUARTZ_ORE);
        blocks.add(Blocks.BEACON);
        blocks.add(Blocks.MOB_SPAWNER);
    }

    public static ArrayList<Block> blocks;

    public enum modes {
        Normal, Circuits;
    }

    public static ValueEnum Mode = new ValueEnum("Mode", "Mode", "", modes.Normal);
    public static ValueNumber Opacity = new ValueNumber("Opacity", "Opacity", "", 128.0f, 0.0f, 255.0f);
    public static ValueBoolean HandBlock = new ValueBoolean("HandBlock", "HandBlock", "", false);
    public final ValueBoolean SoftReload = new ValueBoolean("SoftReload", "SoftReload", "", false);

    boolean doReload = false;

    @SubscribeEvent
    public void onSetting(EventClient event) {
        if(mc.player == null || mc.world == null)
            return;
        if(event.getSetting() == Mode || event.getSetting() == Opacity || event.getSetting() == HandBlock || event.getSetting() == SoftReload) {
            doReload = true;
        }
    }

    public void onUpdate() {
        if(doReload) {
            reloadWorld();
            doReload = false;
        }
    }

    public static Block _block;

    public String getMetaData()
    {
        return String.valueOf(Mode.getValue());
    }

    @Override
    public void onEnable()
    {
        super.onEnable();
        mc.renderChunksMany = false;
        reloadWorld();
        ForgeModContainer.forgeLightPipelineEnabled = false;

        if (HandBlock.getValue())
        {
            ItemStack stack = mc.player.getHeldItemMainhand();

            if (stack.getItem() instanceof ItemBlock)
            {
                ItemBlock item = (ItemBlock) stack.getItem();

                _block = item.getBlock();
            }
        }
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
        mc.renderChunksMany = false;
        reloadWorld();
        ForgeModContainer.forgeLightPipelineEnabled = true;
    }

    private void reloadWorld()
    {
        if (mc.world == null || mc.renderGlobal == null)
            return;

        if (SoftReload.getValue())
        {
            mc.addScheduledTask(() ->
            {
                int x = (int) mc.player.posX;
                int y = (int) mc.player.posY;
                int z = (int) mc.player.posZ;

                int distance = mc.gameSettings.renderDistanceChunks * 16;

                mc.renderGlobal.markBlockRangeForRenderUpdate(x - distance, y - distance, z - distance, x + distance, y + distance, z + distance);
            });
        }
        else
            mc.renderGlobal.loadRenderers();
    }

    public static boolean containsBlock(Block block)
    {
        if (HandBlock.getValue() && _block != null)
            return block == _block;

        if (Mode.getValue().equals("Normal") && block != null)
        {
            return blocks.contains(block);
        }

        return  block == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE ||
                block == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE ||
                block == Blocks.STONE_PRESSURE_PLATE          ||
                block == Blocks.WOODEN_PRESSURE_PLATE         ||
                block == Blocks.STONE_BUTTON                  ||
                block == Blocks.WOODEN_BUTTON                 ||
                block == Blocks.LEVER                         ||
                block == Blocks.COMMAND_BLOCK                 ||
                block == Blocks.CHAIN_COMMAND_BLOCK           ||
                block == Blocks.REPEATING_COMMAND_BLOCK       ||
                block == Blocks.DAYLIGHT_DETECTOR             ||
                block == Blocks.DAYLIGHT_DETECTOR_INVERTED    ||
                block == Blocks.DISPENSER                     ||
                block == Blocks.DROPPER                       ||
                block == Blocks.HOPPER                        ||
                block == Blocks.OBSERVER                      ||
                block == Blocks.TRAPDOOR                      ||
                block == Blocks.IRON_TRAPDOOR                 ||
                block == Blocks.REDSTONE_BLOCK                ||
                block == Blocks.REDSTONE_LAMP                 ||
                block == Blocks.REDSTONE_TORCH                ||
                block == Blocks.UNLIT_REDSTONE_TORCH          ||
                block == Blocks.REDSTONE_WIRE                 ||
                block == Blocks.POWERED_REPEATER              ||
                block == Blocks.UNPOWERED_REPEATER            ||
                block == Blocks.POWERED_COMPARATOR            ||
                block == Blocks.UNPOWERED_COMPARATOR          ||
                block == Blocks.LIT_REDSTONE_LAMP             ||
                block == Blocks.REDSTONE_ORE                  ||
                block == Blocks.LIT_REDSTONE_ORE              ||
                block == Blocks.ACACIA_DOOR                   ||
                block == Blocks.DARK_OAK_DOOR                 ||
                block == Blocks.BIRCH_DOOR                    ||
                block == Blocks.JUNGLE_DOOR                   ||
                block == Blocks.OAK_DOOR                      ||
                block == Blocks.SPRUCE_DOOR                   ||
                block == Blocks.DARK_OAK_DOOR                 ||
                block == Blocks.IRON_DOOR                     ||
                block == Blocks.OAK_FENCE                     ||
                block == Blocks.SPRUCE_FENCE                  ||
                block == Blocks.BIRCH_FENCE                   ||
                block == Blocks.JUNGLE_FENCE                  ||
                block == Blocks.DARK_OAK_FENCE                ||
                block == Blocks.ACACIA_FENCE                  ||
                block == Blocks.OAK_FENCE_GATE                ||
                block == Blocks.SPRUCE_FENCE_GATE             ||
                block == Blocks.BIRCH_FENCE_GATE              ||
                block == Blocks.JUNGLE_FENCE_GATE             ||
                block == Blocks.DARK_OAK_FENCE_GATE           ||
                block == Blocks.ACACIA_FENCE_GATE             ||
                block == Blocks.JUKEBOX                       ||
                block == Blocks.NOTEBLOCK                     ||
                block == Blocks.PISTON                        ||
                block == Blocks.PISTON_EXTENSION              ||
                block == Blocks.PISTON_HEAD                   ||
                block == Blocks.STICKY_PISTON                 ||
                block == Blocks.TNT                           ||
                block == Blocks.SLIME_BLOCK                   ||
                block == Blocks.TRIPWIRE                      ||
                block == Blocks.TRIPWIRE_HOOK                 ||
                block == Blocks.RAIL                          ||
                block == Blocks.ACTIVATOR_RAIL                ||
                block == Blocks.DETECTOR_RAIL                 ||
                block == Blocks.GOLDEN_RAIL;
    }

    @SubscribeEvent
    public void onGetBlockRenderLayer(EventBlockGetRenderLayer event) {
        if(!(blocks.contains(event.getBlock()))) {
            if (!containsBlock(event.getBlock())) {
                event.setCancelled(true);
                event.setLayer(BlockRenderLayer.TRANSLUCENT);
            }
        }
    }

    @SubscribeEvent
    public void renderPutColor(EventRenderPutColorMultiplier event) {
        event.setCancelled(true);
        event.setOpacity(Opacity.getValue().floatValue() / 0xFF);
    }

    public static void processShouldSideBeRendered(Block block, IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callback)
    {
        if (blocks.contains(block))
            callback.setReturnValue(true);
    }

    public static void processGetLightValue(Block block, CallbackInfoReturnable<Integer> callback)
    {
        if (blocks.contains(block))
        {
            callback.setReturnValue(1);
        }
    }
}
