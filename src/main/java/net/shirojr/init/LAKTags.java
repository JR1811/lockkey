package net.shirojr.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.shirojr.LAKMain;
import net.shirojr.util.IdentifierHolder;

import java.util.ArrayList;
import java.util.List;

public interface LAKTags {
    List<IdentifierHolder<TagKey<?>>> ALL = new ArrayList<>();

    interface BlockTags {
        TagKey<Block> SEAT_HOLDER = create("seat_holder");
        TagKey<Block> LOCKABLE = create("lockable");

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

    static void initialize() {
        // static initialisation
        BlockTags.initialize();
    }
}
