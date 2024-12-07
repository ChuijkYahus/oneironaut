package net.beholderface.oneironaut.casting;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.misc.HexDamageSources;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.ktxt.AccessorWrappers;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.beholderface.oneironaut.network.ParticleBurstPacket;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.beholderface.oneironaut.MiscAPIKt;
import net.beholderface.oneironaut.Oneironaut;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class OvercastDamageEnchant extends Enchantment {
    private static final Map<LivingEntity, Long> cooldownMap = new HashMap<>();
    private static final FrozenColorizer playerlessColor = new FrozenColorizer(HexItems.DYE_COLORIZERS.get(DyeColor.PURPLE).getDefaultStack(), Util.NIL_UUID);
    public OvercastDamageEnchant() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || super.isAcceptableItem(stack);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        applyMindDamage(user, target, level, false);
    }

    public boolean canAccept(Enchantment other){
        return !(other instanceof DamageEnchantment) && this != other;
    }

    /*public float getAttackDamage(int level, EntityGroup group) {
        return 0.5F + (float)Math.max(0, level - 1) * 0.5F;
    }*/

    @Override
    public int getMaxLevel() {
        return 5;
    }

    public static void applyMindDamage(@Nullable LivingEntity user, @NotNull Entity target, int level, boolean autospare){
        World world = target.world;
        long currentTime = world.getTime();
        long lastTime = cooldownMap.getOrDefault(user, 0L);
        if (target instanceof LivingEntity livingTarget && (lastTime + 12) < currentTime && !world.isClient){
            //user.sendMessage(Text.of(String.valueOf(lastTime)));
            //ripped from the trulyHurt method to see if I could get more consistent results
            boolean brainswept = false;
            if (target instanceof MobEntity mob){
                brainswept = IXplatAbstractions.INSTANCE.isBrainswept(mob);
            }
            boolean creative = target instanceof PlayerEntity player && (player.isSpectator() || player.isCreative());
            if (!livingTarget.isInvulnerableTo(HexDamageSources.OVERCAST) && !livingTarget.isDead() && !brainswept && !creative){
                float oldHealth = livingTarget.getHealth();
                float newHealth = oldHealth - (level / 2f);
                if (newHealth > 0){
                    livingTarget.setHealth(newHealth);
                } else if (newHealth <= 0 && autospare && newHealth < oldHealth){
                    livingTarget.setHealth(0.1f);
                } else {
                    //die, avaritia user, die!
                    livingTarget.damage(HexDamageSources.OVERCAST, Float.MAX_VALUE);
                    livingTarget.kill();
                }
                AccessorWrappers.markHurt(livingTarget);
                if (livingTarget.isAlive() && livingTarget.getHealth() <= 1f && target instanceof MobEntity mob){
                    boolean whitelisted = mob.getType().isIn(MiscAPIKt.getEntityTagKey(new Identifier(Oneironaut.MOD_ID, "render_flay_whitelist")));
                    boolean blacklisted = mob.getType().isIn(MiscAPIKt.getEntityTagKey(new Identifier(Oneironaut.MOD_ID, "render_flay_blacklist")));
                    //if it has more than 100 max health, it's probably a boss, and I'm not letting people get flayed dragons
                    if ((mob.getMaxHealth() <= 100.0f || whitelisted) /* but I am letting people get flayed wardens :) */ && !blacklisted){
                        //Brainsweeping.brainsweep(mob);
                        IXplatAbstractions.INSTANCE.brainsweep(mob);
                        if (user instanceof ServerPlayerEntity player){
                            IXplatAbstractions.INSTANCE.sendPacketNear(target.getPos(), 128.0, (ServerWorld) mob.world, new ParticleBurstPacket(
                                    target.getPos(), new Vec3d(0.0, 0.1, 0.0), 0.1, 0.025,
                                    IXplatAbstractions.INSTANCE.getColorizer(player), 64, false));
                            world.playSoundFromEntity(null, mob, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        } else {
                            IXplatAbstractions.INSTANCE.sendPacketNear(target.getPos(), 128.0, (ServerWorld) mob.world, new ParticleBurstPacket(
                                    target.getPos(), new Vec3d(0.0, 0.1, 0.0), 0.1, 0.025,
                                    playerlessColor, 64, false));
                            world.playSoundFromEntity(null, mob, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.BLOCKS, 0.5f, 1.0f);
                        }
                    }
                }
            }
            //Mishap.Companion.trulyHurt(livingTarget, HexDamageSources.OVERCAST, level);
            if (user != null){
                cooldownMap.put(user, currentTime);
            }
        }
    }
}
