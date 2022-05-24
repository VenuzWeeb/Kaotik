package com.mawuote.client.commands;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.command.Command;
import com.mawuote.api.manager.misc.ChatManager;

public class CommandFriend extends Command {
    public CommandFriend() {
        super("friend", "Adds friends using commands.", "friend <add|del> <name> | clear", "f", "friends");
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length == 1) {
            ChatManager.printChatNotifyClient("You have " + (Kaotik.FRIEND_MANAGER.getFriends().size() + 1) + " friends");
            return;
        } else {
            if (args[0].equalsIgnoreCase("add")) {
                if (Kaotik.FRIEND_MANAGER.isFriend(args[1])) {
                    ChatManager.printChatNotifyClient(args[1] + " is already a friend!");
                    return;
                }
                if (!Kaotik.FRIEND_MANAGER.isFriend(args[1])) {
                    Kaotik.FRIEND_MANAGER.addFriend(args[1]);
                    ChatManager.printChatNotifyClient("Added " + args[1] + " to friends list");
                }
            }
            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
                if (!Kaotik.FRIEND_MANAGER.isFriend(args[1])) {
                    ChatManager.printChatNotifyClient(args[1] + " is not a friend!");
                    return;
                }
                if (Kaotik.FRIEND_MANAGER.isFriend(args[1])) {
                    Kaotik.FRIEND_MANAGER.removeFriend(args[1]);
                    ChatManager.printChatNotifyClient("Removed " + args[1] + " from friends list");
                }
            }
        }
    }
}


