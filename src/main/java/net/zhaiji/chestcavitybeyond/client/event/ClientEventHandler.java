package net.zhaiji.chestcavitybeyond.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.capability.IOrgan;
import net.zhaiji.chestcavitybeyond.builder.OrganBuilder;
import net.zhaiji.chestcavitybeyond.client.key.KeyMappings;
import net.zhaiji.chestcavitybeyond.client.overlay.OrganSelectedOverlay;
import net.zhaiji.chestcavitybeyond.client.screen.ChestCavityScreen;
import net.zhaiji.chestcavitybeyond.client.screen.OrganSkillScreen;
import net.zhaiji.chestcavitybeyond.client.util.ChestCavityClientUtil;
import net.zhaiji.chestcavitybeyond.network.server.packet.UseSkillPacket;
import net.zhaiji.chestcavitybeyond.register.InitEntityType;
import net.zhaiji.chestcavitybeyond.register.InitMenuType;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.lwjgl.glfw.GLFW;

public class ClientEventHandler {
    /**
     * @param event 注册MenuScreen事件
     */
    public static void handlerRegisterMenuScreensEvent(RegisterMenuScreensEvent event) {
        event.register(InitMenuType.CHEST_CAVITY.get(), ChestCavityScreen::new);
    }

    /**
     * @param event 按键注册事件
     */
    public static void handlerRegisterKeyMappingsEvent(RegisterKeyMappingsEvent event) {
        event.register(KeyMappings.OPEN_SKILL_GUI);
        event.register(KeyMappings.USE_ORGAN_SKILL);
        KeyMappings.USE_SKILLS_MAPPINGS.forEach(event::register);
    }

    /**
     * @param event 注册实体渲染事件
     */
    public static void handlerEntityRenderersEvent$RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(InitEntityType.THROWN_COBWEB.get(), ThrownItemRenderer::new);
    }

    /**
     * @param event 注册gui覆层事件
     */
    public static void handlerRegisterGuiLayersEvent(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.HOTBAR, OrganSelectedOverlay.ORGAN_SELECTED, OrganSelectedOverlay::render);
    }

    /**
     * 触发器官的工具提示
     *
     * @param event 物品工具提示事件
     */
    public static void handlerItemTooltipEvent(ItemTooltipEvent event) {
        IOrgan organ = ChestCavityUtil.getOrganCap(event.getItemStack());
        if (organ == OrganBuilder.EMPTY_ORGAN) return;
        Minecraft minecraft = Minecraft.getInstance();
        Options options = minecraft.options;
        Player player = event.getEntity();
        TooltipsKeyContext keyContext = new TooltipsKeyContext(
                ChestCavityClientUtil.isKeyDown(options.keyShift),
                ChestCavityClientUtil.isKeyDown(options.keySprint)
        );
        // player为null的情况，可能是正在检索物品
        // 此时需要将所有tooltips显示，以便通过物品提示寻找物品
        if (player == null) {
            player = minecraft.player;
            keyContext = new TooltipsKeyContext(true, true);
        }
        organ.organTooltip(
                ChestCavityUtil.getData(player),
                event.getItemStack(),
                keyContext,
                event.getContext(),
                event.getToolTip(),
                event.getFlags()
        );
    }

    /**
     * 设置自定义按键的功能
     *
     * @param event 键盘按键输入事件
     */
    public static void handlerInputEvent$Key(InputEvent.Key event) {
        if (event.getAction() != InputConstants.PRESS) return;
        Minecraft minecraft = Minecraft.getInstance();
        InputConstants.Key key = InputConstants.getKey(event.getKey(), event.getScanCode());
        if (KeyMappings.OPEN_SKILL_GUI.isActiveAndMatches(key)) {
            if (minecraft.screen instanceof OrganSkillScreen screen) {
                screen.onClose();
            } else if (minecraft.screen == null) {
                minecraft.setScreen(new OrganSkillScreen());
            }
        }
        if (KeyMappings.USE_ORGAN_SKILL.isActiveAndMatches(key)) {
            if (OrganSkillScreen.selectedSlot != -1) {
                PacketDistributor.sendToServer(new UseSkillPacket(OrganSkillScreen.selectedSlot));
            }
        }
        for (int i = 0; i < KeyMappings.USE_SKILLS_MAPPINGS.size(); i++) {
            if (KeyMappings.USE_SKILLS_MAPPINGS.get(i).isActiveAndMatches(key)) {
                PacketDistributor.sendToServer(new UseSkillPacket(i));
            }
        }
    }

    /**
     * 设置技能界面鼠标按键的功能
     *
     * @param event 鼠标按键输入事件
     */
    public static void handlerInputEvent$MouseButton$Pre(InputEvent.MouseButton.Pre event) {
        if (Minecraft.getInstance().screen instanceof OrganSkillScreen screen && event.getAction() == InputConstants.PRESS) {
            if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                screen.changeSelectedSlot();
            }
            screen.onClose();
            event.setCanceled(true);
        }
    }

    /**
     * 在打开OrganSkill界面时禁止渲染准星
     *
     * @param event 渲染界面图层前事件
     */
    public static void handlerRenderGuiLayerEvent$Pre(RenderGuiLayerEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof OrganSkillScreen && event.getName().equals(VanillaGuiLayers.CROSSHAIR)) {
            event.setCanceled(true);
        }
    }

    /**
     * 为了在OrganSkill界面打开时也能够移动
     *
     * @param event 移动输入更新事件
     */
    public static void handlerMovementInputUpdateEvent(MovementInputUpdateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof OrganSkillScreen) {
            Options options = minecraft.options;
            Input input = event.getInput();
            // 按键检测
            input.up = ChestCavityClientUtil.isKeyDown(options.keyUp);
            input.down = ChestCavityClientUtil.isKeyDown(options.keyDown);
            input.left = ChestCavityClientUtil.isKeyDown(options.keyLeft);
            input.right = ChestCavityClientUtil.isKeyDown(options.keyRight);
            // 移动计算
            input.forwardImpulse = input.up == input.down ? 0.0F : (input.up ? 1.0F : -1.0F);
            input.leftImpulse = input.left == input.right ? 0.0F : (input.left ? 1.0F : -1.0F);
            input.jumping = ChestCavityClientUtil.isKeyDown(options.keyJump);
            input.shiftKeyDown = ChestCavityClientUtil.isKeyDown(options.keyShift);
            if (minecraft.player.isMovingSlowly()) {
                input.leftImpulse *= 0.3F;
                input.forwardImpulse *= 0.3F;
            }
        }
    }
}
