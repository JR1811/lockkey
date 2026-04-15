package net.shirojr.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import net.shirojr.init.LAKDataAttachments;
import net.shirojr.item.component.GroovesComponent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public record LockedDataAttachment(UUID grooves, @NotNull Optional<ItemStack> lockStack) {
    public static final Codec<LockedDataAttachment> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            UUIDUtil.CODEC.fieldOf("grooves").forGetter(LockedDataAttachment::grooves),
            ItemStack.CODEC.optionalFieldOf("lockStack").forGetter(LockedDataAttachment::lockStack)
    ).apply(builder, LockedDataAttachment::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LockedDataAttachment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, LockedDataAttachment::grooves,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC), LockedDataAttachment::lockStack,
            LockedDataAttachment::new
    );

    @Nullable
    public static HashMap<BlockPos, LockedDataAttachment> get(ChunkAccess chunk) {
        return chunk.getAttached(LAKDataAttachments.LOCKED_POSITIONS);
    }

    @Nullable
    public static LockedDataAttachment get(Entity entity) {
        return entity.getAttached(LAKDataAttachments.LOCKED);
    }

    public static HashMap<BlockPos, LockedDataAttachment> getOrCreate(ChunkAccess chunk) {
        return chunk.getAttachedOrCreate(LAKDataAttachments.LOCKED_POSITIONS);
    }

    public static Optional<LockedDataAttachment> from(@Nullable ItemStack stack) {
        if (stack == null) return Optional.empty();
        return GroovesComponent.from(stack).map(component ->
                new LockedDataAttachment(component.grooves(), Optional.of(stack))
        );
    }

    public static boolean isLocked(Level level, BlockPos pos, @Nullable UUID allowedGrooves) {
        HashMap<BlockPos, LockedDataAttachment> positions = get(level.getChunk(pos));
        if (positions == null || positions.isEmpty()) return false;
        LockedDataAttachment attachment = positions.get(pos);
        if (allowedGrooves == null) return attachment != null;
        return !attachment.grooves().equals(allowedGrooves);
    }

    public static boolean isLocked(Entity entity, @Nullable UUID allowedGrooves) {
        LockedDataAttachment attached = get(entity);
        if (attached == null) return false;
        if (allowedGrooves == null) return true;
        return !attached.grooves().equals(allowedGrooves);
    }

    public static HashSet<ItemStack> setBlockLockWithItem(Level level, Collection<BlockPos> positions, @NotNull ItemStack lockStack) {
        return setBlockLock(level, positions, LockedDataAttachment.from(lockStack).orElse(null));
    }

    public static HashSet<ItemStack> setBlockLock(Level level, Collection<BlockPos> positions, @Nullable LockedDataAttachment attachment) {
        HashSet<ItemStack> returnedLockStacks = new HashSet<>();
        HashMap<BlockPos, ChunkAccess> posToChunkMap = new HashMap<>();
        for (BlockPos entry : positions) {
            posToChunkMap.put(entry, level.getChunk(entry));
        }
        for (var entry : posToChunkMap.entrySet()) {
            ChunkAccess chunk = entry.getValue();
            if (attachment == null) {
                HashMap<BlockPos, LockedDataAttachment> attached = get(chunk);
                if (attached != null) {
                    attached.remove(entry.getKey()).lockStack().ifPresent(returnedLockStacks::add);
                }
            } else {
                HashMap<BlockPos, LockedDataAttachment> newState = getOrCreate(chunk);
                newState.put(entry.getKey(), attachment);
                chunk.setAttached(LAKDataAttachments.LOCKED_POSITIONS, newState);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, entry.getKey(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                }
            }
        }
        return returnedLockStacks;
    }

    public static Optional<ItemStack> setEntityLockWithItem(Entity entity, @NotNull ItemStack lockStack) {
        return setEntityLock(entity, from(lockStack).orElse(null));
    }

    public static Optional<ItemStack> setEntityLock(Entity entity, @Nullable LockedDataAttachment attachment) {
        if (attachment == null) {
            return Optional.ofNullable(entity.removeAttached(LAKDataAttachments.LOCKED)).flatMap(LockedDataAttachment::lockStack);
        }
        Optional<ItemStack> oldLock = Optional.empty();
        LockedDataAttachment oldAttached = get(entity);
        if (oldAttached != null) {
            oldLock = oldAttached.lockStack();
        }
        entity.setAttached(LAKDataAttachments.LOCKED, attachment);
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, entity, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1f, 1f);
        }
        return oldLock;
    }

    public static void drop(ServerLevel level, Vec3 dropPos, Collection<ItemStack> locks, SoundSource soundSource) {
        if (locks.isEmpty()) return;
        for (ItemStack lock : locks) {
            Containers.dropItemStack(level, dropPos.x, dropPos.y, dropPos.z, lock);
        }
        level.playSound(null, dropPos.x, dropPos.y, dropPos.z, SoundEvents.ITEM_FRAME_REMOVE_ITEM, soundSource);
    }

    public static void drop(ServerLevel level, Vec3 dropPos, @Nullable ItemStack lock, SoundSource soundSource) {
        if (lock == null) return;
        Containers.dropItemStack(level, dropPos.x, dropPos.y, dropPos.z, lock);
        level.playSound(null, dropPos.x, dropPos.y, dropPos.z, SoundEvents.ITEM_FRAME_REMOVE_ITEM, soundSource);
    }
}
