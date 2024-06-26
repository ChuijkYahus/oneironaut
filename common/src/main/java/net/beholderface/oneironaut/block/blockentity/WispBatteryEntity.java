package net.beholderface.oneironaut.block.blockentity;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.utils.MediaHelper;
import at.petrak.hexcasting.common.items.colorizer.ItemDyeColorizer;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import kotlin.collections.CollectionsKt;
import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.block.WispBattery;
import net.beholderface.oneironaut.registry.OneironautBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ram.talia.hexal.api.FunUtilsKt;
import ram.talia.hexal.common.entities.WanderingWisp;

import java.util.ArrayList;

public class WispBatteryEntity extends BlockEntity implements SidedInventory {
    public static final int CAPACITY = MediaConstants.DUST_UNIT * 6400;
    private int media = 0;
    public WispBatteryEntity(BlockPos pos, BlockState state){
        super(OneironautBlockRegistry.WISP_BATTERY_ENTITY.get(), pos, state);
    }

    public static int[] getColors(Random random){
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 32; i++){
            for (ItemDyeColorizer color : HexItems.DYE_COLORIZERS.values()){
                colors.add(FunUtilsKt.nextColour((new FrozenColorizer(new ItemStack(color), Util.NIL_UUID)), random));
            }
        }
        return CollectionsKt.toIntArray(colors);
    }

    public void tick(World world, BlockPos pos, BlockState state){
        //only do anything when powered
        if (state.get(WispBattery.REDSTONE_POWERED)){
            if (world.isClient){
                Vec3d doublePos = new Vec3d(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
                int[] colors = getColors(world.random);
                world.addParticle(
                        new ConjureParticleOptions(colors[world.random.nextInt(colors.length)], true),
                        doublePos.x, doublePos.y, doublePos.z,
                        0.0125 * (world.random.nextDouble() - 0.5),
                        0.0125 * (world.random.nextDouble() - 0.5),
                        0.0125 * (world.random.nextDouble() - 0.5)
                );
            } else {
                if (world.getTime() % 80 == 0 && world.getEntitiesByClass(WanderingWisp.class, Box.of(Vec3d.ofCenter(pos), 64.0, 64.0, 64.0), (idfk)-> true).size() < 20){
                    int wispSpawnCost = MediaConstants.CRYSTAL_UNIT * 2;
                    if (this.media >= wispSpawnCost || this.media < 0){
                        WanderingWisp wisp = new WanderingWisp(world, Vec3d.ofCenter(pos, 1));
                        wisp.setColouriser(new FrozenColorizer(
                                new ItemStack(CollectionsKt.elementAt(HexItems.DYE_COLORIZERS.values(),
                                        world.random.nextInt(HexItems.DYE_COLORIZERS.size()))),
                                Util.NIL_UUID
                        ));
                        world.spawnEntity(wisp);
                        if (this.media > 0){
                            this.media = this.media - wispSpawnCost;
                        }
                        this.sync();
                    }
                }
            }
        }
    }

    private static final int[] SLOTS = {0};

    @Override
    public int[] getAvailableSlots(Direction side) {
        return SLOTS;
    }

    public int remainingMediaCapacity() {
        if (this.media < 0) {
            return 0;
        }
        return Math.max(0, CAPACITY - this.media);
    }

    public int extractMediaFromItem(ItemStack stack, boolean simulate) {
        if (this.media < 0) {
            return 0;
        }
        return MediaHelper.extractMedia(stack, remainingMediaCapacity(), true, simulate);
    }

    public void insertMedia(ItemStack stack) {
        if (getMedia() >= 0 && !stack.isEmpty() && stack.getItem() == HexItems.CREATIVE_UNLOCKER) {
            setInfiniteMedia();
            stack.decrement(1);
        } else {
            var mediamount = extractMediaFromItem(stack, false);
            if (mediamount > 0) {
                this.media = Math.min(mediamount + media, CAPACITY);
                this.sync();
            }
        }
    }

    public void setInfiniteMedia() {
        this.media = -1;
        this.sync();
    }

    @Override
    public boolean isValid(int index, ItemStack stack) {
        if (remainingMediaCapacity() == 0) {
            return false;
        }

        if (stack.isOf(HexItems.CREATIVE_UNLOCKER)) {
            return true;
        }

        var mediamount = extractMediaFromItem(stack, true);
        return mediamount > 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return ItemStack.EMPTY.copy();
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return ItemStack.EMPTY.copy();
    }

    @Override
    public ItemStack removeStack(int slot) {
        return ItemStack.EMPTY.copy();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        insertMedia(stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {

    }

    public int getMedia() {
        return this.media;
    }

    public void sync() {
        this.markDirty();
        this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("media", this.media);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.media = nbt.getInt("media");
    }
}
