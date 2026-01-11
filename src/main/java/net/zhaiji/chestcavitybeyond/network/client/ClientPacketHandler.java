package net.zhaiji.chestcavitybeyond.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.client.task.GuardianLaserRenderTask;
import net.zhaiji.chestcavitybeyond.client.screen.OrganSkillScreen;
import net.zhaiji.chestcavitybeyond.network.client.packet.AddGuardianLaserRenderTaskPacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.ChestOpenerMessagePacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

public class ClientPacketHandler {
    public static void handlerSyncChestCavityDataPacket(SyncChestCavityDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ChestCavityUtil.getData(context.player()).sync(packet);
            OrganSkillScreen.selectedSlot = packet.slot();
        });
    }

    public static void handlerAddGuardianLaserRenderTaskPacket(AddGuardianLaserRenderTaskPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            if (
                    level.getEntity(packet.attackerId()) instanceof LivingEntity attacker
                            && level.getEntity(packet.targetId()) instanceof LivingEntity target
            ) {
                ChestCavityUtil.getData(player).addTask(new GuardianLaserRenderTask(attacker, target, packet.elder()));
            }
        });
    }

    public static void handlerChestOpenerMessagePacket(ChestOpenerMessagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            Component component;
            if (packet.isEquipChestPlate()) {
                component = Component.translatable("message." + ChestCavityBeyond.MOD_ID + ".obstructed");
            } else {
                component = Component.translatable("message." + ChestCavityBeyond.MOD_ID + ".healthy");
            }
            minecraft.gui.setOverlayMessage(component, false);
            minecraft.getNarrator().sayNow(component);
        });
    }
}
