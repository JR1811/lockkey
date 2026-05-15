package net.shirojr.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.shirojr.init.LAKGamerules;
import net.shirojr.network.packet.SyncAllLockableS2CPayload;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class NBVNetworkingDataHolder {
    private static NBVNetworkingDataHolder instance;

    public boolean cachedAllLockableGamerule;


    private NBVNetworkingDataHolder() {
    }

    public static NBVNetworkingDataHolder getInstance() {
        if (instance == null) instance = new NBVNetworkingDataHolder();
        return instance;
    }

    public boolean areAllBlocksLockable(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(LAKGamerules.ALL_LOCKABLE.entry());
        }
        return this.cachedAllLockableGamerule;
    }

    public void setAllLockable(boolean allLockable, @Nullable Collection<ServerPlayer> targets) {
        this.cachedAllLockableGamerule = allLockable;
        if (targets != null) {
            new SyncAllLockableS2CPayload(allLockable).send(targets);
        }
    }
}
