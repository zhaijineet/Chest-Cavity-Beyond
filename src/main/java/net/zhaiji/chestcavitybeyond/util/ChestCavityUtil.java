package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.OrganInteractContext;
import net.zhaiji.chestcavitybeyond.api.TargetResolver;
import net.zhaiji.chestcavitybeyond.api.capability.IOrgan;
import net.zhaiji.chestcavitybeyond.api.event.OrganChangeEvent;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.manager.CapabilityManager;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;
import net.zhaiji.chestcavitybeyond.manager.OrganManager;
import net.zhaiji.chestcavitybeyond.menu.ChestCavityMenu;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.register.InitAttachmentType;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ChestCavityUtil {
    /**
     * 获取槽位id
     *
     * @param index 槽位索引
     * @return 槽位id
     */
    public static ResourceLocation getSlotId(int index) {
        return ChestCavityBeyond.of("organ_" + index);
    }

    /**
     * 获取目标的胸腔数据
     *
     * @param entity 目标
     * @return 目标的胸腔数据
     */
    public static ChestCavityData getData(LivingEntity entity) {
        return entity.getData(InitAttachmentType.CHEST_CAVITY);
    }

    /**
     * 获取物品的器官capability
     *
     * @param stack 物品
     * @return 器官capability
     */
    public static IOrgan getOrganCap(ItemStack stack) {
        return Objects.requireNonNullElse(stack.getCapability(CapabilityManager.ORGAN), OrganManager.EMPTY_ORGAN);
    }

    /**
     * 检测物品是否为器官
     *
     * @param stack 物品
     * @return 是否为器官
     */
    public static boolean isOrgan(ItemStack stack) {
        IOrgan organCap = getOrganCap(stack);
        return stack.is(ItemTagManager.ORGANS) || organCap != OrganManager.EMPTY_ORGAN;
    }

    /**
     * 判断玩家手持物品是否应取消实体交互
     * <p>
     * 当玩家手持开胸器或生物分析仪，且目标可被 {@link TargetResolver} 解析为 {@link LivingEntity} 时，
     * 取消原版交互以使 {@code Item.use()} 正常触发。
     * </p>
     *
     * @param player 玩家
     * @param hand   交互手
     * @param target 射线命中的目标实体
     * @return 是否应取消交互
     */
    public static boolean shouldCancelEntityInteract(Player player, InteractionHand hand, Entity target) {
        ItemStack stack = player.getItemInHand(hand);
        return (stack.is(ItemTagManager.CHEST_OPENERS) || stack.is(InitItem.BIOLOGICAL_ANALYZER.get())) && TargetResolver.resolve(target) instanceof LivingEntity;
    }

    /**
     * 创建胸腔槽位上下文
     *
     * @param data  胸腔数据 (可能为null)
     * @param index 位置索引
     * @param stack 对应物品
     * @return 胸腔槽位上下文
     */
    public static ChestCavitySlotContext createContext(
        @Nullable ChestCavityData data,
        int index,
        ItemStack stack
    ) {
        return new ChestCavitySlotContext(data, data != null ? data.getOwner() : null, getSlotId(index), index, stack);
    }

    /**
     * 获取器官提供的属性
     *
     * @param context 胸腔槽位上下文
     * @return 器官提供的属性
     */
    public static Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ChestCavitySlotContext context) {
        return getOrganCap(context.stack()).getAttributeModifiers(context);
    }

    /**
     * 器官更换
     */
    public static void changeOrgan(ChestCavityData data, LivingEntity entity, int index, ItemStack oldStack, ItemStack newStack) {
        // 有时候会出来新旧都为空的情况
        if (entity.level().isClientSide() || oldStack.isEmpty() && newStack.isEmpty()) return;
        OrganAttributeUtil.updateOrganAttributeModifier(data, entity, index, oldStack, newStack);
        ChestCavityUtil.organRemoved(data, entity, index, oldStack);
        ChestCavityUtil.organAdded(data, entity, index, newStack);
        // 触发所有其他器官的变化回调
        triggerOtherOrganChange(data, entity, index, oldStack, newStack);
        // 发布器官更换事件
        NeoForge.EVENT_BUS.post(new OrganChangeEvent(data, entity, index, oldStack, newStack));
        // 递增变更计数，通知使用方重建缓存
        data.bumpOrganChangeCount();
    }

    /**
     * 器官tick
     */
    public static void organTick(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        getOrganCap(stack).tick(createContext(data, index, stack));
    }

    /**
     * 器官移植时
     */
    public static void organAdded(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        ChestCavitySlotContext changedContext = createContext(data, index, stack);
        getOrganCap(stack).organAdded(changedContext);
    }

    /**
     * 器官摘除时
     */
    public static void organRemoved(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        ChestCavitySlotContext changedContext = createContext(data, index, stack);
        getOrganCap(stack).organRemoved(changedContext);
    }

    /**
     * 触发所有其他器官的变化回调
     *
     * @param data         胸腔数据
     * @param entity       实体
     * @param changedIndex 变化的器官索引
     * @param oldStack     旧器官物品栈（可能为空）
     * @param newStack     新器官物品栈（可能为空）
     */
    private static void triggerOtherOrganChange(
        ChestCavityData data,
        LivingEntity entity,
        int changedIndex,
        ItemStack oldStack,
        ItemStack newStack
    ) {
        for (int i = 0; i < data.getSlots(); i++) {
            // 跳过变化的器官本身
            if (i == changedIndex) continue;
            ItemStack otherStack = data.getStackInSlot(i);
            if (otherStack.isEmpty()) continue;
            ChestCavitySlotContext otherContext = createContext(data, i, otherStack);
            getOrganCap(otherStack).otherOrganChange(otherContext, changedIndex, oldStack, newStack);
        }
    }

    /**
     * 器官技能
     */
    public static void organSkill(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        getOrganCap(stack).organSkill(createContext(data, index, stack));
    }

    /**
     * 胸腔打开时遍历所有器官
     */
    public static void chestCavityOpen(ChestCavityData data, LivingEntity entity) {
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (!stack.isEmpty()) {
                getOrganCap(stack).chestCavityOpen(createContext(data, i, stack));
            }
        }
    }

    /**
     * 胸腔关闭时遍历所有器官
     */
    public static void chestCavityClose(ChestCavityData data, LivingEntity entity) {
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (!stack.isEmpty()) {
                getOrganCap(stack).chestCavityClose(createContext(data, i, stack));
            }
        }
    }

    /**
     * 遍历所有器官触发受到伤害前回调
     */
    public static void incomingDamage(ChestCavityData data, LivingEntity entity, LivingIncomingDamageEvent event) {
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (!stack.isEmpty()) {
                getOrganCap(stack).incomingDamage(createContext(data, i, stack), event);
            }
        }
    }

    /**
     * 遍历所有器官触发攻击回调
     */
    public static void attack(
        ChestCavityData data,
        LivingEntity entity,
        LivingEntity target,
        DamageSource source,
        DamageContainer damageContainer
    ) {
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (!stack.isEmpty()) {
                getOrganCap(stack).attack(createContext(data, i, stack), target, source, damageContainer);
            }
        }
    }

    /**
     * 遍历所有器官触发受伤回调
     */
    public static void hurt(ChestCavityData data, LivingEntity entity, DamageSource source, DamageContainer damageContainer) {
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (!stack.isEmpty()) {
                getOrganCap(stack).hurt(createContext(data, i, stack), source, damageContainer);
            }
        }
    }

    /**
     * 遍历所有器官触发被治疗回调
     */
    public static void heal(ChestCavityData data, LivingEntity entity, LivingHealEvent event) {
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (!stack.isEmpty()) {
                getOrganCap(stack).heal(createContext(data, i, stack), event);
            }
        }
    }

    /**
     * 遍历所有器官触发被动交互回调
     */
    public static void interact(ChestCavityData data, LivingEntity entity, Player player, InteractionHand hand, PlayerInteractEvent.EntityInteractSpecific event) {
        OrganInteractContext interactContext = new OrganInteractContext(player, hand, event.getPos(), event.getLocalPos());
        Set<Item> skippedTypes = new HashSet<>();
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            Item item = stack.getItem();
            if (skippedTypes.contains(item)) continue;
            getOrganCap(stack).interact(createContext(data, i, stack), interactContext);
            if (interactContext.consumeSkipSameTypeFlag()) {
                skippedTypes.add(item);
            }
            if (interactContext.shouldStopAll()) break;
        }
        // 遍历结束：只要有任意回调标记了 consume，就取消事件 + SUCCESS
        if (interactContext.isConsumed()) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    /**
     * 打开目标的胸腔
     *
     * @param player 打开胸腔的玩家
     * @param entity 被打开的目标
     */
    public static void openChestCavity(Player player, LivingEntity entity) {
        ChestCavitySize size = ChestCavityUtil.getData(entity).getSize();
        player.openMenu(
            new SimpleMenuProvider(
                (containerId, playerInventory, player1) ->
                    new ChestCavityMenu(containerId, playerInventory, size, entity),
                entity.getName()
            ),
            extraData -> {
                extraData.writeEnum(size);
                extraData.writeInt(entity.getId());
            }
        );
    }

    /**
     * 打开自身的胸腔
     *
     * @param player 自身
     */
    public static void openChestCavity(Player player) {
        openChestCavity(player, player);
    }

    /**
     * 为物品附加调整后的PotionContents
     */
    public static ItemStack attachPotionContents(ItemStack stack, Iterable<MobEffectInstance> effects) {
        stack.set(DataComponents.POTION_CONTENTS, calculatePotionContents(effects));
        return stack;
    }

    /**
     * 计算调整后的effects
     */
    public static PotionContents calculatePotionContents(Iterable<MobEffectInstance> effects) {
        List<MobEffectInstance> instances = new ArrayList<>();
        for (MobEffectInstance effect : effects) {
            MobEffectInstance temp = new MobEffectInstance(effect);
            if (!effect.getEffect().value().isInstantenous()) {
                ((IMobEffectInstance) temp).setDuration(duration -> duration / 10, null);
            }
            instances.add(temp);
        }
        return new PotionContents(Optional.empty(), Optional.empty(), instances);
    }

    /**
     * 计算九宫格相邻的 8 个槽位索引，越界自动排除
     *
     * @param slotIndex 当前槽位索引
     * @param maxSlots  胸腔总槽位数（27/36/45/54）
     * // TODO 当 WAIC 迁移使用本方法后移除此标记
     */
    public static List<Integer> getAdjacentSlots(int slotIndex, int maxSlots) {
        List<Integer> result = new ArrayList<>();
        int column = slotIndex % 9;
        int row = slotIndex / 9;
        int maxRow = maxSlots / 9 - 1;
        for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
            for (int columnDelta = -1; columnDelta <= 1; columnDelta++) {
                if (rowDelta == 0 && columnDelta == 0) continue;
                int neighborRow = row + rowDelta;
                int neighborColumn = column + columnDelta;
                if (neighborRow < 0 || neighborRow > maxRow || neighborColumn < 0 || neighborColumn > 8) continue;
                result.add(neighborRow * 9 + neighborColumn);
            }
        }
        return result;
    }

    /**
     * 获取标签下的器官数量，当前器官不在胸时按标签匹配结果计入自身
     * // TODO 当 WAIC 迁移使用本方法后移除此标记
     */
    public static int getOrganCountWithSelf(ChestCavitySlotContext slotContext, TagKey<Item> tag) {
        ChestCavityData data = slotContext.data();
        boolean selfMatches = slotContext.stack().is(tag);
        if (data == null) {
            return selfMatches ? 1 : 0;
        }
        int count = data.getOrganCount(tag);
        if (slotContext.index() < 0 && selfMatches) {
            count++;
        }
        return count;
    }

    /**
     * 获取水平镜像槽位索引（沿中心列翻转：0↔8, 1↔7, 2↔6, 3↔5, 4 不变）
     * <p>
     * 由 WAIC 的 getSymmetricRibIndex 下沉重命名而来。
     * </p>
     * // TODO 当 WAIC 迁移使用本方法后移除此标记
     */
    public static int getMirrorSlotIndex(int slotIndex) {
        int row = slotIndex / 9;
        int column = slotIndex % 9;
        return row * 9 + (8 - column);
    }
}
