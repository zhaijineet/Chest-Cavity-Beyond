package net.zhaiji.chestcavitybeyond.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.Collection;

public class ChestCavityCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(
        Component.translatable("commands.chestcavitybeyond.resize.failed")
    );
    private static final SimpleCommandExceptionType ERROR_INVALID_SIZE = new SimpleCommandExceptionType(
        Component.translatable("commands.chestcavitybeyond.resize.failed.invalid_size")
    );
    private static final SimpleCommandExceptionType ERROR_RESET_ORGANS_FAILED = new SimpleCommandExceptionType(
        Component.translatable("commands.chestcavitybeyond.resetorgans.failed")
    );
    private static final SimpleCommandExceptionType ERROR_ATTRIBUTES_NOT_PLAYER = new SimpleCommandExceptionType(
        Component.translatable("commands.chestcavitybeyond.attributes.failed.not_player")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("chestcavity")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                    .then(Commands.argument("size", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (ChestCavitySize size : ChestCavitySize.values()) {
                                builder.suggest(size.getSerializedName());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ChestCavityCommand::execute)
                    )
                    .then(Commands.literal("resetorgans")
                        .executes(ChestCavityCommand::executeResetOrgans)
                    )
                    .then(Commands.literal("attributes")
                        .executes(ChestCavityCommand::executeAttributes)
                    )
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        ChestCavitySize newSize = ChestCavitySize.CODEC.byName(StringArgumentType.getString(context, "size"), null);
        if (newSize == null) {
            throw ERROR_INVALID_SIZE.create();
        }

        int successCount = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                ChestCavityData data = ChestCavityUtil.getData(livingEntity);
                data.resize(newSize);
                syncChestCavityData(livingEntity, data);
                successCount++;
            }
        }

        if (successCount == 0) {
            throw ERROR_FAILED.create();
        }

        if (targets.size() == 1) {
            Entity target = targets.iterator().next();
            context.getSource().sendSuccess(
                () -> Component.translatable(
                    "commands.chestcavitybeyond.resize.success.single",
                    target.getDisplayName(),
                    newSize.getSerializedName(),
                    newSize.getSlots()
                ), true
            );
        } else {
            context.getSource().sendSuccess(
                () -> Component.translatable(
                    "commands.chestcavitybeyond.resize.success.multiple",
                    targets.size(),
                    newSize.getSerializedName(),
                    newSize.getSlots()
                ), true
            );
        }

        return successCount;
    }

    private static int executeResetOrgans(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");

        int successCount = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                // 重置为默认器官
                ChestCavityData data = ChestCavityUtil.getData(livingEntity);
                data.reset();
                syncChestCavityData(livingEntity, data);
                successCount++;
            }
        }

        if (successCount == 0) {
            throw ERROR_RESET_ORGANS_FAILED.create();
        }

        if (targets.size() == 1) {
            Entity target = targets.iterator().next();
            context.getSource().sendSuccess(
                () -> Component.translatable("commands.chestcavitybeyond.resetorgans.success.single", target.getDisplayName()),
                true
            );
        } else {
            int count = successCount;
            context.getSource().sendSuccess(
                () -> Component.translatable("commands.chestcavitybeyond.resetorgans.success.multiple", count),
                true
            );
        }

        return successCount;
    }

    private static int executeAttributes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) {
            throw ERROR_ATTRIBUTES_NOT_PLAYER.create();
        }
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");

        int count = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                count += TooltipUtil.sendAttributeDisplay(player, livingEntity);
            }
        }
        return count;
    }

    private static void syncChestCavityData(LivingEntity entity, ChestCavityData data) {
        if (entity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncChestCavityDataPacket(data.getOrgans(), data.selectedSlot, data.getSize()));
        }
    }
}
