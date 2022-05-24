package com.mawuote.api.utilities.entity;

import com.mawuote.api.utilities.IMinecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.ConcurrentModificationException;
import java.util.Objects;

public class DamageUtils implements IMinecraft {
    public static float calculateDamage(double posX, double posY, double posZ, EntityLivingBase entity) {
        try {
            double distance = entity.getDistance(posX, posY, posZ) / 12.0f;
            double value = (1.0 - distance) * mc.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());

            float damage = ((float) ((int) ((value * value + value) / 2.0 * 7.0 * 12.0 + 1.0)) * (mc.world.getDifficulty().getId() == 0 ? 0.0f : (mc.world.getDifficulty().getId() == 2 ? 1.0f : (mc.world.getDifficulty().getId() == 1 ? 0.5f : 1.5f))));
            damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp(EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), DamageSource.causeExplosionDamage(new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true))), 0.0f, 20.0f) / 25.0f;
            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) damage -= damage / 4.0f;

            return damage;
        } catch (NullPointerException | ConcurrentModificationException exception){
            return 0;
        }
    }

    public static
    float getDamageMultiplied ( float damage ) {
        int diff = DamageUtils.mc.world.getDifficulty ( ).getId ( );
        return damage * ( diff == 0 ? 0.0f : ( diff == 2 ? 1.0f : ( diff == 1 ? 0.5f : 1.5f ) ) );
    }

    public static
    float getBlastReduction ( EntityLivingBase entity , float damageI , Explosion explosion ) {
        float damage = damageI;
        if ( entity instanceof EntityPlayer ) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage ( explosion );
            damage = CombatRules.getDamageAfterAbsorb ( damage , (float) ep.getTotalArmorValue ( ) , (float) ep.getEntityAttribute ( SharedMonsterAttributes.ARMOR_TOUGHNESS ).getAttributeValue ( ) );
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage ( ep.getArmorInventoryList ( ) , ds );
            } catch ( Exception exception ) {
                // empty catch block
            }
            float f = MathHelper.clamp ( (float) k , 0.0f , 20.0f );
            damage *= 1.0f - f / 25.0f;
            if ( entity.isPotionActive ( MobEffects.RESISTANCE ) ) {
                damage -= damage / 4.0f;
            }
            damage = Math.max ( damage , 0.0f );
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb ( damage , (float) entity.getTotalArmorValue ( ) , (float) entity.getEntityAttribute ( SharedMonsterAttributes.ARMOR_TOUGHNESS ).getAttributeValue ( ) );
        return damage;
    }


    public static boolean shouldBreakArmor(EntityLivingBase entity, float targetPercent){
        for (ItemStack stack : entity.getArmorInventoryList()){
            if (stack == null || stack.getItem() == Items.AIR) return true;
            float armorPercent = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / stack.getMaxDamage()) * 100.0f;
            if (targetPercent >= armorPercent && stack.stackSize < 2) return true;
        }

        return false;
    }

    public static boolean hasDurability(final ItemStack stack) {
        final Item item = stack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }

    public static int getRoundedDamage(ItemStack stack) {
        return (int) ((stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f);
    }

    public static
    float calculateDamage ( double posX , double posY , double posZ , Entity entity ) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance ( posX , posY , posZ ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d ( posX , posY , posZ );
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity ( vec3d , entity.getEntityBoundingBox ( ) );
        } catch ( Exception exception ) {
            // empty catch block
        }
        double v = ( 1.0 - distancedsize ) * blockDensity;
        float damage = (int) ( ( v * v + v ) / 2.0 * 7.0 * (double) doubleExplosionSize + 1.0 );
        double finald = 1.0;
        if ( entity instanceof EntityLivingBase ) {
            finald = DamageUtils.getBlastReduction ( (EntityLivingBase) entity , DamageUtils.getDamageMultiplied ( damage ) , new Explosion ( DamageUtils.mc.world , null , posX , posY , posZ , 6.0f , false , true ) );
        }
        return (float) finald;
    }

    public static
    float calculateDamage ( Entity crystal , Entity entity ) {
        return DamageUtils.calculateDamage ( crystal.posX , crystal.posY , crystal.posZ , entity );
    }

    public static
    float calculateDamage ( BlockPos pos , Entity entity ) {
        return DamageUtils.calculateDamage ( (double) pos.getX ( ) + 0.5 , pos.getY ( ) + 1 , (double) pos.getZ ( ) + 0.5 , entity );
    }
}