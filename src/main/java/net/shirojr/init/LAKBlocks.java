package net.shirojr.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.shirojr.LAKMain;
import net.shirojr.block.*;
import net.shirojr.util.IdentifierHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface LAKBlocks {
    List<IdentifierHolder<? extends Block>> ALL = new ArrayList<>();
    List<IdentifierHolder<? extends LockerBlock>> LOCKERS = new ArrayList<>();

    IdentifierHolder<EmptyLockerBlock> LOCKER_EMPTY = registerLocker("locker_empty", EmptyLockerBlock::new,
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops(), true);
    IdentifierHolder<StorageLockerBlock> LOCKER_STORAGE = registerLocker("locker_storage", StorageLockerBlock::new,
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops(), true);
    IdentifierHolder<TrappedLockerBlock> LOCKER_TRAP = registerLocker("locker_trap", TrappedLockerBlock::new,
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops(), true);
    IdentifierHolder<AlarmLockerBlock> LOCKER_ALARM = registerLocker("locker_alarm", AlarmLockerBlock::new,
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops(), true);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> IdentifierHolder<T> register(String name, Function<BlockBehaviour.Properties, T> blockFactory,
                                                BlockBehaviour.Properties settings, boolean registerDefaultItem) {
        Identifier id = LAKMain.getId(name);
        T registeredEntry = Registry.register(
                BuiltInRegistries.BLOCK,
                id,
                blockFactory.apply(settings.setId(ResourceKey.create(Registries.BLOCK, id)))
        );
        IdentifierHolder<T> holder = new IdentifierHolder<T>(registeredEntry, id);
        ALL.add(holder);
        if (registerDefaultItem) {
            LAKItems.register(id, properties -> new BlockItem(registeredEntry, properties), LAKItems.properties());
        }
        return holder;
    }

    private static <T extends LockerBlock> IdentifierHolder<T> registerLocker(String name, Function<BlockBehaviour.Properties, T> blockFactory,
                                                                  BlockBehaviour.Properties settings, boolean registerDefaultItem) {
        IdentifierHolder<T> registeredEntry = register(name, blockFactory, settings, registerDefaultItem);
        LOCKERS.add(registeredEntry);
        return registeredEntry;
    }

    static void initialize() {
        // static initialisation
    }
}
