package com.mawuote.api.manager.element;

import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.Value;
import com.mawuote.client.gui.hud.ElementFrame;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class Element extends Module<B> {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Value> values;

    public ElementFrame frame;

    public Element(final String name, final String description){
        super(name, name, description, ModuleCategory.HUD);
        this.values = new ArrayList<>();
    }

    public Element(final String name, final String tag, final String description){
        super(name, tag, description, ModuleCategory.HUD);
        this.values = new ArrayList<>();
    }

    @Override public void onUpdate() {}
    @Override public void onMotionUpdate() {}
    @Override public void onRender2D(EventRender2D event) {}
    @Override public void onEnable() {}
    @Override public void onDisable() {}
    @Override public void onLogin() {}
    @Override public void onLogout() {}
    @Override public void onDeath() {}

    public void setFrame(final ElementFrame frame){
        this.frame = frame;
    }

    @Override
    public String getHudInfo(){
        return "";
    }

    @Override
    public void toggle(){
        if (isToggled()){
            disable();
        } else {
            enable();
        }
    }

    @Override
    public void enable(){
        setToggled(true);
        onEnable();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void disable(){
        setToggled(false);
        onDisable();

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void addValue(Value value){
        values.add(value);
    }

    @Override
    public ArrayList<Value> getValues() {
        return values;
    }
}
