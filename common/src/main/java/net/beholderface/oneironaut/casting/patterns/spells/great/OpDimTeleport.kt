package net.beholderface.oneironaut.casting.patterns.spells.great

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapImmuneEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapLocationInWrongDimension
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.api.player.FlightAbility
import at.petrak.hexcasting.common.blocks.BlockConjured
import at.petrak.hexcasting.common.lib.HexBlocks
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.beholderface.oneironaut.*
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.block.Blocks
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.TeleportTarget
import net.beholderface.oneironaut.casting.DepartureEntry
import net.minecraft.util.math.Vec3i
import ram.talia.hexal.api.div
//import net.oneironaut.registry.OneironautThingRegistry
import kotlin.math.floor




class OpDimTeleport : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        if (env.castingEntity == null){
            throw MishapBadCaster()
        }
        val target = args.getLivingEntityButNotArmorStand(0, argc)
        env.assertEntityInRange(target)
        val origin = env.world
        val coords = target.pos
        var noosphere = false
        val destination = if (args[1] is NullIota){
            noosphere = true;
            Oneironaut.getNoosphere()
        } else {
            args.getDimIota(1, argc).toWorld(env.world.server)
        }
        val worldKey = destination.registryKey
        //do not do the bad thing
        if (!HexConfig.server().canTeleportInThisDimension(worldKey))
            throw MishapLocationInWrongDimension(worldKey.value)
        if (!target.canUsePortals() || target.type.isIn(HexTags.Entities.CANNOT_TELEPORT))
            throw MishapImmuneEntity(target)
        if (target.isPlayer && target != env.caster as LivingEntity && !OneironautConfig.server.planeShiftOtherPlayers){
            throw MishapImmuneEntity(target)
        }

        var departure = false
        if (target == env.caster){
            val entry = DepartureEntry.getEntry(env, destination)
            if (entry != null){
                if (entry.isWithinCylinder(target.pos)){
                    departure = true
                }
            }
        }

        val cost = if(departure){
            5 * MediaConstants.DUST_UNIT
        } else {
            20 * MediaConstants.CRYSTAL_UNIT
        }

        return if (origin == destination && !noosphere){
            SpellAction.Result(
                Spell(target, origin, destination, coords, false),
                //don't consume amethyst if trying to teleport to the same dimension you're already in
                0,
                listOf(ParticleSpray.cloud(target.pos, 2.0))
            )
        } else {
            SpellAction.Result(
                Spell(target, origin, destination, coords, noosphere),
                cost,
                listOf(ParticleSpray.cloud(target.pos, 2.0))
            )
        }
    }

    private data class Spell(var target: LivingEntity, val origin: ServerWorld, val destination: ServerWorld, val coords: Vec3d, val noosphere: Boolean) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            var x = coords.x
            var y = floor(coords.y)
            var z = coords.z
            val border = destination.worldBorder
            val compressionFactor = origin.dimension.coordinateScale / destination.dimension.coordinateScale
            x *= compressionFactor
            z *= compressionFactor
            var isFlying = false
            var flightSpell : FlightAbility? = null
            if (target is ServerPlayerEntity){
                val playerTarget = target as ServerPlayerEntity
                isFlying = playerTarget.abilities.flying
                flightSpell = IXplatAbstractions.INSTANCE.getFlight(playerTarget)
                if (target == env.caster){
                    DepartureEntry(env, origin)
                    val entry = DepartureEntry.getEntry(env, destination)
                    if (entry != null){
                        if (entry.isWithinCylinder(Vec3d(x, 0.0, z))){
                            //Oneironaut.LOGGER.info("Found an existing departure, teleporting there.")
                            val entryOrigin = entry.originPos
                            playerTarget.teleport(destination, entryOrigin.x, entryOrigin.y, entryOrigin.z, target.yaw, target.pitch)
                            playerTarget.abilities.flying = isFlying
                            playerTarget.sendAbilitiesUpdate()
                            return
                        }
                    }
                    //Oneironaut.LOGGER.info("No existing departure found, behaving as normal.")
                }
                playerTarget.sendAbilitiesUpdate()
            }
            var floorSpot : BlockPos = BlockPos(Vec3i.ZERO)
            var floorNeeded = false
            //make sure you don't end up under the nether or something
            if (destination.bottomY > coords.y - 5.0){
                y = ((destination.bottomY + 5).toDouble())
            }
            //make sure you don't end up outside the world border
            if (x > border.boundEast){
                x = border.boundEast - 2
            } else if (x < border.boundWest){
                x = border.boundWest + 2
            }
            if (z > border.boundSouth){
                z = border.boundSouth - 2
            } else if (z < border.boundNorth){
                z = border.boundNorth + 2
            }
            //actually put you on the floor if possible
            var scanPoint = BlockPos(Vec3d(x, y+1, z).toVec3i())
            if (!isFlying){
                while(!isSolid(destination, scanPoint)){
                    scanPoint = BlockPos(Vec3d(x, scanPoint.y.toDouble() - 1, z).toVec3i())
                    //check for void
                    if (scanPoint.y < destination.bottomY || isUnsafe(destination, scanPoint, false)){
                        scanPoint = BlockPos(Vec3d(x, y+1, z).toVec3i())
                        break
                    }
                }
            }
            //try to avoid putting your head in solid rock or something
            while(isUnsafe(destination, scanPoint, true) || isSolid(destination, scanPoint)){
                scanPoint = BlockPos(Vec3d(x, scanPoint.y.toDouble() + 1, z).toVec3i())
                //check for ceiling
                if (destination.getBlockState(scanPoint).block.equals(Blocks.BEDROCK)){
                    break
                }
            }
            if (!(destination.getBlockState(scanPoint).block.equals(Blocks.BEDROCK))){
                if (isUnsafe(destination, BlockPos(Vec3d(x, (scanPoint.y - 1).toDouble(), z).toVec3i()), true) || !isSolid(destination, BlockPos(Vec3d(x, (scanPoint.y - 1).toDouble(), z).toVec3i()))){
                    y = (scanPoint.y + 1).toDouble()
                    if (!isSolid(destination, BlockPos(Vec3d(x, (scanPoint.y - 1).toDouble(), z).toVec3i()))){
                        floorNeeded = true
                        floorSpot = BlockPos(Vec3d(x, (scanPoint.y - 1).toDouble(), z).toVec3i())
                    }
                }
                y = (scanPoint.y).toDouble()
            }
            val colorizer = env.pigment
            if (origin == destination){
                env.caster!!.sendMessage(Text.translatable("hexcasting.spell.oneironaut:dimteleport.samedim"));
            } else {
                if (target is ServerPlayerEntity){
                    val playerTarget = target as ServerPlayerEntity
                    playerTarget.teleport(destination, x, y, z, target.yaw, target.pitch)
                    if (flightSpell != null){
                        val compressedOrigin = Vec3d(flightSpell.origin.x * compressionFactor, flightSpell.origin.y, flightSpell.origin.z * compressionFactor)
                        val newFlight = FlightAbility(flightSpell.timeLeft, destination.registryKey, compressedOrigin, flightSpell.radius)
                        IXplatAbstractions.INSTANCE.setFlight(playerTarget, newFlight)
                    }
                    playerTarget.abilities.flying = isFlying
                    playerTarget.sendAbilitiesUpdate()

                    //FabricDimensions.teleport(target, destination, TeleportTarget(Vec3d(x, y, z), Vec3d.ZERO, target.yaw, target.pitch))
                    if (noosphere){
                        target.addStatusEffect(StatusEffectInstance(StatusEffects.NAUSEA, 200))
                        target.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 100))
                    }
                    if (floorNeeded && !isFlying){
                        destination.setBlockState((floorSpot), HexBlocks.CONJURED_BLOCK.defaultState)
                        BlockConjured.setColor(destination, floorSpot, colorizer)
                    }
                } else {
                    FabricDimensions.teleport(target, destination, TeleportTarget(Vec3d(x, y, z), target.velocity, target.yaw, target.pitch))
                    if (floorNeeded && !isFlying){
                        destination.setBlockState((floorSpot), HexBlocks.CONJURED_BLOCK.defaultState)
                        BlockConjured.setColor(destination, floorSpot, colorizer)
                    }
                }
                if (!isFlying){
                    target.addStatusEffect(StatusEffectInstance(StatusEffects.SLOW_FALLING, 1200))
                }
            }
        }
    }
}