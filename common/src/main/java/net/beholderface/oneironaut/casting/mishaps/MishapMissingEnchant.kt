package net.beholderface.oneironaut.casting.mishaps

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class MishapMissingEnchant(val stack: ItemStack, val enchant: Enchantment) : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.PURPLE)

    override fun particleSpray(ctx: CastingEnvironment) =
        ParticleSpray.burst(stack.holder?.pos!!, 1.0)

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Text =
        error("oneironaut:missingenchant", stack.name, Text.translatable(enchant.translationKey))

    override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
        stack.add(GarbageIota())
        if (ctx.castingEntity != null && ctx.castingEntity is ServerPlayerEntity){
            (ctx.castingEntity as ServerPlayerEntity).setExperienceLevel(((ctx.castingEntity as ServerPlayerEntity).experienceLevel - 3).coerceAtLeast(0))
        }
    }

    companion object {
        @JvmStatic
        fun of(stack: ItemStack, enchant: Enchantment): MishapMissingEnchant {
            return MishapMissingEnchant(stack, enchant)
        }
    }

}