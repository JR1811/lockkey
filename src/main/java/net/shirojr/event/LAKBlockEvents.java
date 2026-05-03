package net.shirojr.event;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.init.LAKItemDataComponents;
import net.shirojr.item.component.GroovesComponent;
import net.shirojr.util.constants.MiscTranslationKeys;

//TODO: check only on one hand side but on it analyze the second hand too so that the key could be in both but needs only one side
public class LAKBlockEvents implements AttackBlockCallback, UseBlockCallback {
    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        if (player.isSpectator() || player.isCreative() || hand.equals(InteractionHand.OFF_HAND)) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        GroovesComponent itemComponent = !hand.equals(InteractionHand.MAIN_HAND) ? null : stack.get(LAKItemDataComponents.GROOVES);
        if (LockedDataAttachment.isLocked(level, pos, itemComponent == null ? null : itemComponent.grooves())) {
            player.sendOverlayMessage(Component.translatable(MiscTranslationKeys.ATTACK_LOCKED_KEY));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isSpectator() || player.isCreative() || hand.equals(InteractionHand.OFF_HAND)) return InteractionResult.PASS;
        BlockPos hitPos = hitResult.getBlockPos();
        ItemStack stack = player.getItemInHand(hand);
        GroovesComponent itemComponent = !hand.equals(InteractionHand.MAIN_HAND) ? null : stack.get(LAKItemDataComponents.GROOVES);
        boolean locked = LockedDataAttachment.isLocked(level, hitPos, itemComponent == null ? null : itemComponent.grooves());
        if (locked) {
            player.sendOverlayMessage(Component.translatable(MiscTranslationKeys.ATTACK_LOCKED_KEY));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}
