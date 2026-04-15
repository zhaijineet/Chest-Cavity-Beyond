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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

import java.util.Collection;

public class ChestCavityCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(
        Component.translatable("commands.chestcavitybeyond.resize.failed")
    );
    private static final SimpleCommandExceptionType ERROR_RESET_ORGANS_FAILED = new SimpleCommandExceptionType(
        Component.translatable("commands.chestcavitybeyond.resetorgans.failed")
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
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        ChestCavitySize newSize = ChestCavitySize.byName(StringArgumentType.getString(context, "size"));

        int successCount = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                ChestCavityUtil.getData(livingEntity).resize(newSize);
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
                ChestCavityUtil.getData(livingEntity).reset();
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
}
