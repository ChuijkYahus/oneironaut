package net.oneironaut.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import dev.architectury.platform.Platform
import net.minecraft.entity.EntityType
import net.minecraft.text.Text
import net.oneironaut.casting.ParticleBurstPacket

class OpParticleBurst : ConstMediaAction {
    override val argc = 4
    override val mediaCost = MediaConstants.DUST_UNIT / 100
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        if (Platform.isFabric()){
            val target = args.getVec3(0, argc)
            ctx.assertVecInRange(target)
            var direction = args.getVec3(1, argc)
            if (direction.length() > 2){
                direction = direction.normalize().multiply(2.0)
            }
            val posRandom = args.getPositiveDoubleUnderInclusive(2, 4.0, argc)  /*args.getDoubleBetween(2, 0.0, 4.0, argc)*/
            val speedRandom = args.getPositiveDoubleUnderInclusive(3, 4.0, argc)
            val color = IXplatAbstractions.INSTANCE.getColorizer(ctx.caster)
            IXplatAbstractions.INSTANCE.sendPacketNear(target, 128.0, ctx.world, ParticleBurstPacket(target, direction, posRandom, speedRandom, color, 16, false))
            return listOf()
        } else {
            //definitely never going to be encountered, but still
            ctx.caster.sendMessage(Text.translatable("text.oneironaut.noforgeyet"))
            return listOf(args[0], args[1], args[2], args[3])
        }

    }
}