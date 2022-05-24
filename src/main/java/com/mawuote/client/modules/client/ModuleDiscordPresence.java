package com.mawuote.client.modules.client;

import                                                                                                                                                                                                                                                      com.mawuote.api.manager.misc.DiscordPresence;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;

public class ModuleDiscordPresence extends Module<B> {
    public ModuleDiscordPresence() {
        super("Discord Presence", "Discord Presence", "Makes your Discord profile have a Rich Presence.", ModuleCategory.CLIENT);
    }

    public void onEnable() {
        DiscordPresence.startRPC();
    }

    public void onDisable() {
        DiscordPresence.stopRPC();
    }
}
