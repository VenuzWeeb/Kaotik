package com.mawuote.api.manager.command;

import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.client.commands.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    private String prefix = ";";
    private ArrayList<Command> commands;

    public CommandManager(){
        MinecraftForge.EVENT_BUS.register(this);
        commands = new ArrayList<>();

        register(new CommandBind());
        register(new CommandFriend());
        register(new CommandPrefix());
        register(new CommandValue());
    }

    @SubscribeEvent
    public void onChatSent(ClientChatEvent event){
        String message = event.getMessage();

        if (message.startsWith(getPrefix())){
            event.setCanceled(true);
            message = message.substring(getPrefix().length());

            if (message.split(" ").length > 0){
                String name = message.split(" ")[0];
                boolean found = false;

                for (Command command : getCommands()){
                    if (command.getAliases().contains(name.toLowerCase()) || command.getName().equalsIgnoreCase(name)){
                        mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                        command.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length));
                        found = true;
                        break;
                    }
                }

                if (!found){
                    ChatManager.printChatNotifyClient("Command could not be found.");
                }
            }
        }
    }

    public void register(Command command){
        commands.add(command);
    }

    public ArrayList<Command> getCommands(){
        return commands;
    }

    public String getPrefix(){
        return prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
}

