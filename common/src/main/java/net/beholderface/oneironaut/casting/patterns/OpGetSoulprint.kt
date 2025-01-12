package net.beholderface.oneironaut.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.text.Text
import net.minecraft.util.Pair
import net.beholderface.oneironaut.casting.mishaps.MishapNoStaff
import net.beholderface.oneironaut.casting.iotatypes.SoulprintIota
import net.minecraft.entity.LivingEntity

class OpGetSoulprint : ConstMediaAction {
    override val argc = 0
    override val mediaCost = MediaConstants.DUST_UNIT / 10
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env !is StaffCastEnv){
            throw MishapNoStaff(Text.translatable("hexcasting.spell.oneironaut:getsoulprint"))
        }
        if (env.castingEntity !is LivingEntity){
            throw MishapBadCaster()
        }
        return listOf(
            SoulprintIota(
                Pair(
                    env.castingEntity!!.uuid,
                    env.castingEntity!!.name.string
                )
            )
        )
    }
}