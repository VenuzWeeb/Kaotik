package com.mawuote.client.modules.render;

import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.api.utilities.world.BlockUtils;
import com.mawuote.api.utilities.world.HoleUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleHoleESP extends Module {
    public ModuleHoleESP(){super("HoleESP", "Hole ESP", "Draws an ESP on holes that you can get into.", ModuleCategory.RENDER);}

    public enum modes {
        Normal, Glow
    }

    ValueEnum mode = new ValueEnum("Mode", "Mode", "", modes.Normal);
    ValueNumber height = new ValueNumber("MainHeight", "MainHeight", "", 1.0D, -1.0D, 3.0D);
    ValueNumber outlineWidth = new ValueNumber("OutlineWidth", "OutlineWidth", "", 2.0, 0.5, 5.0);
    ValueBoolean doubles = new ValueBoolean("Doubles", "Doubles", "", true);
    ValueNumber range = new ValueNumber("Range", "Range", "", 8, 0, 10);
    ValueColor obsidian = new ValueColor("ObiColor", "ObiColor", "", new Color(255, 0, 0, 120));
    ValueColor bedrock = new ValueColor("BrockColor", "BrockColor", "", new Color(0, 255, 0, 120));
    ValueColor doubleColor = new ValueColor("DoubleColor", "DoubleColor", "", new Color(100, 0, 255, 120));
    ValueColor obsidianOL = new ValueColor("ObiOutlineColor", "ObiOutlineColor", "", new Color(255, 0, 0, 255));
    ValueColor bedrockOL = new ValueColor("BrockOutlineColor", "BrockOutlineColor", "", new Color(0, 255, 0, 255));
    ValueColor doubleOL = new ValueColor("DoubleOutlineColor", "DoubleOutlineColor", "", new Color(100, 0, 255, 255));

    private List<HoleInfo> hole = new ArrayList<>();

    public void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null)
            return;
        RenderUtils.camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        List<HoleInfo> currentHoles = new ArrayList<>();
        currentHoles.addAll(hole);
        currentHoles.forEach(holeInfo ->   renderHole(holeInfo.pos, holeInfo.type, holeInfo.length, holeInfo.width));
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null)
            return;

        ArrayList<HoleInfo> holes = new ArrayList();
        for (BlockPos potentialHole : BlockUtils.getNearbyBlocks(mc.player, range.getValue().intValue(), false)) {
            if (!(mc.world.getBlockState(potentialHole).getBlock() instanceof BlockAir)) continue;
            if (HoleUtils.isBedrockHole(potentialHole))
                holes.add(new HoleInfo(potentialHole, Type.Bedrock, 0, 0));
            else if (HoleUtils.isObiHole(potentialHole))
                holes.add(new HoleInfo(potentialHole, Type.Obsidian, 0, 0));

            if (doubles.getValue()) {
                if (HoleUtils.isDoubleBedrockHoleX(potentialHole.west()) || HoleUtils.isDoubleObsidianHoleX(potentialHole.west()))
                    holes.add(new HoleInfo(potentialHole.west(), Type.Double, 1, 0));
                else if (HoleUtils.isDoubleBedrockHoleZ(potentialHole.north()) || HoleUtils.isDoubleObsidianHoleZ(potentialHole.north()))
                    holes.add(new HoleInfo(potentialHole.north(), Type.Double, 0, 1));
            }
        }
        hole.clear();
        hole.addAll(holes);
    }

    public void renderHole(BlockPos hole, Type type, double length, double width) {
        AxisAlignedBB box = new AxisAlignedBB(hole.getX() - mc.getRenderManager().viewerPosX, hole.getY() - mc.getRenderManager().viewerPosY, hole.getZ() - mc.getRenderManager().viewerPosZ, hole.getX() + 1 - mc.getRenderManager().viewerPosX + length, hole.getY() + height.getValue().doubleValue() - mc.getRenderManager().viewerPosY, hole.getZ() + width + 1 - mc.getRenderManager().viewerPosZ);
        Color color = type == Type.Bedrock ? bedrockOL.getValue() : (type == Type.Obsidian ? obsidianOL.getValue() : doubleOL.getValue());
        if (mode.getValue().equals(modes.Normal)) {
            RenderUtils.drawBlockOutline(box, color, outlineWidth.getValue().floatValue());
            color = type == Type.Bedrock ? bedrock.getValue() : (type == Type.Obsidian ? obsidian.getValue() : doubleColor.getValue());
            RenderUtils.drawBoxBB(box.shrink(0.002f), color);

        }
        if (mode.getValue().equals(modes.Glow)) {
            RenderUtils.drawGradientBlockOutline(box, new Color(0, 0, 0, 0), color, outlineWidth.getValue().floatValue());
            color = type == Type.Bedrock ? bedrock.getValue() : (type == Type.Obsidian ? obsidian.getValue() : doubleColor.getValue());
            RenderUtils.drawOpenGradientBoxBB(box.shrink(0.005f), color, new Color(0, 0, 0, 0), false);
        }
    }

    public enum Type {
        Obsidian,
        Bedrock,
        Double
    }

    class HoleInfo {
        BlockPos pos;
        Type type;
        int length;
        int width;

        public HoleInfo(BlockPos pos, Type type, int length, int width) {
            this.pos = pos;
            this.type = type;
            this.length = length;
            this.width = width;
        }
    }
}
