package net.shirojr.event;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.init.LAKItemDataComponents;
import net.shirojr.item.component.GroovesComponent;
import net.shirojr.util.constants.MiscTranslationKeys;

public class LAKBlockEvents implements AttackBlockCallback, UseBlockCallback {
    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        if (player.isSpectator() || hand.asEquipmentSlot().equals(EquipmentSlot.OFFHAND)) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        GroovesComponent itemComponent = stack.get(LAKItemDataComponents.GROOVES);
        if (LockedDataAttachment.isLocked(level, pos, itemComponent == null ? null : itemComponent.grooves())) {
            player.sendOverlayMessage(Component.translatable(MiscTranslationKeys.ATTACK_LOCKED_KEY));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isSpectator()) return InteractionResult.PASS;
        BlockPos hitPos = hitResult.getBlockPos();
        ItemStack stack = player.getItemInHand(hand);
        GroovesComponent itemComponent = stack.get(LAKItemDataComponents.GROOVES);
        if (LockedDataAttachment.isLocked(level, hitPos, itemComponent == null ? null : itemComponent.grooves())) {
            player.sendOverlayMessage(Component.translatable(MiscTranslationKeys.ATTACK_LOCKED_KEY));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}
