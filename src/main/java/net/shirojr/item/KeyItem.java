package net.shirojr.item;

import net.minecraft.world.item.Item;
import net.shirojr.item.component.GroovesComponent;

public class KeyItem extends Item implements GroovesComponent.Grooved {
    public KeyItem(Properties properties) {
        super(properties);
    }
}
