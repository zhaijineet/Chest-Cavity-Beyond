package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.goal.GoalCombatContext;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillIntent;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillMetadata;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillTargetResolver;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

public class GoalSkillUtil {
    /**
     * 末影阑尾——传送（逃跑/战术/随机）
     */
    public static GoalSkillMetadata enderAppendixGoalSkill() {
        return GoalSkillMetadata.defense((GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                LivingEntity entity = slotContext.entity();
                double ender = slotContext.data().getCurrentValue(InitAttribute.ENDER);
                double healthPercent = entity.getHealth() / entity.getMaxHealth();
                if (healthPercent < 0.3) {
                    return OrganSkillUtil.fleeTeleport(entity, combatContext.target(), ender);
                } else if (combatContext.target() != null) {
                    return OrganSkillUtil.teleportBehind(entity, combatContext.target(), ender);
                }
                return OrganSkillUtil.randomTeleport(entity, ender);
            })
            .entityFilter(mob -> !(mob instanceof EnderMan))
            .build();
    }

    /**
     * 食草动物瘤胃——吃草回血
     */
    public static GoalSkillMetadata herbivoreRumenGoalSkill() {
        return GoalSkillMetadata.blockInteract(
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    BlockPos blockTarget = combatContext.blockTarget();
                    if (blockTarget == null) return false;
                    BlockState state = entity.level().getBlockState(blockTarget);
                    // 高草丛倍率2，矮草丛/草方块倍率1
                    float base = state.is(Blocks.TALL_GRASS) ? 2.0F : 1.0F;
                    ChestCavityData data = slotContext.data();
                    double digestionDifference = data.getDifferenceValue(InitAttribute.DIGESTION)
                                                 + data.getDifferenceValue(InitAttribute.HERBIVOROUS_DIGESTION);
                    double nutritionDifference = data.getDifferenceValue(InitAttribute.NUTRITION)
                                                 + data.getDifferenceValue(InitAttribute.HERBIVOROUS_NUTRITION);
                    // 正数时营养削弱参与，负数时保持原值以确保惩罚力度
                    nutritionDifference = nutritionDifference > 0 ? nutritionDifference / 4 : nutritionDifference;
                    float healAmount = (float) (
                        base * MathUtil.getDirectScale(digestionDifference) * MathUtil.getDirectScale(nutritionDifference)
                    );
                    entity.heal(healAmount);
                    if (state.is(Blocks.GRASS_BLOCK)) {
                        // 草方块变为泥土，播放方块破坏粒子
                        entity.level().levelEvent(2001, blockTarget, Block.getId(state));
                        entity.level().setBlock(blockTarget, Blocks.DIRT.defaultBlockState(), 2);
                    } else {
                        entity.level().destroyBlock(blockTarget, false);
                    }
                    return true;
                }
            ).canUse((mob, skillEntry) -> {
                if (mob.getHealth() >= mob.getMaxHealth()) return false;
                ChestCavityData data = ChestCavityUtil.getData(mob);
                return data.getCurrentValue(InitAttribute.DIGESTION) > 0 || data.getCurrentValue(InitAttribute.HERBIVOROUS_DIGESTION) > 0;
            })
            .blockTargetResolver(
                (mob, skillEntry) -> GoalSkillTargetResolver.DEFAULT_BLOCK_RESOLVER.apply(
                    mob,
                    state -> state.is(Blocks.SHORT_GRASS)
                             || state.is(Blocks.TALL_GRASS)
                             || state.is(Blocks.GRASS_BLOCK)
                )
            )
            .build();
    }

    /**
     * 苦力怕阑尾——自爆
     */
    public static GoalSkillMetadata creeperAppendixGoalSkill() {
        return GoalSkillMetadata.aoeAttack(
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) ->
                    OrganSkillUtil.explosion(
                        slotContext.entity(),
                        slotContext.data().getCurrentValue(InitAttribute.EXPLOSIVE)
                    )
            )
            .entityFilter(mob -> !(mob instanceof Creeper))
            .weightOverride((mob, combatContext, skillEntry) -> {
                boolean hasThreat = combatContext.target() != null || combatContext.nearbyEnemyCount() > 0;
                return hasThreat && combatContext.selfHealthPercent() < 0.3 ? 150 : 0;
            })
            .build();
    }

    /**
     * 丝腺——朝目标吐丝
     */
    public static GoalSkillMetadata silkGlandGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.silk(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target())
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof Spider))
            .build();
    }

    /**
     * 羊驼肺——朝目标吐口水
     */
    public static GoalSkillMetadata llamaLungGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.spit(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target())
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof Llama))
            .build();
    }

    /**
     * 活性烈焰棒——朝目标连续发射小火球
     */
    public static GoalSkillMetadata activeBlazeRodGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) ->
                    OrganSkillUtil.smallFireball(
                        slotContext.data(),
                        (int) slotContext.data().getCurrentValue(InitAttribute.VOMIT_FIREBALL),
                        combatContext.target()
                    )
            )
            .entityFilter(mob -> !(mob instanceof Blaze))
            .build();
    }

    /**
     * 雪之核心——朝目标发射雪球
     */
    public static GoalSkillMetadata snowCoreGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.snowball(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target())
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof SnowGolem))
            .build();
    }

    /**
     * 恶魂胃——朝目标发射大火球
     */
    public static GoalSkillMetadata ghastStomachGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.largeFireball(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target()),
                        slotContext.data().getCurrentValue(InitAttribute.GHASTLY)
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof Ghast))
            .build();
    }

    /**
     * 潜影贝脾脏——朝目标发射潜影子弹
     */
    public static GoalSkillMetadata shulkerSpleenGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                (mob, target, skillEntry) -> ChestCavityBeyondConfig.shulkerBulletDistance,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    OrganSkillUtil.shulkerBullet(slotContext.entity(), combatContext.target());
                    return true;
                }
            )
            .entityFilter(mob -> !(mob instanceof Shulker))
            .build();
    }

    /**
     * 龙之肺脏——朝目标发射龙息弹
     */
    public static GoalSkillMetadata dragonLungGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.dragonFireball(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target())
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof EnderDragon))
            .build();
    }

    /**
     * 幽匿核心——朝目标释放音爆
     */
    public static GoalSkillMetadata sculkCoreGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                (mob, target, skillEntry) -> ChestCavityBeyondConfig.sonicBoomDistance,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.sonicBoom(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target())
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof Warden))
            // 原版监守者音爆可穿墙攻击，不要求视线无遮挡
            .requireLineOfSight(false)
            .weightOverride((mob, combatContext, skillEntry) -> {
                // 音爆专为穿墙设计，移除 LOS 的 ±20/-40 权重影响
                if (combatContext.target() == null && combatContext.nearbyEnemyCount() == 0) return 0;
                boolean repeatPenalty = combatContext.lastUsedIntent() == GoalSkillIntent.ATTACK;
                return 80 + (combatContext.target() != null ? (1 - combatContext.targetHealthPercent()) * 40 : 0)
                       + (repeatPenalty ? -30 : 0);
            })
            .build();
    }

    /**
     * 守卫者之眼——朝目标发射激光
     */
    public static GoalSkillMetadata guardianEyeGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                (mob, target, skillEntry) -> ChestCavityBeyondConfig.guardianLaserDistance,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) ->
                    OrganSkillUtil.guardianLaser(
                        slotContext.entity(), combatContext.target(), false)
            )
            .entityFilter(mob -> !(mob instanceof Guardian))
            .build();
    }

    /**
     * 远古守卫者之眼——朝目标发射远古激光
     */
    public static GoalSkillMetadata elderGuardianEyeGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                (mob, target, skillEntry) -> ChestCavityBeyondConfig.guardianLaserDistance,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) ->
                    OrganSkillUtil.guardianLaser(
                        slotContext.entity(), combatContext.target(), true)
            )
            .entityFilter(mob -> !(mob instanceof ElderGuardian))
            .build();
    }

    /**
     * 炼金腺——给自己添加药水效果
     */
    public static GoalSkillMetadata alchemistGlandGoalSkill() {
        return GoalSkillMetadata.recovery((GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                slotContext.stack()
                    .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                    .forEachEffect(slotContext.entity()::addEffect);
                return true;
            })
            .build();
    }

    /**
     * 旋风核心——朝目标发射风弹
     */
    public static GoalSkillMetadata breezeCoreGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.windCharge(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target())
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof Breeze))
            .build();
    }

    /**
     * 下界之星——朝目标发射凋零骷髅头
     */
    public static GoalSkillMetadata netherStarGoalSkill() {
        return GoalSkillMetadata.targetedAttack(
                GoalSkillMetadata::defaultRange,
                (GoalCombatContext combatContext, ChestCavitySlotContext slotContext) -> {
                    LivingEntity entity = slotContext.entity();
                    return OrganSkillUtil.witherSkull(
                        entity,
                        OrganSkillUtil.directionTo(entity, combatContext.target())
                    );
                }
            )
            .entityFilter(mob -> !(mob instanceof WitherBoss))
            .build();
    }
}
