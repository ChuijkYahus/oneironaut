package net.beholderface.oneironaut.casting.patterns.rod

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.beholderface.oneironaut.isUsingRod

class OpCheckForRod : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
        return isUsingRod(ctx).asActionResult
    }
}