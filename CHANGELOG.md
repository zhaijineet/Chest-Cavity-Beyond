# 更新日志 (Changelog)

本文档记录了 Chest Cavity Beyond 所有版本的更改。

## 1.8.1 Tooltip 动态公式展示与管线签名重构

- 新增 DynamicValues / FormulaValue，支持按 Ctrl 展开效果计算公式
- Tooltip 管线统一使用 ChestCavitySlotContext 替代 (data, index, stack) 三元组
- TooltipsKeyContext 由 Shift/Sprint 改为 Shift/Ctrl，改用 Screen.hasShiftDown/hasControlDown
- OrganTooltip.Builder 新增 dynamicPassiveEffect / dynamicActiveSkill / hint，按需自动挂载 Ctrl 提示
- TooltipUtil 新增 dynamicEffectLines 及 formulaOperator/attributeName/tagName/tagOrganCountName 辅助方法
- ChestCavityUtil 新增 getAdjacentSlots / getOrganCountWithSelf / getMirrorSlotIndex（WAIC 迁移预备）
- 语言文件：使用 %s 占位符合并 hint 三段拆分，新增 formula.tag_organ_count

## 1.8.0

- 新增宝藏附魔原始回归：器官属性 × 1.5，通过考古可疑方块 25% 概率获取附魔书
- 新增 AppleSkin 兼容：食物显示值与回血估算按本模组消化/新陈代谢/耐力实际计算
- 重构新陈代谢机制，值过高导致的击穿原本18饥饿值的保护下限的bug已修正
- 修复盾牌格挡时仍触发末影弹射物闪避的问题
- 打开自身胸腔界面时 tooltip 数据来源修正

## 1.7.1

- 修复生物因自伤将自身设为目标，导致不断攻击自己的问题
- 改进器官投射物发射点，现在会根据生物体型自动缩放，小型生物和大型生物的弹道起点更加合理
- 修复部分两栖类生物被误判为水生生物，导致在陆地上窒息的问题
- 新增对 Ribbits 模组生物的胸腔类型支持

## 1.7.0

- 装备器官的生物现在能自动施放器官技能
- 部分器官新增被动交互效果
- 新增生物器官技能相关配置项

## 1.6.2

- 新增 TargetResolver，将射线命中的子部件解析为父 LivingEntity
- 同时拦截 interact 和 interactAt 两条交互路径，确保 Item.use() 正常触发
- 开放 ChestCavityRegisterEvent#registerTargetResolver 供第三方模组注册
- JEI 催化剂改为遍历 chest_openers tag，支持所有开胸器变体
- JEI 胸腔槽位从 InputSlot 改为 OutputSlot

## 1.6.1

- 修复 JEI 胸腔类型页面滚动翻页检测逻辑，不再在非边界处误翻页

## 1.6.0

- 新增 JEI 兼容，提供胸腔类型信息页面插件
- 守卫者之眼、远古守卫者之眼新增方块交互范围属性，并且远古守卫者之眼的属性提升为原来的2倍
- `AttributeDisplay` 新增 `valueEffect` 实时效果描述
- `addConversion` 器官转换规则按目标胸腔类型隔离

## 1.5.0

- 新增器官**炼金腺**（Alchemist Gland）：可存储药水效果，激活时将存储的药水效果施加给自身
- 修复属性同步问题：此前属性同步仅在器官替换时触发，现在任何关联属性变更都会自动同步

## 1.4.11

- 新增冰霜抗性阶梯免疫机制：根据冰霜抗性数值免疫冻结伤害并清除冰冻进度，阈值可配置（`frostImmunity`）
- `minChestOpenMaxHealth` 配置最大值上限从 40 提升至 Integer.MAX_VALUE
- 新增 `chestOpenBaseHealthRatio` 配置：超出直接开胸阈值时，目标当前生命值需低于最大生命值的该比例才能开胸（默认 0.3）

## 1.4.10

- 新增液压钳附魔：开胸时消耗目标胸甲耐久（每级 25 点），若胸甲被破坏则可正常开胸
- 新增火焰抗性阶梯免疫机制：根据火焰抗性数值分级免疫热方块/火焰/岩浆伤害，阈值可配置

## 1.4.9

- 修复跳跃属性安全摔落距离比例失衡问题：改为基于实际跳跃高度按原版比例同步缩放(2.4倍)
- 削弱史莱姆核心：移除普通消化和营养属性
- 削弱粘液胃：移除普通消化和营养属性，腐食营养 4→2
- 削弱熔岩胃：火焰抗性 4→3，消化 3→2，营养 3→2

## 1.4.8

- 修复默认器官配平机制对 `ADD_MULTIPLIED_TOTAL`（最终乘算）不生效的 Bug：补偿修饰符改为按操作类型分别生成（`default_value`、`default_base`、`default_total`），确保乘算类器官的配平正确抵消
- 新增非玩家实体器官冷却机制（`OrganCooldownTask`）：非玩家实体现在也能正确使用器官技能冷却

## 1.4.7

- 新增通用配置 `chestplateBlocksChestOpener`：控制胸甲是否阻挡开胸器打开胸腔（默认开启）

## 1.4.6

- 修复其他模组添加的实体子类（如矿石苦力怕）因无法匹配胸腔类型而丢失功能的问题：`ChestCavityTypeManager.getType()` 的回退逻辑新增 17 种 `instanceof` 检测，覆盖多种实体
- 创造模式玩家使用开胸器时跳过 `canOpen` 和胸甲阻挡检查，不再受到限制
- `AttributeDisplay` 属性显示信息重构：`showWhenZero` 替换为 `hideValue`（通用隐藏值）+ `percentageDisplay`（百分比格式显示），Builder 支持自动推断 `PercentageAttribute` 和默认隐藏值
- 开胸器交互取消判断从 `ItemStack.is(Item)` 改为 `ItemStack.is(TagKey)`，使其他模组通过 `chest_openers` 标签添加的开胸器物品也能正确触发交互取消

## 1.4.5

- 胸腔类型注册名从 `String` 改为 `ResourceLocation`，避免不同模组之间的命名冲突，与 Minecraft 注册体系保持一致
- 胸腔类型新增 `setFourthRow`、`setFifthRow`、`setSixthRow` 便捷方法，补全 4~6 行器官设置的支持
- 轮盘菜单（技能选择界面）打开时自动聚焦到当前已激活的技能，方便快速切换

## 1.4.4

- 新增 `chest_openers` 物品标签，并将相关附魔的支持物品改为标签引用
- 重构器官 Builder：新增 `wrap()` 包装构建器，支持在保留原器官配置的基础上局部覆盖属性、技能和其他回调
- 提高了吃草的恢复量：矮草丛/草方块每次由 1 点饥饿值和 0.4 点饱和度提升至 2 点饥饿值和 4 点饱和度，高草丛会触发两次恢复

## 1.4.3

- 修复了 DragonSurvival（龙之生存）兼容性，⚠️ **海洋龙/洞穴龙仍需要装备鳃（水下呼吸属性 `WATER_BREATH`）才能在水中/熔岩中呼吸**

## 1.4.2

- 新增物品**生物分析仪**（Biological Analyzer）：可以右键查看自身/目标的胸腔属性，也可通过 `/chestcavity <targets> attributes` 命令查看，属性名可悬停查看详细描述
- 新增 `AttributeDisplay` 属性显示信息类：通过 Builder 模式构造，支持 `showWhenZero`（值为0时是否显示）、`priority`（显示优先级）字段
- 新增 `AttributeDisplayManager` 属性显示管理器：33 个属性按功能分组分配优先级（生存核心 40 → 体能基础 30 → 代谢系统 20 → 专食消化 10 → 特殊能力 0）
- 调整器官 Tooltip 渲染管线顺序为 Tags → Description → Attributes → ShiftHint，Description 现在显示在 Attributes 之前
- 修正熔炉内核技能描述：「效果等级等于熔炉之力属性值减1」→「效果等级等于熔炉之力属性值」
- 补充毒腺被动效果描述：新增「可与任意药水合成以更换存储的药水效果」说明

## 1.4.1

- 修复末影器官的传送技能与航空学（Create: Aeronautics）结构的交互。

## 1.4.0

> ⚠️ 此版本改动较大，部分 API 不向后兼容，请备份存档后更新。

- 新增器官工具提示管线系统（`OrganTooltip`）：将原本分散的 `descriptionTooltip`、`attributeTooltip`、`skillTooltip` 三个回调统一合并为 `organTooltip` 单一回调，通过 Builder 配置 Tags → Attributes → Description → ShiftHint → PassiveEffect → ActiveSkill 六段渲染管线，每段均提供 afterXxx 注入点
- 新增客户端配置 `detailedTooltips`：控制器官 tooltip 默认显示模式（详细/简略），可通过配置文件或按住 Shift 切换
- 新增被动效果/主动技能分层显示：器官 tooltip 中的技能描述区分为「被动效果」和「主动技能」两段，各自支持简略（`.simple.0~N`）和详细（`.0~N`）双语系索引格式
- 新增标签显示注册系统：`ItemTagManager` 新增 `TagDisplay` 注册/查询引擎，支持颜色、优先级自定义，附属模组可通过 `register` 系列方法注册自定义标签显示
- 新增 `TooltipSectionFunction` 函数式接口，作为工具提示段落渲染标准接口
- 简化 `ChestCavityUtil.createContext`：移除冗余的 `entity` 参数，改为从 `data` 中自动获取
- 修复 `ChestCavityMenu` 关闭音效仅在客户端播放的问题：改为通过 `level.playSound` 双端同步
- 修复铁傀儡修复技能（`OrganSkillUtil`）搜索铁锭时未在首次匹配后 break 的问题
- 修复 `ChestCavityTypeManager` 亡灵类型推断错误：Zombie 实体被错误注册为 `SKELETON` 类型，改为 `UNDEAD`
- `ChestCavityData` 新增 `setNeedBreath`、`setNeedHealth` setter 方法
- 尝试兼容 Controllable 手柄模组：虚拟光标点击、右摇杆滚轮、按键映射均可操作环形菜单，但不保证完全兼容
- 环形菜单（OrganSkillScreen）新增键盘/手柄导航：注册 `SKILL_PREV`（上一个技能）、`SKILL_NEXT`（下一个技能）、`SKILL_CONFIRM`（确认选择）三个按键绑定，支持方向键切换、滚轮切换、空格确认，鼠标左键/确认键均可确认选择

## 1.3.5

- 新增「是否需要健康」系统：`ChestCavityType` 新增 `needHealth` 属性，设为 `false` 的实体不会因健康值 ≤ 0 而持续受伤；亡灵、骷髅、凋灵骷髅、凋灵默认不需要健康

## 1.3.4

- 新增实体转换时的器官自动转换系统：村民→僵尸村民时器官自动变为腐化版（MUSCLE→ROTTEN_MUSCLE 等），僵尸村民治愈同理；支持 HUMAN↔UNDEAD、ANIMAL→UNDEAD 三组完整映射
- 新增 `OrganConversionEvent.Pre` / `Post` 事件，允许外部模组干预或接管器官转换逻辑（Pre 可取消默认转换）
- 新增 `ChestCavityType#addConversion` 系列 API（4 个重载），支持简单物品映射、条件匹配、完整回调等多种转换注册方式
- 调整 `attack` 回调时机：从 cancel 检查后移至检查前，确保攻击行为（无论是否被取消）必定触发攻击者器官的 attack 效果

## 1.3.3

- 新增 `OrganSkillConsumer` 函数式接口作为器官技能回调，返回值控制是否添加冷却：`true` 表示技能成功执行并添加冷却，`false` 表示技能未执行不添加冷却
- 新增 `ChestCavityRegisterCompletedEvent` ，在所有胸腔类型注册完成后触发，支持第三方模组替换或移除已注册的胸腔类型映射
- 新增 `OrganRegisterEvent` 和 `OrganRegisterCompletedEvent`，供外部模组添加、替换或移除器官能力
- 重命名 `RegisterChestCavityEvent` → `ChestCavityRegisterEvent`，命名风格统一

## 1.3.2

- 新增 `/chestcavity <targets> resetorgans` 命令，可将目标实体的胸腔器官重置为默认

## 1.3.1

- 修复 `ChestCavityData` 反序列化的向后兼容性：同时兼容旧版 NBT 键名（`Size`、`Items`、`Slot`）和新版键名（`chestCavitySize`、`items`、`slot`），避免旧存档升级后胸腔数据丢失

## 1.3.0

- 新增胸腔容量系统，支持 4 种大小（3~6 行，27~54 槽位），胸腔界面根据容量自动调整布局和背景纹理
- 新增 `/chestcavity` 命令（OP 权限），可调整目标实体的胸腔容量大小
- 器官技能快捷键从 27 个扩展至 54 个
- 胸腔容量缩小时多余器官先尝试放入玩家背包，放不下则掉落到脚下
- 血毒过滤效果增加中毒等级放大（每 2 点过滤不足 +1 级中毒）
- 修复史莱姆、岩浆怪、雪傀儡、旋风人缺少对应基础属性补偿的问题
- 气体囊新增呼吸恢复 +0.25
- 修复属性修饰符 tooltip 浮点数显示不精确的问题（如 `1.5001` → `1.5`）

## 1.2.1

- 新增冰霜抗性属性，使用原版 `is_freezing` 伤害类型标签，与火焰抗性机制对等

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
