package com.mawuote.client.modules.misc;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatMod extends Module<B> {

    public ChatMod() {
        super("ChatMod", "ChatMod", "Let's you customize your chat.", ModuleCategory.MISC);
    }

    public enum modes {
        mawuote, kaotik;
    }

    public static ValueBoolean background = new ValueBoolean("NoBackground","background","",false);
    public static ValueBoolean chatSuffix = new ValueBoolean("ChatSuffix", "ChatSuffix", "", false);
    public static ValueEnum mode = new ValueEnum("Mode","Mode","Mode", modes.kaotik);
    public static ValueBoolean greenText = new ValueBoolean("GreenText", "GreenText", "", false);

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        if(chatSuffix.getValue()) {
            String kaotik = " \u23d0" + " \u03BA\u039B\u0398\u03A4\u03AA\u03BA";
            String mawuote = " \u23d0" + " mawuouotehack";

            if (event.getMessage().startsWith(".")) return;
            if (event.getMessage().startsWith(",")) return;
            if (event.getMessage().startsWith("/")) return;
            if (event.getMessage().startsWith("*")) return;

            switch ((modes) mode.getValue()) {
                case kaotik: {
                    event.setMessage(event.getMessage() + kaotik);
                    break;
                }
                case mawuote: {
                    event.setMessage(event.getMessage() + mawuote);
                    break;
                }
            }
        }
        if(greenText.getValue()) {
            String greeeeen = "> ";

            if (event.getMessage().startsWith(".")) return;
            if (event.getMessage().startsWith(",")) return;
            if (event.getMessage().startsWith("/")) return;
            if (event.getMessage().startsWith("*")) return;

            event.setMessage(greeeeen + event.getMessage());
        }
    }
}
