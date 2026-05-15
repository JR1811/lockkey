package net.shirojr.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.shirojr.LAKMain;
import net.shirojr.util.IdentifierHolder;

import java.util.ArrayList;
import java.util.List;

public interface LAKTags {
    List<IdentifierHolder<TagKey<?>>> ALL = new ArrayList<>();

    interface BlockTags {
        TagKey<Block> LOCKABLE = create("lockable");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Block> create(final String name) {
            Identifier id = LAKMain.getId(name);
            TagKey<Block> registeredEntry = TagKey.create(Registries.BLOCK, id);
            LAKTags.ALL.add(new IdentifierHolder<>(registeredEntry, id));
            return registeredEntry;
        }

        static void initialize() {
            // static initialisation
        }
    }

    interface EntityTags {
        TagKey<EntityType<?>> LOCKABLE = create("lockable");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<EntityType<?>> create(final String name) {
            Identifier id = LAKMain.getId(name);
            TagKey<EntityType<?>> registeredEntry = TagKey.create(Registries.ENTITY_TYPE, id);
            LAKTags.ALL.add(new IdentifierHolder<>(registeredEntry, id));
            return registeredEntry;
        }

        static void initialize() {
            // static initialisation
        }
    }

    static void initialize() {
        // static initialisation
        BlockTags.initialize();
        EntityTags.initialize();
    }
}
