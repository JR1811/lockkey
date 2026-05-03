package net.shirojr.block;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shirojr.init.LAKBlockStateProperties;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.function.Predicate;

public abstract class LockerBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<Part> PART = LAKBlockStateProperties.LOCKER_PART;

    public LockerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(PART, Part.SINGLE)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN, PART, WATERLOGGED, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState stateBelow = level.getBlockState(pos.below());
        BlockState stateAbove = level.getBlockState(pos.above());

        Direction direction = null;
        if (player != null && !player.isCrouching()) {
            if (stateBelow.is(this)) {
                direction = stateBelow.getValue(FACING);
            } else if (stateAbove.is(this)) {
                direction = stateAbove.getValue(FACING);
            }
        }
        if (direction == null) {
            direction = context.getHorizontalDirection().getOpposite();
        }

        return this.defaultBlockState()
                .setValue(WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER))
                .setValue(FACING, direction)
                .setValue(PART, Part.getFromSurrounding(stateBelow, stateAbove, otherState -> otherState.is(this)));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState,
                                     RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        Direction facing = state.getValue(FACING);
        state = state.setValue(PART, Part.getFromSurrounding(level, pos, otherState ->
                otherState.is(this) && otherState.getValue(FACING).equals(facing)
        ));
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected FluidState getFluidState(final BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    public enum Part implements StringRepresentable {
        SINGLE(Shapes.empty()),
        BOTTOM(Block.box(2.0, 14.0, 0.0, 14.0, 16.0, 14.0)),
        MIDDLE(Block.box(2.0, 0.0, 0.0, 14.0, 16.0, 14.0)),
        TOP(Block.box(2.0, 0.0, 0.0, 14.0, 2.0, 14.0));

        public static final Codec<Part> CODEC = StringRepresentable.fromEnum(Part::values);

        private final VoxelShape cutoutShape;

        Part(VoxelShape cutoutShape) {
            this.cutoutShape = cutoutShape;
        }

        public VoxelShape getCutoutShape() {
            return cutoutShape;
        }

        public static Part getFromSurrounding(LevelReader level, BlockPos pos, Predicate<BlockState> connectableState) {
            return getFromSurrounding(level.getBlockState(pos.below()), level.getBlockState(pos.above()), connectableState);
        }

        public static Part getFromSurrounding(BlockState stateBelow, BlockState stateAbove, Predicate<BlockState> connectableState) {
            boolean connectsBelow = connectableState.test(stateBelow);
            boolean connectsAbove = connectableState.test(stateAbove);
            if (connectsBelow && connectsAbove) return MIDDLE;
            if (connectsAbove) return BOTTOM;
            if (connectsBelow) return TOP;
            return SINGLE;
        }

        public boolean holdsBlockEntity() {
            return this.equals(SINGLE) || this.equals(BOTTOM);
        }

        @Override
        public String toString() {
            return getSerializedName();
        }

        public String asSuffix() {
            return "_" + this;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
