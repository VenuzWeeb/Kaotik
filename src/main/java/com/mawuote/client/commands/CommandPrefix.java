package com.mawuote.client.commands;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.command.Command;
import com.mawuote.api.manager.misc.ChatManager;

public class CommandPrefix extends Command {
    public CommandPrefix() {
        super("prefix", "Let's you change the prefix using commands.", "prefix <input>", "cmdprefix", "commandprefix", "cmdp", "commandp");
    }

    @Override
    public void onCommand(String[] args) {
        Kaotik.COMMAND_MANAGER.setPrefix(args[0]);
        ChatManager.printChatNotifyClient("Prefix has been set to " + args[0]);
    }
}
