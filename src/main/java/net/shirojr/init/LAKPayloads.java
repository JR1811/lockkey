package net.shirojr.init;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.shirojr.network.packet.SyncAllLockableS2CPayload;

public interface LAKPayloads {

    static void initialize() {
        // static initialisation
        PayloadTypeRegistry.clientboundPlay().register(SyncAllLockableS2CPayload.TYPE, SyncAllLockableS2CPayload.CODEC);
    }
}
