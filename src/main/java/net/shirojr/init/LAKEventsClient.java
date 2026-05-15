package net.shirojr.init;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.shirojr.event.LAKItemTooltipEvents;

public interface LAKEventsClient {

    static void initialize() {
        // static initialisation
        ItemTooltipCallback.EVENT.register(new LAKItemTooltipEvents());
    }
}
