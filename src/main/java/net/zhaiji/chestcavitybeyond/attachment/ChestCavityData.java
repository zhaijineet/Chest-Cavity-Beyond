package net.zhaiji.chestcavitybeyond.attachment;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
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
import net.zhaiji.chestcavitybeyond.api.task.IChestCavityTask;
import net.zhaiji.chestcavitybeyond.client.screen.OrganSkillScreen;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityManager;
import net.zhaiji.chestcavitybeyond.manager.DamageSourceManager;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ChestCavityData extends ItemStackHandler {
    /**
     * 选择使用技能的槽位索引
     * <p>
     * 此项仅用于序列化存储，具体使用请看{@link OrganSkillScreen}
     * </p>
     */
    public int selectedSlot = -1;

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

    private List<IChestCavityTask> tasks = new ArrayList<>();

    public ChestCavityData(IAttachmentHolder attachmentHolder) {
        super(27);
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
            stacks.set(i, organs.get(i).getDefaultInstance());
        }
        resetAttributeModifier();
        init = true;
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
     * 获取所有器官
     *
     * @return 器官列表
     */
    public NonNullList<ItemStack> getOrgans() {
        return stacks;
    }

    /**
     * 是否拥有某个器官
     */
    public boolean hasOrgan(Item item) {
        for (ItemStack organ : stacks) {
            if (organ.is(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOrgan(ItemStack stack) {
        for (ItemStack organ : stacks) {
            if (!organ.isEmpty() && ItemStack.isSameItemSameComponents(organ, stack)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOrgan(TagKey<Item> tag) {
        for (ItemStack organ : stacks) {
            if (!organ.isEmpty() && organ.is(tag)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOrgan(Predicate<ItemStack> predicate) {
        for (ItemStack organ : stacks) {
            if (predicate.test(organ)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 客户端同步用
     */
    public void sync(SyncChestCavityDataPacket packet) {
        NonNullList<ItemStack> organs = packet.organs();
        for (int i = 0; i < organs.size(); i++) {
            setStackInSlot(i, organs.get(i));
        }
    }

    /**
     * 重置修饰符
     */
    public void resetAttributeModifier() {
        for (int i = 0; i < getSlots(); i++) {
            OrganAttributeUtil.updateOrganAttributeModifier(this, owner, i, ItemStack.EMPTY, getStackInSlot(i));
        }
        OrganAttributeUtil.updateDefaultModifier(this, owner);
    }

    /**
     * 器官tick
     */
    public void tick() {
        applyHealth();
        applyFiltration();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            ChestCavityUtil.organTick(this, owner, i, stack);
        }
        Iterator<IChestCavityTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            IChestCavityTask task = iterator.next();
            if (task.canRemove()) {
                task.onRemoved();
                iterator.remove();
            } else {
                task.tick();
            }
        }
    }

    /**
     * 添加胸腔任务
     *
     * @param task 胸腔任务
     */
    public void addTask(IChestCavityTask task) {
        tasks.add(task);
    }

    /**
     * 如果健康小于等于0，就会持续受伤
     */
    private void applyHealth() {
        double health = getCurrentValue(InitAttribute.HEALTH);
        if (health <= 0) {
            owner.hurt(DamageSourceManager.organLoss(owner.level()), 2);
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
        ItemStack oldStack = getStackInSlot(slot);
        super.setStackInSlot(slot, stack);
        ChestCavityUtil.changeOrgan(this, owner, slot, oldStack, stack);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack removeStack = super.extractItem(slot, amount, simulate);
        if (!simulate) ChestCavityUtil.changeOrgan(this, owner, slot, removeStack, getStackInSlot(slot));
        return removeStack;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = super.serializeNBT(provider);
        compoundTag.putInt("selectedSlot", selectedSlot);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        super.deserializeNBT(provider, nbt);
        selectedSlot = nbt.getInt("selectedSlot");
    }
}
