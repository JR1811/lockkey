package net.shirojr.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.util.constants.MiscTranslationKeys;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class KeyItem extends Item {
    public KeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getHand().equals(InteractionHand.OFF_HAND)) {
            return super.useOn(context);
        }
        Player player = context.getPlayer();
        if (player == null || !player.isCrouching()) {
            return super.useOn(context);
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!LockedDataAttachment.isLocked(level, pos, null)) {
            return super.useOn(context);
        }
        HashSet<ItemStack> removedLocks = LockedDataAttachment.setBlockLock(level, Set.of(pos), null);
        if (level instanceof ServerLevel serverLevel) {
            LockedDataAttachment.drop(serverLevel, pos.above().getBottomCenter(), removedLocks, SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
        if (type.asEquipmentSlot().equals(EquipmentSlot.OFFHAND)) {
            return super.interactLivingEntity(itemStack, player, target, type);
        }
        if (!player.isCrouching()) {
            return super.interactLivingEntity(itemStack, player, target, type);
        }
        if (!LockedDataAttachment.isLocked(target, null)) {
            return InteractionResult.PASS;
        }
        Optional<ItemStack> removedLock = LockedDataAttachment.setEntityLock(target, null);
        if (player.level() instanceof ServerLevel serverLevel && removedLock.isPresent()) {
            LockedDataAttachment.drop(serverLevel, target.position(), Set.of(removedLock.get()), SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("unused")
    public void getTooltip(ItemStack stack, TooltipContext context, TooltipFlag flag, List<Component> lines) {
        lines.add(Component.translatable(MiscTranslationKeys.TOOLTIP_KEY_1));
        lines.add(Component.translatable(MiscTranslationKeys.TOOLTIP_KEY_2));
    }
}
