package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.capability.IOrgan;
import net.zhaiji.chestcavitybeyond.api.event.OrganChangeEvent;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.manager.CapabilityManager;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;
import net.zhaiji.chestcavitybeyond.manager.OrganManager;
import net.zhaiji.chestcavitybeyond.menu.ChestCavityMenu;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.register.InitAttachmentType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
     * 创建胸腔槽位上下文
     *
     * @param data   胸腔数据 (可能为null)
     * @param entity 胸腔主人 (可能为null)
     * @param index  位置索引
     * @param stack  对应物品
     * @return 胸腔槽位上下文
     */
    public static ChestCavitySlotContext createContext(
        @Nullable ChestCavityData data,
        @Nullable LivingEntity entity,
        int index,
        ItemStack stack
    ) {
        return new ChestCavitySlotContext(data, entity, getSlotId(index), index, stack);
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
    }

    /**
     * 器官tick
     */
    public static void organTick(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        getOrganCap(stack).tick(createContext(data, entity, index, stack));
    }

    /**
     * 器官移植时
     */
    public static void organAdded(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        ChestCavitySlotContext changedContext = createContext(data, entity, index, stack);
        getOrganCap(stack).organAdded(changedContext);
    }

    /**
     * 器官摘除时
     */
    public static void organRemoved(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        ChestCavitySlotContext changedContext = createContext(data, entity, index, stack);
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
            ChestCavitySlotContext otherContext = createContext(data, entity, i, otherStack);
            getOrganCap(otherStack).otherOrganChange(otherContext, changedIndex, oldStack, newStack);
        }
    }

    /**
     * 器官技能
     */
    public static void organSkill(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        getOrganCap(stack).organSkill(createContext(data, entity, index, stack));
    }

    /**
     * 胸腔打开时遍历所有器官
     */
    public static void chestCavityOpen(ChestCavityData data, LivingEntity entity) {
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (!stack.isEmpty()) {
                getOrganCap(stack).chestCavityOpen(createContext(data, entity, i, stack));
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
                getOrganCap(stack).chestCavityClose(createContext(data, entity, i, stack));
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
                getOrganCap(stack).incomingDamage(createContext(data, entity, i, stack), event);
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
                getOrganCap(stack).attack(createContext(data, entity, i, stack), target, source, damageContainer);
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
                getOrganCap(stack).hurt(createContext(data, entity, i, stack), source, damageContainer);
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
                getOrganCap(stack).heal(createContext(data, entity, i, stack), event);
            }
        }
    }

    /**
     * 打开目标的胸腔
     *
     * @param player 打开胸腔的玩家
     * @param entity 被打开的目标
     */
    public static void openChestCavity(Player player, LivingEntity entity) {
        player.openMenu(
            new SimpleMenuProvider(
                (containerId, playerInventory, player1) ->
                    new ChestCavityMenu(containerId, playerInventory, entity),
                entity.getName()
            ),
            (extraData) -> extraData.writeInt(entity.getId())
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
                ((IMobEffectInstance) temp).setDuration(duration -> duration / 10);
            }
            instances.add(temp);
        }
        return new PotionContents(Optional.empty(), Optional.empty(), instances);
    }
}
