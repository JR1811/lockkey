package net.shirojr.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shirojr.entity.custom.SeatEntity;
import net.shirojr.init.LAKEntityTypes;

import java.util.Map;
import java.util.function.Function;

public class EmptyLockerBlock extends LockerBlock {
    public static final MapCodec<EmptyLockerBlock> CODEC = simpleCodec(EmptyLockerBlock::new);
    private static final Function<Part, Map<Direction, VoxelShape>> CUTOUT_SHAPE = Util.memoize(
            part -> Shapes.rotateHorizontal(
                    Shapes.joinUnoptimized(
                            Block.box(2.0, 2.0, 0.0, 14.0, 14.0, 14.0),
                            part.getCutoutShape(),
                            BooleanOp.OR
                    )
            )
    );

    public EmptyLockerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!state.getBlock().equals(oldState.getBlock())) {
            if (level instanceof ServerLevel serverLevel) {
                SeatEntity seatEntity = LAKEntityTypes.SEAT.create(serverLevel, EntitySpawnReason.EVENT);
                if (seatEntity != null) {
                    seatEntity.setAttachedPos(pos);
                }
            }
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape cutout = CUTOUT_SHAPE.apply(state.getValue(PART)).get(state.getValue(FACING));
        return Shapes.joinUnoptimized(super.getShape(state, level, pos, context), cutout, BooleanOp.ONLY_FIRST);
    }
}
