package net.zhaiji.chestcavitybeyond.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.client.screen.OrganSkillScreen;
import net.zhaiji.chestcavitybeyond.network.server.packet.UseSkillPacket;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

import java.util.ArrayList;
import java.util.List;

public class KeyMappings {
    public static final String KEY_CATEGORY_TRANSLATABLE = "key.chestcavitybeyond.categories";
    public static final String OPEN_SKILL_GUI_TRANSLATABLE = "key.chestcavitybeyond.open_skill_gui";
    public static final String USE_ORGAN_SKILL_TRANSLATABLE = "key.chestcavitybeyond.use_organ_skill";
    public static final String DESCEND_VEHICLE_TRANSLATABLE = "key.chestcavitybeyond.vehicle_down";
    public static final String USE_ORGAN_SKILLS_TRANSLATABLE = "key.chestcavitybeyond.use_organ_skill_";
    public static final String SKILL_PREV_TRANSLATABLE = "key.chestcavitybeyond.skill_prev";
    public static final String SKILL_NEXT_TRANSLATABLE = "key.chestcavitybeyond.skill_next";
    public static final String SKILL_CONFIRM_TRANSLATABLE = "key.chestcavitybeyond.skill_confirm";

    public static final List<KeyMapping> USE_SKILLS_MAPPINGS = new ArrayList<>();

    // 打开技能界面
    public static final KeyMapping OPEN_SKILL_GUI = new KeyMapping(
            OPEN_SKILL_GUI_TRANSLATABLE,
            KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_V,
            KEY_CATEGORY_TRANSLATABLE
    );

    // 使用器官技能
    public static final KeyMapping USE_ORGAN_SKILL = new KeyMapping(
            USE_ORGAN_SKILL_TRANSLATABLE,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_C,
            KEY_CATEGORY_TRANSLATABLE
    );

    // 乘坐实体下降
    public static final KeyMapping DESCEND_VEHICLE = new KeyMapping(
            DESCEND_VEHICLE_TRANSLATABLE,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_LCONTROL,
            KEY_CATEGORY_TRANSLATABLE
    );

    // 环形菜单切换上一个技能
    public static final KeyMapping SKILL_PREV = new KeyMapping(
            SKILL_PREV_TRANSLATABLE,
            KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_LEFT,
            KEY_CATEGORY_TRANSLATABLE
    );

    // 环形菜单切换下一个技能
    public static final KeyMapping SKILL_NEXT = new KeyMapping(
            SKILL_NEXT_TRANSLATABLE,
            KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_RIGHT,
            KEY_CATEGORY_TRANSLATABLE
    );

    // 环形菜单确认选择
    public static final KeyMapping SKILL_CONFIRM = new KeyMapping(
            SKILL_CONFIRM_TRANSLATABLE,
            KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_SPACE,
            KEY_CATEGORY_TRANSLATABLE
    );

    // 快捷技能使用
    static {
        for (int i = 0; i < ChestCavitySize.ROW_6.getSlots(); i++) {
            register(
                    USE_ORGAN_SKILLS_TRANSLATABLE + i,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    -1,
                    KEY_CATEGORY_TRANSLATABLE
            );
        }
    }

    public static KeyMapping register(String description, IKeyConflictContext keyConflictContext, InputConstants.Type inputType, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(description, keyConflictContext, inputType, keyCode, category);
        USE_SKILLS_MAPPINGS.add(keyMapping);
        return keyMapping;
    }

    /**
     * 根据按键触发对应功能
     */
    public static void customKeyTrigger(InputConstants.Key key) {
        if (OPEN_SKILL_GUI.isActiveAndMatches(key)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen instanceof OrganSkillScreen screen) {
                screen.onClose();
            } else if (minecraft.screen == null) {
                minecraft.setScreen(new OrganSkillScreen());
            }
        }
        if (USE_ORGAN_SKILL.isActiveAndMatches(key)) {
            int selectedSlot = ChestCavityUtil.getData(Minecraft.getInstance().player).getSelectedSlot();
            if (selectedSlot != -1) {
                PacketDistributor.sendToServer(new UseSkillPacket(selectedSlot));
            }
        }
        for (int i = 0; i < USE_SKILLS_MAPPINGS.size(); i++) {
            if (USE_SKILLS_MAPPINGS.get(i).isActiveAndMatches(key)) {
                PacketDistributor.sendToServer(new UseSkillPacket(i));
            }
        }
    }
}
