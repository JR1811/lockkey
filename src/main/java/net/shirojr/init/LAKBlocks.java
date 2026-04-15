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
import net.shirojr.block.LockerBlock;
import net.shirojr.util.IdentifierHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface LAKBlocks {
    List<IdentifierHolder<Block>> ALL_BLOCKS = new ArrayList<>();

    LockerBlock LOCKER_SMALL = register("locker_small", LockerBlock::new,
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops(), true);
    LockerBlock LOCKER_BIG = register("locker_big", LockerBlock::new,
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops(), true);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> blockFactory,
                                                BlockBehaviour.Properties settings, boolean registerDefaultItem) {
        Identifier id = LAKMain.getId(name);
        T registeredEntry = Registry.register(
                BuiltInRegistries.BLOCK,
                id,
                blockFactory.apply(settings.setId(ResourceKey.create(Registries.BLOCK, id)))
        );
        ALL_BLOCKS.add(new IdentifierHolder<>(registeredEntry, id));
        if (registerDefaultItem) {
            LAKKeyItems.register(id, properties -> new BlockItem(registeredEntry, properties), LAKKeyItems.properties());
        }
        return registeredEntry;
    }

    static void initialize() {
        // static initialisation
    }
}
