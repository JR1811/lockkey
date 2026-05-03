package net.shirojr.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.shirojr.data.attachment.LockedDataAttachment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
    @Shadow
    @Final
    private Level level;

    @Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0))
    private void onBlockStateModified(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir, @Local(name = "section") LevelChunkSection section) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!LockedDataAttachment.isLocked(serverLevel, pos, null)) return;
        int localX = pos.getX() & 15;
        int localY = pos.getY() & 15;
        int localZ = pos.getZ() & 15;
        BlockState oldState = section.getBlockState(localX, localY, localZ);
        if (!oldState.getBlock().equals(state.getBlock())) {
            HashSet<ItemStack> removedLocks = LockedDataAttachment.setBlockLock(serverLevel, Set.of(pos), null);
            for (ItemStack stack : removedLocks) {
                LockedDataAttachment.drop(serverLevel, pos.above().getBottomCenter(), stack, SoundSource.BLOCKS);
            }
        }
    }
}
