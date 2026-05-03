package net.shirojr.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.shirojr.LAKMain;
import net.shirojr.blockentity.StorageLockerBlockEntity;

public interface LAKBlockEntityTypes {
    BlockEntityType<StorageLockerBlockEntity> LOCKER = register(
            "locker",
            StorageLockerBlockEntity::new,
            LAKBlocks.LOCKER_EMPTY.entry(), LAKBlocks.LOCKER_STORAGE.entry(), LAKBlocks.LOCKER_TRAP.entry(), LAKBlocks.LOCKER_ALARM.entry()
    );


    @SuppressWarnings("SameParameterValue")
    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entry, Block... blocks) {
        return Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                LAKMain.getId(name),
                FabricBlockEntityTypeBuilder.<T>create(entry, blocks).build()
        );
    }

    static void initialize() {
        // static initialisation
    }
}
