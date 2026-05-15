package net.shirojr;

import net.fabricmc.api.ClientModInitializer;
import net.shirojr.init.LAKNetworkingS2C;

public class LAKClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LAKNetworkingS2C.initialize();
    }
}
