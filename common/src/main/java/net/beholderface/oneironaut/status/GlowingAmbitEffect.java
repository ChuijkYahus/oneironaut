package net.beholderface.oneironaut.status;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.beholderface.oneironaut.registry.OneironautMiscRegistry;

public class GlowingAmbitEffect extends StatusEffect {
    //this class is (mostly) only necessary because StatusEffect's constructor method is protected for some reason, the actual functionality of the effect is in a couple mixins
    //the glow is in GlowAmbitEffectGlowMixin, and the ambit is in GlowAmbitEffectAmbitMixin
    public GlowingAmbitEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x7355ff);
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier){
        long time = entity.getWorld().getTime();
        Random rand = entity.getWorld().random;
        if (!(entity.getWorld().isClient) && !(entity.hasStatusEffect(OneironautMiscRegistry.DETECTION_RESISTANCE.get()))){
            final int dissonance = Math.min(amplifier, 5);
            final int defaultInterval = 20;
            int thisInterval = (int) Math.min(Math.floor(Math.abs(rand.nextGaussian() - 0.5) * 6 * dissonance), defaultInterval - 1);
            if (time % (defaultInterval - thisInterval) == 0) {
                ((ServerWorld) entity.getWorld()).playSoundFromEntity(
                        null, entity, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, entity.getSoundCategory(),
                        (float)(2 + (((rand.nextGaussian() - 0.5) * dissonance) / 3)), (1 - (rand.nextFloat() * (dissonance / 3f))));
            }
        }
    }
}
