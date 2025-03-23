package net.beholderface.oneironaut.casting.patterns

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.beholderface.oneironaut.casting.environments.ExtradimensionalCastEnv
import net.beholderface.oneironaut.casting.iotatypes.DimIota
import net.beholderface.oneironaut.casting.mishaps.MishapExtradimensionalFail
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class OpEvalExtradimensional : Action {
    override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation
    ): OperationResult {
        if (env !is PlayerBasedCastEnv){
            throw MishapBadCaster()
        }
        val stack = image.stack.toMutableList()
        val toExecute = stack.removeLastOrNull() ?: throw MishapNotEnoughArgs(2, 0)
        val dimension = stack.removeLastOrNull() ?: throw MishapNotEnoughArgs(2, 1)
        if (dimension !is DimIota){
            throw MishapInvalidIota.ofType(dimension, 1, "oneironaut:imprint")
        }
        val newEnv = ExtradimensionalCastEnv(env.caster, env, dimension.toWorld(env.world.server))
        return exec(newEnv, image.copy(stack = stack), continuation, stack, toExecute, dimension.toWorld(env.world.server))
    }

    fun exec(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation, newStack: MutableList<Iota>, instrs: Iota, dimension : ServerWorld): OperationResult {
        val toExecute = if (instrs is ListIota){
            instrs.list.toList()
        } else {
            listOf(instrs)
        }
        val subHarness = CastingVM.empty(env)
        subHarness.image = image
        val executionResult = subHarness.queueExecuteAndWrapIotas(toExecute, env.world)
        if (!executionResult.resolutionType.success){
            throw MishapExtradimensionalFail()
        }
        return OperationResult(subHarness.image.withUsedOp(), listOf(), continuation, HexEvalSounds.HERMES)
    }
}