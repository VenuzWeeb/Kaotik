package com.mawuote.client.commands;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.command.Command;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.value.Value;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mojang.realmsclient.gui.ChatFormatting;

public class CommandValue extends Command {
    public CommandValue() {
        super("value", "Let's you change module values with commands.", "value <module> <setting> <value>", "val", "v", "set");
    }

    @Override
    public void onCommand(String[] args) {
        Module m = Kaotik.getModuleManager().getModule(args[0]);

        if(m != null && args.length == 1) {
            ChatManager.printChatNotifyClient("Showing settings for " + args[0] + ": " + ChatFormatting.GRAY + "Placeholder");
        }

        if(m == null) {
            ChatManager.printChatNotifyClient("Unknown module.");
            return;
        } else if(m != null) {
            if (args[1] != null) {
                Value v = m.getValue(args[1]);
                if (v == null) {
                    ChatManager.printChatNotifyClient("Unknown setting.");
                    return;
                } else if (v != null) {
                    if (v instanceof ValueBoolean) {
                        if (args[2] == null) {
                            ChatManager.printChatNotifyClient("Please give a value.");
                            return;
                        } else if (args[2] != null) {
                            ((ValueBoolean) v).setValue(Boolean.parseBoolean(args[2]));
                            ChatManager.printChatNotifyClient(args[1] + " from " + args[0] + " has been set to " + args[2]);
                        }
                    } else if (v instanceof ValueNumber) {
                        if (args[2] == null) {
                            ChatManager.printChatNotifyClient("Please give a value.");
                            return;
                        } else if (args[2] != null) {
                            if (((ValueNumber) v).getType() == ValueNumber.FLOAT) {
                                ((ValueNumber) v).setValue(Float.parseFloat(args[2]));
                                ChatManager.printChatNotifyClient(args[1] + " from " + args[0] + " has been set to " + args[2]);
                            } else if (((ValueNumber) v).getType() == ValueNumber.DOUBLE) {
                                ((ValueNumber) v).setValue(Double.parseDouble(args[2]));
                                ChatManager.printChatNotifyClient(args[1] + " from " + args[0] + " has been set to " + args[2]);
                            } else if (((ValueNumber) v).getType() == ValueNumber.INTEGER) {
                                ((ValueNumber) v).setValue(Integer.parseInt(args[2]));
                                ChatManager.printChatNotifyClient(args[1] + " from " + args[0] + " has been set to " + args[2]);
                            }
                        }
                    } else if (v instanceof ValueEnum) {
                        if (args[2] == null) {
                            ChatManager.printChatNotifyClient("Please give a value.");
                            return;
                        } else if (args[2] != null) {
                            for (Enum enumValue : ((ValueEnum) v).getValues()) {
                                enumValue = ((ValueEnum) v).getEnumByName(args[2]);
                                if (enumValue != null) {
                                    ((ValueEnum) v).setValue(enumValue);
                                    ChatManager.printChatNotifyClient(args[1] + " from " + args[0] + " has been set to " + args[2]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
