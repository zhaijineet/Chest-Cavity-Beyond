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
    }

    public void Chinese() {
        add(InitCreativeModeTab.CHEST_CAVITY_BEYOND_TAB_TRANSLATABLE, "胸腔");

        add(InitItem.CHEST_OPENER.get(), "开胸器");
        add(InitItem.HEART.get(), "心脏");

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
    }

    @Override
    protected void addTranslations() {
        switch (locale) {
            case EN_US -> English();
            case ZH_CN -> Chinese();
        }
    }
}
