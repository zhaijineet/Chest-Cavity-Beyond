package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.client.key.KeyMappings;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitCreativeModeTab;
import net.zhaiji.chestcavitybeyond.register.InitDamageType;
import net.zhaiji.chestcavitybeyond.register.InitEffect;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;
import net.zhaiji.chestcavitybeyond.register.InitItem;

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
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.0", "Hold [");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.1", "Shift");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.2", "] for details");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.cooldown", "Cooldown: %ss");

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

        // ===== Active Skills (simple + detailed) =====
        addOrganSimpleActiveSkill(InitItem.ENDER_APPENDIX, "Teleport in viewing direction");
        addOrganActiveSkill(InitItem.ENDER_APPENDIX,
            "Teleport along the line of sight",
            "Teleport distance depends on Ender attribute value",
            "Can pass through thin walls when close"
        );

        addOrganSimpleActiveSkill(InitItem.HERBIVORE_RUMEN, "Eat grass");
        addOrganActiveSkill(InitItem.HERBIVORE_RUMEN,
            "Consume grass, short grass, or tall grass for food",
            "Grass blocks turn into dirt",
            "Tall grass restores more hunger than short grass"
        );

        addOrganSimpleActiveSkill(InitItem.CREEPER_APPENDIX, "Explode");
        addOrganActiveSkill(InitItem.CREEPER_APPENDIX,
            "Create an explosion centered on self",
            "Power equals 3 times Explosive attribute value",
            "Does not destroy blocks"
        );

        addOrganSimpleActiveSkill(InitItem.INNER_FURNACE, "Burn fuel for power");
        addOrganActiveSkill(InitItem.INNER_FURNACE,
            "Consume burnable items in hand to gain Furnace Power",
            "Sneak to consume multiple items at once",
            "Effect level equals Furnace Power attribute minus 1",
            "Duration accumulates from consumed fuel burn time"
        );

        addOrganSimpleActiveSkill(InitItem.GOLEM_ARMOR_PLATE, "Repair itself using iron ingots");
        addOrganActiveSkill(InitItem.GOLEM_ARMOR_PLATE,
            "Consume an iron ingot from inventory to heal",
            "Healing equals 2.5 times Iron Repair attribute"
        );

        addOrganSimpleActiveSkill(InitItem.SILK_GLAND, "Shoot cobwebs");
        addOrganActiveSkill(InitItem.SILK_GLAND,
            "Throw a cobweb that can trap entities",
            "Costs hunger to use"
        );

        // venom_gland: passive effect (triggered on attack, not an active skill)
        addOrganSimplePassiveEffect(InitItem.VENOM_GLAND, "Apply potion effects on attack");
        addOrganPassiveEffect(InitItem.VENOM_GLAND,
            "Apply stored potion effects to target on attack",
            "Effect duration is reduced to 1/10 of original",
            "4 seconds cooldown"
        );

        addOrganActiveSkill(InitItem.LLAMA_LUNG,
            "Shoot a llama spit projectile"
        );

        addOrganSimpleActiveSkill(InitItem.ACTIVE_BLAZE_ROD, "Shoot continuous fireballs");
        addOrganActiveSkill(InitItem.ACTIVE_BLAZE_ROD,
            "Launch a barrage of small fireballs",
            "Number of fireballs equals Vomit Fireball attribute",
            "Fires one fireball every 6 ticks"
        );

        addOrganActiveSkill(InitItem.SNOW_CORE,
            "Throw a snowball projectile"
        );

        addOrganSimpleActiveSkill(InitItem.GHAST_STOMACH, "Shoot large fireballs");
        addOrganActiveSkill(InitItem.GHAST_STOMACH,
            "Launch a ghast-style large fireball",
            "Explosion power equals Ghastly attribute value"
        );

        addOrganActiveSkill(InitItem.SHULKER_SPLEEN,
            "Fire a homing shulker bullet at target in sight"
        );

        addOrganSimpleActiveSkill(InitItem.BREEZE_CORE, "Shoot wind charges");
        addOrganActiveSkill(InitItem.BREEZE_CORE,
            "Launch a wind charge projectile",
            "Creates wind burst on impact"
        );

        addOrganSimpleActiveSkill(InitItem.DRAGON_LUNG, "Shoot dragon fireballs");
        addOrganActiveSkill(InitItem.DRAGON_LUNG,
            "Launch a dragon fireball",
            "Creates area-of-effect dragon breath on impact"
        );

        addOrganSimpleActiveSkill(InitItem.SCULK_CORE, "Sonic boom");
        addOrganActiveSkill(InitItem.SCULK_CORE,
            "Emit a sonic boom in the looking direction",
            "Deals 10 damage to all targets in the path",
            "Passes through entities"
        );

        addOrganSimpleActiveSkill(InitItem.GUARDIAN_EYE, "Shoot charged laser");
        addOrganActiveSkill(InitItem.GUARDIAN_EYE,
            "Charge and fire a laser beam at target in sight",
            "Deals 2 magic damage plus 3 attack damage",
            "Target must remain in range during charging"
        );

        addOrganSimpleActiveSkill(InitItem.ELDER_GUARDIAN_EYE, "Shoot charged laser");
        addOrganActiveSkill(InitItem.ELDER_GUARDIAN_EYE,
            "Charge and fire a laser beam at target in sight",
            "Deals 4 magic damage plus 6 attack damage",
            "Target must remain in range during charging"
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

        add("message." + ChestCavityBeyond.MOD_ID + ".obstructed", "Target's chest is obstructed by equipment");
        add("message." + ChestCavityBeyond.MOD_ID + ".healthy", "Target is too healthy");
        add("message." + ChestCavityBeyond.MOD_ID + ".unopenable", "This entity cannot be opened");

        add("commands.chestcavitybeyond.resize.failed", "No valid targets found");
        add("commands.chestcavitybeyond.resize.success.single", "Resized %s's chest cavity to %s(%s slots)");
        add("commands.chestcavitybeyond.resize.success.multiple", "Resized chest cavity of %s entities to %s(%s slots)");

        add("commands.chestcavitybeyond.resetorgans.failed", "No valid targets found");
        add("commands.chestcavitybeyond.resetorgans.success.single", "Reset %s's chest cavity organs to defaults");
        add("commands.chestcavitybeyond.resetorgans.success.multiple", "Reset chest cavity organs of %s entities to defaults");

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
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.0", "按住[");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.1", "Shift");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.hint.2", "]查看详细说明");
        add("organ." + ChestCavityBeyond.MOD_ID + ".tooltip.cooldown", "冷却时间：%s秒");

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

        // ===== 主动技能 (simple + 详细描述) =====
        addOrganSimpleActiveSkill(InitItem.ENDER_APPENDIX, "向视线方向传送");
        addOrganActiveSkill(InitItem.ENDER_APPENDIX,
            "沿视线方向进行传送",
            "传送距离取决于末影属性值",
            "近距离时可穿墙传送"
        );

        addOrganSimpleActiveSkill(InitItem.HERBIVORE_RUMEN, "吃草");
        addOrganActiveSkill(InitItem.HERBIVORE_RUMEN,
            "食用草方块、矮草丛或高草丛",
            "草方块会变成泥土",
            "高草丛比矮草丛恢复更多饱食度"
        );

        addOrganSimpleActiveSkill(InitItem.CREEPER_APPENDIX, "自爆");
        addOrganActiveSkill(InitItem.CREEPER_APPENDIX,
            "以自身为中心制造爆炸",
            "威力等于3倍爆炸属性值",
            "不会破坏方块"
        );

        addOrganSimpleActiveSkill(InitItem.INNER_FURNACE, "燃烧燃料获得动力");
        addOrganActiveSkill(InitItem.INNER_FURNACE,
            "消耗手持可燃物品获得熔炉之力效果",
            "潜行时一次性消耗多个物品",
            "效果等级等于熔炉之力属性值减1",
            "持续时间从消耗的燃料燃烧时间累计"
        );

        addOrganSimpleActiveSkill(InitItem.GOLEM_ARMOR_PLATE, "使用铁锭修补自身");
        addOrganActiveSkill(InitItem.GOLEM_ARMOR_PLATE,
            "消耗背包中的铁锭来治疗自身",
            "治疗量等于2.5倍铁修复属性值"
        );

        addOrganSimpleActiveSkill(InitItem.SILK_GLAND, "发射蛛网");
        addOrganActiveSkill(InitItem.SILK_GLAND,
            "投掷蛛网困住实体",
            "使用消耗饱食度"
        );

        // 毒腺：被动效果（攻击时触发，非主动技能）
        addOrganSimplePassiveEffect(InitItem.VENOM_GLAND, "攻击时施加药水效果");
        addOrganPassiveEffect(InitItem.VENOM_GLAND,
            "攻击时对目标施加存储的药水效果",
            "药水效果持续时间缩短为原来的1/10",
            "冷却时间：4秒"
        );

        addOrganActiveSkill(InitItem.LLAMA_LUNG,
            "发射羊驼口水弹"
        );

        addOrganSimpleActiveSkill(InitItem.ACTIVE_BLAZE_ROD, "发射连续的火球");
        addOrganActiveSkill(InitItem.ACTIVE_BLAZE_ROD,
            "连续发射小型火球",
            "火球数量等于呕火属性值",
            "每6tick发射一颗火球"
        );

        addOrganActiveSkill(InitItem.SNOW_CORE,
            "投掷雪球"
        );

        addOrganSimpleActiveSkill(InitItem.GHAST_STOMACH, "发射大型火球");
        addOrganActiveSkill(InitItem.GHAST_STOMACH,
            "发射恶魂式大型火球",
            "爆炸威力等于可怖属性值"
        );

        addOrganActiveSkill(InitItem.SHULKER_SPLEEN,
            "向视线中的目标发射追踪潜影子弹"
        );

        addOrganSimpleActiveSkill(InitItem.BREEZE_CORE, "发射风弹");
        addOrganActiveSkill(InitItem.BREEZE_CORE,
            "发射风弹",
            "击中后产生风爆效果"
        );

        addOrganSimpleActiveSkill(InitItem.DRAGON_LUNG, "发射龙息弹");
        addOrganActiveSkill(InitItem.DRAGON_LUNG,
            "发射龙息弹",
            "击中后产生范围龙息效果"
        );

        addOrganSimpleActiveSkill(InitItem.SCULK_CORE, "发射音爆");
        addOrganActiveSkill(InitItem.SCULK_CORE,
            "向视线方向发射音爆",
            "对路径上所有目标造成10点伤害",
            "穿透实体"
        );

        addOrganSimpleActiveSkill(InitItem.GUARDIAN_EYE, "发射充能光线");
        addOrganActiveSkill(InitItem.GUARDIAN_EYE,
            "对视线中的目标充能并发射光线",
            "造成2点魔法伤害加3点攻击伤害",
            "充能期间目标需保持在范围内"
        );

        addOrganSimpleActiveSkill(InitItem.ELDER_GUARDIAN_EYE, "发射充能光线");
        addOrganActiveSkill(InitItem.ELDER_GUARDIAN_EYE,
            "对视线中的目标充能并发射光线",
            "造成4点魔法伤害加6点攻击伤害",
            "充能期间目标需保持在范围内"
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

        add("message." + ChestCavityBeyond.MOD_ID + ".obstructed", "目标的胸腔被装备阻挡");
        add("message." + ChestCavityBeyond.MOD_ID + ".healthy", "目标太过健康");
        add("message." + ChestCavityBeyond.MOD_ID + ".unopenable", "此生物无法被开胸");

        add("commands.chestcavitybeyond.resize.failed", "没有找到有效目标");
        add("commands.chestcavitybeyond.resize.success.single", "已将%s的胸腔容量调整为%s（%s个槽位）");
        add("commands.chestcavitybeyond.resize.success.multiple", "已将%s个实体的胸腔容量调整为%s（%s个槽位）");

        add("commands.chestcavitybeyond.resetorgans.failed", "没有找到有效目标");
        add("commands.chestcavitybeyond.resetorgans.success.single", "已将%s的胸腔器官重置为默认");
        add("commands.chestcavitybeyond.resetorgans.success.multiple", "已将%s个实体的胸腔器官重置为默认");
    }

    private void addAttribute(Holder<Attribute> attribute, String value) {
        add(attribute.value().getDescriptionId(), value);
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

    private void addOrganSimpleActiveSkill(Supplier<Item> item, String... lines) {
        String base = "organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(item.get()).getPath() + ".active_skill.simple.";
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
        String base = "organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(item.get()).getPath() + ".passive_effect.simple.";
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

    @Override
    protected void addTranslations() {
        switch (locale) {
            case EN_US -> English();
            case ZH_CN -> Chinese();
        }
    }
}
