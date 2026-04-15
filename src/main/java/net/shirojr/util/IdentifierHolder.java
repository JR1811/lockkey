package net.shirojr.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public record IdentifierHolder<T>(T entry, Identifier identifier) {
    public <S extends ResourceKey<Registry<T>>> ResourceKey<T> getResourceKey(S registry) {
        return ResourceKey.create(registry, identifier());
    }
}
