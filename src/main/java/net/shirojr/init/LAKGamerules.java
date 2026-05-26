package net.shirojr.init;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.shirojr.LAKMain;
import net.shirojr.network.NBVNetworkingDataHolder;
import net.shirojr.util.IdentifierHolder;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface LAKGamerules {
    List<IdentifierHolder<? extends GameRule<?>>> ALL = new ArrayList<>();

    IdentifierHolder<GameRule<Boolean>> ALL_BLOCKS_LOCKABLE = register(
            "all_blocks_lockable",
            GameRuleBuilder.forBoolean(false).category(GameRuleCategory.MISC),
            (value, server) -> NBVNetworkingDataHolder.getInstance().setAllBlocksLockable(value, PlayerLookup.all(server))
    );

    IdentifierHolder<GameRule<Boolean>> ALL_ENTITIES_LOCKABLE = register(
            "all_entities_lockable",
            GameRuleBuilder.forBoolean(false).category(GameRuleCategory.MISC),
            (value, server) -> NBVNetworkingDataHolder.getInstance().setAllEntitiesLockable(value, PlayerLookup.all(server))
    );

    @SuppressWarnings("SameParameterValue")
    private static <T> IdentifierHolder<GameRule<T>> register(String path, GameRuleBuilder<T> entry, GameRuleEvents.@Nullable ValueUpdate<T> callback) {
        Identifier id = LAKMain.getId(path);
        GameRule<T> registeredEntry = entry.buildAndRegister(id);
        IdentifierHolder<GameRule<T>> holder = new IdentifierHolder<>(registeredEntry, id);
        if (callback != null) {
            GameRuleEvents.changeCallback(registeredEntry).register(callback);
        }
        ALL.add(holder);
        return holder;
    }

    static void initialize() {
        // static initialisation
    }
}
