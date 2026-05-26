package net.shirojr.data.gen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
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

    private static class EntityTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
        public EntityTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
            super(output, registryLookupFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider registries) {
            valueLookupBuilder(LAKTags.EntityTags.LOCKABLE)
                    .add(EntityType.PIG, EntityType.SHEEP, EntityType.COW, EntityType.CHICKEN, EntityType.HORSE,
                            EntityType.WOLF, EntityType.CAT, EntityType.ARMOR_STAND, EntityType.ARMADILLO)
                    .addOptionalTag(EntityTypeTags.BOAT)
                    .addOptionalTag(ConventionalEntityTypeTags.MINECARTS)
                    .addOptionalTag(ConventionalEntityTypeTags.ITEM_FRAMES)
                    .addOptionalTag(EntityTypeTags.ZOMBIES)
                    .addOptionalTag(EntityTypeTags.SKELETONS)
                    .addOptionalTag(EntityTypeTags.ARTHROPOD)
                    .addOptionalTag(EntityTypeTags.AQUATIC)
                    .addOptionalTag(EntityTypeTags.CAN_EQUIP_SADDLE)
                    .addOptionalTag(EntityTypeTags.CAN_EQUIP_HARNESS)
                    .addOptionalTag(EntityTypeTags.CAN_WEAR_HORSE_ARMOR)
                    .addOptionalTag(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR);
        }
    }

    public static void addProviders(FabricDataGenerator.Pack pack) {
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(EntityTagProvider::new);
    }
}
