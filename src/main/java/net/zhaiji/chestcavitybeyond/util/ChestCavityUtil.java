package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.capability.IOrgan;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.builder.OrganBuilder;
import net.zhaiji.chestcavitybeyond.manager.CapabilityManager;
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
        return Objects.requireNonNullElse(stack.getCapability(CapabilityManager.ORGAN), OrganBuilder.EMPTY_ORGAN);
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
    public static ChestCavitySlotContext createContext(@Nullable ChestCavityData data, @Nullable LivingEntity entity, int index, ItemStack stack) {
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
        getOrganCap(stack).organAdded(createContext(data, entity, index, stack));
    }

    /**
     * 器官摘除时
     */
    public static void organRemoved(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        getOrganCap(stack).organRemoved(createContext(data, entity, index, stack));
    }

    /**
     * 器官技能
     */
    public static void organSkill(ChestCavityData data, LivingEntity entity, int index, ItemStack stack) {
        if (stack.isEmpty()) return;
        getOrganCap(stack).organSkill(createContext(data, entity, index, stack));
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
                ((IMobEffectInstance) temp).setDuration(temp.mapDuration(duration -> duration / 10));
            }
            instances.add(temp);
        }
        return new PotionContents(Optional.empty(), Optional.empty(), instances);
    }
}
