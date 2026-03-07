# 更新日志 (Changelog)

本文档记录了 Chest Cavity Beyond 所有版本的更改。

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
