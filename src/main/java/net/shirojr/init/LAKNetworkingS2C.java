package net.shirojr.init;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.shirojr.network.NBVNetworkingDataHolder;
import net.shirojr.network.packet.SyncAllBlocksLockableS2CPayload;
import net.shirojr.network.packet.SyncAllEntitiesLockableS2CPayload;

public interface LAKNetworkingS2C {
    static void initialize() {
        // static initialisation
        ClientPlayNetworking.registerGlobalReceiver(SyncAllBlocksLockableS2CPayload.TYPE, LAKNetworkingS2C::handleAllBlocksLockableGameruleSync);
        ClientPlayNetworking.registerGlobalReceiver(SyncAllEntitiesLockableS2CPayload.TYPE, LAKNetworkingS2C::handleAllEntitiesLockableGameruleSync);
    }

    static void handleAllBlocksLockableGameruleSync(SyncAllBlocksLockableS2CPayload packet, ClientPlayNetworking.Context context) {
        NBVNetworkingDataHolder.getInstance().setAllBlocksLockable(packet.allLockable(), null);
    }

    static void handleAllEntitiesLockableGameruleSync(SyncAllEntitiesLockableS2CPayload packet, ClientPlayNetworking.Context context) {
        NBVNetworkingDataHolder.getInstance().setAllEntitiesLockable(packet.allLockable(), null);
    }
}
