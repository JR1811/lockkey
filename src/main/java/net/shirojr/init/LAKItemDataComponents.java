package net.shirojr.init;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.shirojr.LAKMain;
import net.shirojr.item.component.GroovesComponent;

import java.util.function.Consumer;

public interface LAKItemDataComponents {
    DataComponentType<GroovesComponent> GROOVES = register("grooves", uuidBuilder -> uuidBuilder
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

    static void initialize() {
        // static initialisation
    }
}
