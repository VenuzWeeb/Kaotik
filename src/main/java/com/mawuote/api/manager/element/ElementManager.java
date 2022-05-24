package com.mawuote.api.manager.element;

import com.mawuote.api.manager.event.impl.player.EventMotionUpdate;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.value.Value;
import com.mawuote.client.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

public class ElementManager {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Element> elements;

    public ElementManager(){
        MinecraftForge.EVENT_BUS.register(this);
        this.elements = new ArrayList<>();

        addElement(new ElementWatermark());
        addElement(new ElementWelcomer());
        addElement(new ElementCoordinates());
        addElement(new ElementStickyNotes());
        addElement(new ElementFriends());
        addElement(new ElementArmor());
        addElement(new ElementPlayerViewer());

        elements.sort(Comparator.comparing(Element::getName));
    }

    public void addElement(Element element){
        try {
            for (Field field : element.getClass().getDeclaredFields()){
                if (Value.class.isAssignableFrom(field.getType())){
                    if (!field.isAccessible()) field.setAccessible(true);
                    element.addValue((Value) field.get(element));
                }
            }

            elements.add(element);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Element> getElements(){
        return elements;
    }

    public Element getElement(String name) {
        for (Element module : elements) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }

        return null;
    }

    public boolean isElementEnabled(String name){
        Element module = elements.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
        if (module != null){
            return module.isToggled();
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (mc.player != null && mc.world != null){
            elements.stream().filter(Element::isToggled).forEach(Element::onUpdate);
        }
    }

    @SubscribeEvent
    public void onUpdate(EventMotionUpdate event){
        if (mc.player != null && mc.world != null){
            elements.stream().filter(Element::isToggled).forEach(Element::onMotionUpdate);
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Post event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            elements.stream().filter(Element::isToggled).forEach(m -> m.onRender2D(new EventRender2D(event.getPartialTicks())));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @SubscribeEvent
    public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event){
        elements.stream().filter(Element::isToggled).forEach(Element::onLogin);
    }

    @SubscribeEvent
    public void onLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        elements.stream().filter(Element::isToggled).forEach(Element::onLogout);
    }
}
