package com.mawuote.client.modules.client;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueString;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;


//TODO
public class ModuleStreamerMode extends Module<B> {
    public ModuleStreamerMode(){super("StreamerMode", "Streamer Mode", "", ModuleCategory.CLIENT);}

    public static ValueBoolean hideYou = new ValueBoolean("HideIGN", "HideIGN", "", false);
    public static ValueString yourName = new ValueString("YourName", "YourName", "", "You");
    public static ValueBoolean hideName = new ValueBoolean("HideOthers", "HideOthers", "", false);
    public static ValueString otherName = new ValueString("OthersName", "OthersName", "", "Enemy");
    public static ValueBoolean hideF3 = new ValueBoolean("HideF3", "HideF3", "", false);

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat)event.getPacket();
            if (packet.getType() != ChatType.GAME_INFO && this.getChatNames(packet.getChatComponent().getFormattedText(), packet.getChatComponent().getUnformattedText())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean getChatNames(String message, final String unformatted) {
        String out = message;
        if (hideName.getValue()) {
            if (mc.player == null) {
                return false;
            }
            for (Object o : mc.world.playerEntities) {
                if (o instanceof EntityPlayer && o != mc.player) {
                    EntityPlayer ent = (EntityPlayer) o;
                    if(!Kaotik.FRIEND_MANAGER.isFriend(ent.getName())) {
                        if (out.contains(ent.getName())) {
                            out = out.replaceAll(ent.getName(), otherName.getValue());
                        }
                    } else if (Kaotik.FRIEND_MANAGER.isFriend(ent.getName())) {
                        if (out.contains(ent.getName())) {
                            out = out.replaceAll(ent.getName(), "Friend");
                        }
                    }
                }
            }
        }
        if(hideYou.getValue()) {
            if (mc.player == null) {
                return false;
            }
            out = out.replace(mc.player.getName(), yourName.getValue());
        }
        ChatManager.sendRawMessage(out);
        return true;
    }

    @SubscribeEvent
    public void renderOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (FMLClientHandler.instance().getClient().player.capabilities.isCreativeMode)
            return;
        if(!hideF3.getValue())
            return;
        Iterator<String> it = event.getLeft().listIterator();
        while (it.hasNext()) {
            String value = it.next();
            if (value != null && (value.startsWith("XYZ:")) || value.startsWith("Looking at:") || value.startsWith("Block:") || value.startsWith("Facing:"))
                it.remove();
        }
    }

    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        String dname = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team) networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        dname = dname.replace(mc.player.getName(), yourName.getValue());
        return dname;
    }
}
