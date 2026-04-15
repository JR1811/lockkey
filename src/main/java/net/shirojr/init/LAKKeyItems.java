package net.shirojr.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.shirojr.LAKMain;
import net.shirojr.item.KeyItem;
import net.shirojr.item.LockItem;
import net.shirojr.util.IdentifierHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface LAKKeyItems {
    List<IdentifierHolder<Item>> ALL_ITEMS = new ArrayList<>();

    KeyItem KEY = register("key", KeyItem::new, properties().stacksTo(4));
    LockItem LOCK = register("lock", LockItem::new, properties());

    static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        return register(LAKMain.getId(name), factory, properties);
    }

    static <T extends Item> T register(Identifier id, Function<Item.Properties, T> factory, Item.Properties properties) {
        T registeredEntry = Registry.register(BuiltInRegistries.ITEM, id, factory.apply(properties.setId(ResourceKey.create(Registries.ITEM, id))));
        ALL_ITEMS.add(new IdentifierHolder<>(registeredEntry, id));
        return registeredEntry;
    }

    static Item.Properties properties() {
        return new Item.Properties();
    }

    static void initialize() {
        // static initialisation
    }
}
