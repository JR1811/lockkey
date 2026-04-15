package net.shirojr.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.item.component.GroovesComponent;

import java.util.Optional;
import java.util.Set;

public class LockItem extends Item implements GroovesComponent.Grooved {
    public LockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (LockedDataAttachment.isLocked(level, pos, null)) {
            return InteractionResult.PASS;
        }
        Optional<LockedDataAttachment> itemLockData = LockedDataAttachment.from(context.getItemInHand());
        if (itemLockData.isPresent()) {
            LockedDataAttachment.setBlockLock(level, Set.of(pos), itemLockData.get());
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
        return super.interactLivingEntity(itemStack, player, target, type);
    }
}
