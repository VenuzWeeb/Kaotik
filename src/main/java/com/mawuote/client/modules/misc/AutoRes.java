package com.mawuote.client.modules.misc;

import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRes extends Module {

    public AutoRes() {
        super("AutoRespawn", "Auto Respawn", "Automatically respawns you when you die.", ModuleCategory.MISC);
    }

    public static ValueBoolean showCoords = new ValueBoolean("ShowDeathCoords","showdeathcoords","shows the coords at which you died",false);

    @Override
    public void onUpdate() {
        if(mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
        if (showCoords.getValue() && (mc.currentScreen instanceof GuiGameOver)) {
            ChatManager.printChatNotifyClient(String.format("You died at x %d y %d z %d", (int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ));
        }
    }
}
