package net.beholderface.oneironaut.block;

import net.beholderface.oneironaut.Oneironaut;
import net.beholderface.oneironaut.registry.OneironautItemRegistry;
import net.beholderface.oneironaut.registry.OneironautMiscRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.beholderface.oneironaut.Oneironaut;
import net.minecraft.util.registry.Registry;
import net.beholderface.oneironaut.registry.OneironautItemRegistry;
import net.beholderface.oneironaut.registry.OneironautMiscRegistry;
import org.jetbrains.annotations.Nullable;



public class ThoughtSlurry extends FlowableFluid {
    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }
    public static final Identifier ID =
            Identifier.of(Oneironaut.MOD_ID, "thought_slurry");



    public static final Identifier FLOWING_ID =
            Identifier.of(Oneironaut.MOD_ID, "flowing_thought_slurry");

    public static final ThoughtSlurry.Flowing FLOWING_FLUID =
            new ThoughtSlurry.Flowing();
    public static final ThoughtSlurry.Still STILL_FLUID =
            new ThoughtSlurry.Still();

    //public static FlowableFluid THOUGHT_SLURRY;
    //public static FlowableFluid THOUGHT_SLURRY_FLOWING;

    public static final TagKey<Fluid> TAG =
            TagKey.of(Registry.FLUID_KEY, ThoughtSlurry.ID);

    @Override
    public Fluid getFlowing() {
        return OneironautMiscRegistry.THOUGHT_SLURRY_FLOWING.get();
    }

    @Override
    public Fluid getStill() {
        return OneironautMiscRegistry.THOUGHT_SLURRY.get();
    }

@Override
    public FluidState getFlowing(int level, boolean falling) {
        return (this.getFlowing().getDefaultState().with(LEVEL, level)).with(FALLING, falling);
        //return ThoughtSlurry.FLOWING_FLUID;
    }


/*@Override
    public Fluid getFlowing() {
        return Flowing.FLOWING_FLUID;
    }*/




    @Override
    protected boolean isInfinite() {
        return true;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    protected int getFlowSpeed(WorldView world) {
        return 3;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

@Override
    public Item getBucketItem() {
        return OneironautItemRegistry.THOUGHT_SLURRY_BUCKET.get();
       // return Items.LAVA_BUCKET;
    }


    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 5;
    }

    @Override
    protected float getBlastResistance() {
        return 100.0f;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return ThoughtSlurryBlock.INSTANCE.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    @Override
    public boolean isStill(FluidState state) {
        return state.isStill();
    }

    @Override
    public int getLevel(FluidState state) {
        //return state.getLevel();
        return 8;
    }

    @Override
    public RegistryEntry<Fluid> arch$holder() {
        return super.arch$holder();
    }

    @Override
    public @Nullable Identifier arch$registryName() {
        return super.arch$registryName();
    }

    public static class Flowing extends ThoughtSlurry {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(FlowableFluid.LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(FlowableFluid.LEVEL);
        }

    }

    public static class Still extends ThoughtSlurry {
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(FlowableFluid.LEVEL);
        }


        @Override
        public boolean isStill(FluidState state) {
            return true;
        }

        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

    }


}
