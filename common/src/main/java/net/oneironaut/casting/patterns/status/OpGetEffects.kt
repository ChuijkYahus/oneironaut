package net.oneironaut.casting.patterns.status

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import net.oneironaut.registry.PotionIota

class OpGetEffects : ConstMediaAction {
    override val argc = 1
    override val mediaCost = 0
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val target = args.getLivingEntityButNotArmorStand(0, argc)
        val effects = target.statusEffects
        //val effectIotas : MutableList<PotionIota> = mutableListOf()
        var currentList : ListIota
        val effectDetails : MutableList<ListIota> = mutableListOf()
        for (effect in effects){
            currentList = ListIota(listOf(PotionIota(effect.effectType), DoubleIota(effect.duration.toDouble() / 20), DoubleIota((effect.amplifier + 1).toDouble())))
            effectDetails.add(currentList)
            //effectIotas.add(PotionIota(effect.effectType))
        }
        return listOf(ListIota(effectDetails.toList()))
    }
}