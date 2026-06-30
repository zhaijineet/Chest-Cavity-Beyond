package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;

import java.util.UUID;

/**
 * 实体关系判定工具，统一处理 AoE 技能和目标选择中的友善关系判定
 * <p>
 * 提供两个 AoE 过滤入口：
 * <ul>
 *   <li>{@link #shouldAoeDamage}：攻击型 AoE（AoE 伤害 / Goal 技能目标 / 反击记忆），
 *       应用永远友善过滤 + 宠物型 Mob 反击配置。</li>
 *   <li>{@link #shouldAoeSpread}：溅射型 AoE（摔落溅射 / 效果传播等非反击型扩散），
 *       仅应用永远友善过滤，不受反击配置约束。</li>
 * </ul>
 * </p>
 */
public class EntityRelationUtil {
    /**
     * 判定两实体是否互为友善关系（不应被彼此的 AoE 技能伤害）
     * <p>
     * 覆盖四种关系：自身、attacker 的主人、attacker 的宠物、attacker 同主人的宠物。
     * 双向判定：既包含宠物→主人方向，也包含玩家→自己宠物方向。
     * </p>
     *
     * @param attacker 主动发起 AoE / 技能的实体
     * @param target   被作用目标
     * @return true 表示 target 不应被 attacker 伤害
     */
    public static boolean isFriendlyTarget(LivingEntity attacker, LivingEntity target) {
        if (target == null) return false;
        if (attacker == target) return true;
        // attacker 是宠物，target 是其主人（主人误伤保护 + 宠物 AoE 不伤主人）
        if (isOwnerOf(attacker, target)) return true;
        // 防止玩家 AoE 伤害玩家自己的宠物（如玩家病变心脏不传给自己的狼）
        if (isOwnerOf(target, attacker)) return true;
        // 同主人宠物（狼群互伤保护）
        if (isSameOwner(attacker, target)) return true;
        return false;
    }

    /**
     * 判定两实体是否同主人（两者均为 OwnableEntity 且 owner UUID 相同且非空）
     *
     * @param first  第一个实体
     * @param second 第二个实体
     * @return true 表示两者拥有相同的主人
     */
    public static boolean isSameOwner(Entity first, Entity second) {
        if (!(first instanceof OwnableEntity firstOwnable) || !(second instanceof OwnableEntity secondOwnable)) {
            return false;
        }
        UUID firstOwnerUuid = firstOwnable.getOwnerUUID();
        UUID secondOwnerUuid = secondOwnable.getOwnerUUID();
        return firstOwnerUuid != null && firstOwnerUuid.equals(secondOwnerUuid);
    }

    /**
     * 判定 possibleOwner 是否是 possiblePet 的主人
     * <p>
     * possiblePet 须为 OwnableEntity，且其 owner UUID 匹配 possibleOwner 的 UUID。
     * </p>
     *
     * @param possiblePet   被判定可能为宠物的实体
     * @param possibleOwner 被判定可能为主人的实体
     * @return true 表示 possibleOwner 是 possiblePet 的主人
     */
    public static boolean isOwnerOf(Entity possiblePet, Entity possibleOwner) {
        if (!(possiblePet instanceof OwnableEntity ownable)) return false;
        UUID ownerUuid = ownable.getOwnerUUID();
        return ownerUuid != null && ownerUuid.equals(possibleOwner.getUUID());
    }

    /**
     * 攻击型 AoE 综合过滤器：判定 attacker 的 AoE 技能是否应当伤害 target
     * <p>
     * 适用于所有攻击性行为的目标判定（AoE 伤害 / Goal 技能目标 / 受伤反击记忆），
     * 内部按顺序应用三层规则：
     * </p>
     * <ol>
     *   <li>永远友善过滤（同主/主人，对所有 LivingEntity 生效）</li>
     *   <li>宠物型 Mob（OwnableEntity）的反击玩家配置</li>
     *   <li>宠物型 Mob（OwnableEntity）的反击其他宠物配置</li>
     * </ol>
     * <p>
     * 调用方只需一行 lambda 调用本方法，无需重复 if 链。
     * 溅射 / 传播类等非反击型 AoE 请改用 {@link #shouldAoeSpread}。
     * </p>
     *
     * @param attacker 主动发起 AoE / 技能的实体
     * @param target   被作用目标
     * @return true 表示应伤害 target，false 表示应跳过
     */
    public static boolean shouldAoeDamage(LivingEntity attacker, LivingEntity target) {
        if (target == null) return false;
        if (target == attacker) return false;
        if (isFriendlyTarget(attacker, target)) return false;
        if (attacker instanceof OwnableEntity) {
            if (target instanceof Player
                && !ChestCavityBeyondConfig.mobSkillRetaliatePlayer) {
                return false;
            }
            if (target instanceof OwnableEntity
                && !ChestCavityBeyondConfig.mobSkillRetaliateOtherPet) {
                return false;
            }
        }
        return true;
    }

    /**
     * 溅射型 AoE 综合过滤器：判定 attacker 的溅射 / 传播类技能是否应当作用到 target
     * <p>
     * 仅排除自身与永远友善目标（主人 / 同主宠物），不应用反击配置，
     * 适用于摔落溅射、效果传播等非反击型扩散行为。
     * </p>
     *
     * @param attacker 主动发起溅射 / 传播的实体
     * @param target   被作用目标
     * @return true 表示应作用到 target，false 表示应跳过
     */
    public static boolean shouldAoeSpread(LivingEntity attacker, LivingEntity target) {
        if (target == null) return false;
        if (target == attacker) return false;
        return !isFriendlyTarget(attacker, target);
    }
}
