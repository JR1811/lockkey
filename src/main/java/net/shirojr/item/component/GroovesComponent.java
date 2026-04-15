package net.shirojr.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.shirojr.LAKMain;
import net.shirojr.init.LAKItemDataComponents;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public record GroovesComponent(UUID grooves) implements TooltipProvider {
    public static final String TOOLTIP_TRANSLATION_KEY = "tooltip.%s.grooves".formatted(LAKMain.MOD_ID);
    public static final Codec<GroovesComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            UUIDUtil.CODEC.fieldOf("grooves").forGetter(GroovesComponent::grooves)
    ).apply(builder, GroovesComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GroovesComponent> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, GroovesComponent::grooves,
            GroovesComponent::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(Component.translatable(TOOLTIP_TRANSLATION_KEY));
    }

    public static Optional<GroovesComponent> from(@Nullable ItemStack stack) {
        if (stack == null) return Optional.empty();
        return Optional.ofNullable(stack.get(LAKItemDataComponents.GROOVES));
    }

    public interface Grooved {
        DataComponentType<GroovesComponent> GROOVES_COMPONENT = LAKItemDataComponents.GROOVES;

        default boolean isBlank(ItemStack stack) {
            return from(stack).isEmpty();
        }

        default void setGrooves(ItemStack stack, @Nullable UUID uuid) {
            if (uuid == null) {
                stack.remove(LAKItemDataComponents.GROOVES);
                return;
            }
            stack.set(LAKItemDataComponents.GROOVES, new GroovesComponent(uuid));
        }
    }
}
