package net.shirojr.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shirojr.block.LockerBlock;
import net.shirojr.init.LAKBlockEntityTypes;
import net.shirojr.init.LAKBlockStateProperties;

public class LockerBlockEntity extends BlockEntity {


    public LockerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(LAKBlockEntityTypes.LOCKER, worldPosition, blockState);

    }

    public int getSlotAmount(Level level, BlockPos pos) {
        if (!(this.getBlockState().getBlock() instanceof LockerBlock block)) {
            throw new IllegalStateException("No matching BlockState for BlockEntity (%s)".formatted(this.getBlockState()));
        }
        return block.getConnected(level, pos, state -> {
            if (!state.hasProperty(LAKBlockStateProperties.LOCKER_TYPE)) return false;
            return state.getValue(LAKBlockStateProperties.LOCKER_TYPE).equals(LockerBlock.Type.STORAGE);
        });
    }
}
