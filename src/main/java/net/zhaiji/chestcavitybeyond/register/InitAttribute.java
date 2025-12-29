package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;

public class InitAttribute {
    public static final DeferredRegister<Attribute> ATTRIBUTE = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, ChestCavityBeyond.MOD_ID);
    // 健康
    public static final Holder<Attribute> HEALTH = registerRangedAttribute("health");
    // 神经效率
    public static final Holder<Attribute> NERVES = registerRangedAttribute("nerves");
    // 防御
    public static final Holder<Attribute> DEFENSE = registerRangedAttribute("defense");
    // 消化效率
    public static final Holder<Attribute> DIGESTION = registerRangedAttribute("digestion");
    // 营养获取效率
    public static final Holder<Attribute> NUTRITION = registerRangedAttribute("nutrition");
    // 耐力
    public static final Holder<Attribute> ENDURANCE = registerRangedAttribute("endurance");
    // 新陈代谢效率
    public static final Holder<Attribute> METABOLISM = registerRangedAttribute("metabolism");
    // 肺活量
    public static final Holder<Attribute> BREATH_CAPACITY = registerRangedAttribute("breath_capacity");
    // 呼吸效率
    public static final Holder<Attribute> BREATH_RECOVERY = registerRangedAttribute("breath_recovery");
    // 水下呼吸
    public static final Holder<Attribute> WATER_BREATH = registerRangedAttribute("water_breath");
    // 解毒效率
    public static final Holder<Attribute> DETOXIFICATION = registerRangedAttribute("detoxification");
    // 血液过滤效率
    public static final Holder<Attribute> FILTRATION = registerRangedAttribute("filtration");
    // 力量
    public static final Holder<Attribute> STRENGTH = registerRangedAttribute("strength");
    // 速度
    public static final Holder<Attribute> SPEED = registerRangedAttribute("speed");
    // 火焰抗性
    public static final Holder<Attribute> FIRE_RESISTANCE = registerRangedAttribute("fire_resistance");
    // 水过敏
    // TODO 水过敏伤害没有应用函数计算进行增减，后续考虑进行计算
    public static final Holder<Attribute> WATER_ALLERGY = registerRangedAttribute("water_allergy");
    // 末影
    public static final Holder<Attribute> ENDER = registerRangedAttribute("ender");
    // 弹射物闪避
    // 拥有末影属性才能触发?
    public static final Holder<Attribute> PROJECTILE_DODGE = registerRangedAttribute("projectile_dodge");
    // 跳跃力
    public static final Holder<Attribute> LEAPING = registerRangedAttribute("leaping");
    // 肉食消化
    public static final Holder<Attribute> CARNIVOROUS_DIGESTION = registerRangedAttribute("carnivorous_digestion");
    // 肉食营养
    public static final Holder<Attribute> CARNIVOROUS_NUTRITION = registerRangedAttribute("carnivorous_nutrition");
    // 草食消化
    public static final Holder<Attribute> HERBIVOROUS_DIGESTION = registerRangedAttribute("herbivorous_digestion");
    // 草食营养
    public static final Holder<Attribute> HERBIVOROUS_NUTRITION = registerRangedAttribute("herbivorous_nutrition");
    // 腐食消化
    public static final Holder<Attribute> SCAVENGER_DIGESTION = registerRangedAttribute("scavenger_digestion");
    // 腐食营养
    public static final Holder<Attribute> SCAVENGER_NUTRITION = registerRangedAttribute("scavenger_nutrition");
    // 爆炸
    public static final Holder<Attribute> EXPLOSIVE = registerRangedAttribute("explosive");
    // 光合作用
    public static final Holder<Attribute> PHOTOSYNTHESIS = registerRangedAttribute("photosynthesis");
    // 发射
    public static final Holder<Attribute> LAUNCH = registerRangedAttribute("launch");
    // 铁修复
    public static final Holder<Attribute> IRON_REPAIR = registerRangedAttribute("iron_repair");


    public static Holder<Attribute> registerRangedAttribute(String name) {
        return ATTRIBUTE.register(
                name,
                () -> new RangedAttribute(
                        "attribute." + "chestcavitybeyond." + name,
                        0,
                        Integer.MIN_VALUE,
                        Integer.MAX_VALUE
                ).setSyncable(true)
        );
    }
}
