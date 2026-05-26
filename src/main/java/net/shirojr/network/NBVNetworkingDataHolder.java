package net.shirojr.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.shirojr.init.LAKGamerules;
import net.shirojr.network.packet.SyncAllBlocksLockableS2CPayload;
import net.shirojr.network.packet.SyncAllEntitiesLockableS2CPayload;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class NBVNetworkingDataHolder {
    private static NBVNetworkingDataHolder instance;

    public boolean cachedAllBlocksLockableGamerule;
    public boolean cachedAllEntitiesLockableGamerule;


    private NBVNetworkingDataHolder() {
    }

    public static NBVNetworkingDataHolder getInstance() {
        if (instance == null) instance = new NBVNetworkingDataHolder();
        return instance;
    }

    public boolean areAllBlocksLockable(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(LAKGamerules.ALL_BLOCKS_LOCKABLE.entry());
        }
        return this.cachedAllBlocksLockableGamerule;
    }

    public boolean areAllEntitiesLockable(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(LAKGamerules.ALL_ENTITIES_LOCKABLE.entry());
        }
        return this.cachedAllEntitiesLockableGamerule;
    }

    public void setAllBlocksLockable(boolean allLockable, @Nullable Collection<ServerPlayer> targets) {
        this.cachedAllBlocksLockableGamerule = allLockable;
        if (targets != null) {
            new SyncAllBlocksLockableS2CPayload(allLockable).send(targets);
        }
    }

    public void setAllEntitiesLockable(boolean allLockable, @Nullable Collection<ServerPlayer> targets) {
        this.cachedAllEntitiesLockableGamerule = allLockable;
        if (targets != null) {
            new SyncAllEntitiesLockableS2CPayload(allLockable).send(targets);
        }
    }
}
