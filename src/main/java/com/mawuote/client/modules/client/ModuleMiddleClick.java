package com.mawuote.client.modules.client;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.utilities.entity.InventoryUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

//TODO
public class ModuleMiddleClick extends Module {
    public ModuleMiddleClick(){super("MiddleClick", "Middle Click", "", ModuleCategory.CLIENT);}

    public enum modes {
        MCF, XP, Pearl;
    }

    ValueEnum mode = new ValueEnum("Mode", "Mode", "", modes.XP);

    int oldSlot = -1;

    public void onMotionUpdate() {
        oldSlot = mc.player.inventory.currentItem;
        int pearlSlot = InventoryUtils.getHotbarItemSlot(Items.ENDER_PEARL);
        if(mode.getValue().equals(modes.XP)) {
            if (Mouse.isButtonDown(2)) {
                if (hotbarXP() != -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(hotbarXP()));
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                }
            }
        } else if(mode.getValue().equals(modes.Pearl)) {
            if (Mouse.isButtonDown(2)) {
                if (pearlSlot != -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(pearlSlot));
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                }
            }
        }
    }

    private int delay = 0;

    @Override
    public void onUpdate() {
        if(mode.getValue().equals(modes.MCF)) {
            delay++;
            RayTraceResult object = mc.objectMouseOver;
            if (object == null) {
                return;
            }
            if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
                Entity entity = object.entityHit;
                if (entity instanceof EntityPlayer && !(entity instanceof EntityArmorStand) && !mc.player.isDead && mc.player.canEntityBeSeen(entity)) {
                    EntityPlayer player = (EntityPlayer) entity;
                    String ID = entity.getName();

                    if (Mouse.isButtonDown(2) && mc.currentScreen == null && !Kaotik.FRIEND_MANAGER.isFriend(ID) && delay > 10) {
                        Kaotik.FRIEND_MANAGER.addFriend(ID);
                        ChatManager.printChatNotifyClient("" + ChatFormatting.GREEN + ChatFormatting.BOLD + "Added " + ChatFormatting.RESET + ChatFormatting.WHITE + ID + " as friend");
                        delay = 0;
                    }
                    if (Mouse.isButtonDown(2) && mc.currentScreen == null && Kaotik.FRIEND_MANAGER.isFriend(ID) && delay > 10) {
                        Kaotik.FRIEND_MANAGER.removeFriend(ID);
                        ChatManager.printChatNotifyClient("" + ChatFormatting.RED + ChatFormatting.BOLD + "Removed " + ChatFormatting.RESET + ChatFormatting.WHITE + ID + " as friend");
                        delay = 0;
                    }
                }
            }
        }
    }

    private int hotbarXP() {
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() != Items.EXPERIENCE_BOTTLE) continue;
            return i;
        }
        return -1;
    }
}
