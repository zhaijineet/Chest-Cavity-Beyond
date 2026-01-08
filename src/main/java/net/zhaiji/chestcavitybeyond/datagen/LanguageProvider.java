package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.client.key.KeyMappings;
import net.zhaiji.chestcavitybeyond.register.*;

public class LanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {
    public static final String EN_US = "en_us";
    public static final String ZH_CN = "zh_cn";

    public final String locale;

    public LanguageProvider(PackOutput output, String locale) {
        super(output, ChestCavityBeyond.MOD_ID, locale);
        this.locale = locale;
    }

    public void English() {
        add(InitCreativeModeTab.CHEST_CAVITY_BEYOND_TAB_TRANSLATABLE, "Chest Cavity Beyond");

        add(KeyMappings.KEY_CATEGORY_TRANSLATABLE, "Chest Cavity");
        add(KeyMappings.OPEN_SKILL_GUI_TRANSLATABLE, "Open Skill Gui");
        add(KeyMappings.USE_ORGAN_SKILL_TRANSLATABLE, "Use Organ Skill");
        for (int i = 0; i < 27; i++) {
            add(KeyMappings.USE_ORGAN_SKILLS_TRANSLATABLE + i, "Organ Skill " + (i < 9 ? "0" + (i + 1) : (i + 1)));
        }

        addItem(InitItem.CHEST_OPENER, "Chest Opener");

        addItem(InitItem.HEART, "Heart");
        addItem(InitItem.SPINE, "Spine");
        addItem(InitItem.LUNG, "Lung");
        addItem(InitItem.STOMACH, "Stomach");
        addItem(InitItem.INTESTINE, "Intestine");
        addItem(InitItem.KIDNEY, "Kidney");
        addItem(InitItem.SPLEEN, "Spleen");
        addItem(InitItem.LIVER, "Liver");
        addItem(InitItem.APPENDIX, "Appendix");
        addItem(InitItem.RIB, "Rib");
        addItem(InitItem.MUSCLE, "Muscle");

        addItem(InitItem.ANIMAL_HEART, "Animal Heart");
        addItem(InitItem.ANIMAL_SPINE, "Animal Spine");
        addItem(InitItem.ANIMAL_LUNG, "Animal Lung");
        addItem(InitItem.ANIMAL_STOMACH, "Animal Stomach");
        addItem(InitItem.ANIMAL_INTESTINE, "Animal Intestine");
        addItem(InitItem.ANIMAL_KIDNEY, "Animal Kidney");
        addItem(InitItem.ANIMAL_SPLEEN, "Animal Spleen");
        addItem(InitItem.ANIMAL_LIVER, "Animal Liver");
        addItem(InitItem.ANIMAL_APPENDIX, "Animal Appendix");
        addItem(InitItem.ANIMAL_RIB, "Animal Rib");
        addItem(InitItem.ANIMAL_MUSCLE, "Animal Muscle");

        addItem(InitItem.SMALL_ANIMAL_HEART, "Small Animal Heart");
        addItem(InitItem.SMALL_ANIMAL_SPINE, "Small Animal Spine");
        addItem(InitItem.SMALL_ANIMAL_LUNG, "Small Animal Lung");
        addItem(InitItem.SMALL_ANIMAL_STOMACH, "Small Animal Stomach");
        addItem(InitItem.SMALL_ANIMAL_INTESTINE, "Small Animal Intestine");
        addItem(InitItem.SMALL_ANIMAL_KIDNEY, "Small Animal Kidney");
        addItem(InitItem.SMALL_ANIMAL_SPLEEN, "Small Animal Spleen");
        addItem(InitItem.SMALL_ANIMAL_LIVER, "Small Animal Liver");
        addItem(InitItem.SMALL_ANIMAL_APPENDIX, "Small Animal Appendix");
        addItem(InitItem.SMALL_ANIMAL_RIB, "Small Animal Rib");
        addItem(InitItem.SMALL_ANIMAL_MUSCLE, "Small Animal Muscle");

        addItem(InitItem.GILLS, "Gills");
        addItem(InitItem.SMALL_GILLS, "Small Gills");
        addItem(InitItem.AQUATIC_MUSCLE, "Aquatic Muscle");
        addItem(InitItem.SMALL_AQUATIC_MUSCLE, "Small Aquatic Muscle");
        addItem(InitItem.FISH_SPINE, "Fish Spine");
        addItem(InitItem.SMALL_FISH_SPINE, "Small Fish Spine");
        addItem(InitItem.FISH_BONE, "Fish Bone");
        addItem(InitItem.SMALL_FISH_BONE, "Small Fish Bone");
        addItem(InitItem.FISH_INTESTINE, "Fish Intestine");
        addItem(InitItem.SMALL_FISH_INTESTINE, "Small Fish Intestine");
        addItem(InitItem.FISH_MUSCLE, "Fish Muscle");
        addItem(InitItem.SMALL_FISH_MUSCLE, "Small Fish Muscle");

        addItem(InitItem.SALTWATER_HEART, "Saltwater Heart");
        addItem(InitItem.SALTWATER_LUNG, "Saltwater Lung");
        addItem(InitItem.SALTWATER_MUSCLE, "Saltwater Muscle");

        addItem(InitItem.FIREPROOF_HEART, "Fireproof Heart");
        addItem(InitItem.FIREPROOF_LUNG, "Fireproof Lung");
        addItem(InitItem.FIREPROOF_SPINE, "Fireproof Spine");
        addItem(InitItem.FIREPROOF_STOMACH, "Fireproof Stomach");
        addItem(InitItem.FIREPROOF_INTESTINE, "Fireproof Intestine");
        addItem(InitItem.FIREPROOF_KIDNEY, "Fireproof Kidney");
        addItem(InitItem.FIREPROOF_SPLEEN, "Fireproof Spleen");
        addItem(InitItem.FIREPROOF_LIVER, "Fireproof Liver");
        addItem(InitItem.FIREPROOF_APPENDIX, "Fireproof Appendix");
        addItem(InitItem.FIREPROOF_RIB, "Fireproof Rib");
        addItem(InitItem.FIREPROOF_MUSCLE, "Fireproof Muscle");

        addItem(InitItem.ENDER_HEART, "Ender Heart");
        addItem(InitItem.ENDER_LUNG, "Ender Lung");
        addItem(InitItem.ENDER_SPINE, "Ender Spine");
        addItem(InitItem.ENDER_STOMACH, "Ender Stomach");
        addItem(InitItem.ENDER_INTESTINE, "Ender Intestine");
        addItem(InitItem.ENDER_KIDNEY, "Ender Kidney");
        addItem(InitItem.ENDER_SPLEEN, "Ender Spleen");
        addItem(InitItem.ENDER_LIVER, "Ender Liver");
        addItem(InitItem.ENDER_APPENDIX, "Ender Appendix");
        addItem(InitItem.ENDER_RIB, "Ender Rib");
        addItem(InitItem.ENDER_MUSCLE, "Ender Muscle");

        addItem(InitItem.BRUTE_MUSCLE, "Brute Muscle");
        addItem(InitItem.SWIFT_MUSCLE, "Swift Muscle");
        addItem(InitItem.LEAPING_MUSCLE, "Leaping Muscle");
        addItem(InitItem.SMALL_LEAPING_MUSCLE, "Small Leaping Muscle");

        addItem(InitItem.CARNIVORE_STOMACH, "Carnivore Stomach");
        addItem(InitItem.SMALL_CARNIVORE_STOMACH, "Small Carnivore Stomach");
        addItem(InitItem.CARNIVORE_INTESTINE, "Carnivore Intestine");
        addItem(InitItem.SMALL_CARNIVORE_INTESTINE, "Small Carnivore Intestine");
        addItem(InitItem.HERBIVORE_STOMACH, "Herbivore Stomach");
        addItem(InitItem.SMALL_HERBIVORE_STOMACH, "Small Herbivore Stomach");
        addItem(InitItem.HERBIVORE_INTESTINE, "Herbivore Intestine");
        addItem(InitItem.SMALL_HERBIVORE_INTESTINE, "Small Herbivore Intestine");
        addItem(InitItem.HERBIVORE_RUMEN, "Herbivore Rumen");

        addItem(InitItem.CREEPER_APPENDIX, "Creeper Appendix");
        addItem(InitItem.CREEPER_LEAF, "Creeper Leaf");

        addItem(InitItem.ROTTEN_HEART, "Rotten Heart");
        addItem(InitItem.ROTTEN_SPINE, "Rotten Spine");
        addItem(InitItem.ROTTEN_LUNG, "Rotten Lung");
        addItem(InitItem.ROTTEN_STOMACH, "Rotten Stomach");
        addItem(InitItem.ROTTEN_INTESTINE, "Rotten Intestine");
        addItem(InitItem.ROTTEN_KIDNEY, "Rotten Kidney");
        addItem(InitItem.ROTTEN_SPLEEN, "Rotten Spleen");
        addItem(InitItem.ROTTEN_LIVER, "Rotten Liver");
        addItem(InitItem.ROTTEN_APPENDIX, "Rotten Appendix");
        addItem(InitItem.ROTTEN_RIB, "Rotten Rib");
        addItem(InitItem.ROTTEN_MUSCLE, "Rotten Muscle");
        addItem(InitItem.WITHERED_SPINE, "Withered Spine");
        addItem(InitItem.WITHERED_RIB, "Withered Rib");
        addItem(InitItem.WRITHING_SOUL_SAND, "Writhing Soul Sand");

        addItem(InitItem.GOLEM_CORE, "Golem Core");
        addItem(InitItem.GOLEM_CABLE, "Golem Cable");
        addItem(InitItem.INNER_FURNACE, "Inner Furnace");
        addItem(InitItem.PISTON_MUSCLE, "Piston Muscle");
        addItem(InitItem.GOLEM_ARMOR_PLATE, "Golem Armor Plate");

        addItem(InitItem.SILK_GLAND, "Silk Gland");
        addItem(InitItem.VENOM_GLAND, "Venom Gland");
        addItem(InitItem.ARTHROPOD_HEART, "Arthropod Heart");
        addItem(InitItem.ARTHROPOD_INTESTINE, "Arthropod Intestine");
        addItem(InitItem.ARTHROPOD_LUNG, "Arthropod Lung");
        addItem(InitItem.ARTHROPOD_MUSCLE, "Arthropod Muscle");
        addItem(InitItem.ARTHROPOD_STOMACH, "Arthropod Stomach");
        addItem(InitItem.ARTHROPOD_CAECUM, "Arthropod Caecum");

        addItem(InitItem.LLAMA_LUNG, "Llama Lung");

        addItem(InitItem.BLAZE_CORE, "Blaze Core");
        addItem(InitItem.BLAZE_SHELL, "Blaze Shell");
        addItem(InitItem.ACTIVE_BLAZE_ROD, "Active Blaze Rod");

        addItem(InitItem.SNOW_HEART, "Snow Heart");

        addItem(InitItem.GHAST_STOMACH, "Ghast Stomach");
        addItem(InitItem.GAS_SAC, "Gas Sac");

        addItem(InitItem.SHULKER_SPLEEN, "Shlker Spleen");

        addItem(InitItem.BREEZE_HEART, "Breeze Heart");
        addItem(InitItem.ACTIVE_BREEZE_ROD, "Active Breeze Rod");

        addItem(InitItem.DRAGON_HEART, "Dragon Heart");
        addItem(InitItem.DRAGON_LUNG, "Dragon Lung");
        addItem(InitItem.DRAGON_SPINE, "Dragon Spine");
        addItem(InitItem.DRAGON_KIDNEY, "Dragon Kidney");
        addItem(InitItem.DRAGON_SPLEEN, "Dragon Spleen");
        addItem(InitItem.DRAGON_LIVER, "Dragon Liver");
        addItem(InitItem.DRAGON_APPENDIX, "Dragon Appendix");
        addItem(InitItem.DRAGON_RIB, "Dragon Rib");
        addItem(InitItem.DRAGON_MUSCLE, "Dragon Muscle");
        addItem(InitItem.MANA_REACTOR, "Mana Reactor");

        addItem(InitItem.SLIME_CORE, "Slime Core");
        addItem(InitItem.SLIME_STOMACH, "Slime Stomach");
        addItem(InitItem.MAGMA_CUBE_CORE, "Magma Cube Core");
        addItem(InitItem.MAGMA_STOMACH, "Magma Stomach");

        addItem(InitItem.SCULK_HEART, "Sculk Heart");
        addItem(InitItem.SCULK_SPINE, "Sculk Spine");
        addItem(InitItem.SCULK_RIB, "Sculk Rib");
        addItem(InitItem.SCULK_MUSCLE, "Sculk Muscle");
        addItem(InitItem.SCULK_CORE, "Sculk Core");

        addAttribute(InitAttribute.HEALTH, "Health");
        addAttribute(InitAttribute.NERVES, "Nerves");
        addAttribute(InitAttribute.DEFENSE, "Defense");
        addAttribute(InitAttribute.DIGESTION, "Digestion");
        addAttribute(InitAttribute.NUTRITION, "Nutrition");
        addAttribute(InitAttribute.ENDURANCE, "Endurance");
        addAttribute(InitAttribute.METABOLISM, "Metabolism");
        addAttribute(InitAttribute.BREATH_CAPACITY, "Breath Capacity");
        addAttribute(InitAttribute.BREATH_RECOVERY, "Breath Recovery");
        addAttribute(InitAttribute.WATER_BREATH, "Water Breath");
        addAttribute(InitAttribute.DETOXIFICATION, "Detoxification");
        addAttribute(InitAttribute.FILTRATION, "Filtration");
        addAttribute(InitAttribute.STRENGTH, "Strength");
        addAttribute(InitAttribute.SPEED, "Speed");
        addAttribute(InitAttribute.FIRE_RESISTANCE, "Fire Resistance");
        addAttribute(InitAttribute.WATER_ALLERGY, "Water Allergy");
        addAttribute(InitAttribute.ENDER, "Ender");
        addAttribute(InitAttribute.PROJECTILE_DODGE, "Projectile Dodge");
        addAttribute(InitAttribute.LEAPING, "Leaping");
        addAttribute(InitAttribute.CARNIVOROUS_DIGESTION, "Carnivorous Digestion");
        addAttribute(InitAttribute.CARNIVOROUS_NUTRITION, "Carnivorous Nutrition");
        addAttribute(InitAttribute.HERBIVOROUS_DIGESTION, "Herbivorous Digestion");
        addAttribute(InitAttribute.HERBIVOROUS_NUTRITION, "Herbivorous Nutrition");
        addAttribute(InitAttribute.SCAVENGER_DIGESTION, "Scavenger Digestion");
        addAttribute(InitAttribute.SCAVENGER_NUTRITION, "Scavenger Nutrition");
        addAttribute(InitAttribute.EXPLOSIVE, "Explosive");
        addAttribute(InitAttribute.PHOTOSYNTHESIS, "Photosynthesis");
        addAttribute(InitAttribute.LAUNCH, "Launch");
        addAttribute(InitAttribute.IRON_REPAIR, "Iron Repair");
        addAttribute(InitAttribute.FURNACE_POWER, "Furnace Power");
        addAttribute(InitAttribute.WITHERED, "Withered");
        addAttribute(InitAttribute.VOMIT_FIREBALL, "Vomit Fireball");
        addAttribute(InitAttribute.GHASTLY, "Ghastly");
        addAttribute(InitAttribute.CRYSTALLIZATION, "Crystallization");

        addEffect(InitEffect.FURNACE_POWER::value, "Furnace Powered");

        addDamageType(InitDamageType.ORGAN_LOSS, "%1$s died from organ loss");
    }

    public void Chinese() {
        add(InitCreativeModeTab.CHEST_CAVITY_BEYOND_TAB_TRANSLATABLE, "胸腔");

        add(KeyMappings.KEY_CATEGORY_TRANSLATABLE, "胸腔");
        add(KeyMappings.OPEN_SKILL_GUI_TRANSLATABLE, "打开技能界面");
        add(KeyMappings.USE_ORGAN_SKILL_TRANSLATABLE, "使用器官技能");
        for (int i = 0; i < 27; i++) {
            add(KeyMappings.USE_ORGAN_SKILLS_TRANSLATABLE + i, "器官技能" + (i < 9 ? "0" + (i + 1) : (i + 1)));
        }

        addItem(InitItem.CHEST_OPENER, "开胸器");

        addItem(InitItem.HEART, "心脏");
        addItem(InitItem.SPINE, "脊柱");
        addItem(InitItem.LUNG, "肺脏");
        addItem(InitItem.STOMACH, "胃");
        addItem(InitItem.INTESTINE, "肠子");
        addItem(InitItem.KIDNEY, "肾脏");
        addItem(InitItem.SPLEEN, "脾脏");
        addItem(InitItem.LIVER, "肝脏");
        addItem(InitItem.APPENDIX, "阑尾");
        addItem(InitItem.RIB, "肋骨");
        addItem(InitItem.MUSCLE, "肌肉");

        addItem(InitItem.ANIMAL_HEART, "动物心脏");
        addItem(InitItem.ANIMAL_SPINE, "动物脊柱");
        addItem(InitItem.ANIMAL_LUNG, "动物肺脏");
        addItem(InitItem.ANIMAL_STOMACH, "动物胃");
        addItem(InitItem.ANIMAL_INTESTINE, "动物肠子");
        addItem(InitItem.ANIMAL_KIDNEY, "动物肾脏");
        addItem(InitItem.ANIMAL_SPLEEN, "动物脾脏");
        addItem(InitItem.ANIMAL_LIVER, "动物肝脏");
        addItem(InitItem.ANIMAL_APPENDIX, "动物阑尾");
        addItem(InitItem.ANIMAL_RIB, "动物肋骨");
        addItem(InitItem.ANIMAL_MUSCLE, "动物肌肉");

        addItem(InitItem.SMALL_ANIMAL_HEART, "小型动物心脏");
        addItem(InitItem.SMALL_ANIMAL_SPINE, "小型动物脊柱");
        addItem(InitItem.SMALL_ANIMAL_LUNG, "小型动物肺脏");
        addItem(InitItem.SMALL_ANIMAL_STOMACH, "小型动物胃");
        addItem(InitItem.SMALL_ANIMAL_INTESTINE, "小型动物肠子");
        addItem(InitItem.SMALL_ANIMAL_KIDNEY, "小型动物肾脏");
        addItem(InitItem.SMALL_ANIMAL_SPLEEN, "小型动物脾脏");
        addItem(InitItem.SMALL_ANIMAL_LIVER, "小型动物肝脏");
        addItem(InitItem.SMALL_ANIMAL_APPENDIX, "小型动物阑尾");
        addItem(InitItem.SMALL_ANIMAL_RIB, "小型动物肋骨");
        addItem(InitItem.SMALL_ANIMAL_MUSCLE, "小型动物肌肉");

        addItem(InitItem.GILLS, "鳃");
        addItem(InitItem.SMALL_GILLS, "小型鳃");
        addItem(InitItem.AQUATIC_MUSCLE, "水生生物肌肉");
        addItem(InitItem.SMALL_AQUATIC_MUSCLE, "小型水生生物肌肉");
        addItem(InitItem.FISH_SPINE, "鱼类脊柱");
        addItem(InitItem.SMALL_FISH_SPINE, "小型鱼类脊柱");
        addItem(InitItem.FISH_BONE, "鱼骨");
        addItem(InitItem.SMALL_FISH_BONE, "细小鱼骨");
        addItem(InitItem.FISH_INTESTINE, "鱼肠");
        addItem(InitItem.SMALL_FISH_INTESTINE, "小型鱼肠");
        addItem(InitItem.FISH_MUSCLE, "鱼类肌肉");
        addItem(InitItem.SMALL_FISH_MUSCLE, "小型鱼类肌肉");
        addItem(InitItem.SALTWATER_HEART, "盐水型心脏");
        addItem(InitItem.SALTWATER_LUNG, "盐水型肺脏");
        addItem(InitItem.SALTWATER_MUSCLE, "盐水型肌肉");

        addItem(InitItem.FIREPROOF_HEART, "抗火生物心脏");
        addItem(InitItem.FIREPROOF_LUNG, "抗火生物肺脏");
        addItem(InitItem.FIREPROOF_SPINE, "抗火生物脊柱");
        addItem(InitItem.FIREPROOF_STOMACH, "抗火生物胃");
        addItem(InitItem.FIREPROOF_INTESTINE, "抗火生物肠子");
        addItem(InitItem.FIREPROOF_KIDNEY, "抗火生物肾脏");
        addItem(InitItem.FIREPROOF_SPLEEN, "抗火生物脾脏");
        addItem(InitItem.FIREPROOF_LIVER, "抗火生物肝脏");
        addItem(InitItem.FIREPROOF_APPENDIX, "抗火生物阑尾");
        addItem(InitItem.FIREPROOF_RIB, "抗火生物肋骨");
        addItem(InitItem.FIREPROOF_MUSCLE, "抗火生物肌肉");

        addItem(InitItem.ENDER_HEART, "末影心脏");
        addItem(InitItem.ENDER_LUNG, "末影肺脏");
        addItem(InitItem.ENDER_SPINE, "末影脊柱");
        addItem(InitItem.ENDER_STOMACH, "末影胃");
        addItem(InitItem.ENDER_INTESTINE, "末影肠子");
        addItem(InitItem.ENDER_KIDNEY, "末影肾脏");
        addItem(InitItem.ENDER_SPLEEN, "末影脾脏");
        addItem(InitItem.ENDER_LIVER, "末影肝脏");
        addItem(InitItem.ENDER_APPENDIX, "末影阑尾");
        addItem(InitItem.ENDER_RIB, "末影肋骨");
        addItem(InitItem.ENDER_MUSCLE, "末影肌肉");

        addItem(InitItem.BRUTE_MUSCLE, "力量型肌肉");
        addItem(InitItem.SWIFT_MUSCLE, "速度型肌肉");
        addItem(InitItem.LEAPING_MUSCLE, "弹跳型肌肉");
        addItem(InitItem.SMALL_LEAPING_MUSCLE, "小型弹跳型肌肉");

        addItem(InitItem.CARNIVORE_STOMACH, "食肉动物胃");
        addItem(InitItem.SMALL_CARNIVORE_STOMACH, "小型食肉动物胃");
        addItem(InitItem.CARNIVORE_INTESTINE, "食肉动物肠子");
        addItem(InitItem.SMALL_CARNIVORE_INTESTINE, "小型食肉动物肠子");
        addItem(InitItem.HERBIVORE_STOMACH, "食草动物胃");
        addItem(InitItem.SMALL_HERBIVORE_STOMACH, "小型食草动物胃");
        addItem(InitItem.HERBIVORE_INTESTINE, "食草动物肠子");
        addItem(InitItem.SMALL_HERBIVORE_INTESTINE, "小型食草动物肠子");
        addItem(InitItem.HERBIVORE_RUMEN, "食草动物瘤胃");

        addItem(InitItem.CREEPER_APPENDIX, "苦力怕阑尾");
        addItem(InitItem.CREEPER_LEAF, "苦力怕叶");

        addItem(InitItem.ROTTEN_HEART, "腐烂心脏");
        addItem(InitItem.ROTTEN_SPINE, "腐烂脊柱");
        addItem(InitItem.ROTTEN_LUNG, "腐烂肺脏");
        addItem(InitItem.ROTTEN_STOMACH, "腐烂胃");
        addItem(InitItem.ROTTEN_INTESTINE, "腐烂肠子");
        addItem(InitItem.ROTTEN_KIDNEY, "腐烂肾脏");
        addItem(InitItem.ROTTEN_SPLEEN, "腐烂脾脏");
        addItem(InitItem.ROTTEN_LIVER, "腐烂肝脏");
        addItem(InitItem.ROTTEN_APPENDIX, "腐烂阑尾");
        addItem(InitItem.ROTTEN_RIB, "腐烂肋骨");
        addItem(InitItem.ROTTEN_MUSCLE, "腐烂肌肉");
        addItem(InitItem.WITHERED_SPINE, "凋零脊柱");
        addItem(InitItem.WITHERED_RIB, "凋零肋骨");
        addItem(InitItem.WRITHING_SOUL_SAND, "扭曲灵魂沙");

        addItem(InitItem.GOLEM_CORE, "傀儡核心");
        addItem(InitItem.GOLEM_CABLE, "傀儡电缆");
        addItem(InitItem.INNER_FURNACE, "熔炉内核");
        addItem(InitItem.PISTON_MUSCLE, "活塞型肌肉");
        addItem(InitItem.GOLEM_ARMOR_PLATE, "傀儡装甲板");

        addItem(InitItem.SILK_GLAND, "丝腺");
        addItem(InitItem.VENOM_GLAND, "毒腺");
        addItem(InitItem.ARTHROPOD_HEART, "节肢生物心脏");
        addItem(InitItem.ARTHROPOD_INTESTINE, "节肢生物肠子");
        addItem(InitItem.ARTHROPOD_LUNG, "节肢生物肺脏");
        addItem(InitItem.ARTHROPOD_MUSCLE, "节肢生物肌肉");
        addItem(InitItem.ARTHROPOD_STOMACH, "节肢生物胃");
        addItem(InitItem.ARTHROPOD_CAECUM, "节肢生物盲囊");

        addItem(InitItem.LLAMA_LUNG, "羊驼肺脏");

        addItem(InitItem.BLAZE_CORE, "烈焰核心");
        addItem(InitItem.BLAZE_SHELL, "烈焰外壳");
        addItem(InitItem.ACTIVE_BLAZE_ROD, "活性烈焰棒");

        addItem(InitItem.SNOW_HEART, "雪之心");

        addItem(InitItem.GHAST_STOMACH, "恶魂胃");
        addItem(InitItem.GAS_SAC, "气囊");

        addItem(InitItem.SHULKER_SPLEEN, "潜影贝脾脏");

        addItem(InitItem.BREEZE_HEART, "旋风核心");
        addItem(InitItem.ACTIVE_BREEZE_ROD, "活性旋风棒");

        addItem(InitItem.DRAGON_HEART, "龙之心脏");
        addItem(InitItem.DRAGON_LUNG, "龙之肺脏");
        addItem(InitItem.DRAGON_SPINE, "龙之脊柱");
        addItem(InitItem.DRAGON_KIDNEY, "龙之肾脏");
        addItem(InitItem.DRAGON_SPLEEN, "龙之脾脏");
        addItem(InitItem.DRAGON_LIVER, "龙之肝脏");
        addItem(InitItem.DRAGON_APPENDIX, "龙之阑尾");
        addItem(InitItem.DRAGON_RIB, "龙之肋骨");
        addItem(InitItem.DRAGON_MUSCLE, "龙之肌肉");
        addItem(InitItem.MANA_REACTOR, "魔力反应装置");

        addItem(InitItem.SLIME_CORE, "史莱姆核心");
        addItem(InitItem.SLIME_STOMACH, "粘液胃");
        addItem(InitItem.MAGMA_CUBE_CORE, "岩浆怪核心");
        addItem(InitItem.MAGMA_STOMACH, "熔岩胃");

        addItem(InitItem.SCULK_HEART, "幽匿心脏");
        addItem(InitItem.SCULK_SPINE, "幽匿脊柱");
        addItem(InitItem.SCULK_RIB, "幽匿肋骨");
        addItem(InitItem.SCULK_MUSCLE, "幽匿肌肉");
        addItem(InitItem.SCULK_CORE, "幽匿核心");

        addAttribute(InitAttribute.HEALTH, "健康");
        addAttribute(InitAttribute.NERVES, "神经效率");
        addAttribute(InitAttribute.DEFENSE, "防御");
        addAttribute(InitAttribute.DIGESTION, "消化效率");
        addAttribute(InitAttribute.NUTRITION, "营养获取效率");
        addAttribute(InitAttribute.ENDURANCE, "耐力");
        addAttribute(InitAttribute.METABOLISM, "新陈代谢效率");
        addAttribute(InitAttribute.BREATH_CAPACITY, "肺活量");
        addAttribute(InitAttribute.BREATH_RECOVERY, "呼吸效率");
        addAttribute(InitAttribute.WATER_BREATH, "水下呼吸");
        addAttribute(InitAttribute.DETOXIFICATION, "解毒效率");
        addAttribute(InitAttribute.FILTRATION, "血液过滤效率");
        addAttribute(InitAttribute.STRENGTH, "力量");
        addAttribute(InitAttribute.SPEED, "速度");
        addAttribute(InitAttribute.FIRE_RESISTANCE, "火焰抗性");
        addAttribute(InitAttribute.WATER_ALLERGY, "水过敏");
        addAttribute(InitAttribute.ENDER, "末影");
        addAttribute(InitAttribute.PROJECTILE_DODGE, "弹射物闪避");
        addAttribute(InitAttribute.LEAPING, "跳跃力");
        addAttribute(InitAttribute.CARNIVOROUS_DIGESTION, "肉食消化");
        addAttribute(InitAttribute.CARNIVOROUS_NUTRITION, "肉食营养");
        addAttribute(InitAttribute.HERBIVOROUS_DIGESTION, "草食消化");
        addAttribute(InitAttribute.HERBIVOROUS_NUTRITION, "草食营养");
        addAttribute(InitAttribute.SCAVENGER_DIGESTION, "腐食消化");
        addAttribute(InitAttribute.SCAVENGER_NUTRITION, "腐食营养");
        addAttribute(InitAttribute.EXPLOSIVE, "爆炸");
        addAttribute(InitAttribute.PHOTOSYNTHESIS, "光合作用");
        addAttribute(InitAttribute.LAUNCH, "发射");
        addAttribute(InitAttribute.IRON_REPAIR, "铁修复");
        addAttribute(InitAttribute.FURNACE_POWER, "熔炉之力");
        addAttribute(InitAttribute.WITHERED, "凋零化");
        addAttribute(InitAttribute.VOMIT_FIREBALL, "呕火");
        addAttribute(InitAttribute.GHASTLY, "可怖");
        addAttribute(InitAttribute.CRYSTALLIZATION, "结晶化");

        addEffect(InitEffect.FURNACE_POWER::value, "熔炉之力");

        addDamageType(InitDamageType.ORGAN_LOSS, "%1$s死于器官缺失");
    }

    private void addAttribute(Holder<Attribute> attribute, String value) {
        add(attribute.value().getDescriptionId(), value);
    }

    private void addDamageType(ResourceKey<DamageType> damageType, String value) {
        add("death.attack." + damageType.location().getNamespace() + "." + damageType.location().getPath(), value);
    }

    @Override
    protected void addTranslations() {
        switch (locale) {
            case EN_US -> English();
            case ZH_CN -> Chinese();
        }
    }
}
