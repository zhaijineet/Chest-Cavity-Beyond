package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.TagDisplay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Tag 注册中心 + 查询引擎
 * <p>
 * 包含预定义的器官类型 TagKey 常量和 TagDisplay 显示系统。
 * 附属 mod 可通过 {@link #register(String, int, int)} 或 {@link #register(TagKey, int, int)} 注册自定义 TagDisplay。
 * </p>
 */
public class ItemTagManager {
    private static final List<TagDisplay> DISPLAYS = new ArrayList<>();

    /** 排序比较器：priority 降序 → tag location 字典序 */
    private static final Comparator<TagDisplay> TAG_COMPARATOR = Comparator.comparingInt(TagDisplay::priority)
        .reversed()
        .thenComparing(display -> display.tag().location());

    // 开胸器
    public static final TagKey<Item> CHEST_OPENERS = create("chest_openers");
    // 器官
    public static final TagKey<Item> ORGANS = create("organs");
    // 心脏
    public static final TagKey<Item> HEART = create("organs/heart");
    // 肺脏
    public static final TagKey<Item> LUNG = create("organs/lung");
    // 肌肉
    public static final TagKey<Item> MUSCLE = create("organs/muscle");
    // 肋骨
    public static final TagKey<Item> RIB = create("organs/rib");
    // 阑尾
    public static final TagKey<Item> APPENDIX = create("organs/appendix");
    // 脾脏
    public static final TagKey<Item> SPLEEN = create("organs/spleen");
    // 肾脏
    public static final TagKey<Item> KIDNEY = create("organs/kidney");
    // 脊柱
    public static final TagKey<Item> SPINE = create("organs/spine");
    // 肝脏
    public static final TagKey<Item> LIVER = create("organs/liver");
    // 肠子
    public static final TagKey<Item> INTESTINE = create("organs/intestine");
    // 胃
    public static final TagKey<Item> STOMACH = create("organs/stomach");
    // 特殊
    public static final TagKey<Item> SPECIAL = create("organs/special");
    // 骨质器官
    public static final TagKey<Item> BONE = create("organs/bone");
    // 腐烂器官
    public static final TagKey<Item> ROTTEN = create("organs/rotten");
    // 铁质器官
    public static final TagKey<Item> IRON = create("organs/iron");

    /**
     * 创建 TagKey
     */
    public static TagKey<Item> create(String name) {
        return ItemTags.create(ChestCavityBeyond.of(name));
    }

    /**
     * 注册一个 Tag 显示信息。
     * <p>
     * 如果同一 {@link TagKey} 已存在注册，则替换为最新的 {@link TagDisplay}。
     * </p>
     */
    public static void register(TagDisplay display) {
        DISPLAYS.removeIf(d -> d.tag().equals(display.tag()));
        DISPLAYS.add(display);
    }

    /**
     * 对已注册的所有 TagDisplay 执行排序。
     */
    public static void sort() {
        DISPLAYS.sort(TAG_COMPARATOR);
    }

    public static TagKey<Item> register(String name, int color, int priority) {
        TagKey<Item> tagKey = ItemTags.create(ChestCavityBeyond.of(name));
        TagDisplay.builder(tagKey).color(color).priority(priority).build();
        return tagKey;
    }

    public static void register(TagKey<Item> tag, int color, int priority) {
        TagDisplay.builder(tag).color(color).priority(priority).build();
    }

    public static TagKey<Item> register(String name, int color) {
        TagKey<Item> tagKey = ItemTags.create(ChestCavityBeyond.of(name));
        TagDisplay.builder(tagKey).color(color).build();
        return tagKey;
    }

    public static void register(TagKey<Item> tag, int color) {
        register(tag, color, 0);
    }

    public static TagKey<Item> register(String name) {
        TagKey<Item> tagKey = ItemTags.create(ChestCavityBeyond.of(name));
        TagDisplay.builder(tagKey).build();
        return tagKey;
    }

    public static void register(TagKey<Item> tag) {
        TagDisplay.builder(tag).build();
    }

    /**
     * 获取物品的 Tags 行
     *
     * @return 包含所有匹配 tag 的 Component，如 "魔法 机械"
     */
    public static Component getTagsLine(ItemStack stack) {
        List<TagDisplay> matched = DISPLAYS.stream()
            .filter(display -> stack.is(display.tag()))
            .toList();

        MutableComponent line = Component.empty();

        if (matched.isEmpty()) return line;

        for (int i = 0; i < matched.size(); i++) {
            if (i > 0) line = line.append(" ");
            TagDisplay tagDisplay = matched.get(i);
            line = line.append(getTagDisplayName(tagDisplay.tag()).withColor(tagDisplay.color()));
        }
        return line;
    }

    /**
     * 从语言键获取 Tag 显示名
     */
    public static MutableComponent getTagDisplayName(TagKey<Item> tag) {
        String translationKey = Tags.getTagTranslationKey(tag);
        if (Language.getInstance().has(translationKey)) {
            return Component.translatable(translationKey);
        }
        // 翻译不存在时，取 path 最后一段，将下划线替换为空格，每个单词首字母大写作为显示名
        String path = tag.location().getPath();
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash >= 0) {
            path = path.substring(lastSlash + 1);
        }
        String[] parts = path.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) stringBuilder.append(' ');
            if (!parts[i].isEmpty()) {
                stringBuilder.append(Character.toUpperCase(parts[i].charAt(0)));
                if (parts[i].length() > 1) {
                    stringBuilder.append(parts[i].substring(1));
                }
            }
        }
        return Component.literal(stringBuilder.toString());
    }

    // ==================== 测试用 TagDisplay 注册（集中管理，方便删改） ====================
//    static {
//        // 器官类型 — 位置标签（高优先级）
//        register(HEART,     0xFFFF5555, 100);  // 红色
//        register(LUNG,      0xFF55FFFF, 100);  // 青色
//        register(MUSCLE,    0xFFFFAA00, 100);  // 橙色
//        register(RIB,       0xFFAAAAAA, 100);  // 灰色
//        register(APPENDIX,  0xFF55FF55, 100);  // 绿色
//        register(SPLEEN,    0xFFAA00AA, 100);  // 紫色
//        register(KIDNEY,    0xFF55AA55, 100);  // 深绿
//        register(SPINE,     0xFFAAAAAA, 100);  // 灰色
//        register(LIVER,     0xFFAA3333, 100);  // 深红
//        register(INTESTINE, 0xFFAA7744, 100);  // 棕色
//        register(STOMACH,   0xFFAAFF55, 100);  // 黄绿
//        register(SPECIAL,   0xFF5555FF, 100);  // 蓝色
//
//        // 材质标签（低优先级）
//        register(BONE,   0xFFDDDDDD, 50);  // 浅灰
//        register(ROTTEN, 0xFF55AA55,  50);  // 暗绿
//        register(IRON,   0xFFDDDDDD, 50);  // 银白
//    }
}
