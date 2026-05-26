package net.shirojr.init;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.shirojr.network.packet.SyncAllBlocksLockableS2CPayload;
import net.shirojr.network.packet.SyncAllEntitiesLockableS2CPayload;

public interface LAKPayloads {

    static void initialize() {
        // static initialisation
        PayloadTypeRegistry.clientboundPlay().register(SyncAllBlocksLockableS2CPayload.TYPE, SyncAllBlocksLockableS2CPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncAllEntitiesLockableS2CPayload.TYPE, SyncAllEntitiesLockableS2CPayload.CODEC);
    }
}
