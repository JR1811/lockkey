package net.shirojr.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.shirojr.item.KeyItem;
import net.shirojr.item.LockItem;

import java.util.List;

public class LAKItemTooltipEvents implements ItemTooltipCallback {
    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
        Item item = stack.getItem();
        switch (item) {
            case LockItem lock -> lock.getTooltip(stack, tooltipContext, tooltipFlag, lines);
            case KeyItem key -> key.getTooltip(stack, tooltipContext, tooltipFlag, lines);
            default -> {
            }
        }
    }
}
