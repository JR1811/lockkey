package net.shirojr.init;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.shirojr.command.LockCommands;
import net.shirojr.event.LAKBlockEvents;
import net.shirojr.event.LAKEntityEvents;

public interface LAKCommonEvents {
    LAKBlockEvents SHARED_BLOCK_EVENTS = new LAKBlockEvents();
    LAKEntityEvents SHARED_ENTITY_EVENTS = new LAKEntityEvents();

    static void initialize() {
        AttackBlockCallback.EVENT.register(SHARED_BLOCK_EVENTS);
        UseBlockCallback.EVENT.register(SHARED_BLOCK_EVENTS);
        UseEntityCallback.EVENT.register(SHARED_ENTITY_EVENTS);

        CommandRegistrationCallback.EVENT.register(new LockCommands());
    }
}
