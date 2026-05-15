package net.shirojr.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.init.LAKTags;
import net.shirojr.network.NBVNetworkingDataHolder;
import net.shirojr.util.constants.MiscTranslationKeys;

import java.util.Optional;
import java.util.Set;

public class LockItem extends Item {
    public LockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getHand().equals(InteractionHand.OFF_HAND)) {
            return super.useOn(context);
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        if (!NBVNetworkingDataHolder.getInstance().areAllBlocksLockable(level) && !blockState.is(LAKTags.BlockTags.LOCKABLE)) {
            if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendOverlayMessage(Component.translatable(MiscTranslationKeys.NOT_LOCKABLE));
            }
            return InteractionResult.PASS;
        }
        if (LockedDataAttachment.isLocked(level, pos, null)) {
            return InteractionResult.PASS;
        }
        ItemStack itemInHand = context.getItemInHand();
        Optional<LockedDataAttachment> itemLockData = LockedDataAttachment.from(itemInHand.copyWithCount(1));
        if (itemLockData.isPresent()) {
            LockedDataAttachment.setBlockLock(level, Set.of(pos), itemLockData.get());
            itemInHand.consume(1, context.getPlayer());
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
        if (type.asEquipmentSlot().equals(EquipmentSlot.OFFHAND)) {
            return super.interactLivingEntity(itemStack, player, target, type);
        }
        if (LockedDataAttachment.isLocked(target, null)) {
            return InteractionResult.PASS;
        }
        Optional<LockedDataAttachment> itemLockData = LockedDataAttachment.from(itemStack.copyWithCount(1));
        if (itemLockData.isPresent()) {
            LockedDataAttachment.setEntityLock(target, itemLockData.get());
            itemStack.consume(1, player);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
}
