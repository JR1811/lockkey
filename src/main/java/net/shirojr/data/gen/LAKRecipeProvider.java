package net.shirojr.data.gen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.shirojr.LAKMain;
import net.shirojr.init.LAKItems;

import java.util.concurrent.CompletableFuture;

public class LAKRecipeProvider extends FabricRecipeProvider {
    public LAKRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                // HolderLookup.RegistryLookup<Item> itemRegistryLookup = registries.lookupOrThrow(Registries.ITEM);

                shaped(RecipeCategory.MISC, LAKItems.KEY)
                        .pattern("  n")
                        .pattern(" n ")
                        .pattern("i  ")
                        .define('n', Items.IRON_NUGGET)
                        .define('i', Items.IRON_INGOT)
                        .group("locking")
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .save(output);

                shaped(RecipeCategory.MISC, LAKItems.LOCK, 1)
                        .pattern(" n ")
                        .pattern("n n")
                        .pattern("nin")
                        .define('n', Items.IRON_NUGGET)
                        .define('i', Items.IRON_INGOT)
                        .group("locking")
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return LAKMain.MOD_ID + "-Recipe-Provider";
    }
}
