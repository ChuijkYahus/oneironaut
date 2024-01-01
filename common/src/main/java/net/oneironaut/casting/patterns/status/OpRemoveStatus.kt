package net.oneironaut.casting.patterns.status

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.text.Text
import net.oneironaut.casting.mishaps.MishapMissingEffect
import net.oneironaut.getStatusEffect
import kotlin.math.ln
import kotlin.math.pow

class OpRemoveStatus : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        val target = args.getLivingEntityButNotArmorStand(0, argc)
        val effect = args.getStatusEffect(1, argc, true)
        val existingEffect =  target.getStatusEffect(effect)
        if (existingEffect == null){
            throw MishapMissingEffect(target, effect)
        }
        val effectDuration = existingEffect.duration.toDouble() / 20
        val effectStrenth = (existingEffect.amplifier + 1).toDouble()
        var costExponent = when(effect.category){
            StatusEffectCategory.BENEFICIAL -> 1.1
            StatusEffectCategory.NEUTRAL -> 1.5
            StatusEffectCategory.HARMFUL -> 2.0
            null -> 1.0
        }
        if (costExponent.equals(1.0) && !(target.equals(ctx.caster))){
            costExponent = 2.0
        }
        var cost = ((effectStrenth.pow(costExponent) * effectDuration) * MediaConstants.DUST_UNIT).toInt()
        if (costExponent == 1.1){
            cost /= 10
        }
        //ctx.caster.sendMessage(Text.of((cost.toDouble() / MediaConstants.DUST_UNIT).toString() + " dust"))
        return Triple(
            Spell(target, effect),
            cost,
            listOf(ParticleSpray.cloud(ctx.caster.pos, 2.0))
        )
    }
    private data class Spell(val target : LivingEntity, val effect : StatusEffect) : RenderedSpell {
        override fun cast(ctx: CastingContext){
            target.removeStatusEffect(effect)
        }
    }
}