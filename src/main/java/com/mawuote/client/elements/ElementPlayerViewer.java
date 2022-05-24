package com.mawuote.client.elements;

import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;

public class ElementPlayerViewer extends Element {
    public ElementPlayerViewer() {
        super("PlayerViewer", "Player Viewer", "Renders yourself on the screen.");
    }

    public static ValueBoolean viewTarget = new ValueBoolean("ViewTarget", "ViewTarget", "Views the nearest player, and when there are no players it renders you.", false);
    public static ValueEnum lookMode = new ValueEnum("Look", "Look", "The mode for the player's looking.", LookModes.None);
    public static ValueNumber scale = new ValueNumber("Scale", "Scale", "The scale for the player.", 3, 1, 10);

    /*@Override
    public void onRender2D(EventRender2D event){
        frame.setWidth(scale.getValue().intValue() * 10);
        frame.setHeight(scale.getValue().intValue() * 20);

        ArrayList<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
        players.sort(Comparator.comparing(p -> mc.player.getDistance(p)));

        final ScaledResolution resolution = new ScaledResolution(mc);

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (lookMode.getValue().equals(LookModes.Free)){

        } else {
            GuiInventory.drawEntityOnScreen((int) (frame.getX() + 17), (int) (frame.getY() + 60), scale.getValue().intValue() * 10, lookMode.getValue().equals(LookModes.None) ? 0.0f : (frame.getX() - Mouse.getX()), lookMode.getValue().equals(LookModes.None) ? 0.0f : (-resolution.getScaledHeight() + Mouse.getY()), viewTarget.getValue() ? players.isEmpty() ? mc.player : players.get(0) : mc.player);
        }

        GlStateManager.popMatrix();
    }*/

    public enum LookModes { None, Free, Mouse }
}
