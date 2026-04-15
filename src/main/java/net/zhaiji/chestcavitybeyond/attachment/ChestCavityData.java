package net.zhaiji.chestcavitybeyond.attachment;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.AttributeBonus;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.api.task.IChestCavityTask;
import net.zhaiji.chestcavitybeyond.api.task.ISerializableTask;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.manager.DamageSourceManager;
import net.zhaiji.chestcavitybeyond.manager.TaskManager;
import net.zhaiji.chestcavitybeyond.menu.ChestCavityMenu;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ChestCavityData extends ItemStackHandler {
    private final List<IChestCavityTask> tasks = new ArrayList<>();

    /**
     * 选择使用技能的槽位索引
     * <p>
     * 此项仅用于序列化存储，具体使用请看{@code OrganSkillScreen}
     * </p>
     */
    public int selectedSlot = -1;

    /**
     * 剩余氧气缓存
     */
    public double oxygenRemainder;

    private boolean init = false;

    /**
     * 是否需要呼吸
     */
    private boolean needBreath;

    private ChestCavityType type;

    private ChestCavitySize size;

    private LivingEntity owner;

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

    public ChestCavityData(IAttachmentHolder attachmentHolder) {
        // TODO: 此处槽位是硬编码54，但大概率不会改？先写个TODO后续再优化
        super(54);
        if (attachmentHolder instanceof LivingEntity entity) {
            owner = entity;
            type = ChestCavityTypeManager.getType(entity);
            size = type.getSize();
            filtrationPeriod = ChestCavityBeyondConfig.filtrationPeriod;
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
        needBreath = type.getNeedBreath();
        initAttributeModifier();
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
     * 获取当前胸腔容量大小
     */
    public ChestCavitySize getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        resize(ChestCavitySize.bySlots(size));
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        ItemStack oldStack = getStackInSlot(slot);
        super.setStackInSlot(slot, stack);
        ChestCavityUtil.changeOrgan(this, owner, slot, oldStack, stack);
    }

    @Override
    public int getSlots() {
        return size.getSlots();
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack removeStack = super.extractItem(slot, amount, simulate);
        if (!simulate) ChestCavityUtil.changeOrgan(this, owner, slot, removeStack, getStackInSlot(slot));
        return removeStack;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag itemsTag = new ListTag();
        for (int i = 0; i < getSlots(); i++) {
            if (!stacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("slot", i);
                itemsTag.add(stacks.get(i).save(provider, itemTag));
            }
        }
        compoundTag.put("items", itemsTag);

        compoundTag.putInt("selectedSlot", selectedSlot);
        compoundTag.putBoolean("needBreath", needBreath);
        compoundTag.putInt("chestCavitySize", size.getId());
        // 序列化可序列化的tasks
        ListTag tasksList = new ListTag();
        for (IChestCavityTask task : tasks) {
            if (task instanceof ISerializableTask serializableTask) {
                CompoundTag taskTag = new CompoundTag();
                taskTag.putString("type", serializableTask.getType().toString());
                taskTag.put("data", serializableTask.serializeNBT(provider));
                tasksList.add(taskTag);
            }
        }
        compoundTag.put("tasks", tasksList);
        return compoundTag;
    }

    /**
     * 此处进行了重构，需要增加向后兼容
     * <p>
     * 下一个1.4.0版本再改回来
     * </p>
     */
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        stacks.clear();

        // 读取胸腔容量：优先读新字段 chestCavitySize，兜底读旧字段 Size
        if (nbt.contains("chestCavitySize", Tag.TAG_INT)) {
            size = ChestCavitySize.byId(nbt.getInt("chestCavitySize"));
        } else if (nbt.contains("Size", Tag.TAG_INT)) {
            size = ChestCavitySize.bySlots(nbt.getInt("Size"));
        } else {
            size = type != null ? type.getSize() : ChestCavitySize.DEFAULT_SIZE;
        }

        // 读取物品：同时兼容旧格式大写 "Items" 和新格式小写 "items"
        String itemsKey = nbt.contains("items", Tag.TAG_LIST) ? "items" : "Items";
        ListTag tagList = nbt.getList(itemsKey, Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            // 同时兼容旧格式大写 "Slot" 和新格式小写 "slot"
            int slot = itemTags.contains("slot", Tag.TAG_INT) ? itemTags.getInt("slot") : itemTags.getInt("Slot");
            if (slot >= 0 && slot < getSlots()) {
                ItemStack.parse(provider, itemTags).ifPresent(stack -> stacks.set(slot, stack));
            }
        }

        selectedSlot = nbt.getInt("selectedSlot");
        needBreath = nbt.getBoolean("needBreath");

        // 反序列化tasks
        tasks.clear();
        ListTag tasksList = nbt.getList("tasks", Tag.TAG_COMPOUND);
        for (int i = 0; i < tasksList.size(); i++) {
            CompoundTag taskTag = tasksList.getCompound(i);
            ResourceLocation type = ResourceLocation.parse(taskTag.getString("type"));
            CompoundTag data = taskTag.getCompound("data");
            IChestCavityTask task = TaskManager.deserializeTask(this, type, provider, data);
            if (task != null) {
                tasks.add(task);
            }
        }

        onLoad();
    }

    @Override
    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= getSlots()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        init = true;
    }

    /**
     * 仅更新胸腔大小，不做副作用处理（缩小不掉落物品、不关闭菜单等）
     * <p>
     * 因为服务端不往客户端发size，所以需要在打开menu时，使用此方法来让客户端同步size
     * </p>
     */
    public void updateSize(ChestCavitySize newSize) {
        if (size != newSize) {
            size = newSize;
        }
    }

    /**
     * 调整胸腔容量大小
     * 缩小时多余器官先尝试放入玩家背包，放不下再掉落到脚下
     */
    public void resize(ChestCavitySize newSize) {
        if (newSize == size || !owner.isAlive()) return;

        int oldSlots = size.getSlots();
        int newSlots = newSize.getSlots();

        if (newSlots < oldSlots) {
            // 缩小：处理多余器官
            List<ItemStack> excess = new ArrayList<>();
            for (int i = newSlots; i < oldSlots; i++) {
                ItemStack stack = getStackInSlot(i);
                if (!stack.isEmpty()) {
                    excess.add(stack.copy());
                    ChestCavityUtil.changeOrgan(this, owner, i, stack, ItemStack.EMPTY);
                }
                stacks.set(i, ItemStack.EMPTY);
            }
            // 尝试放入玩家背包
            if (owner instanceof Player player) {
                for (ItemStack stack : excess) {
                    if (!player.getInventory().add(stack)) {
                        player.drop(stack, false);
                    }
                }
            } else {
                // 非玩家实体，直接掉落
                for (ItemStack stack : excess) {
                    owner.spawnAtLocation(stack);
                }
            }
        }

        updateSize(newSize);
        OrganAttributeUtil.updateScale(this, owner);

        // 关闭后重新打开本实体胸腔的 GUI，更新布局
        if (owner.level() instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.players()) {
                if (player.containerMenu instanceof ChestCavityMenu menu && menu.getData() == this) {
                    player.closeContainer();
                    ChestCavityUtil.openChestCavity(player, owner);
                }
            }
        }
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
     * @return 是否需要呼吸
     */
    public boolean isNeedBreath() {
        return needBreath;
    }

    /**
     * 是否拥有某个器官
     */
    public boolean hasOrgan(Item item) {
        for (int i = 0; i < getSlots(); i++) {
            if (stacks.get(i).is(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOrgan(ItemStack stack) {
        for (int i = 0; i < getSlots(); i++) {
            if (ItemStack.isSameItemSameComponents(stacks.get(i), stack)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOrgan(TagKey<Item> tag) {
        for (int i = 0; i < getSlots(); i++) {
            if (stacks.get(i).is(tag)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOrgan(Predicate<ItemStack> predicate) {
        for (int i = 0; i < getSlots(); i++) {
            if (predicate.test(stacks.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得同类器官在胸腔内的数量
     */
    public int getOrganCount(Item item) {
        int count = 0;
        for (int i = 0; i < getSlots(); i++) {
            if (stacks.get(i).is(item)) {
                count++;
            }
        }
        return count;
    }

    public int getOrganCount(ItemStack stack) {
        int count = 0;
        for (int i = 0; i < getSlots(); i++) {
            if (ItemStack.isSameItemSameComponents(stacks.get(i), stack)) {
                count++;
            }
        }
        return count;
    }

    public int getOrganCount(TagKey<Item> tag) {
        int count = 0;
        for (int i = 0; i < getSlots(); i++) {
            if (stacks.get(i).is(tag)) {
                count++;
            }
        }
        return count;
    }

    public int getOrganCount(Predicate<ItemStack> predicate) {
        int count = 0;
        for (int i = 0; i < getSlots(); i++) {
            if (predicate.test(stacks.get(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取第一个匹配的器官
     */
    public Optional<ItemStack> getFirstOrgan(Item item) {
        for (int i = 0; i < getSlots(); i++) {
            if (stacks.get(i).getItem() == item) {
                return Optional.of(stacks.get(i));
            }
        }
        return Optional.empty();
    }

    public Optional<ItemStack> getFirstOrgan(ItemStack stack) {
        for (int i = 0; i < getSlots(); i++) {
            if (ItemStack.isSameItemSameComponents(stacks.get(i), stack)) {
                return Optional.of(stacks.get(i));
            }
        }
        return Optional.empty();
    }

    public Optional<ItemStack> getFirstOrgan(TagKey<Item> tag) {
        for (int i = 0; i < getSlots(); i++) {
            if (stacks.get(i).is(tag)) {
                return Optional.of(stacks.get(i));
            }
        }
        return Optional.empty();
    }

    public Optional<ItemStack> getFirstOrgan(Predicate<ItemStack> predicate) {
        for (int i = 0; i < getSlots(); i++) {
            if (predicate.test(stacks.get(i))) {
                return Optional.of(stacks.get(i));
            }
        }
        return Optional.empty();
    }

    /**
     * 根据指定条件筛选器官
     *
     * @param predicate 筛选条件
     * @return 匹配的器官列表
     */
    public List<ItemStack> filterOrgans(Predicate<ItemStack> predicate) {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < getSlots(); i++) {
            if (predicate.test(stacks.get(i))) {
                list.add(stacks.get(i));
            }
        }
        return list;
    }

    /**
     * 客户端同步用
     */
    public void sync(SyncChestCavityDataPacket packet) {
        this.size = packet.size();
        NonNullList<ItemStack> organs = packet.organs();
        stacks.clear();
        for (int i = 0; i < organs.size(); i++) {
            stacks.set(i, organs.get(i));
        }
    }

    /**
     * 初始化修饰符
     */
    public void initAttributeModifier() {
        // 应用胸腔类型的默认属性加成
        for (AttributeBonus bonus : type.getTypeDefaultBonuses()) {
            OrganAttributeUtil.updateAttributeModifier(owner, bonus.attribute(), bonus.create(ChestCavityBeyond.of("type_default")));
        }

        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack.isEmpty()) continue;
            // 器官基础属性
            OrganAttributeUtil.updateOrganAttributeModifier(this, owner, i, ItemStack.EMPTY, stack);
            // 器官补偿属性
            for (AttributeBonus bonus : type.getAttributeBonuses(stack.getItem())) {
                OrganAttributeUtil.updateAttributeModifier(owner, bonus.attribute(), bonus.create(ChestCavityUtil.getSlotId(i)));
            }
        }
        OrganAttributeUtil.updateDefaultModifier(this, owner);
        OrganAttributeUtil.updateScale(this, owner);
    }

    /**
     * 器官tick
     */
    public void tick() {
        if (!owner.level().isClientSide()) {
            applyHealth();
            applyFiltration();
            for (int i = 0; i < getSlots(); i++) {
                ItemStack stack = stacks.get(i);
                ChestCavityUtil.organTick(this, owner, i, stack);
            }
        }
        // 客户端和服务端都要执行task
        Iterator<IChestCavityTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            IChestCavityTask task = iterator.next();
            if (task.canRemove(owner)) {
                task.onRemoved(owner);
                iterator.remove();
            } else {
                task.tick(owner);
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
        task.onAdded(owner);
    }

    /**
     * 获取胸腔任务列表
     *
     * @return 胸腔任务列表
     */
    public List<IChestCavityTask> getTasks() {
        return tasks;
    }

    /**
     * 检查是否存在符合条件的任务
     *
     * @param predicate 匹配条件
     * @return 是否存在符合条件的任务
     */
    public boolean hasTaskIf(Predicate<IChestCavityTask> predicate) {
        for (IChestCavityTask task : tasks) {
            if (predicate.test(task)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取第一个符合条件的任务
     *
     * @param predicate 匹配条件
     * @return 第一个符合条件的任务
     */
    public Optional<IChestCavityTask> getFirstTaskIf(Predicate<IChestCavityTask> predicate) {
        for (IChestCavityTask task : tasks) {
            if (predicate.test(task)) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    /**
     * 删除第一个符合条件的任务
     *
     * @param predicate 匹配条件
     * @return 是否成功删除
     */
    public boolean removeTaskIf(Predicate<IChestCavityTask> predicate) {
        Iterator<IChestCavityTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            IChestCavityTask task = iterator.next();
            if (predicate.test(task)) {
                task.onRemoved(owner);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * 删除指定的任务实例
     *
     * @param task 要删除的任务
     * @return 是否成功删除
     */
    public boolean removeTask(IChestCavityTask task) {
        Iterator<IChestCavityTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            IChestCavityTask current = iterator.next();
            if (current == task) {
                task.onRemoved(owner);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * 删除所有符合条件的任务
     *
     * @param predicate 匹配条件
     * @return 删除的任务数量
     */
    public int removeTasks(Predicate<IChestCavityTask> predicate) {
        int removed = 0;
        Iterator<IChestCavityTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            IChestCavityTask task = iterator.next();
            if (predicate.test(task)) {
                task.onRemoved(owner);
                iterator.remove();
                removed++;
            }
        }
        return removed;
    }

    /**
     * 清空所有任务
     */
    public void clearTasks() {
        Iterator<IChestCavityTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            IChestCavityTask task = iterator.next();
            task.onRemoved(owner);
            iterator.remove();
        }
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
            int amplifier = Math.max(0, (int) Math.floor(-filtration / 2));
            owner.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (30 * MathUtil.getInverseScale(filtration)), amplifier));
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
}
