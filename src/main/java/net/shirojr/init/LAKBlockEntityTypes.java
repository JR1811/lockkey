package net.shirojr.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.shirojr.LAKMain;
import net.shirojr.blockentity.LockerBlockEntity;

public interface LAKBlockEntityTypes {
    BlockEntityType<LockerBlockEntity> LOCKER = register(
            "locker",
            LockerBlockEntity::new,
            LAKBlocks.LOCKER_SMALL, LAKBlocks.LOCKER_BIG
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
