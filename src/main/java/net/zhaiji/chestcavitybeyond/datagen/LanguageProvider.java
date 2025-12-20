package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.data.PackOutput;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitCreativeModeTab;
import net.zhaiji.chestcavitybeyond.register.InitItem;

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

        add(InitItem.CHEST_OPENER.get(), "Chest Opener");

        add(InitItem.HEART.get(), "Heart");
        add(InitItem.SPINE.get(), "Spine");
        add(InitItem.LUNG.get(), "Lung");
        add(InitItem.STOMACH.get(), "Stomach");
        add(InitItem.INTESTINE.get(), "Intestine");
        add(InitItem.KIDNEY.get(), "Kidney");
        add(InitItem.SPLEEN.get(), "Spleen");
        add(InitItem.LIVER.get(), "Liver");
        add(InitItem.APPENDIX.get(), "Appendix");
        add(InitItem.RIB.get(), "Rib");
        add(InitItem.MUSCLE.get(), "Muscle");

        add(InitItem.ANIMAL_HEART.get(), "Animal Heart");
        add(InitItem.ANIMAL_SPINE.get(), "Animal Spine");
        add(InitItem.ANIMAL_LUNG.get(), "Animal Lung");
        add(InitItem.ANIMAL_STOMACH.get(), "Animal Stomach");
        add(InitItem.ANIMAL_INTESTINE.get(), "Animal Intestine");
        add(InitItem.ANIMAL_KIDNEY.get(), "Animal Kidney");
        add(InitItem.ANIMAL_SPLEEN.get(), "Animal Spleen");
        add(InitItem.ANIMAL_LIVER.get(), "Animal Liver");
        add(InitItem.ANIMAL_APPENDIX.get(), "Animal Appendix");
        add(InitItem.ANIMAL_RIB.get(), "Animal Rib");
        add(InitItem.ANIMAL_MUSCLE.get(), "Animal Muscle");

        add(InitItem.SMALL_ANIMAL_HEART.get(), "Small Animal Heart");
        add(InitItem.SMALL_ANIMAL_SPINE.get(), "Small Animal Spine");
        add(InitItem.SMALL_ANIMAL_LUNG.get(), "Small Animal Lung");
        add(InitItem.SMALL_ANIMAL_STOMACH.get(), "Small Animal Stomach");
        add(InitItem.SMALL_ANIMAL_INTESTINE.get(), "Small Animal Intestine");
        add(InitItem.SMALL_ANIMAL_KIDNEY.get(), "Small Animal Kidney");
        add(InitItem.SMALL_ANIMAL_SPLEEN.get(), "Small Animal Spleen");
        add(InitItem.SMALL_ANIMAL_LIVER.get(), "Small Animal Liver");
        add(InitItem.SMALL_ANIMAL_APPENDIX.get(), "Small Animal Appendix");
        add(InitItem.SMALL_ANIMAL_RIB.get(), "Small Animal Rib");
        add(InitItem.SMALL_ANIMAL_MUSCLE.get(), "Small Animal Muscle");

        add(InitItem.GILLS.get(), "Gills");
        add(InitItem.SMALL_GILLS.get(), "Small Gills");
        add(InitItem.AQUATIC_MUSCLE.get(), "Aquatic Muscle");
        add(InitItem.SMALL_AQUATIC_MUSCLE.get(), "Small Aquatic Muscle");
        add(InitItem.FISH_MUSCLE.get(), "Fish Muscle");
        add(InitItem.SMALL_FISH_MUSCLE.get(), "Small Fish Muscle");

        add(InitItem.SALTWATER_HEART.get(), "Saltwater Heart");
        add(InitItem.SALTWATER_LUNG.get(), "Saltwater Lung");
        add(InitItem.SALTWATER_MUSCLE.get(), "Saltwater Muscle");

        add(InitItem.FIREPROOF_HEART.get(), "Fireproof Heart");
        add(InitItem.FIREPROOF_LUNG.get(), "Fireproof Lung");
        add(InitItem.FIREPROOF_SPINE.get(), "Fireproof Spine");
        add(InitItem.FIREPROOF_STOMACH.get(), "Fireproof Stomach");
        add(InitItem.FIREPROOF_INTESTINE.get(), "Fireproof Intestine");
        add(InitItem.FIREPROOF_KIDNEY.get(), "Fireproof Kidney");
        add(InitItem.FIREPROOF_SPLEEN.get(), "Fireproof Spleen");
        add(InitItem.FIREPROOF_LIVER.get(), "Fireproof Liver");
        add(InitItem.FIREPROOF_APPENDIX.get(), "Fireproof Appendix");
        add(InitItem.FIREPROOF_RIB.get(), "Fireproof Rib");
        add(InitItem.FIREPROOF_MUSCLE.get(), "Fireproof Muscle");

        add(InitAttribute.HEALTH.value().getDescriptionId(), "Health");
        add(InitAttribute.NERVES.value().getDescriptionId(), "Nerves");
        add(InitAttribute.DEFENSE.value().getDescriptionId(), "Defense");
        add(InitAttribute.DIGESTION.value().getDescriptionId(), "Digestion");
        add(InitAttribute.NUTRITION.value().getDescriptionId(), "Nutrition");
        add(InitAttribute.ENDURANCE.value().getDescriptionId(), "Endurance");
        add(InitAttribute.METABOLISM.value().getDescriptionId(), "Metabolism");
        add(InitAttribute.BREATH_CAPACITY.value().getDescriptionId(), "Breath Capacity");
        add(InitAttribute.BREATH_RECOVERY.value().getDescriptionId(), "Breath Recovery");
        add(InitAttribute.WATER_BREATH.value().getDescriptionId(), "Water Breath");
        add(InitAttribute.DETOXIFICATION.value().getDescriptionId(), "Detoxification");
        add(InitAttribute.FILTRATION.value().getDescriptionId(), "Filtration");
        add(InitAttribute.STRENGTH.value().getDescriptionId(), "Strength");
        add(InitAttribute.SPEED.value().getDescriptionId(), "Speed");
        add(InitAttribute.FIRE_RESISTANCE.value().getDescriptionId(), "Fire Resistance");
        add(InitAttribute.WATER_ALLERGY.value().getDescriptionId(), "Water Allergy");
        add(InitAttribute.HYDROPHOBIA.value().getDescriptionId(), "Hydrophobia");
        add(InitAttribute.ENDER.value().getDescriptionId(), "Ender");
        add(InitAttribute.PROJECTILE_DODGE.value().getDescriptionId(), "Projectile Dodge");
        add(InitAttribute.LEAPING.value().getDescriptionId(), "Leaping");
    }

    public void Chinese() {
        add(InitCreativeModeTab.CHEST_CAVITY_BEYOND_TAB_TRANSLATABLE, "胸腔");

        add(InitItem.CHEST_OPENER.get(), "开胸器");

        add(InitItem.HEART.get(), "心脏");
        add(InitItem.SPINE.get(), "脊柱");
        add(InitItem.LUNG.get(), "肺脏");
        add(InitItem.STOMACH.get(), "胃");
        add(InitItem.INTESTINE.get(), "肠子");
        add(InitItem.KIDNEY.get(), "肾脏");
        add(InitItem.SPLEEN.get(), "脾脏");
        add(InitItem.LIVER.get(), "肝脏");
        add(InitItem.APPENDIX.get(), "阑尾");
        add(InitItem.RIB.get(), "肋骨");
        add(InitItem.MUSCLE.get(), "肌肉");

        add(InitItem.ANIMAL_HEART.get(), "动物心脏");
        add(InitItem.ANIMAL_SPINE.get(), "动物脊柱");
        add(InitItem.ANIMAL_LUNG.get(), "动物肺脏");
        add(InitItem.ANIMAL_STOMACH.get(), "动物胃");
        add(InitItem.ANIMAL_INTESTINE.get(), "动物肠子");
        add(InitItem.ANIMAL_KIDNEY.get(), "动物肾脏");
        add(InitItem.ANIMAL_SPLEEN.get(), "动物脾脏");
        add(InitItem.ANIMAL_LIVER.get(), "动物肝脏");
        add(InitItem.ANIMAL_APPENDIX.get(), "动物阑尾");
        add(InitItem.ANIMAL_RIB.get(), "动物肋骨");
        add(InitItem.ANIMAL_MUSCLE.get(), "动物肌肉");

        add(InitItem.SMALL_ANIMAL_HEART.get(), "小型动物心脏");
        add(InitItem.SMALL_ANIMAL_SPINE.get(), "小型动物脊柱");
        add(InitItem.SMALL_ANIMAL_LUNG.get(), "小型动物肺脏");
        add(InitItem.SMALL_ANIMAL_STOMACH.get(), "小型动物胃");
        add(InitItem.SMALL_ANIMAL_INTESTINE.get(), "小型动物肠子");
        add(InitItem.SMALL_ANIMAL_KIDNEY.get(), "小型动物肾脏");
        add(InitItem.SMALL_ANIMAL_SPLEEN.get(), "小型动物脾脏");
        add(InitItem.SMALL_ANIMAL_LIVER.get(), "小型动物肝脏");
        add(InitItem.SMALL_ANIMAL_APPENDIX.get(), "小型动物阑尾");
        add(InitItem.SMALL_ANIMAL_RIB.get(), "小型动物肋骨");
        add(InitItem.SMALL_ANIMAL_MUSCLE.get(), "小型动物肌肉");

        add(InitItem.GILLS.get(), "鳃");
        add(InitItem.SMALL_GILLS.get(), "小型鳃");
        add(InitItem.AQUATIC_MUSCLE.get(), "水生生物肌肉");
        add(InitItem.SMALL_AQUATIC_MUSCLE.get(), "小型水生生物肌肉");
        add(InitItem.FISH_MUSCLE.get(), "鱼类肌肉");
        add(InitItem.SMALL_FISH_MUSCLE.get(), "小型鱼类肌肉");
        add(InitItem.SALTWATER_HEART.get(), "盐水型心脏");
        add(InitItem.SALTWATER_LUNG.get(), "盐水型肺脏");
        add(InitItem.SALTWATER_MUSCLE.get(), "盐水型肌肉");

        add(InitItem.FIREPROOF_HEART.get(), "抗火生物心脏");
        add(InitItem.FIREPROOF_LUNG.get(), "抗火生物肺脏");
        add(InitItem.FIREPROOF_SPINE.get(), "抗火生物脊柱");
        add(InitItem.FIREPROOF_STOMACH.get(), "抗火生物胃");
        add(InitItem.FIREPROOF_INTESTINE.get(), "抗火生物肠子");
        add(InitItem.FIREPROOF_KIDNEY.get(), "抗火生物肾脏");
        add(InitItem.FIREPROOF_SPLEEN.get(), "抗火生物脾脏");
        add(InitItem.FIREPROOF_LIVER.get(), "抗火生物肝脏");
        add(InitItem.FIREPROOF_APPENDIX.get(), "抗火生物阑尾");
        add(InitItem.FIREPROOF_RIB.get(), "抗火生物肋骨");
        add(InitItem.FIREPROOF_MUSCLE.get(), "抗火生物肌肉");

        add(InitAttribute.HEALTH.value().getDescriptionId(), "健康");
        add(InitAttribute.NERVES.value().getDescriptionId(), "神经效率");
        add(InitAttribute.DEFENSE.value().getDescriptionId(), "防御");
        add(InitAttribute.DIGESTION.value().getDescriptionId(), "消化效率");
        add(InitAttribute.NUTRITION.value().getDescriptionId(), "营养获取效率");
        add(InitAttribute.ENDURANCE.value().getDescriptionId(), "耐力");
        add(InitAttribute.METABOLISM.value().getDescriptionId(), "新陈代谢效率");
        add(InitAttribute.BREATH_CAPACITY.value().getDescriptionId(), "肺活量");
        add(InitAttribute.BREATH_RECOVERY.value().getDescriptionId(), "呼吸效率");
        add(InitAttribute.WATER_BREATH.value().getDescriptionId(), "水下呼吸");
        add(InitAttribute.DETOXIFICATION.value().getDescriptionId(), "解毒效率");
        add(InitAttribute.FILTRATION.value().getDescriptionId(), "血液过滤效率");
        add(InitAttribute.STRENGTH.value().getDescriptionId(), "力量");
        add(InitAttribute.SPEED.value().getDescriptionId(), "速度");
        add(InitAttribute.FIRE_RESISTANCE.value().getDescriptionId(), "火焰抗性");
        add(InitAttribute.WATER_ALLERGY.value().getDescriptionId(), "水过敏");
        add(InitAttribute.HYDROPHOBIA.value().getDescriptionId(), "恐水");
        add(InitAttribute.ENDER.value().getDescriptionId(), "末影");
        add(InitAttribute.PROJECTILE_DODGE.value().getDescriptionId(), "弹射物闪避");
        add(InitAttribute.LEAPING.value().getDescriptionId(), "跳跃力");
    }

    @Override
    protected void addTranslations() {
        switch (locale) {
            case EN_US -> English();
            case ZH_CN -> Chinese();
        }
    }
}
