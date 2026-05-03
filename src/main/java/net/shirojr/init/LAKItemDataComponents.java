package net.shirojr.init;

import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.TooltipProvider;
import net.shirojr.LAKMain;
import net.shirojr.item.component.GroovesComponent;

import java.util.function.Consumer;

public interface LAKItemDataComponents {
    DataComponentType<GroovesComponent> GROOVES = registerWithTooltip("grooves", uuidBuilder -> uuidBuilder
            .persistent(GroovesComponent.CODEC)
            .networkSynchronized(GroovesComponent.STREAM_CODEC)
            .cacheEncoding()
    );


    @SuppressWarnings("SameParameterValue")
    private static <T> DataComponentType<T> register(String name, Consumer<DataComponentType.Builder<T>> consumer) {
        DataComponentType.Builder<T> builder = DataComponentType.builder();
        consumer.accept(builder);
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, LAKMain.getId(name), builder.build());
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends TooltipProvider> DataComponentType<T> registerWithTooltip(String name, Consumer<DataComponentType.Builder<T>> consumer) {
        DataComponentType.Builder<T> builder = DataComponentType.builder();
        consumer.accept(builder);
        DataComponentType<T> registeredEntry = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, LAKMain.getId(name), builder.build());
        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, registeredEntry);
        return registeredEntry;
    }

    static void initialize() {
        // static initialisation
    }
}
