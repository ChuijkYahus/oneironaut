package net.beholderface.oneironaut.casting.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class MishapExtradimensionalFail : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.RED)

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Text? = null

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {

    }
}