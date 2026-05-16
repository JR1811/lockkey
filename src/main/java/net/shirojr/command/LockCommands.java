package net.shirojr.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.shirojr.data.attachment.LockedDataAttachment;
import net.shirojr.item.component.GroovesComponent;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class LockCommands implements CommandRegistrationCallback {
    public static final SimpleCommandExceptionType ERROR_NO_LOCK = new SimpleCommandExceptionType(Component.literal("No lock found"));
    public static final SimpleCommandExceptionType ERROR_NO_GROOVES = new SimpleCommandExceptionType(Component.literal("No grooves found"));
    public static final SimpleCommandExceptionType ERROR_NOT_APPLICABLE = new SimpleCommandExceptionType(Component.literal("Data not applicable"));

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection commandSelection) {
        dispatcher.register(Commands.literal("lock").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("block")
                        .then(Commands.argument("target", BlockPosArgument.blockPos())
                                .then(Commands.literal("set")
                                        .executes(context -> LockCommands.setBlockLock(context, UUID.randomUUID()))
                                        .then(Commands.argument("grooves", UuidArgument.uuid())
                                                .executes(context -> LockCommands.setBlockLock(context, UuidArgument.getUuid(context, "grooves")))
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .executes(LockCommands::removeBlockLock)
                                )
                        )
                )
                .then(Commands.literal("entity")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .then(Commands.literal("set")
                                        .executes(context -> LockCommands.setEntityLock(context, UUID.randomUUID()))
                                        .then(Commands.argument("grooves", UuidArgument.uuid())
                                                .executes(context -> LockCommands.setEntityLock(context, UuidArgument.getUuid(context, "grooves")))
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .executes(LockCommands::removeEntityLock)
                                )
                        )
                )
                .then(Commands.literal("grooves")
                        .then(Commands.literal("set")
                                .then(Commands.argument("uuid", UuidArgument.uuid())
                                        .executes(context -> LockCommands.setItemGrooves(context, UuidArgument.getUuid(context, "uuid"), null))
                                        .then(Commands.argument("target", EntityArgument.entity())
                                                .executes(context -> LockCommands.setItemGrooves(context, UuidArgument.getUuid(context, "uuid"), EntityArgument.getEntity(context, "target")))
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .executes(context -> LockCommands.removeItemGrooves(context, null))
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(context -> LockCommands.removeItemGrooves(context, EntityArgument.getEntity(context, "target")))
                                )
                        )
                )
        );
    }

    private static int removeItemGrooves(CommandContext<CommandSourceStack> context, @Nullable Entity target) throws CommandSyntaxException {
        if (target == null) target = context.getSource().getPlayer();
        if (!(target instanceof LivingEntity livingEntity)) {
            throw ERROR_NOT_APPLICABLE.create();
        }
        if (GroovesComponent.isBlank(livingEntity.getActiveItem())) {
            throw ERROR_NO_GROOVES.create();
        }
        GroovesComponent.setGrooves(livingEntity.getActiveItem(), null);
        context.getSource().sendSuccess(() -> Component.literal("Removed Grooves data"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setItemGrooves(CommandContext<CommandSourceStack> context, UUID uuid, @Nullable Entity target) throws CommandSyntaxException {
        if (target == null) target = context.getSource().getPlayer();
        if (!(target instanceof LivingEntity livingEntity)) {
            throw ERROR_NOT_APPLICABLE.create();
        }
        ItemStack activeItem = livingEntity.getActiveItem();
        if (activeItem.isEmpty()) {
            throw ERROR_NOT_APPLICABLE.create();
        }
        GroovesComponent.setGrooves(activeItem, uuid);
        context.getSource().sendSuccess(() -> Component.literal("Applied main hand ItemStack groove: " + uuid), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int removeBlockLock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgument.getBlockPos(context, "target");
        ServerLevel level = context.getSource().getLevel();
        if (!LockedDataAttachment.isLocked(level, pos, null)) {
            throw ERROR_NO_LOCK.create();
        }
        HashSet<ItemStack> removedLocks = LockedDataAttachment.setBlockLock(level, pos, null);
        if (level.getGameRules().get(GameRules.BLOCK_DROPS)) {
            LockedDataAttachment.drop(level, pos.above().getBottomCenter(), removedLocks, SoundSource.BLOCKS);
        }
        context.getSource().sendSuccess(() -> Component.literal("Removed Lock of ").append(pos.toShortString()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setBlockLock(CommandContext<CommandSourceStack> context, UUID grooves) {
        BlockPos pos = BlockPosArgument.getBlockPos(context, "target");
        ServerLevel level = context.getSource().getLevel();
        LockedDataAttachment.setBlockLock(level, pos, new LockedDataAttachment(grooves, Optional.empty()));
        context.getSource().sendSuccess(() -> Component.literal("Applied Lock to ").append(pos.toShortString()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int removeEntityLock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(context, "target");
        if (!LockedDataAttachment.isLocked(entity, null)) {
            throw ERROR_NO_LOCK.create();
        }
        ServerLevel level = context.getSource().getLevel();
        Optional<ItemStack> oldLock = LockedDataAttachment.setEntityLock(entity, null);
        if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            LockedDataAttachment.drop(level, entity.getEyePosition(), oldLock.orElse(null), SoundSource.NEUTRAL);
        }
        context.getSource().sendSuccess(() -> Component.literal("Removed Lock of ").append(entity.getName()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setEntityLock(CommandContext<CommandSourceStack> context, UUID grooves) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(context, "target");
        LockedDataAttachment.setEntityLock(entity, new LockedDataAttachment(grooves, Optional.empty()));
        context.getSource().sendSuccess(() -> Component.literal("Applied Lock to ").append(entity.getName()), true);
        return Command.SINGLE_SUCCESS;
    }
}
