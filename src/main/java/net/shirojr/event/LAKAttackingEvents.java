package net.shirojr.event;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.init.LAKItemDataComponents;
import net.shirojr.item.component.GroovesComponent;
import net.shirojr.util.constants.MiscTranslationKeys;
import org.jspecify.annotations.Nullable;

public class LAKAttackingEvents implements AttackBlockCallback, AttackEntityCallback {
    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        if (player.isSpectator() || player.isCreative() || hand.equals(InteractionHand.OFF_HAND)) return InteractionResult.PASS;
        GroovesComponent itemComponent = player.getMainHandItem().get(LAKItemDataComponents.GROOVES);
        if (itemComponent == null) {
            itemComponent = player.getOffhandItem().get(LAKItemDataComponents.GROOVES);
        }

        if (LockedDataAttachment.isLocked(level, pos, itemComponent == null ? null : itemComponent.grooves())) {
            player.sendOverlayMessage(Component.translatable(MiscTranslationKeys.ATTACK_LOCKED));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (player.isSpectator() || player.isCreative() || hand.equals(InteractionHand.OFF_HAND)) {
            return InteractionResult.PASS;
        }
        GroovesComponent itemComponent = !hand.equals(InteractionHand.MAIN_HAND) ? null : player.getMainHandItem().get(LAKItemDataComponents.GROOVES);
        if (itemComponent == null) {
            itemComponent = player.getOffhandItem().get(LAKItemDataComponents.GROOVES);
        }
        if (LockedDataAttachment.isLocked(entity, itemComponent == null ? null : itemComponent.grooves())) {
            player.sendOverlayMessage(Component.translatable(MiscTranslationKeys.INTERACT_LOCKED));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}
