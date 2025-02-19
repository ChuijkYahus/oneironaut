package net.beholderface.oneironaut.item;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.items.storage.ItemFocus;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.casting.iotatypes.DimIota;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLibraryCard extends Item implements IotaHolderItem {
    public ItemLibraryCard(Settings settings) {
        super(settings);
    }

    @Nullable
    public RegistryKey<World> getDimension(ItemStack stack){
        NbtCompound nbt = stack.getNbt();
        //Oneironaut.LOGGER.info(nbt);
        if(nbt == null || !nbt.contains(ItemFocus.TAG_DATA, NbtElement.COMPOUND_TYPE))
            return null;
        return new DimIota(nbt.getCompound(ItemFocus.TAG_DATA).getCompound("hexcasting:data").getString(DimIota.DIM_KEY)).getWorldKey();
    }

    @Override
    public @Nullable NbtCompound readIotaTag(ItemStack stack) {
        RegistryKey<World> regKey = this.getDimension(stack);
        if (regKey != null){
            return IotaType.serialize(new DimIota(regKey));
        }
        return null;
    }

    @Override
    public boolean writeable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        return iota instanceof DimIota || iota == null;
    }

    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if (iota == null){
            stack.removeSubNbt(ItemFocus.TAG_DATA);
        } else {
            NBTHelper.put(stack, ItemFocus.TAG_DATA, IotaType.serialize(iota));
        }
    }

    @Override
    public void appendTooltip(ItemStack pStack, @Nullable World pLevel, List<Text> pTooltipComponents,
                              TooltipContext pIsAdvanced) {
        IotaHolderItem.appendHoverText(this, pStack, pTooltipComponents, pIsAdvanced);
    }
}
