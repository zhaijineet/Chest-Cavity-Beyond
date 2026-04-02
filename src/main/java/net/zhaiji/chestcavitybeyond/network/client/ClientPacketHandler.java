package net.zhaiji.chestcavitybeyond.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.api.client.task.GuardianLaserRenderTask;
import net.zhaiji.chestcavitybeyond.client.screen.OrganSkillScreen;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.network.client.packet.AddGuardianLaserRenderTaskPacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.ChestOpenerMessagePacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.UnopenableChestCavityMessagePacket;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

public class ClientPacketHandler {
    public static void handlerSyncChestCavityDataPacket(Player player, SyncChestCavityDataPacket packet) {
        ChestCavityUtil.getData(player).sync(packet);
        OrganSkillScreen.selectedSlot = packet.slot();
    }

    public static void handlerAddGuardianLaserRenderTaskPacket(Player player, AddGuardianLaserRenderTaskPacket packet) {
        Level level = player.level();
        if (
            level.getEntity(packet.attackerId()) instanceof LivingEntity attacker
            && level.getEntity(packet.targetId()) instanceof LivingEntity target
        ) {
            ChestCavityUtil.getData(player).addTask(new GuardianLaserRenderTask(attacker, target, packet.elder()));
        }
    }

    public static void handlerChestOpenerMessagePacket(ChestOpenerMessagePacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        Component component;
        if (packet.isEquipChestPlate()) {
            component = Component.translatable("message." + ChestCavityBeyond.MOD_ID + ".obstructed");
        } else {
            component = Component.translatable("message." + ChestCavityBeyond.MOD_ID + ".healthy");
        }
        minecraft.gui.setOverlayMessage(component, false);
        minecraft.getNarrator().sayNow(component);
    }

    public static void handlerUnopenableCavityMessagePacket(UnopenableChestCavityMessagePacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        // 默认消息
        Component component = Component.translatable("message." + ChestCavityBeyond.MOD_ID + ".unopenable");

        if (minecraft.level instanceof Level level && level.getEntity(packet.entityId()) instanceof LivingEntity entity) {
            ChestCavityType cavityType = ChestCavityTypeManager.getType(entity);
            String customMessageKey = cavityType.getUnopenableMessage();
            if (customMessageKey != null && !customMessageKey.isEmpty()) {
                // 使用自定义消息键
                component = Component.translatable(customMessageKey);
            }
        }
        minecraft.gui.setOverlayMessage(component, false);
        minecraft.getNarrator().sayNow(component);
    }
}
