package com.mawuote.client.modules.client;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueNumber;
import org.lwjgl.input.Keyboard;

public class ModuleGUI extends Module {
    public ModuleGUI(){
        super("GUI", "GUI", "The client's Click GUI.", ModuleCategory.CLIENT);
        setBind(Keyboard.KEY_RSHIFT);
        INSTANCE = this;
    }

    public static ModuleGUI INSTANCE;

    public static ValueBoolean customMenu = new ValueBoolean("CustomMainMenu", "CustomMainMenu", "", true);
    public ValueNumber scrollSpeed = new ValueNumber("ScrollSpeed", "ScrollSpeed", "The speed for scrolling through the GUI.", 10, 1, 20);

    public void onEnable(){
        mc.displayGuiScreen(Kaotik.CLICK_GUI);
    }
}
