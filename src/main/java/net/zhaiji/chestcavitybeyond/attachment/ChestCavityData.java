package net.zhaiji.chestcavitybeyond.attachment;

import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
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

public class ChestCavityData extends ItemStackHandler {
    /**
     * 剩余氧气缓存
     */
    public double oxygenRemainder;

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
        NonNullList<Item> organs = type.getOrgans();
        for (int i = 0; i < getSlots(); i++) {
            setStackInSlot(i, organs.get(i).getDefaultInstance());
        }
        init = true;
    }

    /**
     * 重置修饰符
     */
    public void resetAttributeModifier() {
        for (int i = 0; i < getSlots(); i++) {
            OrganAttributeUtil.updateOrganAttributeModifier(this, owner, i, ItemStack.EMPTY, getStackInSlot(i));
        }
    }

    /**
     * 器官tick
     */
    public void tick() {
        applyHealth();
        applyFiltration();
        applyHydrophobia();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            ChestCavityUtil.organTick(this, owner, i, stack);
        }
    }

    /**
     * 如果健康小于等于0，就会持续受伤
     */
    private void applyHealth() {
        double health = getCurrentValue(InitAttribute.HEALTH);
        if (health <= 0) {
            // TODO 还没把器官属性缺失伤害类型注册
            // 这里用凋零类型测试
            owner.hurt(owner.damageSources().wither(), 1);
        }
    }

    /**
     * 应用血液过滤效果
     *
     * <pre>
     *  filtration |  duration
     * ------------|--------------
     *    0.0      | 30 (1.5s)
     *   -0.5      | 36 (1.8s)
     *   -1.0      | 51 (2.55s)
     *   -1.5      | 71 (3.55s)
     *   -2.0      | 96 (4.8s)
     *   -2.5      | 124 (6.2s)
     *   -3.0      | 155 (7.75s)
     *   -3.5      | 188 (9.4s)
     *   -4.0      | 223 (11.15s)
     * </pre>
     */
    private void applyFiltration() {
        double filtration = getDifferenceValue(InitAttribute.FILTRATION);
        if (filtration < 0 && owner.tickCount % filtrationPeriod == filtrationTickOffset && !(owner instanceof Player player && player.getAbilities().invulnerable)) {
            owner.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (30 * MathUtil.getInverseScale(filtration))));
        }
    }

    /**
     * 应用恐水效果
     */
    private void applyHydrophobia() {
        //TODO
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
        OrganAttributeUtil.updateOrganAttributeModifier(this, owner, slot, oldStack, stack);
        ChestCavityUtil.organRemoved(this, owner, slot, oldStack);
        ChestCavityUtil.organAdded(this, owner, slot, stack);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack removeStack = super.extractItem(slot, amount, simulate);
        if (!simulate) {
            ItemStack addStack = getStackInSlot(slot);
            OrganAttributeUtil.updateOrganAttributeModifier(this, owner, slot, removeStack, addStack);
            ChestCavityUtil.organRemoved(this, owner, slot, removeStack);
            ChestCavityUtil.organAdded(this, owner, slot, addStack);
        }
        return removeStack;
    }
}
