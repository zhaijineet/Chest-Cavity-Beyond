package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.capability.OrganFactory;
import net.zhaiji.chestcavitybeyond.item.ChestOpenerItem;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;

import java.util.function.Supplier;

public class InitItem {
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(BuiltInRegistries.ITEM, ChestCavityBeyond.MOD_ID);
    // 开胸器
    public static final Supplier<Item> CHEST_OPENER = ITEM.register(
            "chest_opener",
            ChestOpenerItem::new
    );

    // 心脏
    public static final Supplier<Item> HEART = ITEM.register(
            "heart",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肺脏
    public static final Supplier<Item> LUNG = ITEM.register(
            "lung",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 脊柱
    public static final Supplier<Item> SPINE = ITEM.register(
            "spine",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 胃
    public static final Supplier<Item> STOMACH = ITEM.register(
            "stomach",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肠子
    public static final Supplier<Item> INTESTINE = ITEM.register(
            "intestine",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肾脏
    public static final Supplier<Item> KIDNEY = ITEM.register(
            "kidney",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 脾脏
    public static final Supplier<Item> SPLEEN = ITEM.register(
            "spleen",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肝脏
    public static final Supplier<Item> LIVER = ITEM.register(
            "liver",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 阑尾
    public static final Supplier<Item> APPENDIX = ITEM.register(
            "appendix",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肋骨
    public static final Supplier<Item> RIB = ITEM.register(
            "rib",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肌肉
    public static final Supplier<Item> MUSCLE = ITEM.register(
            "muscle",
            () -> OrganFactory.builder()
                    .organModifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );
}
