package com.mawuote.api.manager.module;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.value.Value;
import com.mawuote.api.manager.value.impl.ValueBind;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.client.modules.client.ModuleColor;
import com.mawuote.client.modules.client.ModuleNotifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class Module<B> {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Value> values;

    private final String name;
    private final String description;
    private final ModuleCategory category;

    private boolean toggled;
    private final boolean persistent;

    public final ValueString tag = new ValueString("Tag", "Tag", "Let's you customize the module's name.", "Placeholder");
    public final ValueBoolean chatNotify = new ValueBoolean("ChatNotify", "ChatNotify", "Sends a message when the module is toggled.", true);
    public final ValueBoolean drawn = new ValueBoolean("Drawn", "Drawn", "Puts the module on the array list hud component.", true);
    public final ValueBind bind = new ValueBind("Bind", "Bind", "The bind for the module.", Keyboard.KEY_NONE);

    public Module(final String name, final double tag, final double description, final double category){
        this.name = name;
        this.tag.setValue(tag);
        this.description = description;
        this.category = category;
        this.persistent = false;
        this.values = new ArrayList<>();
    }

    public Module(final String name, final String tag, final String description, final ModuleCategory category, final boolean persistent){
        this.name = name;
        this.tag.setValue(tag);
        this.description = description;
        this.category = category;
        this.persistent = persistent;
        this.values = new ArrayList<>();
        persist();
    }

    public void onUpdate() {}
    public void onMotionUpdate() {}
    public void onRender2D(EventRender2D event) {}
    public void onRender3D(EventRender3D event) {}
    public void onEnable() {}
    public void onDisable() {}
    public void onLogin() {}
    public void onLogout() {}
    public void onDeath() {}

    public static
    boolean fullNullCheck ( ) {
        return Module.mc.player == null || Module.mc.world == null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public String getTag() {
        return tag.getValue();
    }

    public void setTag(String tag) {
        this.tag.setValue(tag);
    }

    public boolean isChatNotify() {
        return chatNotify.getValue();
    }

    public void setChatNotify(boolean chatNotify) {
        this.chatNotify.setValue(chatNotify);
    }

    public boolean isDrawn() {
        return drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public int getBind(){
        return bind.getValue();
    }

    public void setBind(int bind){
        this.bind.setValue(bind);
    }

    public String getHudInfo(){
        return "";
    }

    public void persist(){
        if (persistent) {
            setToggled(true);
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void toggle(){
        if (toggled){
            disable();
        } else {
            enable();
        }
    }

    public void enable(){
        if (!persistent){
            setToggled(true);
            onEnable();

            int moduleNumber = 0;
            for (char character : this.name.toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }

            if (isChatNotify()) {
                if (ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Rainbow) || ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Gradient)) {
                    ChatManager.sendClientMessage(this.name + "\u00a7r" + ChatFormatting.GREEN + ChatFormatting.BOLD + " Enabled!", moduleNumber);
                } else {
                    ChatManager.sendClientMessage(this.name + ChatFormatting.GREEN + ChatFormatting.BOLD + " Enabled!", moduleNumber);
                }
            }
            if(Kaotik.getModuleManager().isModuleEnabled("Notifications") && ModuleNotifications.chatNotify.getValue() && isChatNotify()) {
                Kaotik.NOTIFICATION_PROCESSOR.addNotification(this.name + ChatFormatting.GREEN + ChatFormatting.BOLD + " Enabled!", ModuleNotifications.lifetime.getValue().intValue(), ModuleNotifications.inOutTime.getValue().intValue());
            }

            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable(){
        if (!persistent){
            setToggled(false);
            onDisable();

            int moduleNumber = 0;
            for (char character : this.name.toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }

            if (isChatNotify()) {
                if (ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Rainbow) || ModuleColor.prefixMode.getValue().equals(ModuleColor.prefixModes.Gradient)) {
                    ChatManager.sendClientMessage(this.name + "\u00a7r" + ChatFormatting.RED + ChatFormatting.BOLD + " Disabled!", moduleNumber);
                } else {
                    ChatManager.sendClientMessage(this.name + ChatFormatting.RED + ChatFormatting.BOLD + " Disabled!", moduleNumber);
                }
            }
            if(Kaotik.getModuleManager().isModuleEnabled("Notifications") && ModuleNotifications.chatNotify.getValue() && isChatNotify()) {
                Kaotik.NOTIFICATION_PROCESSOR.addNotification(this.name + ChatFormatting.RED + ChatFormatting.BOLD + " Disabled!", ModuleNotifications.lifetime.getValue().intValue(), ModuleNotifications.inOutTime.getValue().intValue());
            }

            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    public Value getValue() {
        for (Value value : values) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }

    public void addValue(Value value){
        values.add(value);
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public static Color globalColor(int alpha) {
        return new Color(ModuleColor.daColor.getValue().getRed(), ModuleColor.daColor.getValue().getGreen(), ModuleColor.daColor.getValue().getBlue(), alpha);
    }

    protected Module<Integer> register(Module<Double> antiFactor) {
        return null;
    }
}
