package net.shirojr.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shirojr.item.component.GroovesComponent;

import java.util.Optional;

public record SafeItemStackData(ItemStack stack) {
    public static final Codec<SafeItemStackData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Identifier.CODEC.fieldOf("item").forGetter(entry -> BuiltInRegistries.ITEM.getKey(entry.stack().getItem())),
            UUIDUtil.CODEC.optionalFieldOf("grooves").forGetter(entry -> GroovesComponent.from(entry.stack).map(GroovesComponent::grooves)),
            ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(entry -> Optional.ofNullable(entry.stack.getCustomName()))
    ).apply(builder, (identifier, uuid, component) -> {
        Item entry = BuiltInRegistries.ITEM.getValueOrThrow(ResourceKey.create(Registries.ITEM, identifier));
        ItemStack defaultStack = entry.getDefaultInstance();
        uuid.ifPresent(grooves -> GroovesComponent.setGrooves(defaultStack, grooves));
        component.ifPresent(name -> defaultStack.set(DataComponents.CUSTOM_NAME, name));
        return new SafeItemStackData(defaultStack);
    }));
}
