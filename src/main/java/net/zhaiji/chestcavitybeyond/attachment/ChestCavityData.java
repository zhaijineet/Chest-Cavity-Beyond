package net.zhaiji.chestcavitybeyond.attachment;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityManager;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ChestCavityData extends ItemStackHandler {
    /**
     * 剩余氧气缓存
     */
    public double oxygenRemainder;

    /**
     * 剩余新陈代谢缓存
     */
    public double metabolismRemainder;

    /**
     * 由于player的特殊性，每次进入游戏都会触发初始化，所以需要加个初始化标记
     */
    private boolean init = false;

    private ChestCavityType type;

    private @Nullable LivingEntity owner;
    /**
     * 血液过滤周期
     */
    private int filtrationPeriod;

    /**
     * 血液过滤周期偏移量
     * <p>
     * 随机分布，防止集中判断
     * </P>
     */
    private int filtrationTickOffset;

    public ChestCavityData(IAttachmentHolder attachmentHolder, int count) {
        super(count);
        if (attachmentHolder instanceof LivingEntity entity) {
            this.owner = entity;
            // TODO 解决type为null的问题
            type = ChestCavityManager.getType(entity);
            // TODO 将周期写入配置
            filtrationPeriod = 60;
            filtrationTickOffset = entity.level().getRandom().nextInt(filtrationPeriod);
        }
    }

    /**
     * 初始化器官
     */
    public void init() {
        if (init) return;
        // TODO 解决type为null的问题
        if (type == null) return;
        NonNullList<Item> organs = type.getOrgans();
        for (int i = 0; i < getSlots(); i++) {
            setStackInSlot(i, organs.get(i).getDefaultInstance());
        }
        init = true;
    }

    public void tick() {
        applyFiltration();
    }

    /**
     * 添加或更新器官属性修饰符
     */
    public void updateOrganAttribute(ItemStack oldStack, ItemStack newStack) {
        if (oldStack.equals(newStack)) return;
        Multimap<Holder<Attribute>, AttributeModifier> removeModifiers = ChestCavityUtil.getAttributeModifiers(oldStack);
        Multimap<Holder<Attribute>, AttributeModifier> addModifiers = ChestCavityUtil.getAttributeModifiers(newStack);
        if (!removeModifiers.isEmpty()) {
            for (Holder<Attribute> attribute : removeModifiers.keySet()) {
                Collection<AttributeModifier> modifiers = removeModifiers.get(attribute);
                OrganAttributeUtil.removeModifier(owner, attribute, modifiers, AttributeModifier.Operation.ADD_VALUE);
                OrganAttributeUtil.removeModifier(owner, attribute, modifiers, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                OrganAttributeUtil.removeModifier(owner, attribute, modifiers, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            }
        }
        if (!addModifiers.isEmpty()) {
            for (Holder<Attribute> attribute : addModifiers.keySet()) {
                Collection<AttributeModifier> modifiers = addModifiers.get(attribute);
                OrganAttributeUtil.addModifier(owner, attribute, modifiers, AttributeModifier.Operation.ADD_VALUE);
                OrganAttributeUtil.addModifier(owner, attribute, modifiers, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                OrganAttributeUtil.addModifier(owner, attribute, modifiers, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            }
        }

        OrganAttributeUtil.updateHealth(owner);
        // TODO 等我把器官搞完了来
//        OrganAttributeUtil.updateNerves(owner);
    }

    /**
     * 添加或更新属性修饰符
     *
     * @param attribute 属性
     * @param modifier  修饰符
     */
    public void updateAttribute(Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = owner.getAttribute(attribute);
        if (instance != null) {
            instance.addOrReplacePermanentModifier(modifier);
        }
    }

    /**
     * 根据{@link InitAttribute#FILTRATION}属性，应用血液过滤效果
     *
     * <pre>
     *  filtration |  duration
     * ------------|--------------
     *    0.0      | 30 (1.5s)
     *   -0.5      | 38 (1.9s)
     *   -1.0      | 49 (2.45s)
     *   -1.5      | 63 (3.15s)
     *   -2.0      | 81 (4.05s)
     *   -2.5      | 104 (5.2s)
     *   -3.0      | 134 (6.7s)
     *   -3.5      | 172 (8.6s)
     *   -4.0      | 221 (11.05s)
     * </pre>
     */
    public void applyFiltration() {
        double filtration = getDifferenceValue(InitAttribute.FILTRATION);
        if (filtration < 0 && owner.tickCount % filtrationPeriod == filtrationTickOffset) {
            owner.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (30 * MathUtil.getInverseExpScale(filtration))));
        }
    }

    /**
     * 获取胸腔类型
     *
     * @return 胸腔类型
     */
    public ChestCavityType getType() {
        return type;
    }

    /**
     * 获取胸腔主人
     *
     * @return 胸腔主人
     */
    public @Nullable LivingEntity getOwner() {
        return owner;
    }

    /**
     * 获取当前属性和默认属性的差值
     *
     * @param attribute 属性
     * @return 当前属性和默认属性的差值
     */
    public double getDifferenceValue(Holder<Attribute> attribute) {
        return getCurrentValue(attribute) - getDefaultValue(attribute);
    }

    /**
     * 获取默认属性值
     *
     * @param attribute 属性
     * @return 默认属性值
     */
    public double getDefaultValue(Holder<Attribute> attribute) {
        // TODO 解决type为null的问题
        if (type == null) return 0;
        return type.getDefaultAttributes(owner.getType()).getOrDefault(attribute, 0D);
    }

    /**
     * 获取当前属性值
     *
     * @param attribute 属性
     * @return 当前属性值
     */
    public double getCurrentValue(Holder<Attribute> attribute) {
        AttributeInstance instance = owner.getAttribute(attribute);
        if (instance != null) {
            return instance.getValue();
        }
        return 0;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        init = true;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        ItemStack oldStack = getStackInSlot(slot).copy();
        super.setStackInSlot(slot, stack);
        updateOrganAttribute(oldStack, stack);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack removeStack = super.extractItem(slot, amount, simulate);
        if (!simulate) {
            updateOrganAttribute(removeStack, getStackInSlot(slot));
        }
        return removeStack;
    }
}
