package net.beholderface.oneironaut.casting.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class MishapNoSentinel : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context) : FrozenPigment = dyeColor(DyeColor.BLUE)

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Text = error("oneironaut:nosentinel")

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
        stack.add(GarbageIota())
    }
}