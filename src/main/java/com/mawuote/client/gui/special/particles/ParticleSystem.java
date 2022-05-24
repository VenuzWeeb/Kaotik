package com.mawuote.client.gui.special.particles;

import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.client.modules.client.ModuleParticles;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glEnable;

public class ParticleSystem {
    public static float SPEED = 0.1f;
    private List<Particle> particleList = new ArrayList<>();
    public int dist;

    public ParticleSystem(int initAmount, int dist) {
        addParticles(initAmount);
        this.dist = dist;
    }

    public void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            particleList.add(Particle.generateParticle());
        }
    }

    public void changeParticles(int amount) {
        particleList.clear();
        for (int i = 0; i < amount; i++) {
            particleList.add(Particle.generateParticle());
        }
    }

    public void tick(int delta) {
        //if (Mouse.isButtonDown(0)) addParticles(1);
        for (Particle particle : particleList) {
            particle.tick(delta, SPEED);
        }
    }

    public void render() {
        for (Particle particle : particleList) {

            Color color = new Color(ModuleParticles.daColor.getValue().getRed(), ModuleParticles.daColor.getValue().getGreen(), ModuleParticles.daColor.getValue().getBlue(), 255);

            for (Particle particle1 : particleList) {
                float distance = particle.getDistanceTo(particle1);
                if(particle.getDistanceTo(particle1) < dist) {
                    float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - distance / dist));
                    RenderUtils.prepareGL();
                    glEnable(GL_LINE_SMOOTH);
                    GL11.glDisable((int)3553);
                    GL11.glLineWidth(ModuleParticles.lineWidth.getValue().floatValue());
                    GL11.glBegin((int)1);

                    GL11.glColor4f(ModuleParticles.daColor.getValue().getRed() / 255.0f, ModuleParticles.daColor.getValue().getGreen() / 255.0f, ModuleParticles.daColor.getValue().getBlue() / 255.0f, alpha);

                    GL11.glVertex2d(particle.getX(), particle.getY());
                    GL11.glVertex2d(particle1.getX(), particle1.getY());

                    GL11.glEnd();
                    GL11.glEnable((int)3553);
                    RenderUtils.releaseGL();
                }
            }

            RenderUtils.drawCircle(particle.getX(), particle.getY(), particle.getSize(), color.getRGB());
        }
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

}
