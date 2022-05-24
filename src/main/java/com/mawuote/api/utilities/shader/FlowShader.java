package com.mawuote.api.utilities.shader;

import com.mawuote.api.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class FlowShader extends FramebufferShader
{
    public static final FlowShader FLOW_SHADER;

    private float time;

    public FlowShader() {
        super("flow.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f(getUniform("resolution"), new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(),  new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight());
        GL20.glUniform1f(getUniform("time"), 1);
        this.time += 0.003f * (float) RenderUtils.deltaTime;
    }

    static {
        FLOW_SHADER = new FlowShader();
    }
}
