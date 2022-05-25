package com.mawuote.client.modules.misc;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.network.EventDeath;
import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.api.utilities.math.TimerUtils;
import com.mawuote.client.modules.combat.AutoCrystalHack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Killsay extends Module {
    public Killsay(){
        super("Killsay", "Killsay", "Automatically sends a message in chat when you kill a player.", ModuleCategory.MISC);
        deathMessages.add("<player> tried to stop the united states legenden PML and got fuzilated in their own territory (pau no CU/pobre landia)");
        deathMessages.add("<player> GOT FOOTPRESSED, BENCHPRESSED, LACHFLIPPED.");
        deathMessages.add("WEAK HUNGARIAN DOG <player> THOUGHT HE CAN WIN VS HIS LEGEND, HE FAILED");
        deathMessages.add("OBESE AUTIST <player> CHOKE SLAMMED BY THE CZECH SULTA");
        deathMessages.add("<player> THE SLAVE DEMOLISHED BRUTALLY FORCED TO KILL HIS OWN MOM");
        deathMessages.add(("WEAK FEMBOY <player> HOLZED AND OWNED BY UNDEFEATED CHESS LEGENDE"));
        deathMessages.add(("CPVPERS VS POLISH TOILET CLEANERS KACPEREK, KEPSO, CYBERGUS, <player> CLAPPED"));
    }

    public enum modes {
        Default, Random, Custom;
    }

    public static ArrayList<String> deathMessages = new ArrayList<>();

    ValueBoolean greenText = new ValueBoolean("GreenText", "GreenText", "", false);
    ValueEnum mode = new ValueEnum("Mode", "Mode", "", modes.Default);
    ValueString customGG = new ValueString("CustomGG", "CustomGG", "", "cuztomizezez here");
    ValueNumber delay = new ValueNumber("Delay", "Delay", "", 10, 0, 30);
    ValueNumber reset = new ValueNumber("Reset", "Reset", "", 30, 0, 90);

    public Map<EntityPlayer, Integer> targets = new ConcurrentHashMap<EntityPlayer, Integer>();
    public List<String> messages = new ArrayList<String>();
    public EntityPlayer cauraTarget;
    private TimerUtils timer = new TimerUtils();
    private TimerUtils cooldownTimer = new TimerUtils();
    private boolean cooldown;

    @Override
    public void onEnable() {
        this.timer.reset();
        this.cooldownTimer.reset();
    }

    @Override
    public void onUpdate() {
        if (AutoCrystalHack.INSTANCE.target != null && this.cauraTarget != AutoCrystalHack.INSTANCE.target) {
            this.cauraTarget = AutoCrystalHack.INSTANCE.target;
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.passed(this.delay.getValue().intValue() * 1000) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        if (AutoCrystalHack.INSTANCE.target != null) {
            this.targets.put(AutoCrystalHack.INSTANCE.target, (int)(this.timer.getTimePassed() / 1000L));
        }
        this.targets.replaceAll((p, v) -> (int)(this.timer.getTimePassed() / 1000L));
        for (EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get(player) <= this.reset.getValue().intValue()) continue;
            this.targets.remove(player);
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onEntityDeath(EventDeath event) {
        if (this.targets.containsKey(event.player) && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
            this.targets.remove(event.player);
        }
        if (event.player == this.cauraTarget && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !Kaotik.FRIEND_MANAGER.isFriend(event.getEntityPlayer().getName())) {
            this.targets.put((EntityPlayer)event.getTarget(), 0);
        }
    }

    @SubscribeEvent
    public void onSendAttackPacket(EventPacket.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)mc.world) instanceof EntityPlayer && !Kaotik.FRIEND_MANAGER.isFriend(((EntityPlayer)packet.getEntityFromWorld((World)mc.world)).getName())) {
            this.targets.put((EntityPlayer)packet.getEntityFromWorld((World)mc.world), 0);
        }
    }

    public String returnRandomMessage() {
        Random random = new Random();
        return deathMessages.get(random.nextInt(deathMessages.size()));
    }

    public void announceDeath(EntityPlayer target) {
        switch((modes)mode.getValue()) {
            case Default: {
                mc.player.connection.sendPacket(new CPacketChatMessage((greenText.getValue() ? ">" : "") + "GG " + target.getDisplayNameString() + ", Europa owns me and all!"));
                break;
            }
            case Random: {
                mc.player.connection.sendPacket(new CPacketChatMessage((greenText.getValue() ? ">" : "") + returnRandomMessage().replaceAll("<player>", target.getDisplayNameString())));
                break;
            }
            case Custom: {
                mc.player.connection.sendPacket(new CPacketChatMessage((greenText.getValue() ? ">" : "") + customGG.getValue().replaceAll("<player>", target.getDisplayNameString())));
            }
        }
    }
}
