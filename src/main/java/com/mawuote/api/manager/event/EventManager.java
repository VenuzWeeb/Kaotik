package com.mawuote.api.manager.event;

import com.mawuote.api.manager.command.CommandManager;
import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.network.EventPlayerJoin;
import com.mawuote.api.manager.event.impl.network.EventPlayerLeave;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * @author SrRina
 * @since 08/10/2020 at 21:28
 **/
public class EventManager {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private int colorRGBEffectRed;
    private int colorRGBEffectGreen;
    private int colorRGBEffectBlue;

    CommandManager commandManager = new CommandManager();

    public EventManager(){
         MinecraftForge.EVENT_BUS.register(this);
     }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) return;
        float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};

        int colorInterpolated = Color.HSBtoRGB(tick_color[0], 1, 1);

        this.colorRGBEffectRed   = ((colorInterpolated >> 16) & 0xFF);
        this.colorRGBEffectGreen = ((colorInterpolated >> 8) & 0xFF);
        this.colorRGBEffectBlue  = ((colorInterpolated) & 0xFF);
    }

    @SubscribeEvent
    public void onReceive(EventPacket.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem) event.getPacket();
            if (packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() != mc.session.getProfile().getId()) {
                        new Thread(() -> {
                            UUID id = playerData.getProfile().getId();
                            String name = resolveName(playerData.getProfile().getId().toString());
                            if (name != null) {
                                if (mc.player != null && mc.player.ticksExisted >= 1000)
                                    MinecraftForge.EVENT_BUS.post(new EventPlayerJoin(name, id));
                            }
                        }).start();
                    }
                }
            }
            if (packet.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() != mc.session.getProfile().getId()) {
                        new Thread(() -> {
                            UUID id = playerData.getProfile().getId();
                            EntityPlayer entity = mc.world.getPlayerEntityByUUID(id);
                            final String name = resolveName(playerData.getProfile().getId().toString());
                            if (name != null) {
                                if (mc.player != null && mc.player.ticksExisted >= 1000)
                                    MinecraftForge.EVENT_BUS.post(new EventPlayerLeave(name, id, entity));
                            }
                        }).start();
                    }
                }
            }
        }
    }

    private final Map<String, String> uuidNameCache = Maps.newConcurrentMap();

    public String resolveName(String uuid) {
        uuid = uuid.replace("-", "");
        if (uuidNameCache.containsKey(uuid)) {
            return uuidNameCache.get(uuid);
        }

        final String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
        try {
            final String nameJson = IOUtils.toString(new URL(url));
            if (nameJson != null && nameJson.length() > 0) {
                final JSONArray jsonArray = (JSONArray) JSONValue.parseWithException(nameJson);
                if (jsonArray != null) {
                    final JSONObject latestName = (JSONObject) jsonArray.get(jsonArray.size() - 1);
                    if (latestName != null) {
                        return latestName.get("name").toString();
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int[] getRGB() {
        return new int[] {
                this.colorRGBEffectRed, this.colorRGBEffectGreen, this.colorRGBEffectBlue
        };
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d(
                (entity.posX - entity.lastTickPosX) * x,
                (entity.posY - entity.lastTickPosY) * y,
                (entity.posZ - entity.lastTickPosZ) * z
        );
    }
}