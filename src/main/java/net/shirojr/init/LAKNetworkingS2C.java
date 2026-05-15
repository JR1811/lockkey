package net.shirojr.init;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.shirojr.network.NBVNetworkingDataHolder;
import net.shirojr.network.packet.SyncAllLockableS2CPayload;

public interface LAKNetworkingS2C {
    static void initialize() {
        // static initialisation
        ClientPlayNetworking.registerGlobalReceiver(SyncAllLockableS2CPayload.TYPE, LAKNetworkingS2C::handleAllLockableGameruleSync);
    }

    static void handleAllLockableGameruleSync(SyncAllLockableS2CPayload packet, ClientPlayNetworking.Context context) {
        NBVNetworkingDataHolder.getInstance().setAllLockable(packet.allLockable(), null);
    }
}
