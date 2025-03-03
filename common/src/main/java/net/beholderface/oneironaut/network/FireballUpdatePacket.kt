package net.beholderface.oneironaut.network

import at.petrak.hexcasting.common.msgs.IMessage
import io.netty.buffer.ByteBuf
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.projectile.ExplosiveProjectileEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.beholderface.oneironaut.Oneironaut
import net.minecraft.entity.Entity
import java.util.ConcurrentModificationException


class FireballUpdatePacket(val targetVelocity : Vec3d, val entity : ExplosiveProjectileEntity?) : IMessage {
    override fun serialize(buf: PacketByteBuf) {
        buf.writeDouble(targetVelocity.x)
        buf.writeDouble(targetVelocity.y)
        buf.writeDouble(targetVelocity.z)
        entity?.x?.let { buf.writeDouble(it) }
        entity?.y?.let { buf.writeDouble(it) }
        entity?.z?.let { buf.writeDouble(it) }
        entity?.id?.let { buf.writeInt(it) }
    }

    override fun getFabricId() = ID

    companion object {
        @JvmField
        val ID: Identifier = Identifier(Oneironaut.MOD_ID, "fireballupdate")

        @JvmStatic
        fun deserialise(buffer: ByteBuf): FireballUpdatePacket? {
            val buf = PacketByteBuf(buffer)
            val targetVelocity = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
            val entityPos = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
            //val entityUUID = buf.readUuid()
            val entityID = buf.readInt()
            var foundEntity : Entity? = null
            val world = MinecraftClient.getInstance().world
            foundEntity = world?.getEntityById(entityID)
            if (foundEntity == null){
                return null
            }
            return FireballUpdatePacket(targetVelocity, foundEntity as ExplosiveProjectileEntity)
        }

        @JvmStatic
        fun handle(self: FireballUpdatePacket?) {
            if (self == null){
                return
            } else if (self.entity == null){
                return
            }
            MinecraftClient.getInstance().execute {
                val targetVelocity = self.targetVelocity
                val entityToUpdate = self.entity
                try {
                    entityToUpdate.powerX = targetVelocity.x
                    entityToUpdate.powerY = targetVelocity.y
                    entityToUpdate.powerZ = targetVelocity.z
                } catch (e : ConcurrentModificationException){
                    Oneironaut.LOGGER.debug("oopsie, concurrent modification!\n$e")
                }
            }
        }
    }
}