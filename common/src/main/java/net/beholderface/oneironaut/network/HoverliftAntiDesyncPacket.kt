package net.beholderface.oneironaut.network

import at.petrak.hexcasting.common.msgs.IMessage
import io.netty.buffer.ByteBuf
import net.beholderface.oneironaut.Oneironaut
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

//this packet is just for the server to tell the client "hey you recently used a hoverlift, make sure you aren't holding on to the status effect for too long"
class HoverliftAntiDesyncPacket : IMessage {
    override fun serialize(buf: PacketByteBuf?) {
        //doesn't do anything
    }

    override fun getFabricId() = ID

    companion object {
        @JvmField
        val ID: Identifier = Identifier(Oneironaut.MOD_ID, "hoverliftdesync")

        @JvmStatic
        fun deserialise(buffer: ByteBuf): HoverliftAntiDesyncPacket {
            return HoverliftAntiDesyncPacket()
        }

        @JvmStatic
        fun handle(self: HoverliftAntiDesyncPacket) {
            MinecraftClient.getInstance().execute {
                val player = MinecraftClient.getInstance().player
                if (player != null && player.hasStatusEffect(StatusEffects.SLOW_FALLING)){
                    player.removeStatusEffect(StatusEffects.SLOW_FALLING)
                }
            }
        }
    }
}