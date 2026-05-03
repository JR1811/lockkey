package net.shirojr.data.gen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.shirojr.block.LockerBlock;
import net.shirojr.init.LAKBlockStateProperties;
import net.shirojr.init.LAKBlocks;
import net.shirojr.init.LAKItems;
import net.shirojr.util.IdentifierHolder;

public class LAKModelProvider extends FabricModelProvider {
    private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
            .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
            .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
            .select(Direction.WEST, BlockModelGenerators.Y_ROT_270)
            .select(Direction.NORTH, BlockModelGenerators.NOP);

    public LAKModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generateLocker(generators, LAKBlocks.LOCKER_EMPTY, LAKBlocks.LOCKER_EMPTY);
        generateLocker(generators, LAKBlocks.LOCKER_STORAGE, LAKBlocks.LOCKER_EMPTY);
        generateLocker(generators, LAKBlocks.LOCKER_TRAP, LAKBlocks.LOCKER_EMPTY);
        generateLocker(generators, LAKBlocks.LOCKER_ALARM, LAKBlocks.LOCKER_EMPTY);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(LAKItems.KEY, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(LAKItems.LOCK, ModelTemplates.FLAT_ITEM);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends LockerBlock, M extends LockerBlock> void generateLocker(BlockModelGenerators generators,
                                                                                      IdentifierHolder<T> locker,
                                                                                      IdentifierHolder<M> model) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(locker.entry())
                .with(PropertyDispatch.initial(LAKBlockStateProperties.LOCKER_PART)
                        .generate(part -> getLockerPartVariants(model.entry(), part))
                )
                .with(ROTATION_HORIZONTAL_FACING)
        );
        generators.itemModelOutput.accept(
                locker.entry().asItem(),
                ItemModelUtils.plainModel(
                        ModelLocationUtils.getModelLocation(model.entry(),
                                LockerBlock.Part.SINGLE.asSuffix())
                )
        );
    }

    private static MultiVariant getLockerPartVariants(Block block, LockerBlock.Part part) {
        return BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block, part.asSuffix()));
    }
}
