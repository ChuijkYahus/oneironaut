package net.beholderface.oneironaut.casting.patterns.rod

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.beholderface.oneironaut.casting.environments.ReverbRodCastEnv
import net.beholderface.oneironaut.casting.mishaps.MishapNoRod
import net.minecraft.text.Text

class OpAccessRAM(val store : Boolean) : ConstMediaAction {
    override val argc = if (store) { 1 } else { 0 }
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env !is ReverbRodCastEnv){
            throw MishapNoRod(false)
        } else {
            return if (store){
                val iotaToStore = args[0]
                if (iotaToStore.type.equals(ListIota.TYPE) || iotaToStore.type.toString().contains("DictionaryIota", true)){
                    throw MishapInvalidIota(iotaToStore, 0, Text.translatable("oneironaut.mishap.nolistsallowed"))
                }
                //not going to check for truenames because it's not like this is persistent storage or anything
                env.setStoredIota(iotaToStore)
                listOf()
            } else {
                listOf(env.storedIota)
            }
        }
    }
}