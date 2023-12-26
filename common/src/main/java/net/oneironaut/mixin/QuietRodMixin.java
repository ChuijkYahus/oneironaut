package net.oneironaut.mixin;

import at.petrak.hexcasting.api.spell.ParticleSpray;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.sideeffects.OperatorSideEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ram.talia.hexal.api.spell.casting.IMixinCastingContext;
import ram.talia.hexal.common.entities.BaseCastingWisp;

import java.util.List;

import static net.oneironaut.MiscAPIKt.isUsingRod;

@SuppressWarnings("ConstantConditions")
@Mixin(value = CastingHarness.class, priority = 1001/*gotta make sure to overwrite the hexal mixin here*/)
public abstract class QuietRodMixin {
    private final CastingHarness harness = (CastingHarness) (Object) this;
    @Redirect(method = "updateWithPattern",
            at = @At(
                    value="INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            ),
            remap = false)
    private boolean updateWithPatternRodOrWisp (List<OperatorSideEffect> sideEffects, Object o) {

        if (o instanceof OperatorSideEffect.Particles particles) {
            CastingContext ctx = harness.getCtx();
            IMixinCastingContext ctxi = (IMixinCastingContext)(Object) ctx;
            //ctx.getCaster().sendMessage(Text.of("mixin is doing a thing"));
            if (!(isUsingRod(ctx) || ctxi.hasWisp()))
                return sideEffects.add(particles);
            else if (isUsingRod(ctx)){
                //do particles every 30 ticks
                if ((((ctx.getCaster().getWorld().getTime() - ctx.getCaster().getActiveItem().getNbt().getDouble("initialTime")) % 30.0) == 0)){
                    return sideEffects.add(new OperatorSideEffect.Particles(new ParticleSpray(ctx.getCaster().getPos(), new Vec3d(0, 1, 0), 0.5, 1.0, 1)));
                } else {
                    return false;
                }
            }else {
                return false;
            }
        }
        return sideEffects.add((OperatorSideEffect) o);
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Redirect(method = "executeIotas",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
                    , remap = true
            ),
            remap = true)
    private void playSoundRodOrWisp (ServerWorld world, PlayerEntity player, double x, double y, double z, SoundEvent soundEvent, SoundCategory soundSource, float v, float p) {
        CastingContext ctx = harness.getCtx();
        IMixinCastingContext wispContext = (IMixinCastingContext) (Object) ctx;

        BaseCastingWisp wisp = wispContext.getWisp();

        if (wisp != null) {
            wisp.scheduleCastSound();
        } else if (isUsingRod(ctx)) {
            ServerPlayerEntity caster = ctx.getCaster();
            if (caster != null){
                ItemStack activeStack = caster.getActiveItem();
                //play cast sound every 1.5 seconds
                if ((((caster.getWorld().getTime() - activeStack.getNbt().getDouble("initialTime")) % 30.0) == 0)){
                    world.playSound(player, x, y, z, soundEvent, soundSource, v, p);
                }
            }
        } else {
            world.playSound(player, x, y, z, soundEvent, soundSource, v, p);
        }
    }
}