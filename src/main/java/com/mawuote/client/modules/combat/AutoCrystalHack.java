package com.mawuote.client.modules.combat;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.network.EventPacket;
import com.mawuote.api.manager.event.impl.render.EventRender3D;
import com.mawuote.api.manager.event.impl.world.EventCrystalAttack;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueColor;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueNumber;
import com.mawuote.api.utilities.crystal.CrystalUtils;
import com.mawuote.api.utilities.entity.DamageUtils;
import com.mawuote.api.utilities.entity.InventoryUtils;
import com.mawuote.api.utilities.math.MathUtils;
import com.mawuote.api.utilities.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AutoCrystalHack extends Module {
    public AutoCrystalHack() {
        super("AutoCrystal", "Auto Crystal", "haha boom", ModuleCategory.COMBAT);
        INSTANCE = this;
    }

    public static AutoCrystalHack INSTANCE;

    private EntityEnderCrystal targetCrystal = null;
    private BlockPos targetPosition = null;

    private final List<Integer> blacklist = new ArrayList<>();
    List<FadePosition> fadePositions = new ArrayList<>();

    public static BlockPos renderPosition = null;
    private float damageNumber = 0.0f;

    public static boolean isSequential = false;
    public EntityPlayer target = null;

    private int breakTicks;
    private int placeTicks;
    // BREAK SETTINGS
    public static ValueBoolean breakMode = new ValueBoolean("Break", "Break", "Breaks the crystals.", true);
    public static ValueNumber breakDelay = new ValueNumber("BreakDelay", "BreakDelay", "The delay for breaking.", 0, 0, 20);
    public static ValueNumber breakRange = new ValueNumber("BreakRange", "BreakRange", "The range for breaking.", 5.0f, 0.0f, 6.0f);
    public static ValueNumber breakWallsRange = new ValueNumber("BreakWallsRange", "BreakWallsRange", "The range for breaking through walls.", 3.5f, 0.0f, 6.0f);
    public static ValueBoolean antiWeakness = new ValueBoolean("AntiWeakness", "AntiWeakness", "Switches to a sword when you have weakness.", false);
    public static ValueBoolean inhibit = new ValueBoolean("Inhibit", "Inhibit", "Prevents an a crystal which is going to explode from being attacked again.", true);
    public static ValueBoolean sequential = new ValueBoolean("Sequential", "Sequential", "Breaks crystals when they spawn. Good for strictless servers.", true);
    public static ValueBoolean sync = new ValueBoolean("Sync", "Sync", "Syncs crystals based on sounds.", true);
    // PLACE SETTINGS
    public static ValueBoolean place = new ValueBoolean("Place", "Place", "Places the crystals.", true);
    public static ValueNumber placeDelay = new ValueNumber("PlaceDelay", "PlaceDelay", "The delay for placing.", 0, 0, 20);
    public static ValueNumber placeRange = new ValueNumber("PlaceRange", "PlaceRange", "The range for placing.", 5.0f, 0.0f, 6.0f);
    public static ValueNumber placeWallsRange = new ValueNumber("PlaceWallsRange", "PlaceWallsRange", "The range for breaking through walls.", 3.5f, 0.0f, 6.0f);
    public static ValueBoolean placeUnderBlock = new ValueBoolean("PlaceUnderBlock", "PlaceUnderBlock", "Places under blocks.", false);
    public static ValueEnum switchMode = new ValueEnum("Switch", "Switch", "Automatically switches to a crystal.", SwitchModes.None);
    public static ValueEnum multiPlace = new ValueEnum("MultiPlace", "MultiPlace", "Places in more positions than one.", MultiPlaceModes.None);
    public static ValueBoolean holePlace = new ValueBoolean("HolePlace", "HolePlace", "Places in the hole when the player jumps.", true);
    // ROTATION SETTINGS
    public static ValueBoolean rotation = new ValueBoolean("Rotation", "Rotation", "Rotates to the crystal and position when placing.", false);
    public static ValueEnum timing = new ValueEnum("Timing", "Timing", "The timing for the breaking.", Timings.Break);
    public static ValueNumber targetRange = new ValueNumber("TargetRange", "TargetRange", "The range for targeting.", 15.0f, 0.0f, 30.0f);
    public static ValueEnum swing = new ValueEnum("Swing", "Swing", "The hand to swing with.", Hands.Mainhand);
    public static ValueBoolean packetSwing = new ValueBoolean("PacketSwing", "PacketSwing", "Swings serverside but not clientside.", false);
    // DAMAGE SETTINGS
    public static ValueNumber minimumDamage = new ValueNumber("MinimumDamage", "MinimumDamage", "The minimum damage that is required for the target.", 6.0f, 0.0f, 36.0f);
    public static ValueNumber maxSelfDamage = new ValueNumber("MaxSelfDamage", "MaxSelfDamage", "The minimum damage that is required for the target.", 6.0f, 0.0f, 36.0f);
    // FACEPLACE SETTINGS
    public static ValueBoolean facePlace = new ValueBoolean("FacePlace", "FacePlace", "Faceplaces the target when the opportunity is right.", true);
    public static ValueNumber facePlaceHealth = new ValueNumber("FacePlaceHealth", "FacePlaceHealth", "The health at which faceplacing would start.", 12.0f, 0.0f, 36.0f);
    public static ValueBoolean armorBreaker = new ValueBoolean("ArmorBreaker", "ArmorBreaker", "Starts faceplacing the enemy when their armor is low.", true);
    public static ValueNumber armorPercent = new ValueNumber("ArmorPercent", "ArmorPercent", "The percent required for the armor breaking to start.", 20, 0, 100);
    // RENDER SETTINGS
    // We're using rendermodes so that we can add glide later or something else -Innards
    // We're using enums for fill and outline because I have plans to add gradient rendering and stuff -Innards
    public static ValueEnum render = new ValueEnum("Render", "Render", "Renders the current target position.", RenderModes.Normal);
    public static ValueEnum fill = new ValueEnum("Fill", "Fill", "The mode for filling the position.", Renders.Normal);
    public static ValueEnum outline = new ValueEnum("Outline", "Outline", "The mode for outlining the position.", Renders.Normal);
    public static ValueBoolean renderDamage = new ValueBoolean("RenderDamage", "RenderDamage", "Renders the amount of damage that the position does.", false);
    public static ValueNumber shrinkSpeed = new ValueNumber("ShrinkSpeed", "ShrinkSpeed", "", 150, 10, 500);
    public static ValueNumber fadeDuration = new ValueNumber("FadeDuration", "FadeDuration", "The duration of the fade.", 15, 1, 50);
    public static ValueNumber lineWidth = new ValueNumber("Width", "Width", "The width for the outline.", 1.0f, 0.0f, 5.0f);
    public static ValueColor fillColor = new ValueColor("FillColor", "FillColor", "The color for the filling.", new Color(0, 0, 255, 100));
    public static ValueColor outlineColor = new ValueColor("OutlineColor", "OutlineColor", "The color for the outline.", new Color(0, 0, 255, 255));

    public void onMotionUpdate() {
        doAutoCrystal();
    }

    public void doAutoCrystal(){
        if (mc.player == null || mc.world == null) return;

        double maxCrystalDamage = 0;
        double maxPositionDamage = 0;

        if (place.getValue() && placeTicks++ > placeDelay.getValue().intValue()){
            placeTicks = 0;

            NonNullList<BlockPos> positions = NonNullList.create();
            for (BlockPos position : CrystalUtils.getSphere(placeRange.getValue().floatValue(), true, false)) {
                if (mc.world.getBlockState(position).getBlock() == Blocks.AIR) continue;
                if (!CrystalUtils.canPlaceCrystal(position, placeUnderBlock.getValue(), multiPlace.getValue().equals(MultiPlaceModes.Static) || (multiPlace.getValue().equals(MultiPlaceModes.Dynamic) && CrystalUtils.isEntityMoving(mc.player) && CrystalUtils.isEntityMoving(target)), holePlace.getValue())) continue;
                positions.add(position);
            }

            for (EntityPlayer player : mc.world.playerEntities) {
                if (mc.player.getDistanceSq(player) > MathUtils.square(targetRange.getValue().floatValue())) continue;
                if (player == mc.player) continue;
                if (Kaotik.FRIEND_MANAGER.isFriend(player.getName())) continue;
                if (player.isDead) continue;
                if (player.getHealth() <= 0) continue;

                for (BlockPos position : positions) {
                    float targetDamage = filterPosition(position, player);
                    if (targetDamage == -1) continue;

                    if (targetDamage > maxPositionDamage) {
                        maxPositionDamage = targetDamage;
                        targetPosition = position;
                        damageNumber = targetDamage;
                        target = player;
                    }
                }
            }

            if (targetPosition != null){
                int slot = InventoryUtils.findItem(Items.END_CRYSTAL, 0, 9);
                int lastSlot = mc.player.inventory.currentItem;

                if (!switchMode.getValue().equals(SwitchModes.None) && slot != -1){
                    InventoryUtils.switchSlot(slot, switchMode.getValue().equals(SwitchModes.Silent));
                }

                if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL || switchMode.getValue().equals(SwitchModes.Silent)) {
                    renderPosition = targetPosition;
                    RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(targetPosition.getX() + 0.5, targetPosition.getY() - 0.5, targetPosition.getZ() + 0.5));
                    EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPosition, facing, switchMode.getValue().equals(SwitchModes.Silent) ? EnumHand.MAIN_HAND : mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND, 0.5f, 0.5f, 0.5f));
                    mc.player.connection.sendPacket(new CPacketAnimation(switchMode.getValue().equals(SwitchModes.Silent) ? EnumHand.MAIN_HAND : mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
                } else {
                    renderPosition = null;
                }

                if (switchMode.getValue().equals(SwitchModes.Silent) && lastSlot != -1){
                    InventoryUtils.switchSlot(lastSlot, switchMode.getValue().equals(SwitchModes.Silent));
                }
            } else {
                renderPosition = null;
            }

            targetPosition = null;
        }

        if (breakMode.getValue() && breakTicks++ > breakDelay.getValue().intValue()){
            breakTicks = 0;

            for (EntityPlayer player : mc.world.playerEntities) {
                if (mc.player.getDistanceSq(player) > MathUtils.square(targetRange.getValue().floatValue())) continue;
                if (player == mc.player) continue;
                if (Kaotik.FRIEND_MANAGER.isFriend(player.getName())) continue;
                if (player.isDead) continue;
                if (player.getHealth() <= 0) continue;

                for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                    if (!(entity instanceof EntityEnderCrystal)) continue;
                    EntityEnderCrystal crystal = (EntityEnderCrystal) entity;

                    if (blacklist.contains(crystal.getEntityId()) && inhibit.getValue()) continue;

                    double targetDamage = filterCrystal(crystal, player);
                    if (targetDamage == -1) continue;

                    if (targetDamage > maxCrystalDamage) {
                        maxCrystalDamage = targetDamage;
                        targetCrystal = crystal;
                        target = player;
                    }
                }
            }

            if (targetCrystal != null){
                int swordSlot = InventoryUtils.findItem(Items.DIAMOND_SWORD, 0, 9);
                int appleSlot = InventoryUtils.findItem(Items.GOLDEN_APPLE, 0, 9);

                if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS) && swordSlot != -1){
                    if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                        InventoryUtils.switchSlot(swordSlot, false);
                    }
                } else if (antiWeakness.getValue() && !mc.player.isPotionActive(MobEffects.WEAKNESS) && appleSlot != -1){
                    if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword){
                        InventoryUtils.switchSlot(appleSlot, false);
                    }
                }

                mc.playerController.attackEntity(mc.player, targetCrystal);
                swingItem();

                targetCrystal = null;
            }
        }
    }

    public void onRender3D(EventRender3D event){
        if (!render.getValue().equals(RenderModes.None)) {
            if (render.getValue().equals(RenderModes.Normal) || render.getValue().equals(RenderModes.Fade) || render.getValue() == RenderModes.Size){
                if (renderPosition != null) {
                    if (render.getValue() == RenderModes.Fade || render.getValue() == RenderModes.Size) {
                        this.fadePositions.removeIf (pos -> pos.position.equals(renderPosition));
                        this.fadePositions.add(new FadePosition(renderPosition));
                    }
                    if (fill.getValue().equals(Renders.Normal)) {
                        RenderUtils.drawBox(renderPosition, fillColor.getValue());
                    }

                    if (outline.getValue().equals(Renders.Normal)) {
                        RenderUtils.drawBlockOutline(new AxisAlignedBB(renderPosition.getX() - mc.getRenderManager().viewerPosX, renderPosition.getY() - mc.getRenderManager().viewerPosY, renderPosition.getZ() - mc.getRenderManager().viewerPosZ, (renderPosition.getX() + 1) - mc.getRenderManager().viewerPosX, (renderPosition.getY() + 1) - mc.getRenderManager().viewerPosY, (renderPosition.getZ() + 1) - mc.getRenderManager().viewerPosZ), outlineColor.getValue(), lineWidth.getValue().floatValue());
                    }
                }

                if (render.getValue().equals(RenderModes.Fade)){
                    fadePositions.forEach(pos -> {
                        if (!pos.getPosition().equals(renderPosition)){
                            float opacity;

                            long time = System.currentTimeMillis();
                            long duration = time - pos.getStartTime();

                            if (duration < (fadeDuration.getValue().intValue() * 100L)){
                                opacity = (fillColor.getValue().getAlpha() / 255.0f) - ((float) duration / (fadeDuration.getValue().intValue() * 100L));
                                int alpha = MathHelper.clamp((int) (opacity * 255.0f), 0, 255);

                                if (fill.getValue().equals(Renders.Normal)) {
                                    RenderUtils.drawBox(pos.getPosition(), new Color(fillColor.getValue().getRed(), fillColor.getValue().getGreen(), fillColor.getValue().getBlue(), alpha));
                                }

                                if (outline.getValue().equals(Renders.Normal)) {
                                    RenderUtils.prepare(7);
                                    RenderUtils.drawBoundingBoxBlockPos(pos.getPosition(), lineWidth.getValue().floatValue(), outlineColor.getValue().getRed(), outlineColor.getValue().getGreen(), outlineColor.getValue().getBlue(), alpha);
                                    RenderUtils.release();
                                }
                            }
                        }
                    });

                    fadePositions.removeIf(fadePosition -> (System.currentTimeMillis() - fadePosition.getStartTime()) >= (fadeDuration.getValue().intValue() * 100L));
                } else if(render.getValue().equals(RenderModes.Size)) {
                    fadePositions.stream().distinct().forEach(p -> {
                        if (!p.position.equals(renderPosition)) {

                            AxisAlignedBB bb = RenderUtils.fixBB(new AxisAlignedBB(p.position));

                            float opacity;

                            long time = System.currentTimeMillis();
                            long duration = time - p.startTime;
                            float startAlpha = (fillColor.getValue().getAlpha()/255f);
                            if (duration < (shrinkSpeed.getValue().intValue()* 10L)) {
                                opacity = startAlpha - ((float) duration / (float) (shrinkSpeed.getValue().intValue()*10));

                                opacity = MathHelper.clamp(opacity, -1.0f, 0.0f);

                                bb = bb.shrink(-opacity);

                                RenderUtils.drawFilledBox(bb, fillColor.getValue().getRGB());
                                RenderUtils.drawBlockOutline(bb, outlineColor.getValue(), 1f);
                            }
                        }
                    });

                    fadePositions.removeIf(p -> (System.currentTimeMillis() - p.startTime) >= (shrinkSpeed.getValue().intValue()* 10L) || mc.world.getBlockState(p.position).getBlock() == Blocks.AIR);
                }
            }

            if (renderDamage.getValue() && renderPosition != null) CrystalUtils.drawText(renderPosition, ((Math.floor(damageNumber) == this.damageNumber) ? Integer.valueOf((int)this.damageNumber) : String.format("%.1f", damageNumber)) + "");
        }
    }

    @SubscribeEvent
    public void onCrystalAttack(EventCrystalAttack event){
        blacklist.add(event.getEntityID());
    }

    @SubscribeEvent
    public void onPacketReceive(EventPacket.Receive event){
        if (event.getPacket() instanceof SPacketSoundEffect && sync.getValue()) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                    if (entity instanceof EntityEnderCrystal) {
                        if (entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) <= 36.0f) {
                            entity.setDead();
                        }
                    }
                }
            }
        }

        if (event.getPacket() instanceof SPacketSpawnObject && sequential.getValue()){
            SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();

            if (target == null) return;
            if (packet.getType() == 51 && breakMode.getValue()){
                final EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world, packet.getX(), packet.getY(), packet.getZ());
                if (target != null && filterCrystal(crystal, target) != -1) {
                    if (blacklist.contains(packet.getEntityID())) return;
                    isSequential = true;

                    CPacketUseEntity crystalPacket = new CPacketUseEntity();
                    crystalPacket.entityId = packet.getEntityID();
                    crystalPacket.action = CPacketUseEntity.Action.ATTACK;

                    mc.player.connection.sendPacket(crystalPacket);
                    if (mc.playerController.getCurrentGameType() != GameType.SPECTATOR) mc.player.resetCooldown();
                    swingItem();

                    blacklist.add(crystalPacket.entityId);
                    isSequential = false;
                }
            }
        }
    }

    public float filterCrystal(EntityEnderCrystal crystal, EntityPlayer target){
        if (mc.player.canEntityBeSeen(crystal) ? mc.player.getDistanceSq(crystal) > MathUtils.square(breakRange.getValue().floatValue()) : mc.player.getDistanceSq(crystal) > MathUtils.square(breakWallsRange.getValue().floatValue())) return -1;
        if (crystal.isDead) return -1;

        float targetDamage = DamageUtils.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, target);
        float selfDamage = DamageUtils.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, mc.player);

        return returnDamage(target, targetDamage, selfDamage);
    }

    public float filterPosition(BlockPos position, EntityPlayer target){
        if (CrystalUtils.canSeePos(position) ? mc.player.getDistanceSq(position) > MathUtils.square(placeRange.getValue().floatValue()) : mc.player.getDistanceSq(position) > MathUtils.square(placeWallsRange.getValue().floatValue())) return -1;

        float targetDamage = DamageUtils.calculateDamage(position.getX() + 0.5, position.getY() + 1.0, position.getZ() + 0.5, target);
        float selfDamage = DamageUtils.calculateDamage(position.getX() + 0.5, position.getY() + 1.0, position.getZ() + 0.5, mc.player);

        return returnDamage(target, targetDamage, selfDamage);
    }

    private float returnDamage(EntityPlayer target, float targetDamage, float selfDamage) {
        if (targetDamage < getMinimumDamage(target) && targetDamage < (target.getHealth() + target.getAbsorptionAmount())) return -1;
        if (selfDamage > maxSelfDamage.getValue().floatValue()) return -1;
        if ((mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= selfDamage) return -1;

        return targetDamage;
    }

    public void swingItem(){
        if (!swing.getValue().equals(Hands.None)) {
            if (packetSwing.getValue()) {
                if (swing.getValue().equals(Hands.Mainhand)) {
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                } else if (swing.getValue().equals(Hands.Offhand)) {
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
                }
            } else {
                if (swing.getValue().equals(Hands.Mainhand)) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                } else if (swing.getValue().equals(Hands.Offhand)) {
                    mc.player.swingArm(EnumHand.OFF_HAND);
                }
            }
        }
    }

    public float getMinimumDamage(EntityLivingBase entity){
        if ((facePlace.getValue() && (entity.getHealth() + entity.getAbsorptionAmount()) < facePlaceHealth.getValue().floatValue()) || (armorBreaker.getValue() && DamageUtils.shouldBreakArmor(entity, armorPercent.getValue().intValue()))){
            return 1;
        } else {
            return minimumDamage.getValue().floatValue();
        }
    }

    @Override
    public void onEnable(){
        super.onEnable();
        isSequential = false;
        target = null;
    }

    @Override
    public void onDisable(){
        super.onDisable();
        isSequential = false;
        target = null;
    }

    @Override
    public void onLogout(){
        blacklist.clear();
    }

    public static class FadePosition {
        private final BlockPos position;
        private final long startTime;

        public FadePosition(BlockPos position){
            this.position = position;
            this.startTime = System.currentTimeMillis();
        }

        public BlockPos getPosition() {
            return position;
        }

        public long getStartTime() {
            return startTime;
        }
    }

    public enum Timings { Break, Place }
    public enum SwitchModes { None, Normal, Silent }
    public enum Hands { None, Mainhand, Offhand }
    public enum MultiPlaceModes { None, Dynamic, Static }
    public enum RenderModes { None, Normal, Fade, Size }
    public enum Renders { None, Normal }
}
