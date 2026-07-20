package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.AttributeDisplay;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.Function;

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

    /** 值 ≤ 0 时隐藏（适用于"需要 > 0 才生效"的属性） */
    private static final DoublePredicate HIDE_WHEN_NOT_POSITIVE = v -> v <= 0;

    /**
     * 排序比较器：priority 降序 → attribute descriptionId 字典序
     */
    private static final Comparator<AttributeDisplay> DISPLAY_COMPARATOR = Comparator.comparingInt(AttributeDisplay::priority)
        .reversed()
        .thenComparing(display -> display.attribute().value().getDescriptionId());

    static {
        // ==================== 生存核心 (40) ====================
        register(
            InitAttribute.HEALTH, 40, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.HEALTH);
                double maxHealthBonus = diff * 2;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.HEALTH),
                    TooltipUtil.formatAttributeValue(maxHealthBonus)
                );
            }
        );

        // ==================== 体能基础 (30) ====================
        register(
            InitAttribute.NERVES, 30, entity -> {
                ChestCavityData data = ChestCavityUtil.getData(entity);
                double diff = data.getDifferenceValue(InitAttribute.NERVES);
                double attackSpeedFactor = MathUtil.getLog10Scale(diff);
                double attackSpeedPercent = (1 + (diff >= 0 ? attackSpeedFactor : -attackSpeedFactor)) * 100;
                double movePercent = data.getCurrentValue(InitAttribute.NERVES) <= 0 ? 0 : (1 + diff / 10.0) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.NERVES),
                    TooltipUtil.formatAttributeValue(attackSpeedPercent),
                    TooltipUtil.formatAttributeValue(movePercent)
                );
            }
        );
        register(
            InitAttribute.DEFENSE, 30, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.DEFENSE);
                // 以10点伤害为例计算减伤比例
                double scale = MathUtil.getAttenuationScale(10, diff);
                double damageReduction = (1 - scale) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.DEFENSE),
                    TooltipUtil.formatAttributeValue(damageReduction)
                );
            }
        );
        register(
            InitAttribute.ENDURANCE, 30, entity -> {
                double enduranceDifference = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.ENDURANCE);
                double enduranceScale = MathUtil.getSquareRootInverseScale(enduranceDifference);
                double percent = enduranceScale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.ENDURANCE),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.STRENGTH, 30, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.STRENGTH);
                double factor = MathUtil.getLog10Scale(diff);
                double damagePercent = (1 + (diff >= 0 ? factor : -factor)) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.STRENGTH),
                    TooltipUtil.formatAttributeValue(damagePercent)
                );
            }
        );
        register(
            InitAttribute.SPEED, 30, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.SPEED);
                double factor = MathUtil.getLog10Scale(diff) / 2;
                double speedPercent = (1 + (diff >= 0 ? factor : -factor)) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.SPEED),
                    TooltipUtil.formatAttributeValue(speedPercent)
                );
            }
        );

        // ==================== 代谢系统 (20) ====================
        register(
            InitAttribute.DIGESTION, 20, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.DIGESTION);
                double scale = MathUtil.getDirectScale(diff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.DIGESTION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.NUTRITION, 20, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.NUTRITION);
                double effectiveDiff = diff > 0 ? diff / 4.0 : diff;
                double scale = MathUtil.getDirectScale(effectiveDiff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.NUTRITION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.METABOLISM, 20, entity -> {
                ChestCavityData data = ChestCavityUtil.getData(entity);
                double healingPercent = MixinUtil.getMetabolismScale(data) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.METABOLISM),
                    TooltipUtil.formatAttributeValue(healingPercent)
                );
            }
        );
        register(
            InitAttribute.BREATH_CAPACITY, 20, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.BREATH_CAPACITY);
                double scale = MathUtil.getSquareRootInverseScale(diff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.BREATH_CAPACITY),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.BREATH_RECOVERY, 20, entity -> {
                ChestCavityData data = ChestCavityUtil.getData(entity);
                double curr = data.getCurrentValue(InitAttribute.BREATH_RECOVERY);
                double def = data.getDefaultValue(InitAttribute.BREATH_RECOVERY);
                double diff = curr - def;
                double percent = (1 + diff / 2.0) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.BREATH_RECOVERY),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.WATER_BREATH, 20, entity -> {
                ChestCavityData data = ChestCavityUtil.getData(entity);
                double curr = data.getCurrentValue(InitAttribute.WATER_BREATH);
                double def = data.getDefaultValue(InitAttribute.WATER_BREATH);
                double diff = curr - def;
                double percent = (1 + diff / 2.0) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.WATER_BREATH),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(AttributeDisplay.builder(InitAttribute.DETOXIFICATION)
            .priority(20)
            .descriptionOverride(() -> {
                MutableComponent hover = Component.empty();
                hover.append(Component.translatable("attribute.chestcavitybeyond.detoxification.description.0"));
                hover.append(Component.literal("\n"));
                hover.append(Component.translatable(
                    "attribute.chestcavitybeyond.detoxification.description.1",
                    TooltipUtil.formatAttributeValue(ChestCavityBeyondConfig.detoxificationImmunityDurationThreshold)
                ));
                return hover;
            })
            .valueEffect(entity -> {
                double detoxificationDifference = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.DETOXIFICATION);
                double scale = MathUtil.getSquareRootInverseScale(detoxificationDifference);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.DETOXIFICATION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            })
            .build());
        register(
            InitAttribute.FILTRATION, 20, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.FILTRATION);
                if (diff >= 0) {
                    return Component.translatable(getValueEffectKey(InitAttribute.FILTRATION) + ".safe");
                }
                int amplifier = Math.max(0, (int) Math.floor(-diff / 2));
                double scale = MathUtil.getInverseScale(diff);
                int duration = (int) (30 * scale);
                return Component.translatable(
                    getValueEffectKey(InitAttribute.FILTRATION),
                    amplifier + 1,
                    TooltipUtil.formatAttributeValue(duration / 20.0)
                );
            }
        );

        // ==================== 专食消化 (10) ====================
        register(
            InitAttribute.CARNIVOROUS_DIGESTION, 10, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.CARNIVOROUS_DIGESTION);
                double scale = MathUtil.getDirectScale(diff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.CARNIVOROUS_DIGESTION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.CARNIVOROUS_NUTRITION, 10, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.CARNIVOROUS_NUTRITION);
                double effectiveDiff = diff > 0 ? diff / 4.0 : diff;
                double scale = MathUtil.getDirectScale(effectiveDiff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.CARNIVOROUS_NUTRITION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.HERBIVOROUS_DIGESTION, 10, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.HERBIVOROUS_DIGESTION);
                double scale = MathUtil.getDirectScale(diff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.HERBIVOROUS_DIGESTION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.HERBIVOROUS_NUTRITION, 10, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.HERBIVOROUS_NUTRITION);
                double effectiveDiff = diff > 0 ? diff / 4.0 : diff;
                double scale = MathUtil.getDirectScale(effectiveDiff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.HERBIVOROUS_NUTRITION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.SCAVENGER_DIGESTION, 10, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.SCAVENGER_DIGESTION);
                double scale = MathUtil.getDirectScale(diff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.SCAVENGER_DIGESTION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            InitAttribute.SCAVENGER_NUTRITION, 10, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.SCAVENGER_NUTRITION);
                double effectiveDiff = diff > 0 ? diff / 4.0 : diff;
                double scale = MathUtil.getDirectScale(effectiveDiff);
                double percent = scale * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.SCAVENGER_NUTRITION),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );

        // ==================== 特殊能力 (0) ====================
        register(AttributeDisplay.builder(InitAttribute.FIRE_RESISTANCE)
            .descriptionOverride(() -> {
                MutableComponent hover = Component.empty();
                hover.append(Component.translatable("attribute.chestcavitybeyond.fire_resistance.description.0"));
                hover.append(Component.literal("\n"));
                hover.append(Component.translatable(
                    "attribute.chestcavitybeyond.fire_resistance.description.1",
                    TooltipUtil.formatAttributeValue(ChestCavityBeyondConfig.fireImmunityHotFloor)
                ));
                hover.append(Component.literal("\n"));
                hover.append(Component.translatable(
                    "attribute.chestcavitybeyond.fire_resistance.description.2",
                    TooltipUtil.formatAttributeValue(ChestCavityBeyondConfig.fireImmunityFire)
                ));
                hover.append(Component.literal("\n"));
                hover.append(Component.translatable(
                    "attribute.chestcavitybeyond.fire_resistance.description.3",
                    TooltipUtil.formatAttributeValue(ChestCavityBeyondConfig.fireImmunityLava)
                ));
                return hover;
            })
            .valueEffect(entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.FIRE_RESISTANCE);
                // 以10点伤害为例计算减伤比例
                double scale = MathUtil.getAttenuationScale(10, curr);
                double damageReduction = (1 - scale) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.FIRE_RESISTANCE),
                    TooltipUtil.formatAttributeValue(damageReduction)
                );
            })
            .build());
        register(AttributeDisplay.builder(InitAttribute.FROST_RESISTANCE)
            .descriptionOverride(() -> {
                MutableComponent hover = Component.empty();
                hover.append(Component.translatable("attribute.chestcavitybeyond.frost_resistance.description.0"));
                hover.append(Component.literal("\n"));
                hover.append(Component.translatable(
                    "attribute.chestcavitybeyond.frost_resistance.description.1",
                    TooltipUtil.formatAttributeValue(ChestCavityBeyondConfig.frostImmunity)
                ));
                return hover;
            })
            .valueEffect(entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.FROST_RESISTANCE);
                double scale = MathUtil.getAttenuationScale(10, curr);
                double damageReduction = (1 - scale) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.FROST_RESISTANCE),
                    TooltipUtil.formatAttributeValue(damageReduction)
                );
            })
            .build());
        register(InitAttribute.WATER_ALLERGY);
        register(
            InitAttribute.ENDER, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.ENDER);
                double distance = MathUtil.getDirectScale(curr);
                return Component.translatable(
                    getValueEffectKey(InitAttribute.ENDER),
                    TooltipUtil.formatAttributeValue(distance)
                );
            }
        );
        register(AttributeDisplay.builder(InitAttribute.PROJECTILE_DODGE)
            .hideWhen(HIDE_WHEN_NOT_POSITIVE)
            .build());
        register(
            InitAttribute.LEAPING, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double currentJumpStr = entity.getAttributeValue(Attributes.JUMP_STRENGTH);
                double jumpHeight = OrganAttributeUtil.calcJumpHeight(currentJumpStr);
                return Component.translatable(
                    getValueEffectKey(InitAttribute.LEAPING),
                    TooltipUtil.formatAttributeValue(jumpHeight)
                );
            }
        );
        register(
            InitAttribute.EXPLOSIVE, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.EXPLOSIVE);
                double power = 3 * curr;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.EXPLOSIVE),
                    TooltipUtil.formatAttributeValue(power)
                );
            }
        );
        register(
            InitAttribute.PHOTOSYNTHESIS, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.PHOTOSYNTHESIS);
                double interval = 800 / curr / 20.0;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.PHOTOSYNTHESIS),
                    TooltipUtil.formatAttributeValue(interval)
                );
            }
        );
        register(
            InitAttribute.LAUNCH, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.LAUNCH);
                double force = 0.04 * curr;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.LAUNCH),
                    TooltipUtil.formatAttributeValue(force)
                );
            }
        );
        register(
            InitAttribute.IRON_REPAIR, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.IRON_REPAIR);
                double healAmount = 2.5 * curr;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.IRON_REPAIR),
                    TooltipUtil.formatAttributeValue(healAmount)
                );
            }
        );
        register(
            InitAttribute.FURNACE_POWER, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.FURNACE_POWER);
                int amplifier = Math.max(0, (int) (curr - 1));
                double maxDuration = ChestCavityBeyondConfig.furnacePowerMaxDuration / 20.0;
                double interval = 200.0 / (amplifier + 1) / 20.0;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.FURNACE_POWER),
                    TooltipUtil.formatAttributeValue(interval),
                    amplifier + 1,
                    TooltipUtil.formatAttributeValue(maxDuration)
                );
            }
        );
        register(
            InitAttribute.WITHERED, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                ChestCavityData data = ChestCavityUtil.getData(entity);
                double curr = data.getCurrentValue(InitAttribute.WITHERED);
                int duration = (int) (40 * curr);
                int amplifier = 0;
                if (data.hasOrgan(Items.NETHER_STAR)) {
                    duration += 200;
                    amplifier++;
                }
                double durationPercent = MathUtil.getSquareRootInverseScale(
                    data.getDifferenceValue(InitAttribute.WITHERED)
                ) * 100;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.WITHERED),
                    TooltipUtil.formatAttributeValue(duration / 20.0),
                    amplifier + 1,
                    TooltipUtil.formatAttributeValue(durationPercent)
                );
            }
        );
        register(
            InitAttribute.VOMIT_FIREBALL, 0, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.VOMIT_FIREBALL);
                return Component.translatable(
                    getValueEffectKey(InitAttribute.VOMIT_FIREBALL),
                    TooltipUtil.formatAttributeValue(curr)
                );
            }
        );
        register(
            InitAttribute.GHASTLY, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.GHASTLY);
                return Component.translatable(
                    getValueEffectKey(InitAttribute.GHASTLY),
                    TooltipUtil.formatAttributeValue(curr)
                );
            }
        );
        register(
            InitAttribute.CRYSTALLIZATION, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.CRYSTALLIZATION);
                double healPerSec = curr * 2.0 / 5;
                return Component.translatable(
                    getValueEffectKey(InitAttribute.CRYSTALLIZATION),
                    TooltipUtil.formatAttributeValue(healPerSec)
                );
            }
        );
        register(
            InitAttribute.LAVA_SWIM_SPEED, 0, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.LAVA_SWIM_SPEED);
                return Component.translatable(
                    getValueEffectKey(InitAttribute.LAVA_SWIM_SPEED),
                    TooltipUtil.formatAttributeValue(curr)
                );
            }
        );
        register(AttributeDisplay.builder(InitAttribute.LAVA_WALK)
            .hideWhen(HIDE_WHEN_NOT_POSITIVE)
            .build());
        register(
            InitAttribute.WATER_WEAKNESS, 0, HIDE_WHEN_NOT_POSITIVE, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(InitAttribute.WATER_WEAKNESS);
                double reductionPercent = 100 - 100 / (1 + diff * 0.1);
                return Component.translatable(
                    getValueEffectKey(InitAttribute.WATER_WEAKNESS),
                    TooltipUtil.formatAttributeValue(reductionPercent)
                );
            }
        );

        register(
            NeoForgeMod.SWIM_SPEED, 0, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(NeoForgeMod.SWIM_SPEED);
                double bonusPercent = (curr - 1) * 100;
                return Component.translatable(
                    getValueEffectKey(NeoForgeMod.SWIM_SPEED),
                    TooltipUtil.formatAttributeValue(bonusPercent)
                );
            }
        );
        register(Attributes.LUCK);
        register(
            Attributes.KNOCKBACK_RESISTANCE, 0, entity -> {
                double curr = ChestCavityUtil.getData(entity).getCurrentValue(Attributes.KNOCKBACK_RESISTANCE);
                return Component.translatable(
                    getValueEffectKey(Attributes.KNOCKBACK_RESISTANCE),
                    TooltipUtil.formatAttributeValue(curr * 100)
                );
            }
        );
        register(
            Attributes.GRAVITY, 0, entity -> {
                ChestCavityData gravityData = ChestCavityUtil.getData(entity);
                double curr = gravityData.getCurrentValue(Attributes.GRAVITY);
                if (curr <= 0) {
                    return Component.translatable(getValueEffectKey(Attributes.GRAVITY) + ".flight");
                }
                double def = gravityData.getDefaultValue(Attributes.GRAVITY);
                double percent = def != 0 ? (curr - def) / def * 100 : 0;
                return Component.translatable(
                    getValueEffectKey(Attributes.GRAVITY),
                    TooltipUtil.formatAttributeValue(percent)
                );
            }
        );
        register(
            Attributes.ENTITY_INTERACTION_RANGE, 0, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(Attributes.ENTITY_INTERACTION_RANGE);
                return Component.translatable(
                    getValueEffectKey(Attributes.ENTITY_INTERACTION_RANGE),
                    TooltipUtil.formatAttributeValue(diff)
                );
            }
        );
        register(
            Attributes.BLOCK_INTERACTION_RANGE, 0, entity -> {
                double diff = ChestCavityUtil.getData(entity).getDifferenceValue(Attributes.BLOCK_INTERACTION_RANGE);
                return Component.translatable(
                    getValueEffectKey(Attributes.BLOCK_INTERACTION_RANGE),
                    TooltipUtil.formatAttributeValue(diff)
                );
            }
        );
    }

    /**
     * 获取属性实时效果描述的翻译键
     *
     * @param attribute 属性 Holder
     * @return 翻译键，格式为 {@code <descriptionId>.value_effect}
     */
    public static String getValueEffectKey(Holder<Attribute> attribute) {
        return attribute.value().getDescriptionId() + ".value_effect";
    }

    /**
     * 注册属性显示信息（使用默认配置）
     *
     * @param attribute 属性 Holder
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute) {
        return AttributeDisplay.builder(attribute).build();
    }

    /**
     * 注册属性显示信息（自定义优先级）
     *
     * @param attribute 属性 Holder
     * @param priority  显示优先级，值越高越靠前显示
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute, int priority) {
        return AttributeDisplay.builder(attribute)
            .priority(priority)
            .build();
    }

    /**
     * 注册属性显示信息（自定义优先级 + 实时效果描述）
     *
     * @param attribute   属性 Holder
     * @param priority    显示优先级，值越高越靠前显示
     * @param valueEffect 实时效果描述函数，接收目标实体返回动态描述组件
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute, int priority, Function<LivingEntity, Component> valueEffect) {
        return AttributeDisplay.builder(attribute)
            .priority(priority)
            .valueEffect(valueEffect)
            .build();
    }

    /**
     * 注册属性显示信息（自定义优先级 + 隐藏条件 + 实时效果描述）
     *
     * @param attribute      属性 Holder
     * @param priority       显示优先级，值越高越靠前显示
     * @param hideWhen       隐藏判定谓词，对当前属性值返回 true 时跳过显示
     * @param valueEffect    实时效果描述函数，接收目标实体返回动态描述组件
     * @return 已注册的属性显示信息
     */
    public static AttributeDisplay register(Holder<Attribute> attribute, int priority, DoublePredicate hideWhen, Function<LivingEntity, Component> valueEffect) {
        return AttributeDisplay.builder(attribute)
            .priority(priority)
            .hideWhen(hideWhen)
            .valueEffect(valueEffect)
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
