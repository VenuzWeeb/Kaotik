package com.mawuote.client.modules.client;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;

public class ModuleHUDEditor extends Module<B> {
    public ModuleHUDEditor() {
        super("HUDEditor", "HUD Editor", "The client's HUD Editor.", ModuleCategory.CLIENT);
        INSTANCE = this;
    }

    public static ModuleHUDEditor INSTANCE;

    @Override
    public void onEnable(){
        super.onEnable();
        if (mc.player == null || mc.world == null){
            disable();
            return;
        }

        mc.displayGuiScreen(Kaotik.HUD_EDITOR);
    }
}