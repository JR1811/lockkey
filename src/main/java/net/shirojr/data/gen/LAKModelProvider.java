package net.shirojr.data.gen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.shirojr.init.LAKItems;

public class LAKModelProvider extends FabricModelProvider {
    public LAKModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(LAKItems.KEY, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(LAKItems.LOCK, ModelTemplates.FLAT_ITEM);
    }
}
