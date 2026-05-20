package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.zhaiji.chestcavitybeyond.api.AttributeDisplay;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 属性显示注册中心
 * <p>
 * 管理所有在属性查询指令中显示的属性信息。
 * 本模组的所有自定义属性在静态初始化时自动注册。
 * 附属模组可通过 {@link AttributeDisplay#builder} 注册自定义属性。
 * </p>
 */
public class AttributeDisplayManager {
    private static final List<AttributeDisplay> DISPLAYS = new ArrayList<>();

    /**
     * 排序比较器：priority 降序 → attribute descriptionId 字典序
     */
    private static final Comparator<AttributeDisplay> DISPLAY_COMPARATOR = Comparator.comparingInt(AttributeDisplay::priority)
        .reversed()
        .thenComparing(display -> display.attribute().value().getDescriptionId());

    static {
        // 生存核心 (40)
        register(InitAttribute.HEALTH, 40);
        // 体能基础 (30)
        register(InitAttribute.NERVES, 30);
        register(InitAttribute.DEFENSE, 30);
        register(InitAttribute.ENDURANCE, 30);
        register(InitAttribute.STRENGTH, 30);
        register(InitAttribute.SPEED, 30);
        // 代谢系统 (20)
        register(InitAttribute.DIGESTION, 20);
        register(InitAttribute.NUTRITION, 20);
        register(InitAttribute.METABOLISM, 20);
        register(InitAttribute.BREATH_CAPACITY, 20);
        register(InitAttribute.BREATH_RECOVERY, 20);
        register(InitAttribute.WATER_BREATH, 20);
        register(InitAttribute.DETOXIFICATION, 20);
        register(InitAttribute.FILTRATION, 20);
        // 专食消化 (10)
        register(InitAttribute.CARNIVOROUS_DIGESTION, 10);
        register(InitAttribute.CARNIVOROUS_NUTRITION, 10);
        register(InitAttribute.HERBIVOROUS_DIGESTION, 10);
        register(InitAttribute.HERBIVOROUS_NUTRITION, 10);
        register(InitAttribute.SCAVENGER_DIGESTION, 10);
        register(InitAttribute.SCAVENGER_NUTRITION, 10);
        // 特殊能力 (0)
        register(InitAttribute.FIRE_RESISTANCE);
        register(InitAttribute.FROST_RESISTANCE);
        register(InitAttribute.WATER_ALLERGY);
        register(InitAttribute.ENDER);
        register(InitAttribute.PROJECTILE_DODGE);
        register(InitAttribute.LEAPING);
        register(InitAttribute.EXPLOSIVE);
        register(InitAttribute.PHOTOSYNTHESIS);
        register(InitAttribute.LAUNCH);
        register(InitAttribute.IRON_REPAIR);
        register(InitAttribute.FURNACE_POWER);
        register(InitAttribute.WITHERED);
        register(InitAttribute.VOMIT_FIREBALL);
        register(InitAttribute.GHASTLY);
        register(InitAttribute.CRYSTALLIZATION);
    }

    /**
     * 注册属性显示信息（使用默认配置）
     *
     * @param attribute 属性 Holder
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute) {
        return register(attribute, 0, false);
    }

    /**
     * 注册属性显示信息（自定义优先级）
     *
     * @param attribute 属性 Holder
     * @param priority  显示优先级，值越高越靠前显示
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute, int priority) {
        return register(attribute, priority, false);
    }

    /**
     * 注册属性显示信息（自定义是否在值为0时显示）
     *
     * @param attribute    属性 Holder
     * @param showWhenZero 值为0时是否显示
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute, boolean showWhenZero) {
        return register(attribute, 0, showWhenZero);
    }

    /**
     * 注册属性显示信息（全部自定义）
     *
     * @param attribute    属性 Holder
     * @param priority     显示优先级，值越高越靠前显示
     * @param showWhenZero 值为0时是否显示
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute, int priority, boolean showWhenZero) {
        return AttributeDisplay.builder(attribute)
            .priority(priority)
            .showWhenZero(showWhenZero)
            .build();
    }

    /**
     * 注册属性显示信息
     * <p>
     * 以属性为 key 自动去重，相同属性后续注册会覆盖之前的配置。
     * </p>
     *
     * @param display 属性显示信息
     */
    public static void register(AttributeDisplay display) {
        DISPLAYS.removeIf(attributeDisplay -> attributeDisplay.attribute().equals(display.attribute()));
        DISPLAYS.add(display);
    }

    /**
     * 对已注册的所有 AttributeDisplay 执行排序。
     */
    public static void sort() {
        DISPLAYS.sort(DISPLAY_COMPARATOR);
    }

    /**
     * 获取所有已注册的属性显示信息（已按 priority 降序排列）
     *
     * @return 按 priority 排序的属性显示信息列表
     */
    public static List<AttributeDisplay> getDisplays() {
        return DISPLAYS;
    }
}
