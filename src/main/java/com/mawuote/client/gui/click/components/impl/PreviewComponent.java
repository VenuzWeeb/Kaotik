package com.mawuote.client.gui.click.components.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.value.impl.ValuePreview;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.client.gui.click.components.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class PreviewComponent extends Component {

    ValuePreview setting;
    public static EntityEnderCrystal entityEnderCrystal;
    public boolean open = false;

    public PreviewComponent(final ValuePreview setting, final ModuleComponent parent, final int offset) {
        super(parent.getParent().getX(), parent.getParent().getY() + offset, parent.getParent());
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        Entity entity = setting.getEntity();

        Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + (this.open ? 100 : 14), new Color(40, 40, 40).getRGB());
        Gui.drawRect(getX() - 1, getY(), getX(), getY() + + (this.open ? 100 : 14), new Color(30, 30, 30).getRGB());
        Gui.drawRect(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + + (this.open ? 100 : 14), new Color(30, 30, 30).getRGB());

        if(this.open) {
            if (entity instanceof EntityEnderCrystal) {
                EntityEnderCrystal ent = new EntityEnderCrystal(mc.world, 0, 0, 0);
                entityEnderCrystal = ent;

                ent.setShowBottom(false);
                ent.rotationYaw = 0;
                ent.rotationPitch = 0;
                ent.innerRotation = 0;
                ent.prevRotationYaw = 0;
                ent.prevRotationPitch = 0;

                if (ent != null) {
                    GL11.glScalef(1f, 1f, 1f);
                    RenderUtils.drawEntityOnScreen(ent, this.getX() + (this.getWidth() / 2), this.getY() + 90, 40, 0, 0);
                }
            }
        }

        Kaotik.FONT_MANAGER.drawString(setting.getName(), getX() + 3, getY() + 3, Color.WHITE);
        Kaotik.FONT_MANAGER.drawString(this.open ? "-" : "+", getX() + this.getWidth() - 3 - Kaotik.FONT_MANAGER.getStringWidth("+"), getY() + 3, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1 && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()) {
            this.open = !this.open;
        }
    }
}
