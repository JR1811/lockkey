package net.shirojr.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shirojr.blockentity.StorageLockerBlockEntity;
import org.jspecify.annotations.Nullable;

public class StorageLockerBlock extends LockerBlock implements EntityBlock {
    public static final MapCodec<StorageLockerBlock> CODEC = simpleCodec(StorageLockerBlock::new);

    public StorageLockerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        if (!blockState.getValue(PART).holdsBlockEntity()) return null;
        return new StorageLockerBlockEntity(worldPosition, blockState);
    }

}
