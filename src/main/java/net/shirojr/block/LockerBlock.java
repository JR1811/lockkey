package net.shirojr.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shirojr.blockentity.LockerBlockEntity;
import net.shirojr.init.LAKBlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.function.Predicate;

public class LockerBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final MapCodec<LockerBlock> CODEC = simpleCodec(LockerBlock::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<Part> PART = LAKBlockStateProperties.LOCKER_PART;
    public static final EnumProperty<Type> TYPE = LAKBlockStateProperties.LOCKER_TYPE;


    public LockerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(PART, Part.SINGLE)
                .setValue(TYPE, Type.EMPTY)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN, PART, WATERLOGGED, FACING, TYPE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        if (!blockState.getValue(PART).holdsBlockEntity()) return null;
        return new LockerBlockEntity(worldPosition, blockState);
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
                .setValue(FACING, direction);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState,
                                     RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (this.canConnectTo(state, pos, neighbourState, neighbourPos)) {
            Part newPart = Part.getFromSurrounding(level, pos);
            if (!state.getValue(PART).equals(newPart)) {
                state = state.setValue(PART, newPart);
            }
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }

    @Nullable
    public LockerBlockEntity searchBlockEntity(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos posWalker = pos.mutable();
        BlockState blockState = level.getBlockState(posWalker);
        while (blockState.is(this)) {
            if (level.getBlockEntity(posWalker) instanceof LockerBlockEntity blockEntity) {
                return blockEntity;
            }
            posWalker.move(Direction.DOWN);
        }
        return null;
    }

    public int getConnected(Level level, BlockPos originPos, Predicate<BlockState> countedStates) {
        if (!countedStates.test(level.getBlockState(originPos))) return 0;
        int counter = 1;
        BlockPos.MutableBlockPos posWalker = originPos.mutable();

        while (countedStates.test(level.getBlockState(posWalker.move(Direction.DOWN)))) {
            counter += 1;
        }
        posWalker.set(originPos);
        while (countedStates.test(level.getBlockState(posWalker.move(Direction.UP)))) {
            counter += 1;
        }
        return counter;
    }

    public boolean canConnectTo(BlockState self, BlockPos selfPos, BlockState other, BlockPos otherPos) {
        if (!self.is(this) || !other.is(this)) return false;
        if (!self.getValue(FACING).equals(other.getValue(FACING))) return false;
        if (selfPos.getX() != otherPos.getX() || selfPos.getZ() != otherPos.getZ()) return false;
        return Math.abs(selfPos.getY() - otherPos.getY()) == 1;
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
        SINGLE,
        BOTTOM,
        MID,
        TOP;

        public static final Codec<Part> CODEC = StringRepresentable.fromEnum(Part::values);

        @Nullable
        public static Part getFromSurrounding(LevelReader level, BlockPos pos) {
            BlockState stateBelow = level.getBlockState(pos.below());
            BlockState stateAbove = level.getBlockState(pos.above());
            if (!stateBelow.hasProperty(PART) && !stateAbove.hasProperty(PART)) return SINGLE;
            if (stateBelow.hasProperty(PART) && stateAbove.hasProperty(PART)) return MID;
            if (stateAbove.hasProperty(PART)) return BOTTOM;
            if (stateBelow.hasProperty(PART)) return TOP;
            return null;
        }

        public boolean holdsBlockEntity() {
            return this.equals(SINGLE) || this.equals(BOTTOM);
        }

        @Override
        public String toString() {
            return getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum Type implements StringRepresentable {
        EMPTY, STORAGE, TRAP, ALARM;

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);


        @Override
        public String toString() {
            return this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
