package net.shirojr.data.gen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.shirojr.init.LAKBlocks;
import net.shirojr.init.LAKTags;

import java.util.concurrent.CompletableFuture;

public class LAKTagsProvider {
    private static class BlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
        public BlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
            super(output, registryLookupFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider registries) {
            getOrCreateRawBuilder(LAKTags.BlockTags.SEAT_HOLDER).add(
                    TagEntry.element(LAKBlocks.LOCKER_EMPTY.identifier())
            );
            LAKBlocks.LOCKERS.forEach(holder -> getOrCreateRawBuilder(BlockTags.CLIMBABLE).add(TagEntry.element(holder.identifier())));
        }
    }

    public static void addProviders(FabricDataGenerator.Pack pack) {
        pack.addProvider(BlockTagProvider::new);
    }
}
