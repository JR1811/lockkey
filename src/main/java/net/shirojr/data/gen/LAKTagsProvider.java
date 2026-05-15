package net.shirojr.data.gen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.shirojr.init.LAKTags;

import java.util.concurrent.CompletableFuture;

public class LAKTagsProvider {
    private static class BlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
        public BlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
            super(output, registryLookupFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider registries) {
            valueLookupBuilder(LAKTags.BlockTags.LOCKABLE)
                    .add(Blocks.LEVER, Blocks.ENCHANTING_TABLE, Blocks.JUKEBOX, Blocks.NOTE_BLOCK, Blocks.RESPAWN_ANCHOR,
                            Blocks.VAULT, Blocks.COPPER_BULB)
                    .addOptionalTag(ConventionalBlockTags.CHESTS)
                    .addOptionalTag(ConventionalBlockTags.BARRELS)
                    .addOptionalTag(BlockTags.BUTTONS)
                    .addOptionalTag(BlockTags.PRESSURE_PLATES)
                    .addOptionalTag(ConventionalBlockTags.PLAYER_WORKSTATIONS_FURNACES)
                    .addOptionalTag(ConventionalBlockTags.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                    .addOptionalTag(BlockTags.ANVIL)
                    .addOptionalTag(BlockTags.CAULDRONS)
                    .addOptionalTag(BlockTags.SHULKER_BOXES)
                    .addOptionalTag(BlockTags.ALL_SIGNS)
                    .addOptionalTag(ConventionalBlockTags.BOOKSHELVES)
                    .addOptionalTag(BlockTags.WOODEN_SHELVES)
                    .addOptionalTag(BlockTags.BEEHIVES)
                    .addOptionalTag(BlockTags.BEDS);
        }
    }

    public static void addProviders(FabricDataGenerator.Pack pack) {
        pack.addProvider(BlockTagProvider::new);
    }
}
