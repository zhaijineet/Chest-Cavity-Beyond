package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.client.key.KeyMappings;
import net.zhaiji.chestcavitybeyond.manager.AttributeDisplayManager;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitCreativeModeTab;
import net.zhaiji.chestcavitybeyond.register.InitDamageType;
import net.zhaiji.chestcavitybeyond.register.InitEffect;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

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
        add(KeyMappings.DESCEND_VEHICLE_TRANSLATABLE, "Descend Vehicle");
        add(KeyMappings.SKILL_PREV_TRANSLATABLE, "Previous Skill");
        add(KeyMappings.SKILL_NEXT_TRANSLATABLE, "Next Skill");
        add(KeyMappings.SKILL_CONFIRM_TRANSLATABLE, "Confirm Skill");
        for (int i = 0; i < ChestCavitySize.ROW_6.getSlots(); i++) {
            add(KeyMappings.USE_ORGAN_SKILLS_TRANSLATABLE + i, "Organ Skill " + (i < 9 ? "0" + (i + 1) : (i + 1)));
        }

        add("organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + AttributeModifier.Operation.ADD_VALUE.ordinal(), "%1$s %2$s");
        add(
            "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + AttributeModifier.Operation.ADD_MULTIPLIED_BASE.ordinal(),
            "%1$s%% %2$s"
        );
        add(
            "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL.ordinal(),
            "%1$s%% Final %2$s"
        );

        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.header.passive_effect", "【Passive Effect】");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.header.active_skill", "【Active Skill】");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.shift_hint", "Hold [%s] for details");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.shift", "Shift");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.ctrl_hint", "Hold [%s] to reveal formula");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.ctrl", "Ctrl");
        add("organ." + ChestCavityBeyond.MOD_ID + ".formula.tag_organ_count", "%s organ count");
        add("organ." + ChestCavityBeyond.MOD_ID + ".formula.tag_organ_type_count", "%s organ type count");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.cooldown", "Cooldown: %ss");

        addItem(InitItem.CHEST_OPENER, "Chest Opener");
        addItem(InitItem.BIOLOGICAL_ANALYZER, "Biological Analyzer");

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

        addItem(InitItem.GILL, "Gill");
        addItem(InitItem.SMALL_GILL, "Small Gill");
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

        addItem(InitItem.BASALT_HEART, "Basalt Heart");
        addItem(InitItem.BASALT_LUNG, "Basalt Lung");
        addItem(InitItem.BASALT_SPINE, "Basalt Spine");
        addItem(InitItem.BASALT_STOMACH, "Basalt Stomach");
        addItem(InitItem.BASALT_INTESTINE, "Basalt Intestine");
        addItem(InitItem.BASALT_KIDNEY, "Basalt Kidney");
        addItem(InitItem.BASALT_SPLEEN, "Basalt Spleen");
        addItem(InitItem.BASALT_LIVER, "Basalt Liver");
        addItem(InitItem.BASALT_APPENDIX, "Basalt Appendix");
        addItem(InitItem.BASALT_RIB, "Basalt Rib");
        addItem(InitItem.BASALT_MUSCLE, "Basalt Muscle");

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
        addItem(InitItem.ALCHEMIST_GLAND, "Alchemist Gland");
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

        addItem(InitItem.SNOW_CORE, "Snow Core");

        addItem(InitItem.GHAST_STOMACH, "Ghast Stomach");
        addItem(InitItem.GAS_SAC, "Gas Sac");

        addItem(InitItem.SHULKER_SPLEEN, "Shlker Spleen");

        addItem(InitItem.BREEZE_CORE, "Breeze Core");
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
        addItem(InitItem.SCULK_RIB, "Sculk Rib");
        addItem(InitItem.SCULK_MUSCLE, "Sculk Muscle");
        addItem(InitItem.SCULK_CORE, "Sculk Core");

        addItem(InitItem.ELDER_HEART, "Elder Heart");
        addItem(InitItem.ELDER_GILL, "Elder Gills");
        addItem(InitItem.ELDER_LUNG, "Elder Lung");
        addItem(InitItem.ELDER_APPENDIX, "Elder Appendix");
        addItem(InitItem.ELDER_LIVER, "Elder Liver");
        addItem(InitItem.ELDER_SPLEEN, "Elder Spleen");
        addItem(InitItem.ELDER_KIDNEY, "Elder Kidney");
        addItem(InitItem.ELDER_STOMACH, "Elder Stomach");
        addItem(InitItem.ELDER_FISH_MUSCLE, "Elder Fish Muscle");
        addItem(InitItem.ELDER_MUSCLE, "Elder Muscle");
        addItem(InitItem.ELDER_FISH_SPINE, "Elder Fish Spine");
        addItem(InitItem.ELDER_FISH_BONE, "Elder Fish Bone");
        addItem(InitItem.ELDER_SPINE, "Elder Spine");
        addItem(InitItem.ELDER_RIB, "Elder Rib");
        addItem(InitItem.ELDER_INTESTINE, "Elder Intestine");
        addItem(InitItem.ELDER_FISH_INTESTINE, "Elder Fish Intestine");

        addItem(InitItem.ELDER_MANA_REACTOR, "Elder Mana Reactor");

        addItem(InitItem.GUARDIAN_EYE, "Guardian Eye");
        addItem(InitItem.ELDER_GUARDIAN_EYE, "Elder Guardian Eye");
        addItem(InitItem.STRIDER_KIDNEY, "Strider Kidney");
        addItem(InitItem.SYMBIOTIC_INTESTINE, "Symbiotic Intestine");
        addItem(InitItem.BEWITCHED_LIVER, "Bewitched Liver");

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
        addAttribute(InitAttribute.FROST_RESISTANCE, "Frost Resistance");
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
        addAttribute(InitAttribute.LAVA_SWIM_SPEED, "Lava Swim-Speed");
        addAttribute(InitAttribute.WATER_WEAKNESS, "Water Weakness");
        addAttribute(InitAttribute.LAVA_WALK, "Lava Walking");

        addEffect(InitEffect.FURNACE_POWER::value, "Furnace Powered");

        addDamageType(InitDamageType.ORGAN_LOSS, "%1$s died from organ loss");
        addDamageTypeWithPlayer(InitDamageType.ORGAN_LOSS, "%1$s died from organ loss");
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s died from a medical accident");
        addDamageTypeWithPlayer(InitDamageType.OPEN_CHEST, "%1$s died from a medical accident");
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s died from %2$s's malpractice", 0);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s died tragically during chest surgery", 1);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s bled out during surgery", 2);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s became a medical experiment of %2$s", 3);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s perished due to %2$s's medical error", 4);

        addOrganSimpleActiveSkill(InitItem.ENDER_APPENDIX, "Teleport in viewing direction");
        addOrganActiveSkill(
            InitItem.ENDER_APPENDIX,
            "Teleport along the line of sight",
            "Teleport distance depends on Ender attribute value",
            "Can pass through thin walls when close"
        );

        addOrganSimpleActiveSkill(InitItem.HERBIVORE_RUMEN, "Can graze on grass blocks, short grass, or tall grass");
        addOrganActiveSkill(
            InitItem.HERBIVORE_RUMEN,
            "Can graze on grass blocks, short grass, or tall grass",
            "Players restore 2 food and 4 saturation",
            "Creatures restore 1 health",
            "Tall grass triggers one extra restoration",
            "Grazed grass blocks turn into dirt"
        );

        addOrganSimpleActiveSkill(InitItem.CREEPER_APPENDIX, "Explode");
        addOrganActiveSkill(
            InitItem.CREEPER_APPENDIX,
            "Create an explosion centered on self",
            "Power equals 3 times Explosive attribute value",
            "Does not destroy blocks"
        );

        addOrganSimpleActiveSkill(InitItem.INNER_FURNACE, "Burn fuel, restore 1 food and 1 saturation every %ss");
        addOrganActiveSkill(
            InitItem.INNER_FURNACE,
            "Consume burnable items in hand to gain Furnace Power",
            "While active, restore 1 food and 1 saturation every %ss",
            "Sneak to consume multiple items at once",
            "Effect level equals Furnace Power attribute",
            "Duration accumulates from consumed fuel burn time"
        );

        addOrganSimpleActiveSkill(InitItem.GOLEM_ARMOR_PLATE, "Repair itself using iron ingots");
        addOrganActiveSkill(
            InitItem.GOLEM_ARMOR_PLATE,
            "Consume an iron ingot from inventory to heal",
            "Healing equals 2.5 times Iron Repair attribute"
        );
        addOrganSimplePassiveEffect(InitItem.GOLEM_ARMOR_PLATE, "Can be healed by other players using iron ingots");
        addOrganPassiveEffect(
            InitItem.GOLEM_ARMOR_PLATE,
            "Can be healed by other players using iron ingots",
            "Healing equals 2.5 times Iron Repair attribute"
        );

        addOrganSimpleActiveSkill(InitItem.SILK_GLAND, "Shoot cobwebs");
        addOrganActiveSkill(
            InitItem.SILK_GLAND,
            "Throw a cobweb that can trap entities",
            "Costs food level to use"
        );

        addOrganSimplePassiveEffect(InitItem.VENOM_GLAND, "Apply potion effects on attack");
        addOrganPassiveEffect(
            InitItem.VENOM_GLAND,
            "Apply stored potion effects to target on attack",
            "Can be crafted with any potion to change stored effects",
            "Effect duration is reduced to 1/10 of original",
            "4 seconds cooldown"
        );

        addOrganSimpleActiveSkill(InitItem.ALCHEMIST_GLAND, "Gain stored potion effects");
        addOrganActiveSkill(
            InitItem.ALCHEMIST_GLAND,
            "Gain stored potion effects on self",
            "Can be crafted with any potion to change stored effects",
            "Cooldown equals the duration of stored effects",
            "Instant effects count as 60s cooldown"
        );

        addOrganActiveSkill(
            InitItem.LLAMA_LUNG,
            "Shoot a llama spit projectile"
        );

        addOrganSimpleActiveSkill(InitItem.ACTIVE_BLAZE_ROD, "Shoot continuous fireballs");
        addOrganActiveSkill(
            InitItem.ACTIVE_BLAZE_ROD,
            "Launch a barrage of small fireballs",
            "Number of fireballs equals Vomit Fireball attribute",
            "Fires one fireball every 6 ticks"
        );

        addOrganActiveSkill(
            InitItem.SNOW_CORE,
            "Throw a snowball projectile"
        );

        addOrganSimpleActiveSkill(InitItem.GHAST_STOMACH, "Shoot large fireballs");
        addOrganActiveSkill(
            InitItem.GHAST_STOMACH,
            "Launch a ghast-style large fireball",
            "Explosion power equals Ghastly attribute value"
        );

        addOrganActiveSkill(
            InitItem.SHULKER_SPLEEN,
            "Fire a homing shulker bullet at target in sight"
        );

        addOrganSimpleActiveSkill(InitItem.BREEZE_CORE, "Shoot wind charges");
        addOrganActiveSkill(
            InitItem.BREEZE_CORE,
            "Launch a wind charge projectile",
            "Creates wind burst on impact"
        );

        addOrganSimpleActiveSkill(InitItem.DRAGON_LUNG, "Shoot dragon fireballs");
        addOrganActiveSkill(
            InitItem.DRAGON_LUNG,
            "Launch a dragon fireball",
            "Creates area-of-effect dragon breath on impact"
        );

        addOrganSimpleActiveSkill(InitItem.SCULK_CORE, "Sonic boom");
        addOrganActiveSkill(
            InitItem.SCULK_CORE,
            "Emit a sonic boom in the looking direction",
            "Deals 10 damage to all targets in the path",
            "Passes through entities"
        );

        addOrganSimpleActiveSkill(InitItem.GUARDIAN_EYE, "Shoot charged laser");
        addOrganActiveSkill(
            InitItem.GUARDIAN_EYE,
            "Charge and fire a laser beam at target in sight",
            "Deals 2 magic damage plus 3 attack damage",
            "Target must remain in range during charging"
        );

        addOrganSimpleActiveSkill(InitItem.ELDER_GUARDIAN_EYE, "Shoot charged laser");
        addOrganActiveSkill(
            InitItem.ELDER_GUARDIAN_EYE,
            "Charge and fire a laser beam at target in sight",
            "Deals 4 magic damage plus 6 attack damage",
            "Target must remain in range during charging"
        );

        addOrganSimpleActiveSkill(InitItem.SYMBIOTIC_INTESTINE, "Consume flowers to gain suspicious stew effects");
        addOrganActiveSkill(
            InitItem.SYMBIOTIC_INTESTINE,
            "Consume held flower or flower block in sight",
            "Gain the corresponding suspicious stew potion effect"
        );

        addOrganSimpleActiveSkill(InitItem.BEWITCHED_LIVER, "Remove all potion effects");
        addOrganActiveSkill(
            InitItem.BEWITCHED_LIVER,
            "Remove all potion effects from self"
        );

        addEnchantment(
            InitEnchantment.TELEOPERATION,
            "Teleoperation",
            "Each level increases reach distance by 1 block"
        );
        addEnchantment(
            InitEnchantment.ADVANCED_SURGERY,
            "Advanced Surgery",
            "Each level increases minimum health threshold by 10%"
        );
        addEnchantment(
            InitEnchantment.PRUDENT_SURGERY,
            "Prudent Surgery",
            "Each level reduces damage by 1 point"
        );
        addEnchantment(
            InitEnchantment.SAFE_SURGERY,
            "Safe Surgery",
            "Level 1: Sneak to open self and pets; Level 2: Only other entities"
        );
        addEnchantment(
            InitEnchantment.HYDRAULIC_CLAMP,
            "Hydraulic Clamp",
            "Each level consumes 25 durability from target's chestplate"
        );
        addEnchantment(
            InitEnchantment.PRIMAL_REVERSION,
            "Primal Reversion",
            "Multiplies organ attributes by 1.5"
        );
        addEnchantment(
            InitEnchantment.ANESTHESIA_SURGERY,
            "Anesthesia Surgery",
            "On successful chest opening, inflicts Slowness on the target for 3 seconds (amplifier = level)"
        );
        addEnchantment(
            InitEnchantment.POSTOPERATIVE_SUTURE,
            "Postoperative Suture",
            "Each level heals the target by 1 HP when the chest cavity UI is closed. After healing, 1-second cooldown"
        );

        add("message." + ChestCavityBeyond.MOD_ID + ".obstructed", "Target's chest is obstructed by equipment");
        add("message." + ChestCavityBeyond.MOD_ID + ".healthy", "Target is too healthy");
        add("message." + ChestCavityBeyond.MOD_ID + ".unopenable", "This entity cannot be opened");

        add("commands.chestcavitybeyond.resize.failed", "No valid targets found");
        add("commands.chestcavitybeyond.resize.failed.invalid_size", "Invalid chest cavity size");
        add("commands.chestcavitybeyond.resize.success.single", "Resized %s's chest cavity to %s(%s slots)");
        add("commands.chestcavitybeyond.resize.success.multiple", "Resized chest cavity of %s entities to %s(%s slots)");

        add("commands.chestcavitybeyond.resetorgans.failed", "No valid targets found");
        add("commands.chestcavitybeyond.resetorgans.success.single", "Reset %s's chest cavity organs to defaults");
        add("commands.chestcavitybeyond.resetorgans.success.multiple", "Reset chest cavity organs of %s entities to defaults");

        add("commands.chestcavitybeyond.attributes.header", "— %s's Analysis Report —");
        add("commands.chestcavitybeyond.attributes.no_description", "No detailed description available");
        add("commands.chestcavitybeyond.attributes.failed.not_player", "The attributes command can only be executed by a player");

        addAttributeDescription(
            InitAttribute.HEALTH,
            "+2 max health per point",
            "Takes continuous damage when value ≤ 0"
        );
        addAttributeDescription(
            InitAttribute.NERVES,
            "Affects attack speed and movement speed",
            "Cannot move when current value ≤ 0"
        );
        addAttributeDescription(
            InitAttribute.DEFENSE,
            "Reduces non-bypassing damage",
            "The higher the damage, the lower the damage reduction ratio"
        );
        addAttributeDescription(
            InitAttribute.DIGESTION,
            "Affects food level restoration from food",
            "Cannot digest food when value ≤ 0"
        );
        addAttributeDescription(
            InitAttribute.NUTRITION,
            "Affects saturation from food",
            "Cannot gain saturation from food when value ≤ 0"
        );
        addAttributeDescription(
            InitAttribute.ENDURANCE,
            "Affects food level consumption rate, higher = slower"
        );
        addAttributeDescription(
            InitAttribute.METABOLISM,
            "Affects the speed of healing by consuming saturation and food level",
            "When the interval is reduced to 1 tick, excess rate increases single healing amount and starvation damage amount"
        );
        addAttributeDescription(
            InitAttribute.BREATH_CAPACITY,
            "Affects maximum air supply",
            "Can only breathe when value > 0"
        );
        addAttributeDescription(
            InitAttribute.BREATH_RECOVERY,
            "Affects air recovery rate in air",
            "Can breathe in air when value > 0"
        );
        addAttributeDescription(
            InitAttribute.WATER_BREATH,
            "Affects air recovery rate underwater",
            "Can breathe underwater when value > 0"
        );
        addAttributeDescription(
            InitAttribute.DETOXIFICATION,
            "Reduces duration of harmful potion effects",
            "Immune when reduced duration ≤ %s ticks"
        );
        addAttributeDescription(
            InitAttribute.FILTRATION,
            "When below default value, periodically applies poison",
            "Lower values cause stronger poison"
        );
        addAttributeDescription(
            InitAttribute.STRENGTH,
            "Affects attack damage"
        );
        addAttributeDescription(
            InitAttribute.SPEED,
            "Affects movement speed"
        );
        addAttributeDescription(
            InitAttribute.FIRE_RESISTANCE,
            "Reduces fire damage taken",
            "The higher the damage, the lower the damage reduction ratio",
            "Immune to hot blocks (magma/campfire) at ≥ %s",
            "Immune to fire damage at ≥ %s, also clears fire status",
            "Immune to lava at ≥ %s"
        );
        addAttributeDescription(
            InitAttribute.FROST_RESISTANCE,
            "Reduces freezing damage taken",
            "The higher the damage, the lower the damage reduction ratio",
            "Immune to freezing damage at ≥ %s, also clears freezing progress"
        );
        addAttributeDescription(
            InitAttribute.WATER_ALLERGY,
            "Takes damage in water or rain"
        );
        addAttributeDescription(
            InitAttribute.ENDER,
            "Randomly teleports nearby when taking drowning damage",
            "Affects teleport distance of Ender Appendix skill",
            "Prerequisite for Projectile Dodge attribute to activate"
        );
        addAttributeDescription(
            InitAttribute.PROJECTILE_DODGE,
            "Requires Ender attribute > 0 to activate",
            "Cancels projectile/water potion damage and teleports randomly"
        );
        addAttributeDescription(
            InitAttribute.LEAPING,
            "Affects jump strength and safe fall distance"
        );
        addAttributeDescription(
            InitAttribute.CARNIVOROUS_DIGESTION,
            "Additional food level restoration from meat"
        );
        addAttributeDescription(
            InitAttribute.CARNIVOROUS_NUTRITION,
            "Additional saturation from meat"
        );
        addAttributeDescription(
            InitAttribute.HERBIVOROUS_DIGESTION,
            "Additional food level restoration from plants"
        );
        addAttributeDescription(
            InitAttribute.HERBIVOROUS_NUTRITION,
            "Additional saturation from plants"
        );
        addAttributeDescription(
            InitAttribute.SCAVENGER_DIGESTION,
            "Additional food level restoration from rotten food",
            "Prevents poison and hunger effects from toxic food"
        );
        addAttributeDescription(
            InitAttribute.SCAVENGER_NUTRITION,
            "Additional saturation from rotten food",
            "Harmful effects in food add extra saturation"
        );
        addAttributeDescription(
            InitAttribute.EXPLOSIVE,
            "Affects Creeper Appendix explosion power",
            "Explosion power equals 3 × attribute value"
        );
        addAttributeDescription(
            InitAttribute.PHOTOSYNTHESIS,
            "Restores food level and saturation over time in daylight with sky visibility",
            "Each trigger +1 food level, or +1 saturation when food level is full"
        );
        addAttributeDescription(
            InitAttribute.LAUNCH,
            "Launches target upward on attack",
            "Knockup is reduced by target's knockback resistance"
        );
        addAttributeDescription(
            InitAttribute.IRON_REPAIR,
            "Affects Golem Armor Plate healing with iron ingots"
        );
        addAttributeDescription(
            InitAttribute.FURNACE_POWER,
            "Activated by consuming fuel, periodically restores food level and saturation",
            "Higher effect level = shorter recovery interval"
        );
        addAttributeDescription(
            InitAttribute.WITHERED,
            "Applies Wither effect to target on attack",
            "With Nether Star organ in chest cavity, extends effect duration and +1 level"
        );
        addAttributeDescription(
            InitAttribute.VOMIT_FIREBALL,
            "Affects number of small fireballs from Active Blaze Rod skill"
        );
        addAttributeDescription(
            InitAttribute.GHASTLY,
            "Affects ghast fireball explosion power from Ghast Stomach skill"
        );
        addAttributeDescription(
            InitAttribute.CRYSTALLIZATION,
            "Only takes effect near End Crystals",
            "Continuously restores health; players additionally restore food and saturation, scaling with Crystallization value"
        );
        addAttributeDescription(
            InitAttribute.LAVA_SWIM_SPEED,
            "Increases horizontal and vertical movement speed in lava"
        );
        addAttributeDescription(
            InitAttribute.WATER_WEAKNESS,
            "Reduces mining speed and attack damage while in water"
        );
        addAttributeDescription(
            InitAttribute.LAVA_WALK,
            "Allows walking on lava surface",
            "Disabled while sneaking"
        );

        addAttributeValueEffect(InitAttribute.HEALTH, "Max Health %s");
        addAttributeValueEffect(InitAttribute.NERVES, "Attack Speed %s%% | Move Speed %s%%");
        addAttributeValueEffect(InitAttribute.DEFENSE, "Damage Reduction ~%s%% (vs 10 dmg)");
        addAttributeValueEffect(InitAttribute.ENDURANCE, "Food level consumption %s%%");
        addAttributeValueEffect(InitAttribute.STRENGTH, "Melee Damage %s%%");
        addAttributeValueEffect(InitAttribute.SPEED, "Move Speed %s%%");
        addAttributeValueEffect(InitAttribute.DIGESTION, "Food level %s%%");
        addAttributeValueEffect(InitAttribute.NUTRITION, "Food saturation %s%%");
        addAttributeValueEffect(InitAttribute.METABOLISM, "Healing rate %s%%");
        addAttributeValueEffect(InitAttribute.BREATH_CAPACITY, "Air Consumption %s%%");
        addAttributeValueEffect(InitAttribute.BREATH_RECOVERY, "Air Recovery %s%%");
        addAttributeValueEffect(InitAttribute.WATER_BREATH, "Underwater Recovery %s%%");
        addAttributeValueEffect(InitAttribute.DETOXIFICATION, "Harmful Effect Duration %s%%");
        addAttributeValueEffect(InitAttribute.FILTRATION, "Poison Lv%s, lasts %ss");
        add(AttributeDisplayManager.getValueEffectKey(InitAttribute.FILTRATION) + ".safe", "Blood filtration normal, no poisoning risk");
        addAttributeValueEffect(InitAttribute.CARNIVOROUS_DIGESTION, "Meat food level %s%%");
        addAttributeValueEffect(InitAttribute.CARNIVOROUS_NUTRITION, "Meat saturation %s%%");
        addAttributeValueEffect(InitAttribute.HERBIVOROUS_DIGESTION, "Plant food level %s%%");
        addAttributeValueEffect(InitAttribute.HERBIVOROUS_NUTRITION, "Plant saturation %s%%");
        addAttributeValueEffect(InitAttribute.SCAVENGER_DIGESTION, "Rotten food level %s%%");
        addAttributeValueEffect(InitAttribute.SCAVENGER_NUTRITION, "Rotten saturation %s%%");
        addAttributeValueEffect(InitAttribute.FIRE_RESISTANCE, "Fire Damage Reduction ~%s%% (vs 10 dmg)");
        addAttributeValueEffect(InitAttribute.FROST_RESISTANCE, "Frost Damage Reduction ~%s%% (vs 10 dmg)");
        addAttributeValueEffect(InitAttribute.ENDER, "Teleport Range %s");
        addAttributeValueEffect(InitAttribute.LEAPING, "Jump Height %s");
        addAttributeValueEffect(InitAttribute.EXPLOSIVE, "Explosion Power %s");
        addAttributeValueEffect(InitAttribute.PHOTOSYNTHESIS, "Interval %ss");
        addAttributeValueEffect(InitAttribute.IRON_REPAIR, "Iron Heal %s");
        addAttributeValueEffect(InitAttribute.WITHERED, "Wither for %ss, Effect Lv%s, Wither Duration %s%%");
        addAttributeValueEffect(InitAttribute.GHASTLY, "Fireball Power %s");
        addAttributeValueEffect(InitAttribute.CRYSTALLIZATION, "Near End Crystals: restores %s health per second");
        addAttributeValueEffect(InitAttribute.LAUNCH, "Knockup Force %s");
        addAttributeValueEffect(InitAttribute.VOMIT_FIREBALL, "Fireball Count %s");
        addAttributeValueEffect(InitAttribute.FURNACE_POWER, "Restores 1 food + 1 saturation every %ss, Effect Lv%s, Max %ss");
        addAttributeValueEffect(InitAttribute.LAVA_SWIM_SPEED, "Lava movement force %sx");
        addAttributeValueEffect(InitAttribute.WATER_WEAKNESS, "Mining speed/Attack damage reduction %s%% in water");

        addAttributeDescription(NeoForgeMod.SWIM_SPEED, "Increases movement speed in water");
        addAttributeValueEffect(NeoForgeMod.SWIM_SPEED, "Swim Speed +%s%%");

        addAttributeDescription(Attributes.LUCK, "Affects loot table rolls and quality");
        addAttributeValueEffect(Attributes.LUCK, "Luck %s");

        addAttributeDescription(Attributes.KNOCKBACK_RESISTANCE, "Reduces knockback taken");
        addAttributeValueEffect(Attributes.KNOCKBACK_RESISTANCE, "Knockback Resistance %s%%");

        addAttributeDescription(
            Attributes.GRAVITY,
            "Affects falling acceleration",
            "At 0 or below, enables free flight and nullifies fall damage"
        );
        addAttributeValueEffect(Attributes.GRAVITY, "Gravity %s%%");
        add(AttributeDisplayManager.getValueEffectKey(Attributes.GRAVITY) + ".flight", "Zero gravity, free flight enabled");

        addAttributeDescription(Attributes.ENTITY_INTERACTION_RANGE, "Increases entity interaction distance");
        addAttributeValueEffect(Attributes.ENTITY_INTERACTION_RANGE, "Entity Reach +%s");

        addAttributeDescription(Attributes.BLOCK_INTERACTION_RANGE, "Increases block interaction distance");
        addAttributeValueEffect(Attributes.BLOCK_INTERACTION_RANGE, "Block Reach +%s");

        add(ItemTagManager.ORGANS, "Organs");
        add(ItemTagManager.HEART, "Hearts");
        add(ItemTagManager.LUNG, "Lungs");
        add(ItemTagManager.MUSCLE, "Muscles");
        add(ItemTagManager.RIB, "Ribs");
        add(ItemTagManager.APPENDIX, "Appendixes");
        add(ItemTagManager.SPLEEN, "Spleens");
        add(ItemTagManager.KIDNEY, "Kidneys");
        add(ItemTagManager.SPINE, "Spines");
        add(ItemTagManager.LIVER, "Livers");
        add(ItemTagManager.INTESTINE, "Intestines");
        add(ItemTagManager.STOMACH, "Stomachs");
        add(ItemTagManager.SPECIAL, "Special");
        add(ItemTagManager.BONE, "Bone");
        add(ItemTagManager.ROTTEN, "Rotten");
        add(ItemTagManager.IRON, "Iron");

        add("jei." + ChestCavityBeyond.MOD_ID + ".chest_cavity_type", "Chest Cavity Type");
        add("jei." + ChestCavityBeyond.MOD_ID + ".need_breath", "Needs Breathing");
        add("jei." + ChestCavityBeyond.MOD_ID + ".no_breath", "No Breathing Needed");
        add("jei." + ChestCavityBeyond.MOD_ID + ".need_health", "Needs Health");
        add("jei." + ChestCavityBeyond.MOD_ID + ".no_health", "No Health Needed");
        add("jei." + ChestCavityBeyond.MOD_ID + ".type_bonus_header", "Chest Cavity Type Bonus:");
        add("jei." + ChestCavityBeyond.MOD_ID + ".type_default_bonus_header", "Type Default Bonus:");

        addChestCavityTypeName(ChestCavityTypeManager.HUMAN, "Human");
        addChestCavityTypeName(ChestCavityTypeManager.WITCH, "Witch");
        addChestCavityTypeName(ChestCavityTypeManager.ANIMAL, "Animal");
        addChestCavityTypeName(ChestCavityTypeManager.HERBIVORE1, "Herbivore");
        addChestCavityTypeName(ChestCavityTypeManager.HERBIVORE2, "Ruminant Herbivore");
        addChestCavityTypeName(ChestCavityTypeManager.HERBIVORE3, "Multi-stomached Herbivore");
        addChestCavityTypeName(ChestCavityTypeManager.MOOSHROOM, "Mooshroom");
        addChestCavityTypeName(ChestCavityTypeManager.LLAMA, "Llama");
        addChestCavityTypeName(ChestCavityTypeManager.CARNIVORE, "Carnivore");
        addChestCavityTypeName(ChestCavityTypeManager.BRUTE_ANIMAL, "Brute Animal");
        addChestCavityTypeName(ChestCavityTypeManager.HOGLIN, "Hoglin");
        addChestCavityTypeName(ChestCavityTypeManager.SWIFT_ANIMAL, "Swift Animal");
        addChestCavityTypeName(ChestCavityTypeManager.LEAPING_ANIMAL, "Leaping Animal");
        addChestCavityTypeName(ChestCavityTypeManager.SHULKER, "Shulker");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_ANIMAL, "Small Animal");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_HERBIVORE, "Small Herbivore");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_CARNIVORE, "Small Carnivore");
        addChestCavityTypeName(ChestCavityTypeManager.TURTLE, "Turtle");
        addChestCavityTypeName(ChestCavityTypeManager.FROG, "Frog");
        addChestCavityTypeName(ChestCavityTypeManager.RABBIT, "Rabbit");
        addChestCavityTypeName(ChestCavityTypeManager.SLIME, "Slime");
        addChestCavityTypeName(ChestCavityTypeManager.MAGMA_CUBE, "Magma Cube");
        addChestCavityTypeName(ChestCavityTypeManager.FIREPROOF, "Fireproof");
        addChestCavityTypeName(ChestCavityTypeManager.BASALT, "Basalt");
        addChestCavityTypeName(ChestCavityTypeManager.GHAST, "Ghast");
        addChestCavityTypeName(ChestCavityTypeManager.ENDER, "Ender");
        addChestCavityTypeName(ChestCavityTypeManager.ENDER_DRAGON, "Ender Dragon");
        addChestCavityTypeName(ChestCavityTypeManager.UNDEAD, "Undead");
        addChestCavityTypeName(ChestCavityTypeManager.SKELETON, "Skeleton");
        addChestCavityTypeName(ChestCavityTypeManager.WITHER_SKELETON, "Wither Skeleton");
        addChestCavityTypeName(ChestCavityTypeManager.WITHER, "Wither");
        addChestCavityTypeName(ChestCavityTypeManager.ARTHROPOD, "Arthropod");
        addChestCavityTypeName(ChestCavityTypeManager.SPIDER, "Spider");
        addChestCavityTypeName(ChestCavityTypeManager.CAVE_SPIDER, "Cave Spider");
        addChestCavityTypeName(ChestCavityTypeManager.AQUATIC, "Aquatic");
        addChestCavityTypeName(ChestCavityTypeManager.DOLPHIN, "Dolphin");
        addChestCavityTypeName(ChestCavityTypeManager.FISH, "Fish");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_AQUATIC, "Small Aquatic");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_FISH, "Small Fish");
        addChestCavityTypeName(ChestCavityTypeManager.SALTWATER, "Saltwater");
        addChestCavityTypeName(ChestCavityTypeManager.CREEPER, "Creeper");
        addChestCavityTypeName(ChestCavityTypeManager.BLAZE, "Blaze");
        addChestCavityTypeName(ChestCavityTypeManager.BREEZE, "Breeze");
        addChestCavityTypeName(ChestCavityTypeManager.IRON_GOLEM, "Iron Golem");
        addChestCavityTypeName(ChestCavityTypeManager.SNOW_GOLEM, "Snow Golem");
        addChestCavityTypeName(ChestCavityTypeManager.WARDEN, "Warden");
        addChestCavityTypeName(ChestCavityTypeManager.ELDER, "Elder");
        addChestCavityTypeName(ChestCavityTypeManager.ELDER_FISH, "Elder Fish");
        addChestCavityTypeName(ChestCavityTypeManager.GUARDIAN, "Guardian");
        addChestCavityTypeName(ChestCavityTypeManager.ELDER_GUARDIAN, "Elder Guardian");
        addChestCavityTypeName(ChestCavityTypeManager.ARMOR_STAND, "Armor Stand");

        addConfig("title", "Chest Cavity Beyond Configuration");
        addConfig("Organ", "Organ System", "Organ system mechanics");
        addConfig("ChestOpener", "Chest Opener", "Chest opening mechanics");
        addConfig("SkillParameters", "Skill Parameters", "Organ skill numerical parameters");
        addConfig("Immunity", "Immunity", "Fire and frost immunity thresholds");
        addConfig("MobSkill", "Mob Skill", "Mob AI organ skill settings");
        addConfig("filtrationPeriod", "Filtration Period", "Filtration period of organ system (ticks)");
        addConfig(
            "detoxificationImmunityDurationThreshold",
            "Detox Immunity Threshold",
            "Immunize harmful effects reduced by detoxification or withered when their duration is at or below this value (ticks, 0 = only effects reduced to 0 ticks)"
        );
        addConfig(
            "minChestOpenMaxHealth",
            "Direct Open Health Threshold",
            "Target max health at or below this value can be opened directly without reducing current health"
        );
        addConfig(
            "chestOpenBaseHealthRatio",
            "Open Base Health Ratio",
            "When target max health exceeds direct-open threshold, target current health must be below this ratio of max health to open (0.3 = ≤30%, each Advanced Surgery level adds +10%)"
        );
        addConfig("guardianLaserDistance", "Guardian Laser Distance", "Effective distance of guardian laser skill");
        addConfig("randomTeleportAttempts", "Teleport Attempts", "Attempt loops for enderman teleport skill");
        addConfig("furnacePowerMaxDuration", "Furnace Power Max Duration", "Maximum duration of furnace power effect (ticks)");
        addConfig("shulkerBulletDistance", "Shulker Bullet Distance", "Detection distance of shulker bullet skill");
        addConfig("sonicBoomDistance", "Sonic Boom Distance", "Distance of warden sonic boom skill");
        addConfig(
            "crystalEffectSearchRange",
            "Crystal Effect Search Range",
            "Search range of end crystal for has crystallization entities"
        );
        addConfig(
            "enableChestCavityScaleSideEffect",
            "Chest Cavity Scale Side Effect",
            "Enable entity scale side effect when chest cavity size increases (each extra row adds 0.25 scale)"
        );
        addConfig(
            "chestplateBlocksChestOpener",
            "Chestplate Blocks Opener",
            "Whether chestplate blocks the chest opener from opening the chest cavity (creative mode is not affected)"
        );
        addConfig(
            "fireImmunityHotFloor",
            "Fire Immunity - Hot Block",
            "Fire resistance threshold: immune to hot block (magma block/campfire) damage"
        );
        addConfig("fireImmunityFire", "Fire Immunity - Fire", "Fire resistance threshold: immune to fire damage, and clear fire status");
        addConfig("fireImmunityLava", "Fire Immunity - Lava", "Fire resistance threshold: immune to lava damage");
        addConfig(
            "frostImmunity",
            "Frost Immunity Threshold",
            "Frost resistance threshold: immune to freezing damage, and clear frozen ticks"
        );
        addConfig("enableMobGoalSkill", "Enable Mob Organ Skill", "Enable non-player entities to automatically use organ skills");
        addConfig(
            "goalSkillEvalInterval",
            "Skill Eval Interval",
            "Goal organ skill evaluation interval (ticks), higher = better performance but slower reaction\nNote: odd values are rounded up to even (e.g. 3→4) due to Mob AI dual-tick, minimum effective value is 2"
        );
        addConfig("goalSkillEnemyDetectRange", "Skill Enemy Detect Range", "Enemy detection range for Goal organ skill (blocks)");
        addConfig(
            "goalSkillTargetMemoryTicks",
            "Skill Target Memory",
            "Goal organ skill target memory duration (ticks), how long the mob keeps attacking after losing target (0 = disabled, 300 = 15s)"
        );
        addConfig(
            "mobSkillRetaliatePlayer",
            "Retaliate vs Player",
            "Whether pet-type mobs' (wolves/cats, i.e. ownable entities) organ skills retaliate against players who dealt damage\nfalse = no, default protects against accidental player damage (only affects OwnableEntity, not monsters)"
        );
        addConfig(
            "mobSkillRetaliateOtherPet",
            "Retaliate vs Other Pet",
            "Whether pet-type mobs' organ skills retaliate against other pets with different owners\nfalse = no, default prevents pet mutual damage (only affects OwnableEntity, not monsters)"
        );
        addConfig(
            "detailedTooltips",
            "Detailed Tooltips",
            "Default mode shows detailed descriptions. When disabled, hold SHIFT to view details."
        );
    }

    public void Chinese() {
        add(InitCreativeModeTab.CHEST_CAVITY_BEYOND_TAB_TRANSLATABLE, "胸腔");

        add(KeyMappings.KEY_CATEGORY_TRANSLATABLE, "胸腔");
        add(KeyMappings.OPEN_SKILL_GUI_TRANSLATABLE, "打开技能界面");
        add(KeyMappings.USE_ORGAN_SKILL_TRANSLATABLE, "使用器官技能");
        add(KeyMappings.DESCEND_VEHICLE_TRANSLATABLE, "乘坐实体下降");
        add(KeyMappings.SKILL_PREV_TRANSLATABLE, "上一个技能");
        add(KeyMappings.SKILL_NEXT_TRANSLATABLE, "下一个技能");
        add(KeyMappings.SKILL_CONFIRM_TRANSLATABLE, "确认技能");
        for (int i = 0; i < ChestCavitySize.ROW_6.getSlots(); i++) {
            add(KeyMappings.USE_ORGAN_SKILLS_TRANSLATABLE + i, "器官技能" + (i < 9 ? "0" + (i + 1) : (i + 1)));
        }

        add("organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + AttributeModifier.Operation.ADD_VALUE.ordinal(), "%1$s %2$s");
        add(
            "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + AttributeModifier.Operation.ADD_MULTIPLIED_BASE.ordinal(),
            "%1$s%% %2$s"
        );
        add(
            "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL.ordinal(),
            "%1$s%% 最终%2$s"
        );

        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.header.passive_effect", "【被动效果】");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.header.active_skill", "【主动技能】");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.shift_hint", "按住[%s]查看详细说明");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.shift", "Shift");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.ctrl_hint", "按住[%s]查看计算公式");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.ctrl", "Ctrl");
        add("organ." + ChestCavityBeyond.MOD_ID + ".formula.tag_organ_count", "%s器官数量");
        add("organ." + ChestCavityBeyond.MOD_ID + ".formula.tag_organ_type_count", "%s器官种类数量");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.cooldown", "冷却时间：%s秒");

        addItem(InitItem.CHEST_OPENER, "开胸器");
        addItem(InitItem.BIOLOGICAL_ANALYZER, "生物分析仪");

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

        addItem(InitItem.GILL, "鳃");
        addItem(InitItem.SMALL_GILL, "小型鳃");
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

        addItem(InitItem.BASALT_HEART, "玄武岩心脏");
        addItem(InitItem.BASALT_LUNG, "玄武岩肺脏");
        addItem(InitItem.BASALT_SPINE, "玄武岩脊柱");
        addItem(InitItem.BASALT_STOMACH, "玄武岩胃");
        addItem(InitItem.BASALT_INTESTINE, "玄武岩肠子");
        addItem(InitItem.BASALT_KIDNEY, "玄武岩肾脏");
        addItem(InitItem.BASALT_SPLEEN, "玄武岩脾脏");
        addItem(InitItem.BASALT_LIVER, "玄武岩肝脏");
        addItem(InitItem.BASALT_APPENDIX, "玄武岩阑尾");
        addItem(InitItem.BASALT_RIB, "玄武岩肋骨");
        addItem(InitItem.BASALT_MUSCLE, "玄武岩肌肉");

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
        addItem(InitItem.ALCHEMIST_GLAND, "炼金腺");
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

        addItem(InitItem.SNOW_CORE, "雪之核心");

        addItem(InitItem.GHAST_STOMACH, "恶魂胃");
        addItem(InitItem.GAS_SAC, "气囊");

        addItem(InitItem.SHULKER_SPLEEN, "潜影贝脾脏");

        addItem(InitItem.BREEZE_CORE, "旋风核心");
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
        addItem(InitItem.SCULK_RIB, "幽匿肋骨");
        addItem(InitItem.SCULK_MUSCLE, "幽匿肌肉");
        addItem(InitItem.SCULK_CORE, "幽匿核心");

        addItem(InitItem.ELDER_HEART, "远古心脏");
        addItem(InitItem.ELDER_LUNG, "远古肺脏");
        addItem(InitItem.ELDER_GILL, "远古鳃");
        addItem(InitItem.ELDER_APPENDIX, "远古阑尾");
        addItem(InitItem.ELDER_LIVER, "远古肝脏");
        addItem(InitItem.ELDER_SPLEEN, "远古脾脏");
        addItem(InitItem.ELDER_KIDNEY, "远古肾脏");
        addItem(InitItem.ELDER_STOMACH, "远古胃");
        addItem(InitItem.ELDER_MUSCLE, "远古肌肉");
        addItem(InitItem.ELDER_FISH_MUSCLE, "远古鱼类肌肉");
        addItem(InitItem.ELDER_SPINE, "远古脊柱");
        addItem(InitItem.ELDER_RIB, "远古肋骨");
        addItem(InitItem.ELDER_FISH_SPINE, "远古鱼类脊柱");
        addItem(InitItem.ELDER_FISH_BONE, "远古鱼骨");
        addItem(InitItem.ELDER_INTESTINE, "远古肠子");
        addItem(InitItem.ELDER_FISH_INTESTINE, "远古鱼肠");

        addItem(InitItem.ELDER_MANA_REACTOR, "远古魔力反应装置");

        addItem(InitItem.GUARDIAN_EYE, "守卫者之眼");
        addItem(InitItem.ELDER_GUARDIAN_EYE, "远古守卫者之眼");
        addItem(InitItem.STRIDER_KIDNEY, "炽足兽肾脏");
        addItem(InitItem.SYMBIOTIC_INTESTINE, "共生肠道");
        addItem(InitItem.BEWITCHED_LIVER, "巫蛊之肝");

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
        addAttribute(InitAttribute.FROST_RESISTANCE, "冰霜抗性");
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
        addAttribute(InitAttribute.LAVA_SWIM_SPEED, "熔岩游泳速度");
        addAttribute(InitAttribute.WATER_WEAKNESS, "水虚弱");
        addAttribute(InitAttribute.LAVA_WALK, "熔岩行者");

        addEffect(InitEffect.FURNACE_POWER::value, "熔炉之力");

        addDamageType(InitDamageType.ORGAN_LOSS, "%1$s死于器官缺失");
        addDamageTypeWithPlayer(InitDamageType.ORGAN_LOSS, "%1$s死于器官缺失");
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s死于医疗事故");
        addDamageTypeWithPlayer(InitDamageType.OPEN_CHEST, "%1$s死于医疗事故");
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s死于庸医%2$s", 0);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s在胸腔手术中不幸去世", 1);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s在手术中失血过多而亡", 2);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s成了%2$s的医疗试验品", 3);
        addDamageType(InitDamageType.OPEN_CHEST, "%1$s因%2$s的医疗失误而丧命", 4);

        addOrganSimpleActiveSkill(InitItem.ENDER_APPENDIX, "向视线方向传送");
        addOrganActiveSkill(
            InitItem.ENDER_APPENDIX,
            "沿视线方向进行传送",
            "传送距离取决于末影属性值",
            "近距离时可穿墙传送"
        );

        addOrganSimpleActiveSkill(InitItem.HERBIVORE_RUMEN, "可以啃食草方块、矮草丛或高草丛");
        addOrganActiveSkill(
            InitItem.HERBIVORE_RUMEN,
            "可以啃食草方块、矮草丛或高草丛",
            "玩家恢复2点饥饿值和4点饱和度",
            "生物恢复1点生命值",
            "高草丛额外触发一次恢复",
            "啃过的草方块会变成泥土"
        );

        addOrganSimpleActiveSkill(InitItem.CREEPER_APPENDIX, "自爆");
        addOrganActiveSkill(
            InitItem.CREEPER_APPENDIX,
            "以自身为中心制造爆炸",
            "威力等于3倍爆炸属性值",
            "不会破坏方块"
        );

        addOrganSimpleActiveSkill(InitItem.INNER_FURNACE, "消耗燃料，每%s秒恢复1点饥饿值与1点饱和度");
        addOrganActiveSkill(
            InitItem.INNER_FURNACE,
            "消耗手持可燃物品获得熔炉之力效果",
            "效果持续期间每%s秒恢复1点饥饿值与1点饱和度",
            "潜行时一次性消耗多个物品",
            "效果等级等于熔炉之力属性值",
            "持续时间从消耗的燃料燃烧时间累计"
        );

        addOrganSimpleActiveSkill(InitItem.GOLEM_ARMOR_PLATE, "使用铁锭修补自身");
        addOrganActiveSkill(
            InitItem.GOLEM_ARMOR_PLATE,
            "消耗背包中的铁锭来治疗自身",
            "治疗量等于2.5倍铁修复属性值"
        );
        addOrganSimplePassiveEffect(InitItem.GOLEM_ARMOR_PLATE, "可被其他玩家使用铁锭进行治疗");
        addOrganPassiveEffect(
            InitItem.GOLEM_ARMOR_PLATE,
            "可被其他玩家使用铁锭进行治疗",
            "治疗量等于2.5倍铁修复属性值"
        );

        addOrganSimpleActiveSkill(InitItem.SILK_GLAND, "发射蛛网");
        addOrganActiveSkill(
            InitItem.SILK_GLAND,
            "投掷蛛网困住实体",
            "使用消耗饥饿值"
        );

        addOrganSimplePassiveEffect(InitItem.VENOM_GLAND, "攻击时施加药水效果");
        addOrganPassiveEffect(
            InitItem.VENOM_GLAND,
            "攻击时对目标施加存储的药水效果",
            "可与任意药水合成以更换存储的药水效果",
            "药水效果持续时间缩短为原来的1/10",
            "冷却时间：4秒"
        );

        addOrganSimpleActiveSkill(InitItem.ALCHEMIST_GLAND, "获得药水效果");
        addOrganActiveSkill(
            InitItem.ALCHEMIST_GLAND,
            "获得存储的药水效果",
            "可与任意药水合成以更换存储的药水效果",
            "冷却时间等于存储效果的持续时间",
            "瞬时效果按60秒冷却计算"
        );

        addOrganActiveSkill(
            InitItem.LLAMA_LUNG,
            "发射羊驼口水弹"
        );

        addOrganSimpleActiveSkill(InitItem.ACTIVE_BLAZE_ROD, "发射连续的火球");
        addOrganActiveSkill(
            InitItem.ACTIVE_BLAZE_ROD,
            "连续发射小型火球",
            "火球数量等于呕火属性值",
            "每6tick发射一颗火球"
        );

        addOrganActiveSkill(
            InitItem.SNOW_CORE,
            "投掷雪球"
        );

        addOrganSimpleActiveSkill(InitItem.GHAST_STOMACH, "发射大型火球");
        addOrganActiveSkill(
            InitItem.GHAST_STOMACH,
            "发射恶魂式大型火球",
            "爆炸威力等于可怖属性值"
        );

        addOrganActiveSkill(
            InitItem.SHULKER_SPLEEN,
            "向视线中的目标发射追踪潜影子弹"
        );

        addOrganSimpleActiveSkill(InitItem.BREEZE_CORE, "发射风弹");
        addOrganActiveSkill(
            InitItem.BREEZE_CORE,
            "发射风弹",
            "击中后产生风爆效果"
        );

        addOrganSimpleActiveSkill(InitItem.DRAGON_LUNG, "发射龙息弹");
        addOrganActiveSkill(
            InitItem.DRAGON_LUNG,
            "发射龙息弹",
            "击中后产生范围龙息效果"
        );

        addOrganSimpleActiveSkill(InitItem.SCULK_CORE, "发射音爆");
        addOrganActiveSkill(
            InitItem.SCULK_CORE,
            "向视线方向发射音爆",
            "对路径上所有目标造成10点伤害",
            "穿透实体"
        );

        addOrganSimpleActiveSkill(InitItem.GUARDIAN_EYE, "发射充能光线");
        addOrganActiveSkill(
            InitItem.GUARDIAN_EYE,
            "对视线中的目标充能并发射光线",
            "造成2点魔法伤害加3点攻击伤害",
            "充能期间目标需保持在范围内"
        );

        addOrganSimpleActiveSkill(InitItem.ELDER_GUARDIAN_EYE, "发射充能光线");
        addOrganActiveSkill(
            InitItem.ELDER_GUARDIAN_EYE,
            "对视线中的目标充能并发射光线",
            "造成4点魔法伤害加6点攻击伤害",
            "充能期间目标需保持在范围内"
        );

        addOrganSimpleActiveSkill(InitItem.SYMBIOTIC_INTESTINE, "食用花朵获得迷之炖菜效果");
        addOrganActiveSkill(
            InitItem.SYMBIOTIC_INTESTINE,
            "食用手持花朵或视线指向的花朵方块",
            "获得对应花朵的迷之炖菜药水效果"
        );

        addOrganSimpleActiveSkill(InitItem.BEWITCHED_LIVER, "清除自身所有药水效果");
        addOrganActiveSkill(
            InitItem.BEWITCHED_LIVER,
            "清除自身所有药水效果"
        );

        add(ItemTagManager.ORGANS, "器官");
        add(ItemTagManager.HEART, "心脏");
        add(ItemTagManager.LUNG, "肺脏");
        add(ItemTagManager.MUSCLE, "肌肉");
        add(ItemTagManager.RIB, "肋骨");
        add(ItemTagManager.APPENDIX, "阑尾");
        add(ItemTagManager.SPLEEN, "脾脏");
        add(ItemTagManager.KIDNEY, "肾脏");
        add(ItemTagManager.SPINE, "脊柱");
        add(ItemTagManager.LIVER, "肝脏");
        add(ItemTagManager.INTESTINE, "肠子");
        add(ItemTagManager.STOMACH, "胃");
        add(ItemTagManager.SPECIAL, "特殊");
        add(ItemTagManager.BONE, "骨质");
        add(ItemTagManager.ROTTEN, "腐烂");
        add(ItemTagManager.IRON, "铁质");

        addEnchantment(
            InitEnchantment.TELEOPERATION,
            "远程操作",
            "每级可提升1格开胸器使用距离"
        );
        addEnchantment(
            InitEnchantment.ADVANCED_SURGERY,
            "先进手术",
            "每级可提升10%的最低开胸生命值上限"
        );
        addEnchantment(
            InitEnchantment.PRUDENT_SURGERY,
            "谨慎手术",
            "每级减少1点开胸造成的伤害"
        );
        addEnchantment(
            InitEnchantment.SAFE_SURGERY,
            "安全手术",
            "1级时下蹲可对自己和宠物开胸，2级时只能对其他生物开胸"
        );
        addEnchantment(
            InitEnchantment.HYDRAULIC_CLAMP,
            "液压钳",
            "每级消耗目标胸甲25点耐久"
        );
        addEnchantment(
            InitEnchantment.PRIMAL_REVERSION,
            "原始回归",
            "器官提供的属性强化至1.5倍"
        );
        addEnchantment(
            InitEnchantment.ANESTHESIA_SURGERY,
            "麻醉手术",
            "成功开胸时对目标施加3秒缓慢，缓慢等级等于附魔等级"
        );
        addEnchantment(
            InitEnchantment.POSTOPERATIVE_SUTURE,
            "术后缝合",
            "关闭胸腔界面时，每级为被开胸目标恢复1点生命值。触发回血后，冷却1秒"
        );

        add("message." + ChestCavityBeyond.MOD_ID + ".obstructed", "目标的胸腔被装备阻挡");
        add("message." + ChestCavityBeyond.MOD_ID + ".healthy", "目标太过健康");
        add("message." + ChestCavityBeyond.MOD_ID + ".unopenable", "此生物无法被开胸");

        add("commands.chestcavitybeyond.resize.failed", "没有找到有效目标");
        add("commands.chestcavitybeyond.resize.failed.invalid_size", "无效的胸腔容量大小");
        add("commands.chestcavitybeyond.resize.success.single", "已将%s的胸腔容量调整为%s（%s个槽位）");
        add("commands.chestcavitybeyond.resize.success.multiple", "已将%s个实体的胸腔容量调整为%s（%s个槽位）");

        add("commands.chestcavitybeyond.resetorgans.failed", "没有找到有效目标");
        add("commands.chestcavitybeyond.resetorgans.success.single", "已将%s的胸腔器官重置为默认");
        add("commands.chestcavitybeyond.resetorgans.success.multiple", "已将%s个实体的胸腔器官重置为默认");

        add("commands.chestcavitybeyond.attributes.header", "——%s的分析报告——");
        add("commands.chestcavitybeyond.attributes.no_description", "暂无详细描述");
        add("commands.chestcavitybeyond.attributes.failed.not_player", "属性查询指令只能由玩家执行");

        addAttributeDescription(
            InitAttribute.HEALTH,
            "每1点增加2点最大生命值",
            "当前值≤0时会持续受伤"
        );
        addAttributeDescription(
            InitAttribute.NERVES,
            "影响攻击速度和移动速度",
            "当前值≤0时无法移动"
        );
        addAttributeDescription(
            InitAttribute.DEFENSE,
            "减少非穿透性伤害",
            "伤害越高，减伤比例越低"
        );
        addAttributeDescription(
            InitAttribute.DIGESTION,
            "影响食物提供的饥饿值恢复量",
            "值≤0时无法消化任何食物"
        );
        addAttributeDescription(
            InitAttribute.NUTRITION,
            "影响食物提供的饱和度",
            "值≤0时无法从食物获取饱和度"
        );
        addAttributeDescription(
            InitAttribute.ENDURANCE,
            "影响饥饿值消耗速率，值越高消耗越慢"
        );
        addAttributeDescription(
            InitAttribute.METABOLISM,
            "影响消耗饱和度与饥饿值回复血量的速度",
            "当间隔降低至1tick后，超出的速率会提高单次回血量和饥饿伤害量"
        );
        addAttributeDescription(
            InitAttribute.BREATH_CAPACITY,
            "影响氧气上限",
            "值>0时才能呼吸"
        );
        addAttributeDescription(
            InitAttribute.BREATH_RECOVERY,
            "影响在空气中的氧气恢复速率",
            "值>0时才能在空气中呼吸"
        );
        addAttributeDescription(
            InitAttribute.WATER_BREATH,
            "影响在水中的氧气恢复速率",
            "值>0时才能在水中呼吸"
        );
        addAttributeDescription(
            InitAttribute.DETOXIFICATION,
            "缩短受到的有害药水效果持续时间",
            "缩减后时长≤%stick时免疫"
        );
        addAttributeDescription(
            InitAttribute.FILTRATION,
            "值低于默认值时，会定期受到中毒效果",
            "值越低中毒效果越强"
        );
        addAttributeDescription(
            InitAttribute.STRENGTH,
            "影响攻击伤害"
        );
        addAttributeDescription(
            InitAttribute.SPEED,
            "影响移动速度"
        );
        addAttributeDescription(
            InitAttribute.FIRE_RESISTANCE,
            "减少受到的火焰伤害",
            "伤害越高，减伤比例越低",
            "≥%s时免疫热方块（岩浆块/营火）伤害",
            "≥%s时免疫火焰伤害，并清除着火状态",
            "≥%s时免疫熔岩伤害"
        );
        addAttributeDescription(
            InitAttribute.FROST_RESISTANCE,
            "减少受到的冰冻伤害",
            "伤害越高，减伤比例越低",
            "≥%s时免疫冰冻伤害，并清除冰冻进度"
        );
        addAttributeDescription(
            InitAttribute.WATER_ALLERGY,
            "在水中或雨中持续受到伤害"
        );
        addAttributeDescription(
            InitAttribute.ENDER,
            "受到溺水伤害时随机传送到附近位置",
            "影响末影阑尾传送技能的传送距离",
            "是弹射物闪避属性生效的前提条件"
        );
        addAttributeDescription(
            InitAttribute.PROJECTILE_DODGE,
            "需要末影属性>0才能生效",
            "受到弹射物或喷溅水药水伤害时，取消伤害并随机传送"
        );
        addAttributeDescription(
            InitAttribute.LEAPING,
            "影响跳跃力度和安全掉落距离"
        );
        addAttributeDescription(
            InitAttribute.CARNIVOROUS_DIGESTION,
            "额外影响肉类食物的饥饿值恢复量"
        );
        addAttributeDescription(
            InitAttribute.CARNIVOROUS_NUTRITION,
            "额外影响肉类食物的饱和度"
        );
        addAttributeDescription(
            InitAttribute.HERBIVOROUS_DIGESTION,
            "额外影响植物食物的饥饿值恢复量"
        );
        addAttributeDescription(
            InitAttribute.HERBIVOROUS_NUTRITION,
            "额外影响植物食物的饱和度"
        );
        addAttributeDescription(
            InitAttribute.SCAVENGER_DIGESTION,
            "额外影响腐肉等有毒食物的饥饿值恢复量",
            "拥有此属性时食用有毒食物不会附加中毒和饥饿效果"
        );
        addAttributeDescription(
            InitAttribute.SCAVENGER_NUTRITION,
            "额外影响腐肉等有毒食物的饱和度",
            "食物中的有害效果会额外增加饱和度"
        );
        addAttributeDescription(
            InitAttribute.EXPLOSIVE,
            "影响苦力怕阑尾自爆技能的爆炸威力",
            "爆炸威力等于属性值 × 3"
        );
        addAttributeDescription(
            InitAttribute.PHOTOSYNTHESIS,
            "在白天且能看见天空时，随时间恢复饥饿值和饱和度",
            "每次触发 +1 饥饿值，当饥饿值满时 +1 饱和度"
        );
        addAttributeDescription(
            InitAttribute.LAUNCH,
            "攻击目标时将其向上击飞",
            "击飞力度会被目标的击退抗性削弱"
        );
        addAttributeDescription(
            InitAttribute.IRON_REPAIR,
            "影响傀儡装甲板技能使用铁锭的治疗量"
        );
        addAttributeDescription(
            InitAttribute.FURNACE_POWER,
            "消耗燃料激活，效果持续期间定期恢复饥饿值和饱和度",
            "效果等级越高，恢复间隔越短"
        );
        addAttributeDescription(
            InitAttribute.WITHERED,
            "攻击目标时施加凋零效果",
            "胸腔内存在下界之星器官时，延长效果持续时间并+1等级"
        );
        addAttributeDescription(
            InitAttribute.VOMIT_FIREBALL,
            "影响活性烈焰棒技能连续发射小型火球的数量"
        );
        addAttributeDescription(
            InitAttribute.GHASTLY,
            "影响恶魂胃技能发射的恶魂火球爆炸威力"
        );
        addAttributeDescription(
            InitAttribute.CRYSTALLIZATION,
            "需要靠近末影水晶才能生效",
            "持续恢复生命值，玩家额外恢复饥饿值与饱和度，恢复量随结晶化属性值提升"
        );
        addAttributeDescription(
            InitAttribute.LAVA_SWIM_SPEED,
            "提升在熔岩中的水平与垂直移动速度"
        );
        addAttributeDescription(
            InitAttribute.WATER_WEAKNESS,
            "在水中时降低挖掘速度和攻击伤害"
        );
        addAttributeDescription(
            InitAttribute.LAVA_WALK,
            "允许在熔岩表面行走",
            "潜行时失效"
        );

        addAttributeValueEffect(InitAttribute.HEALTH, "最大生命值%s");
        addAttributeValueEffect(InitAttribute.NERVES, "攻击速度%s%% | 移动速度%s%%");
        addAttributeValueEffect(InitAttribute.DEFENSE, "减伤比例 ~%s%%（以10点伤害计）");
        addAttributeValueEffect(InitAttribute.ENDURANCE, "饥饿值消耗%s%%");
        addAttributeValueEffect(InitAttribute.STRENGTH, "近战伤害%s%%");
        addAttributeValueEffect(InitAttribute.SPEED, "移动速度%s%%");
        addAttributeValueEffect(InitAttribute.DIGESTION, "食物饥饿值%s%%");
        addAttributeValueEffect(InitAttribute.NUTRITION, "食物饱和度%s%%");
        addAttributeValueEffect(InitAttribute.METABOLISM, "回血速率%s%%");
        addAttributeValueEffect(InitAttribute.BREATH_CAPACITY, "氧气消耗%s%%");
        addAttributeValueEffect(InitAttribute.BREATH_RECOVERY, "空气回复%s%%");
        addAttributeValueEffect(InitAttribute.WATER_BREATH, "水中回复%s%%");
        addAttributeValueEffect(InitAttribute.DETOXIFICATION, "有害效果持续时间%s%%");
        addAttributeValueEffect(InitAttribute.FILTRATION, "中毒效果Lv%s，持续%s秒");
        add(AttributeDisplayManager.getValueEffectKey(InitAttribute.FILTRATION) + ".safe", "血液过滤正常，无中毒风险");
        addAttributeValueEffect(InitAttribute.CARNIVOROUS_DIGESTION, "肉类饥饿值%s%%");
        addAttributeValueEffect(InitAttribute.CARNIVOROUS_NUTRITION, "肉类饱和度%s%%");
        addAttributeValueEffect(InitAttribute.HERBIVOROUS_DIGESTION, "植物饥饿值%s%%");
        addAttributeValueEffect(InitAttribute.HERBIVOROUS_NUTRITION, "植物饱和度%s%%");
        addAttributeValueEffect(InitAttribute.SCAVENGER_DIGESTION, "腐肉饥饿值%s%%");
        addAttributeValueEffect(InitAttribute.SCAVENGER_NUTRITION, "腐肉饱和度%s%%");
        addAttributeValueEffect(InitAttribute.FIRE_RESISTANCE, "火焰减伤比例 ~%s%%（以10点伤害计）");
        addAttributeValueEffect(InitAttribute.FROST_RESISTANCE, "冰霜减伤比例 ~%s%%（以10点伤害计）");
        addAttributeValueEffect(InitAttribute.ENDER, "传送范围%s");
        addAttributeValueEffect(InitAttribute.LEAPING, "跳跃高度%s");
        addAttributeValueEffect(InitAttribute.EXPLOSIVE, "爆炸威力%s");
        addAttributeValueEffect(InitAttribute.PHOTOSYNTHESIS, "触发间隔%s秒");
        addAttributeValueEffect(InitAttribute.IRON_REPAIR, "铁锭回复%s");
        addAttributeValueEffect(InitAttribute.WITHERED, "凋零持续%s秒，效果等级Lv%s，凋零持续时间%s%%");
        addAttributeValueEffect(InitAttribute.GHASTLY, "火球威力%s");
        addAttributeValueEffect(InitAttribute.CRYSTALLIZATION, "靠近末影水晶时每秒回复%s点生命值");
        addAttributeValueEffect(InitAttribute.LAUNCH, "击飞力度%s");
        addAttributeValueEffect(InitAttribute.VOMIT_FIREBALL, "火球数量%s");
        addAttributeValueEffect(InitAttribute.FURNACE_POWER, "每%s秒恢复1饥饿值+1饱和度，效果等级Lv%s，最大持续%s秒");
        addAttributeValueEffect(InitAttribute.LAVA_SWIM_SPEED, "熔岩移动力%s倍");
        addAttributeValueEffect(InitAttribute.WATER_WEAKNESS, "水中挖掘速度/攻击伤害降低%s%%");

        addAttributeDescription(NeoForgeMod.SWIM_SPEED, "提升在水中的移动速度");
        addAttributeValueEffect(NeoForgeMod.SWIM_SPEED, "游泳速度+%s%%");

        addAttributeDescription(Attributes.LUCK, "影响战利品表的掉落与品质");
        addAttributeValueEffect(Attributes.LUCK, "幸运%s");

        addAttributeDescription(Attributes.KNOCKBACK_RESISTANCE, "减少受到的击退效果");
        addAttributeValueEffect(Attributes.KNOCKBACK_RESISTANCE, "击退抗性%s%%");

        addAttributeDescription(Attributes.GRAVITY, "影响下落速度", "降至0及以下时可自由飞行且免疫坠落伤害");
        addAttributeValueEffect(Attributes.GRAVITY, "重力%s%%");
        add(AttributeDisplayManager.getValueEffectKey(Attributes.GRAVITY) + ".flight", "无重力，可自由飞行");

        addAttributeDescription(Attributes.ENTITY_INTERACTION_RANGE, "增加可交互的实体距离");
        addAttributeValueEffect(Attributes.ENTITY_INTERACTION_RANGE, "实体交互距离+%s");

        addAttributeDescription(Attributes.BLOCK_INTERACTION_RANGE, "增加可交互的方块距离");
        addAttributeValueEffect(Attributes.BLOCK_INTERACTION_RANGE, "方块交互距离+%s");

        add("jei." + ChestCavityBeyond.MOD_ID + ".chest_cavity_type", "胸腔类型");
        add("jei." + ChestCavityBeyond.MOD_ID + ".need_breath", "需要呼吸");
        add("jei." + ChestCavityBeyond.MOD_ID + ".no_breath", "不需要呼吸");
        add("jei." + ChestCavityBeyond.MOD_ID + ".need_health", "需要健康");
        add("jei." + ChestCavityBeyond.MOD_ID + ".no_health", "不需要健康");
        add("jei." + ChestCavityBeyond.MOD_ID + ".type_bonus_header", "胸腔类型额外加成：");
        add("jei." + ChestCavityBeyond.MOD_ID + ".type_default_bonus_header", "自带属性加成：");

        addChestCavityTypeName(ChestCavityTypeManager.HUMAN, "人类");
        addChestCavityTypeName(ChestCavityTypeManager.WITCH, "女巫");
        addChestCavityTypeName(ChestCavityTypeManager.ANIMAL, "动物");
        addChestCavityTypeName(ChestCavityTypeManager.HERBIVORE1, "食草动物");
        addChestCavityTypeName(ChestCavityTypeManager.HERBIVORE2, "反刍食草动物");
        addChestCavityTypeName(ChestCavityTypeManager.HERBIVORE3, "多胃食草动物");
        addChestCavityTypeName(ChestCavityTypeManager.MOOSHROOM, "哞菇");
        addChestCavityTypeName(ChestCavityTypeManager.LLAMA, "羊驼");
        addChestCavityTypeName(ChestCavityTypeManager.CARNIVORE, "食肉动物");
        addChestCavityTypeName(ChestCavityTypeManager.BRUTE_ANIMAL, "力量型动物");
        addChestCavityTypeName(ChestCavityTypeManager.HOGLIN, "疣猪兽");
        addChestCavityTypeName(ChestCavityTypeManager.SWIFT_ANIMAL, "速度型动物");
        addChestCavityTypeName(ChestCavityTypeManager.LEAPING_ANIMAL, "弹跳型动物");
        addChestCavityTypeName(ChestCavityTypeManager.SHULKER, "潜影贝");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_ANIMAL, "小型动物");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_HERBIVORE, "小型食草动物");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_CARNIVORE, "小型食肉动物");
        addChestCavityTypeName(ChestCavityTypeManager.TURTLE, "海龟");
        addChestCavityTypeName(ChestCavityTypeManager.FROG, "青蛙");
        addChestCavityTypeName(ChestCavityTypeManager.RABBIT, "兔子");
        addChestCavityTypeName(ChestCavityTypeManager.SLIME, "史莱姆");
        addChestCavityTypeName(ChestCavityTypeManager.MAGMA_CUBE, "岩浆怪");
        addChestCavityTypeName(ChestCavityTypeManager.FIREPROOF, "抗火生物");
        addChestCavityTypeName(ChestCavityTypeManager.BASALT, "玄武岩生物");
        addChestCavityTypeName(ChestCavityTypeManager.GHAST, "恶魂");
        addChestCavityTypeName(ChestCavityTypeManager.ENDER, "末影");
        addChestCavityTypeName(ChestCavityTypeManager.ENDER_DRAGON, "末影龙");
        addChestCavityTypeName(ChestCavityTypeManager.UNDEAD, "亡灵");
        addChestCavityTypeName(ChestCavityTypeManager.SKELETON, "骷髅");
        addChestCavityTypeName(ChestCavityTypeManager.WITHER_SKELETON, "凋灵骷髅");
        addChestCavityTypeName(ChestCavityTypeManager.WITHER, "凋灵");
        addChestCavityTypeName(ChestCavityTypeManager.ARTHROPOD, "节肢生物");
        addChestCavityTypeName(ChestCavityTypeManager.SPIDER, "蜘蛛");
        addChestCavityTypeName(ChestCavityTypeManager.CAVE_SPIDER, "洞穴蜘蛛");
        addChestCavityTypeName(ChestCavityTypeManager.AQUATIC, "水生生物");
        addChestCavityTypeName(ChestCavityTypeManager.DOLPHIN, "海豚");
        addChestCavityTypeName(ChestCavityTypeManager.FISH, "鱼类");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_AQUATIC, "小型水生生物");
        addChestCavityTypeName(ChestCavityTypeManager.SMALL_FISH, "小型鱼类");
        addChestCavityTypeName(ChestCavityTypeManager.SALTWATER, "盐水型");
        addChestCavityTypeName(ChestCavityTypeManager.CREEPER, "苦力怕");
        addChestCavityTypeName(ChestCavityTypeManager.BLAZE, "烈焰人");
        addChestCavityTypeName(ChestCavityTypeManager.BREEZE, "旋风人");
        addChestCavityTypeName(ChestCavityTypeManager.IRON_GOLEM, "铁傀儡");
        addChestCavityTypeName(ChestCavityTypeManager.SNOW_GOLEM, "雪傀儡");
        addChestCavityTypeName(ChestCavityTypeManager.WARDEN, "监守者");
        addChestCavityTypeName(ChestCavityTypeManager.ELDER, "远古生物");
        addChestCavityTypeName(ChestCavityTypeManager.ELDER_FISH, "远古鱼类");
        addChestCavityTypeName(ChestCavityTypeManager.GUARDIAN, "守卫者");
        addChestCavityTypeName(ChestCavityTypeManager.ELDER_GUARDIAN, "远古守卫者");
        addChestCavityTypeName(ChestCavityTypeManager.ARMOR_STAND, "盔甲架");

        addConfig("title", "胸腔扩展配置");
        addConfig("Organ", "器官系统", "器官系统机制");
        addConfig("ChestOpener", "开胸器", "开胸机制");
        addConfig("SkillParameters", "技能参数", "器官技能数值参数");
        addConfig("Immunity", "免疫阈值", "火焰与冰霜免疫阈值");
        addConfig("MobSkill", "生物技能", "生物AI器官技能设置");
        addConfig("filtrationPeriod", "器官过滤周期", "器官过滤系统的周期（tick）");
        addConfig(
            "detoxificationImmunityDurationThreshold",
            "解毒免疫阈值",
            "解毒或凋零化缩短后的有害效果持续时间不超过此值时直接免疫（tick，0 = 仅免疫缩短到0 tick的效果）"
        );
        addConfig("minChestOpenMaxHealth", "直接开胸生命阈值", "目标最大生命值不超过此值时可直接开胸，无需削减当前生命值");
        addConfig(
            "chestOpenBaseHealthRatio",
            "开胸基础生命比例",
            "超出直接开胸阈值时，目标当前生命值需低于最大生命值的该比例才能开胸（0.3 = 目标生命需≤30%，每级高级手术附魔额外+10%）"
        );
        addConfig("guardianLaserDistance", "守卫者激光距离", "守卫者激光技能的有效距离");
        addConfig("randomTeleportAttempts", "末影传送尝试次数", "末影传送技能的尝试循环次数");
        addConfig("furnacePowerMaxDuration", "熔炉之力最大持续", "熔炉之力效果的最大持续时间（tick）");
        addConfig("shulkerBulletDistance", "潜影贝子弹距离", "潜影贝子弹技能的检测距离");
        addConfig("sonicBoomDistance", "音爆距离", "监守者音爆技能的距离");
        addConfig("crystalEffectSearchRange", "水晶效果搜索范围", "末影水晶对拥有结晶实体的搜索范围");
        addConfig("enableChestCavityScaleSideEffect", "胸腔尺寸副作用", "是否启用胸腔容量增大时的实体尺寸副作用（每增加一排scale增加0.25）");
        addConfig("chestplateBlocksChestOpener", "胸甲阻挡开胸", "胸甲是否阻挡开胸器打开胸腔（创造模式不受影响）");
        addConfig("fireImmunityHotFloor", "火焰免疫-热方块", "火焰抗性阈值：免疫热方块（岩浆块/营火）伤害");
        addConfig("fireImmunityFire", "火焰免疫-火焰", "火焰抗性阈值：免疫火焰伤害，并清除着火状态");
        addConfig("fireImmunityLava", "火焰免疫-熔岩", "火焰抗性阈值：免疫熔岩伤害");
        addConfig("frostImmunity", "冰霜免疫阈值", "冰霜抗性阈值：免疫冰冻伤害，并清除冰冻进度");
        addConfig("enableMobGoalSkill", "启用生物器官技能", "是否启用非玩家实体自动使用器官技能");
        addConfig(
            "goalSkillEvalInterval",
            "技能评估间隔",
            "Goal器官技能评估间隔（tick），值越大性能越好但反应越慢\n注意：因MobAI的双tick机制，奇数会自动向上取偶（如 3→4），最小有效值为 2"
        );
        addConfig("goalSkillEnemyDetectRange", "技能敌人检测范围", "Goal器官技能的敌人检测范围（格）");
        addConfig(
            "goalSkillTargetMemoryTicks",
            "技能目标记忆时长",
            "Goal器官技能的目标记忆时长（tick），目标丢失后仍继续攻击的保持时间（0 = 禁用记忆，300 = 15秒）"
        );
        addConfig(
            "mobSkillRetaliatePlayer",
            "技能反击玩家",
            "宠物型Mob（狼/猫等有主生物）的器官技能是否反击造成伤害的玩家\nfalse = 不反击，默认保护玩家误伤场景（仅影响OwnableEntity，不影响怪物）"
        );
        addConfig(
            "mobSkillRetaliateOtherPet",
            "技能反击其他宠物",
            "宠物型Mob（狼/猫等有主生物）的器官技能是否反击造成伤害的其他宠物（不同主人）\nfalse = 不反击，默认防止宠物互伤（仅影响OwnableEntity，不影响怪物）"
        );
        addConfig("detailedTooltips", "默认详细描述", "默认显示详细描述。关闭时按住Shift查看详细描述。");
    }

    private void addAttribute(Holder<Attribute> attribute, String value) {
        add(attribute.value().getDescriptionId(), value);
    }

    private void addAttributeDescription(Holder<Attribute> attribute, String... lines) {
        String base = attribute.value().getDescriptionId() + ".description.";
        for (int i = 0; i < lines.length; i++) {
            add(base + i, lines[i]);
        }
    }

    private void addAttributeValueEffect(Holder<Attribute> attribute, String value) {
        add(AttributeDisplayManager.getValueEffectKey(attribute), value);
    }

    private void addDamageType(ResourceKey<DamageType> damageType, String value) {
        add("death.attack." + damageType.location().getNamespace() + "." + damageType.location().getPath(), value);
    }

    private void addDamageTypeWithPlayer(ResourceKey<DamageType> resourceKey, String value) {
        add("death.attack." + resourceKey.location().getNamespace() + "." + resourceKey.location().getPath() + ".player", value);
    }

    private void addDamageType(ResourceKey<DamageType> resourceKey, String value, int index) {
        add("death.attack." + resourceKey.location().getNamespace() + "." + resourceKey.location().getPath() + "." + index, value);
    }

    private void addOrganDescription(Supplier<Item> item, String... lines) {
        String base = "organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(item.get()).getPath() + ".description.";
        for (int i = 0; i < lines.length; i++) {
            add(base + i, lines[i]);
        }
    }

    private void addOrganSimpleActiveSkill(Supplier<Item> item, String... lines) {
        String base = "organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(item.get())
            .getPath() + ".active_skill.simple.";
        for (int i = 0; i < lines.length; i++) {
            add(base + i, lines[i]);
        }
    }

    private void addOrganActiveSkill(Supplier<Item> item, String... lines) {
        String base = "organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(item.get()).getPath() + ".active_skill.";
        for (int i = 0; i < lines.length; i++) {
            add(base + i, lines[i]);
        }
    }

    private void addOrganSimplePassiveEffect(Supplier<Item> item, String... lines) {
        String base = "organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(item.get())
            .getPath() + ".passive_effect.simple.";
        for (int i = 0; i < lines.length; i++) {
            add(base + i, lines[i]);
        }
    }

    private void addOrganPassiveEffect(Supplier<Item> item, String... lines) {
        String base = "organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(item.get()).getPath() + ".passive_effect.";
        for (int i = 0; i < lines.length; i++) {
            add(base + i, lines[i]);
        }
    }

    private void addEnchantment(ResourceKey<Enchantment> resourceKey, String value, String desc) {
        add("enchantment." + resourceKey.location().getNamespace() + "." + resourceKey.location().getPath(), value);
        add("enchantment." + resourceKey.location().getNamespace() + "." + resourceKey.location().getPath() + ".desc", desc);
    }

    private void addChestCavityTypeName(ChestCavityType type, String name) {
        add(ChestCavityType.getTranslationKey(type.getId()), name);
    }

    /**
     * 添加 ConfigurationScreen 配置项或 section 的名称与可选 tooltip
     */
    private void addConfig(String key, String name, @Nullable String tooltip) {
        add(ChestCavityBeyond.MOD_ID + ".configuration." + key, name);
        if (tooltip != null) {
            add(ChestCavityBeyond.MOD_ID + ".configuration." + key + ".tooltip", tooltip);
        }
    }

    private void addConfig(String key, String name) {
        addConfig(key, name, null);
    }

    @Override
    protected void addTranslations() {
        switch (locale) {
            case EN_US -> English();
            case ZH_CN -> Chinese();
        }
    }
}
