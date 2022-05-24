package com.mawuote.client.modules.misc;

import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.player.EventPlayerDestroyBlock;
import com.mawuote.api.manager.event.impl.player.EventPlayerJump;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Announcer extends Module<B> {

    public Announcer(){
        super("Announcer", "Announcer", "Automatically sends messages in chat saying something that you have done.", ModuleCategory.MISC);
    }

    public static String walkMessage;
    public static String placeMessage;
    public static String jumpMessage;
    public static String breakMessage;
    public static String attackMessage;
    public static String eatMessage;
    public static String guiMessage;

    public static int blockBrokeDelay = 0;
    static int blockPlacedDelay = 0;
    static int jumpDelay = 0;
    static int attackDelay = 0;
    static int eattingDelay = 0;

    static long lastPositionUpdate;
    static double lastPositionX;
    static double lastPositionY;
    static double lastPositionZ;
    private static double speed;
    String heldItem = "";

    int blocksPlaced = 0;
    int blocksBroken = 0;
    int eaten = 0;

    public enum modes {
        English, Hebrew, German, Veneco, Test, Dutch, Portuguese;
    }

    ValueEnum mode = new ValueEnum("Mode", "Mode", "", modes.English);
    ValueBoolean clientSide = new ValueBoolean("Private", "Private", "", true);
    ValueBoolean walk = new ValueBoolean("Walk", "Walk", "", true);
    ValueBoolean place = new ValueBoolean("Place", "Place", "", true);
    ValueBoolean jump = new ValueBoolean("Jump", "Jump", "", true);
    ValueBoolean breaking = new ValueBoolean("Breaking", "Breaking", "", true);
    ValueBoolean attack = new ValueBoolean("Attack", "Attack", "", true);
    ValueBoolean eat = new ValueBoolean("Eat", "Eat", "", true);
    ValueNumber delay = new ValueNumber("Delay", "Delay", "", 5, 1, 20);

    public void onUpdate() {

        blockBrokeDelay++;
        blockPlacedDelay++;
        jumpDelay++;
        attackDelay++;
        eattingDelay++;
        heldItem = mc.player.getHeldItemMainhand().getDisplayName();

        switch((modes)mode.getValue()) {
            case English: {
                walkMessage = "I just walked {blocks} meters thanks to Kaotik! - https://discord.gg/svwWRhbgub";
                placeMessage = "I just placed {amount} {name} thanks to Kaotik! - https://discord.gg/svwWRhbgub";
                jumpMessage = "I just jumped thanks to Kaotik! - https://discord.gg/svwWRhbgub";
                breakMessage = "I just mined {amount} {name} thanks to Kaotik! - https://discord.gg/svwWRhbgub";
                attackMessage = "I just attacked {name} with a {item} thanks to Kaotik! - https://discord.gg/svwWRhbgub";
                eatMessage = "I just ate {amount} {name} thanks to Kaotik! - https://discord.gg/svwWRhbgub";
                guiMessage = "I just opened my advanced GUI thanks to Kaotik! - https://discord.gg/svwWRhbgub";
                break;
            }
            case Hebrew: {
                walkMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d4\u05dc\u05db\u05ea\u05d9 {blocks} \u05de\u05d8\u05e8\u05d9\u05dd \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc" + "Kaotik! - https://discord.gg/svwWRhbgub";
                placeMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e9\u05de\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc" + "Kaotik! - https://discord.gg/svwWRhbgub";
                jumpMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e7\u05e4\u05e6\u05ea\u05d9 \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc" + "Kaotik! - https://discord.gg/svwWRhbgub";
                breakMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e9\u05d1\u05e8\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc" + "Kaotik! - https://discord.gg/svwWRhbgub";
                attackMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d4\u05db\u05d9\u05ea\u05d9 \u05d0\u05ea {name} \u05e2\u05dd {item} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc" + "Kaotik! - https://discord.gg/svwWRhbgub";
                eatMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d0\u05db\u05dc\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc" + "Europa" + "\u0021";
                guiMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e4\u05ea\u05d7\u05ea\u05d9 \u05d0\u05ea \u05d4 \u0047\u0055\u0049 \u05d4\u05de\u05ea\u05e7\u05d3\u05dd \u05e9\u05dc\u05d9 \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc" + "Kaotik! - https://discord.gg/svwWRhbgub";
                break;
            }
            case German: {
                walkMessage = "Ich bin grade {blocks} meter gelaufen dank Kaotik! - https://discord.gg/svwWRhbgub";
                placeMessage = "Ich habe grade {amount}{name} plaziert dank Kaotik! - https://discord.gg/svwWRhbgub";
                jumpMessage = "Ich bin grade gesprungen dank Kaotik! - https://discord.gg/svwWRhbgub";
                breakMessage = "Ich habe grade {amount}{name} gemined dank Kaotik! - https://discord.gg/svwWRhbgub!";
                attackMessage = "Ich habe grade {name} mit einem {item} attackiert dank Kaotik! - https://discord.gg/svwWRhbgub";
                eatMessage = "Ich habe grade {amount} {name} gegessen dank Kaotik! - https://discord.gg/svwWRhbgub";
                guiMessage = "Ich habe grade mein erweitertes GUI ge\u00f6ffnet dank Kaotik! - https://discord.gg/svwWRhbgub";
                break;
            }
            case Veneco: {
                walkMessage = "mardito me acabo de move {blocks} metros gracias a Kaotik! - https://discord.gg/svwWRhbgub";
                placeMessage = "acabo de pone {amount} {name} gracias a Kaotik! - https://discord.gg/svwWRhbgub";
                jumpMessage = "acabo de brinca gracias a Kaotik! - https://discord.gg/svwWRhbgub";
                breakMessage = "acabo de rompe {amount} {name} gracias a Kaotik! - https://discord.gg/svwWRhbgub";
                attackMessage = "acabo de pegale a {name} con {item} gracias a Kaotik! - https://discord.gg/svwWRhbgub";
                eatMessage = "me acabo de come {amount} de {name} gracias a Kaotik! - https://discord.gg/svwWRhbgub";
                guiMessage = "Acabo de abrir mi GUI avanzado gracias a Kaotik! - https://discord.gg/svwWRhbgub";
                break;
            }
            case Test: {
                walkMessage = "";
                placeMessage = "";
                jumpMessage = "";
                breakMessage = "";
                attackMessage = "";
                eatMessage = "";
                guiMessage = "";
                break;
            }
            case Dutch: {
                walkMessage = "Ik heb net {blocks} gelopen door Kaotik! - https://discord.gg/svwWRhbgub";
                placeMessage = "Ik heb net {amount} {name} geplaatst door Kaotik! - https://discord.gg/svwWRhbgub";
                jumpMessage = "Ik sprong door Kaotik! - https://discord.gg/svwWRhbgub";
                breakMessage = "Ik heb {amount} {name} gebroken door Kaotik! - https://discord.gg/svwWRhbgub";
                attackMessage = "Ik heb zojuist {naam} aangevallen met een {item} dankzij Kaotik! - https://discord.gg/svwWRhbgub";
                eatMessage = "Ik heb net {amount} {name} gegeten door Kaotik! - https://discord.gg/svwWRhbgub";
                guiMessage = "Ik heb mijn hackerman menu geopend door Kaotik! - https://discord.gg/svwWRhbgub";
                break;
            }
            case Portuguese: {
                walkMessage = "Eu acabei de andar {blocks} gracas a Kaotik! - https://discord.gg/svwWRhbgub";
                placeMessage = "Eu acabei de colocar {amount} {name} gracas a Kaotik! - https://discord.gg/svwWRhbgub";
                jumpMessage = "Eu acabei de pular gracas a Kaotik! - https://discord.gg/svwWRhbgub";
                breakMessage = "Eu acabei de minerar {amount} {name} gracas a Kaotik! - https://discord.gg/svwWRhbgub";
                attackMessage = "Eu acabei de atacar {name} com {item} gracas a Kaotik! - https://discord.gg/svwWRhbgub";
                eatMessage = "Eu acabei de comer {amount} {name} gracas a Kaotik! - https://discord.gg/svwWRhbgub";
                guiMessage = "Eu acabei de abrir a minha avançada GUI gracas a Kaotik! - https://discord.gg/svwWRhbgub";
            }
        }

        if (walk.getValue()){
            if (lastPositionUpdate + (5000L * delay.getValue().intValue()) < System.currentTimeMillis()){

                double d0 = lastPositionX - mc.player.lastTickPosX;
                double d2 = lastPositionY - mc.player.lastTickPosY;
                double d3 = lastPositionZ - mc.player.lastTickPosZ;


                speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);

                if (!(speed <= 1) && !(speed > 5000)){
                    String walkAmount = new DecimalFormat("0.00").format(speed);

                    Random random = new Random();
                    if (clientSide.getValue()){
                        ChatManager.printChatNotifyClient(walkMessage.replace("{blocks}", walkAmount));
                    } else{
                        mc.player.sendChatMessage(walkMessage.replace("{blocks}", walkAmount));
                    }
                    lastPositionUpdate = System.currentTimeMillis();
                    lastPositionX = mc.player.lastTickPosX;
                    lastPositionY = mc.player.lastTickPosY;
                    lastPositionZ = mc.player.lastTickPosZ;
                }
            }
        }

    }

    @SubscribeEvent
    public void entityUseitem(LivingEntityUseItemEvent.Finish event) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        if (event.getEntity() == mc.player){
            if (event.getItem().getItem() instanceof ItemFood || event.getItem().getItem() instanceof ItemAppleGold){
                eaten++;
                if (eattingDelay >= 300 * delay.getValue().intValue()){
                    if (eat.getValue() && eaten > randomNum){
                        Random random = new Random();
                        if (clientSide.getValue()){
                            ChatManager.printChatNotifyClient(eatMessage.replace("{amount}", eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
                        } else{
                            mc.player.sendChatMessage(eatMessage.replace("{amount}", eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
                        }
                        eaten = 0;
                        eattingDelay = 0;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSend(EventPacket.Send event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock){
            blocksPlaced++;
            int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
            if (blockPlacedDelay >= 150 * delay.getValue().intValue()){
                if (place.getValue() && blocksPlaced > randomNum){
                    Random random = new Random();
                    String msg = placeMessage.replace("{amount}", blocksPlaced + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName());
                    if (clientSide.getValue()){
                        ChatManager.printChatNotifyClient(msg);
                    } else{
                        mc.player.sendChatMessage(msg);
                    }
                    blocksPlaced = 0;
                    blockPlacedDelay = 0;
                }
            }
        }
    }

    @SubscribeEvent
    public void destroyBlock(EventPlayerDestroyBlock event) {
        blocksBroken++;
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        if (blockBrokeDelay >= 300 * delay.getValue().intValue()){
            if (breaking.getValue() && blocksBroken > randomNum){
                Random random = new Random();
                String msg = breakMessage
                        .replace("{amount}", blocksBroken + "")
                        .replace("{name}", mc.world.getBlockState(event.getBlockPos()).getBlock().getLocalizedName());
                if (clientSide.getValue()){
                    ChatManager.printChatNotifyClient(msg);
                } else{
                    mc.player.sendChatMessage(msg);
                }
                blocksBroken = 0;
                blockBrokeDelay = 0;
            }
        }
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent event) {
        if (attack.getValue() && !(event.getTarget() instanceof EntityEnderCrystal)){
            if (attackDelay >= 300 * delay.getValue().intValue()){
                String msg = attackMessage.replace("{name}", event.getTarget().getName()).replace("{item}", mc.player.getHeldItemMainhand().getDisplayName());
                if (clientSide.getValue()){
                    ChatManager.printChatNotifyClient(msg);
                } else{
                    mc.player.sendChatMessage(msg);
                }
                attackDelay = 0;
            }
        }
    }

    @SubscribeEvent
    public void onJump(EventPlayerJump event) {
        if (jump.getValue()){
            if (jumpDelay >= 300 * delay.getValue().intValue()){
                if (clientSide.getValue()){
                    Random random = new Random();
                    ChatManager.printChatNotifyClient(jumpMessage);
                } else{
                    Random random = new Random();
                    mc.player.sendChatMessage(jumpMessage);
                }
                jumpDelay = 0;
            }
        }
    }

    public void onEnable(){
        blocksPlaced = 0;
        blocksBroken = 0;
        eaten = 0;
        speed = 0;
        blockBrokeDelay = 0;
        blockPlacedDelay = 0;
        jumpDelay = 0;
        attackDelay = 0;
        eattingDelay = 0;
    }
}
