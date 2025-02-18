package net.beholderface.oneironaut.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.block.ThoughtSlurry;
import net.beholderface.oneironaut.status.DetectionResistEffect;
import net.beholderface.oneironaut.status.GlowingAmbitEffect;
import net.beholderface.oneironaut.casting.OvercastDamageEnchant;
import net.beholderface.oneironaut.status.MonkfruitDelayEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKeys;

public class OneironautMiscRegistry {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Oneironaut.MOD_ID, RegistryKeys.FLUID);
    public static final DeferredRegister<StatusEffect> EFFECTS = DeferredRegister.create(Oneironaut.MOD_ID, RegistryKeys.STATUS_EFFECT);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Oneironaut.MOD_ID, RegistryKeys.ENCHANTMENT);


    //I will not scream at my computer over this

    public static void init() {
        FLUIDS.register();
        EFFECTS.register();
        ENCHANTMENTS.register();
    }

    public static final RegistrySupplier<DetectionResistEffect> DETECTION_RESISTANCE = EFFECTS.register("detection_resistance", DetectionResistEffect::new);
    public static final RegistrySupplier<StatusEffect> NOT_MISSING = EFFECTS.register("not_missing", GlowingAmbitEffect::new);
    public static final RegistrySupplier<StatusEffect> RUMINATION = EFFECTS.register("rumination", MonkfruitDelayEffect::new);

    public static final RegistrySupplier<ThoughtSlurry> THOUGHT_SLURRY = FLUIDS.register("thought_slurry", () -> ThoughtSlurry.STILL_FLUID /*new ThoughtSlurry.Still(OneironautThingRegistry.THOUGHT_SLURRY_ATTRIBUTES)*/);
    public static final RegistrySupplier<ThoughtSlurry> THOUGHT_SLURRY_FLOWING = FLUIDS.register("thought_slurry_flowing", () -> ThoughtSlurry.FLOWING_FLUID /*new ThoughtSlurry.Flowing(OneironautThingRegistry.THOUGHT_SLURRY_ATTRIBUTES)*/);

    public static final RegistrySupplier<OvercastDamageEnchant> OVERCAST_DAMAGE_ENCHANT = ENCHANTMENTS.register("overcast_damage", OvercastDamageEnchant::new);
}
