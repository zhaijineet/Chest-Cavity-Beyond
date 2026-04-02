# 更新日志 (Changelog)

本文档记录了 Chest Cavity Beyond 所有版本的更改。

## 1.2.0

- 修复了服务端启动时因客户端包处理器引用客户端类导致的崩溃
- 调整龙器官属性数值（属性全面提升至 5，击退抗性降低至 1，结晶化不变）
- 幽匿心脏：健康 2 → 3，幽匿核心：健康 4 → 5，击退抗性降低至 1


## 1.1.9 fix

- 修复了熔炉之力的除零错误

## 1.1.9

- 彩蛋更新

## 1.1.8

- `ChestCavityData` Task API 重命名：`hasTask` → `hasTaskIf`、`getFirstTask` → `getFirstTaskIf`、`removeTask(Predicate)` → `removeTaskIf`，消除与 `removeTask(IChestCavityTask)` 的 lambda 歧义
- `organ_loss` 和 `open_chest` 伤害类型新增玩家击杀消息
- 调整 `golem_core` 和 `golem_cable` 器官属性数值

## 1.1.7

- `cooldown` 支持动态冷却，可根据上下文动态决定冷却时间
- 器官 tooltip 系统新增槽位索引（index）参数，表示器官在胸腔中的槽位索引（-1 表示不在胸腔中）

## 1.1.6

- 新增 `heal` 器官回调：在 `LivingHealEvent` 中触发

## 1.1.5

- 重构 Mixin 业务逻辑，将逻辑从 Mixin 类中提取到独立工具类：新增 `MixinUtil`和 `MixinClientUtil`
- `Organ.Builder` 支持通过 `Function<Item.Properties, Item>` 构造自定义 Item 子类
- `otherOrganChange` 回调时机优化：统一在 `setStackInSlot` 中触发（属性更新之后），不再在 `organAdded`/`organRemoved` 中分别触发

## 1.1.4

- 改进 `otherOrganChange` 器官回调：参数从 `(ItemStack, boolean isAdded)` 改为 `(ItemStack oldStack, ItemStack newStack)`，支持检测器官替换操作
- `ChestCavityData` 新增 `getFirstOrgan()` 方法
- `ChestCavityData` 新增 Task 管理方法：`hasTask()`、`getFirstTask()`、`removeTask()`、`removeTasks()`、`clearTasks()`

## 1.1.3

- 新增 `otherOrganChange` 器官回调：当胸腔内其他器官被添加或移除时触发
- 新增 `organSkillOnCooldown` 器官回调：当器官技能处于冷却状态时触发
- 新增 `OtherOrganChangeConsumer` 函数式接口到 `api.function` 包
- Builder 新增 `otherChange(OtherOrganChangeConsumer)` 和 `skillOnCooldown()` 构建方法
- `IOrgan` 接口新增 `otherOrganChange(ChestCavitySlotContext, ItemStack, int, boolean)` 方法
- `IOrgan` 接口新增 `organSkillOnCooldown(ChestCavitySlotContext)` 方法

## 1.1.2

- 重构器官构建系统：`OrganBuilder` 合并为 `Organ.Builder`
- 修复腐食消化和熔炉力量效果机制
- 优化攻击事件处理逻辑

## 1.1.1_fix

- 修复了默认不可开胸的bug（原 `unopenable` 默认返回 `true` 导致所有实体默认不可开胸）
- API优化：`ChestCavityType` 的开胸判断方法从 `unopenable/isUnopenable/setUnopenable` 重命名为 `canOpen/canOpen/setCanOpen`，逻辑取反，命名更直观

## 1.1.1

- 改进了胸腔类型的不可开胸判断机制，现在支持根据玩家和实体动态判断是否可开胸
- 修复了无限持续时间药效可以被修改的问题
- 新增类型安全的器官属性修饰符 API（`addValueAttribute`、`baseMultipliedAttribute`、`totalMultipliedAttribute`）
- 重构了器官定义系统，简化了 `OrganBuilder` 使用方式
- 新增 `AttributeEntry` 和 `OrganModifierConsumer` 接口
- `ChestCavityData` 新增 `getOrganCount` 方法，支持多种方式统计器官数量
- `ChestCavityUtil` 新增 `isOrgan` 方法，用于检测物品是否为器官

## 1.1.0

- 完善了可序列化任务系统，新增 `ChestCavityData` 参数，反序列化时可以访问胸腔数据上下文

## 1.0.9

- 创造模式标签页新增附魔书
- 新增可序列化任务系统（`ISerializableTask`、`TaskManager`）

## 1.0.8

- 新增了 `OrganChangeEvent` 事件，当胸腔中的器官被添加、移除或替换时触发
- 新增了 `chestCavityOpen` 和 `chestCavityClose` 器官回调方法，在胸腔界面打开/关闭时触发
- 重构了 `incomingDamage` 器官方法，现在使用 `LivingIncomingDamageEvent` 事件代替 `DamageSource` 和 `DamageContainer`

## 1.0.7

- 新增了 `incomingDamage` 器官方法，在 `LivingIncomingDamageEvent` 事件中触发
- 优化了器官技能冷却系统，通过 `cooldown()` 方法统一设置冷却时间

## 1.0.6

- 新增了 OwnableEntity 所有者开胸功能，玩家可以直接打开自己驯服的实体的胸腔（如狼、猫等）
- 修改了 SAFE_SURGERY 附魔，使其也影响已驯服的实体开胸

## 1.0.5

- 新增了胸腔类型默认属性加成功能
- 新增了胸腔类型不可开胸控制，可为特定生物（如多阶段 Boss）设置不可开胸，并支持自定义消息提示

## 1.0.4

- 修复了苦力怕不能移动的错误

## 1.0.3

- 修复了骨头能够合成腐肉的合成表错误
- 修复了生物胸腔内有门时，开胸依旧会造成伤害的错误
- 新增了生物胸腔内有门时，关闭胸腔菜单会播放箱子关闭音效的功能

## 1.0.2

- 新增了基本的胸腔类型推断功能，能够识别一些未注册胸腔类型的实体

## 1.0.1

- 将最低可开胸的生命值要求从 20% 提升至 30%
- 从村民可交易列表中移除开胸器附魔
