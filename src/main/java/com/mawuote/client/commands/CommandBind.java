package com.mawuote.client.commands;


import com.mawuote.Kaotik;
import com.mawuote.api.manager.command.Command;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import org.lwjgl.input.Keyboard;

public class CommandBind extends Command {
    public CommandBind() {
        super("bind", "Binds a module with commands.", "bind <name> <key> | clear", "key", "keybind", "b");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 2){
            boolean found = false;

            for (Module<B> module : Kaotik.getModuleManager().getModules()){
                if (module.getName().equalsIgnoreCase(args[0])){
                    module.setBind(Keyboard.getKeyIndex(args[1].toUpperCase()));
                    ChatManager.printChatNotifyClient(module.getName() + " bound to " + Keyboard.getKeyName(module.getBind()).toUpperCase());
                    found = true;
                    break;
                }
            }

            if (!found){
                ChatManager.printChatNotifyClient("Could not find module.");
            }
        } else if (args.length == 1){
            if (args[0].equalsIgnoreCase("clear")){
                for (Module<B> module : Kaotik.getModuleManager().getModules()){
                    module.setBind(Keyboard.KEY_NONE);
                }
                ChatManager.printChatNotifyClient("Successfully cleared all binds.");
            } else {
                ChatManager.printChatNotifyClient(getSyntax());
            }
        } else {
            ChatManager.printChatNotifyClient(getSyntax());
        }
    }
}