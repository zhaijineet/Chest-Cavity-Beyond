package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;

public class ItemTagManager {
    // 器官
    public static final TagKey<Item> ORGAN = create("organ");
    // 心脏
    public static final TagKey<Item> HEART = create("heart");
    // 肺脏
    public static final TagKey<Item> LUNG = create("lung");
    // 肌肉
    public static final TagKey<Item> MUSCLE = create("muscle");
    // 肋骨
    public static final TagKey<Item> RIB = create("rib");
    // 阑尾
    public static final TagKey<Item> APPENDIX = create("appendix");
    // 脾脏
    public static final TagKey<Item> SPLEEN = create("spleen");
    // 肾脏
    public static final TagKey<Item> KIDNEY = create("kidney");
    // 脊柱
    public static final TagKey<Item> SPINE = create("spine");
    // 肝脏
    public static final TagKey<Item> LIVER = create("liver");
    // 肠子
    public static final TagKey<Item> INTESTINE = create("intestine");
    // 胃
    public static final TagKey<Item> STOMACH = create("stomach");
    // 特殊器官
    public static final TagKey<Item> SPECIAL_ORGAN = create("special_organ");

    public static TagKey<Item> create(String name) {
        return ItemTags.create(ChestCavityBeyond.of(name));
    }
}
