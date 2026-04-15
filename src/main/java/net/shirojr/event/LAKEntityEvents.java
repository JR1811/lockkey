package net.shirojr.event;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.init.LAKItemDataComponents;
import net.shirojr.item.component.GroovesComponent;
import net.shirojr.util.constants.MiscTranslationKeys;
import org.jspecify.annotations.Nullable;

public class LAKEntityEvents implements UseEntityCallback {
    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (player.isSpectator()) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        GroovesComponent itemComponent = stack.get(LAKItemDataComponents.GROOVES);
        if (LockedDataAttachment.isLocked(entity, itemComponent == null ? null : itemComponent.grooves())) {
            player.sendOverlayMessage(Component.translatable(MiscTranslationKeys.INTERACT_LOCKED_KEY));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}
