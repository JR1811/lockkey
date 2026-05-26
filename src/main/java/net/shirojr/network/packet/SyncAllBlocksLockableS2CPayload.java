package net.shirojr.network.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.shirojr.LAKMain;

import java.util.Collection;

public record SyncAllBlocksLockableS2CPayload(boolean allLockable) implements CustomPacketPayload {
    public static final Identifier ALL_BLOCKS_LOCKABLE_SYNC_ID = LAKMain.getId("all_blocks_lockable_sync");
    public static final CustomPacketPayload.Type<SyncAllBlocksLockableS2CPayload> TYPE = new CustomPacketPayload.Type<>(ALL_BLOCKS_LOCKABLE_SYNC_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAllBlocksLockableS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SyncAllBlocksLockableS2CPayload::allLockable,
            SyncAllBlocksLockableS2CPayload::new);


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void send(Collection<ServerPlayer> targets) {
        for (ServerPlayer target : targets) {
            ServerPlayNetworking.send(target, this);
        }
    }
}
