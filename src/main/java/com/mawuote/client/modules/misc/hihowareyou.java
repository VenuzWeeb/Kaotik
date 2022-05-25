package com.mawuote.client.modules.misc;

import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Random;

public class hihowareyou extends Module {
    public hihowareyou(){
        super("Welcomer", "Welcomer", "", ModuleCategory.MISC);

        joinMessages.add("Hello, ");
        joinMessages.add("Welcome to the server, ");
        joinMessages.add("Nice to see you, ");
        joinMessages.add("Hey how are you, ");

        leaveMessages.add("Goodbye, ");
        leaveMessages.add("See you later, ");
        leaveMessages.add("Bye bye, ");
        leaveMessages.add("I hope you had a good time, ");
    }

    public static ValueBoolean privateMsg = new ValueBoolean("Private", "Private", "", false);

    static ArrayList<NetworkPlayerInfo> playerMap;
    static int cachePlayerCount;
    boolean isOnServer;
    public static ArrayList<String> greets;
    public static ArrayList<String> goodbyes;

    public static ArrayList<String> joinMessages = new ArrayList<>();
    public static ArrayList<String> leaveMessages = new ArrayList<>();

    static {
        playerMap = new ArrayList<NetworkPlayerInfo>();
        greets = new ArrayList<String>();
        goodbyes = new ArrayList<String>();
    }

    public void onUpdate() {
        if (mc.player != null) {
            if (mc.player.ticksExisted % 10 == 0) {
                checkPlayers();
            } else if (mc.isSingleplayer()) {
                this.toggle();
            }
        }

    }

    public void onEnable() {
        if(mc.player == null || mc.world == null)
            return;
        onJoinServer();
    }

    private void checkPlayers() {
        final ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<NetworkPlayerInfo>(
                Minecraft.getMinecraft().getConnection().getPlayerInfoMap());
        final int currentPlayerCount = infoMap.size();
        if (currentPlayerCount != cachePlayerCount) {
            final ArrayList<NetworkPlayerInfo> currentInfoMap = (ArrayList<NetworkPlayerInfo>) infoMap.clone();
            currentInfoMap.removeAll(playerMap);
            if (currentInfoMap.size() > 5) {
                cachePlayerCount = playerMap.size();
                this.onJoinServer();
                return;
            }
            final ArrayList<NetworkPlayerInfo> playerMapClone = (ArrayList<NetworkPlayerInfo>) playerMap.clone();
            playerMapClone.removeAll(infoMap);
            for (final NetworkPlayerInfo npi : currentInfoMap) {
                this.playerJoined(npi);
            }
            for (final NetworkPlayerInfo npi : playerMapClone) {
                this.playerLeft(npi);
            }
            cachePlayerCount = playerMap.size();
            this.onJoinServer();
        }
    }

    private void onJoinServer() {
        playerMap = new ArrayList<NetworkPlayerInfo>(Minecraft.getMinecraft().getConnection().getPlayerInfoMap());
        cachePlayerCount = playerMap.size();
        this.isOnServer = true;
    }

    protected void playerJoined(final NetworkPlayerInfo playerInfo) {
        Random random = new Random();
        if(!joinMessages.isEmpty()) {
            if (privateMsg.getValue()) {
                ChatManager.printChatNotifyClient(joinMessages.get(random.nextInt(joinMessages.size())) + playerInfo.getGameProfile().getName());
            } else {
                mc.player.sendChatMessage(joinMessages.get(random.nextInt(joinMessages.size())) + playerInfo.getGameProfile().getName());

            }
        }
    }

    protected void playerLeft(final NetworkPlayerInfo playerInfo) {
        Random random = new Random();
        if(!leaveMessages.isEmpty()) {
            if (privateMsg.getValue()) {
                ChatManager.printChatNotifyClient(leaveMessages.get(random.nextInt(leaveMessages.size())) + playerInfo.getGameProfile().getName());
            } else {
                mc.player.sendChatMessage(leaveMessages.get(random.nextInt(leaveMessages.size())) + playerInfo.getGameProfile().getName());
            }
        }
    }

}
