package net.beholderface.oneironaut.casting.iotatypes;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.beholderface.oneironaut.registry.OneironautIotaTypeRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;

//import java.awt.*;

public class DimIota extends Iota{
    public DimIota(@NotNull String dim){
        super(OneironautIotaTypeRegistry.DIM, dim);
    }

    /*public NbtElement getKey(){
        var ctag = HexUtils.downcast(this.payload, NbtCompound.TYPE);
        return (RegistryKey<World>) ctag.get("dim_key");
    }*/

    @Override
    public boolean isTruthy() {
        return true;
    }

    protected boolean toleratesOther(Iota that) {
        if (that.getType().equals(this.type)){
            DimIota other = (DimIota) that;
            return this.payload.equals(other.payload);
        }
        return false;
    }

    public String getDimString(){
        return this.payload.toString();
    }

    public @NotNull NbtElement serialize() {
        var data = new NbtCompound();
        var payload = this.payload;
        data.putString("dim_key", (String) payload);
        return data;
    }
    public static IotaType<DimIota> TYPE = new IotaType<>() {
        @Override
        public DimIota deserialize(NbtElement tag, ServerWorld world) throws IllegalArgumentException {
            var ctag = HexUtils.downcast(tag, NbtCompound.TYPE);
            return new DimIota(ctag.getString("dim_key"));
        }

        @Override
        public Text display(NbtElement tag) {
            //return Text.translatable("text.oneironaut.dimiota.name");
            var ctag = HexUtils.downcast(tag, NbtCompound.TYPE);
            var text = Text.of(ctag.getString("dim_key"));
            //var id = world.getValue().toString();
            Style originalStyle = text.getStyle();
            Style formattedStyle = switch (text.getString()) {
                case "minecraft:overworld" -> originalStyle.withColor(0x00aa00);
                case "minecraft:the_nether" -> originalStyle.withColor(0xaa0000);
                case "minecraft:the_end" -> originalStyle.withColor(0xffff55);
                case "oneironaut:noosphere" -> originalStyle.withColor(0xaa00aa).withBold(true);
                default -> originalStyle.withColor(0x5555ff);
            };
            return text.copy().setStyle(formattedStyle);
            //return Text.of("bees");
            //return Text.of(ctag.get("dim_ID").toString());
        }
        @Override
        public int color() {
            return 0xff_5555FF;
        }
    };
}
