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
import net.shirojr.util.IdentifierHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface LAKBlocks {
    List<IdentifierHolder<? extends Block>> ALL = new ArrayList<>();

    @SuppressWarnings({"SameParameterValue", "unused"})
    private static <T extends Block> IdentifierHolder<T> register(String name, Function<BlockBehaviour.Properties, T> blockFactory,
                                                                  BlockBehaviour.Properties settings, boolean registerDefaultItem) {
        Identifier id = LAKMain.getId(name);
        T registeredEntry = Registry.register(
                BuiltInRegistries.BLOCK,
                id,
                blockFactory.apply(settings.setId(ResourceKey.create(Registries.BLOCK, id)))
        );
        IdentifierHolder<T> holder = new IdentifierHolder<>(registeredEntry, id);
        ALL.add(holder);
        if (registerDefaultItem) {
            LAKItems.register(id, properties -> new BlockItem(registeredEntry, properties), LAKItems.properties());
        }
        return holder;
    }

    static void initialize() {
        // static initialisation
    }
}
