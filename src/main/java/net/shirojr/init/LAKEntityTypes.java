package net.shirojr.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.shirojr.LAKMain;
import net.shirojr.util.IdentifierHolder;

import java.util.ArrayList;
import java.util.List;

public interface LAKEntityTypes {
    List<IdentifierHolder<EntityType<?>>> ALL = new ArrayList<>();

    @SuppressWarnings("unused")
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        Identifier id = LAKMain.getId(name);
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        EntityType<T> registeredEntity = Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
        ALL.add(new IdentifierHolder<>(registeredEntity, id));
        return registeredEntity;
    }

    static void initialize() {
        // static initialisation
    }
}
