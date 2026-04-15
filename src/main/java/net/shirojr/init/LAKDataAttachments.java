package net.shirojr.init;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.shirojr.LAKMain;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.util.CodecUtils;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public interface LAKDataAttachments {
    AttachmentType<LockedDataAttachment> LOCKED = register(
            "locked", builder -> builder
                    .persistent(LockedDataAttachment.CODEC)
                    .syncWith(LockedDataAttachment.STREAM_CODEC, AttachmentSyncPredicate.all())
    );
    AttachmentType<HashMap<BlockPos, LockedDataAttachment>> LOCKED_POSITIONS = register(
            "locked_pos", builder -> builder
                    .persistent(Codec.unboundedMap(CodecUtils.BLOCKPOS_STRING_CODEC, LockedDataAttachment.CODEC)
                            .xmap(HashMap::new, Function.identity())
                    )
                    .syncWith(
                            ByteBufCodecs.map(HashMap::new, BlockPos.STREAM_CODEC.cast(), LockedDataAttachment.STREAM_CODEC),
                            AttachmentSyncPredicate.all()
                    )
                    .initializer(HashMap::new)
    );

    @SuppressWarnings("SameParameterValue")
    private static <T> AttachmentType<T> register(String name, Consumer<AttachmentRegistry.Builder<T>> entry) {
        return AttachmentRegistry.create(LAKMain.getId(name), entry);
    }

    static void initialize() {
        // static initialisation
    }
}
