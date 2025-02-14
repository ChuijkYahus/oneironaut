package net.beholderface.oneironaut.block;

import at.petrak.hexcasting.common.blocks.circles.BlockSlate;
import net.beholderface.oneironaut.block.blockentity.SpaceBombBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpaceBombBlock extends BlockWithEntity {

    public SpaceBombBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(BlockSlate.ENERGIZED, false));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SpaceBombBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BlockSlate.ENERGIZED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (_world, _pos, _state, _be) -> ((SpaceBombBlockEntity)_be).tick(_world, _pos, _state);
    }
}
