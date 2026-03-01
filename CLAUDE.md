# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

Chest Cavity Beyond 是一个 Minecraft 1.21.1 NeoForge 模组，为游戏添加了胸腔器官系统。玩家可以从生物身上获取器官并安装在胸腔中，获得各种特殊能力。

- **Minecraft 版本**: 1.21.1
- **NeoForge 版本**: 21.1.216
- **Java 版本**: 21
- **主包**: `net.zhaiji.chestcavitybeyond`

## 常用开发命令

```bash
# 首次设置 - 生成 IntelliJ 运行配置
./gradlew genIntellijRuns

# 运行客户端（开发环境）
./gradlew runClient
./gradlew client_1    # 第二个客户端配置

# 运行服务器
./gradlew runServer

# 数据生成
./gradlew runData

# 构建模组 JAR
./gradlew build

# 清理构建产物
./gradlew clean
```

## 架构概述

### 核心系统

1. **胸腔系统 (Chest Cavity System)**
   - 每个生物拥有 3×9 的器官格子
   - 通过 `ChestCavityData` (Attachment) 存储器官数据
   - 使用 NeoForge 的 Attachment 系统，而非传统的 Capability

2. **器官系统 (Organ System)**
   - 核心接口: `net.zhaiji.chestcavitybeyond.api.capability.IOrgan`
   - 基类: `net.zhaiji.chestcavitybeyond.api.capability.Organ`
   - 管理器: `net.zhaiji.chestcavitybeyond.manager.OrganManager`
   - 器官通过 `OrganBuilder` 构建注册

3. **任务系统 (Task System)**
   - 核心接口: `net.zhaiji.chestcavitybeyond.api.task.IChestCavityTask`
   - 实现特殊能力（发射火球、激光、传送等）
   - 客户端渲染: `net.zhaiji.chestcavitybeyond.api.client.task.IRenderTask`

4. **胸腔类型系统 (Chest Cavity Type)**
   - 核心接口: `net.zhaiji.chestcavitybeyond.api.ChestCavityType`
   - 管理器: `net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager`
   - 用于推断实体应具有的器官类型

### 包结构

```
net.zhaiji.chestcavitybeyond
├── api/                          # 公共 API
│   ├── capability/               # 器官接口和基类
│   ├── task/                     # 任务接口
│   ├── client/task/              # 客户端渲染任务
│   ├── function/                 # 消费者接口 (AttackConsumer, HurtConsumer)
│   └── event/                    # 注册事件
├── attachment/                   # 胸腔数据附件
├── builder/                      # 建造者类 (OrganBuilder, DamageTypeBuilder等)
├── client/                       # 客户端特有功能
│   ├── event/                    # 客户端事件
│   ├── renderer/                 # 渲染器
│   └── screen/                   # GUI 界面
├── datagen/                      # 资源数据生成
├── damagesource/                 # 自定义伤害源
├── effect/                       # 自定义药效
├── entity/                       # 自定义实体
├── event/                        # 通用事件处理
├── item/                         # 自定义物品
├── manager/                      # 核心管理器
├── menu/                         # GUI 菜单
├── mixin/                        # Mixin 注入
├── mixinapi/                     # Mixin 接口注入 API
├── network/                      # 网络包
├── recipe/                       # 自定义配方
├── register/                     # 注册类
└── util/                         # 工具类
```

### API 设计模式

**器官行为回调**:
- `onAttack` - 攻击时触发 (通过 `AttackConsumer`)
- `onHurt` - 受伤时触发 (通过 `HurtConsumer`)
- `tick` - 每刻更新
- `appendTooltip` - 添加工具提示 (通过 `OrganTooltipConsumer`)

**上下文对象**:
- `ChestCavitySlotContext` - 提供器官槽位和实体信息
- `TooltipsKeyContext` - 工具提示按键上下文

### 网络系统

网络包位于 `net.zhaiji.chestcavitybeyond.network`:
- 同步胸腔数据
- 同步选中的技能槽位
- 技能激活

### 扩展性设计

其他模组可以通过以下方式扩展:

1. **注册自定义器官类型**: 通过 `RegisterChestCavityEvent` 事件
2. **实现 IOrgan 接口**: 创建新的器官行为
3. **实现 IChestCavityTask**: 添加新的技能/任务
4. **注册自定义胸腔类型**: 通过 `ChestCavityTypeManager`

### 资源来源

美术资源（贴图、模型）来自 [Chest Cavity](https://github.com/Tigereye504/chestcavity) (Apache License 2.0)。
所有代码均为独立开发，未使用原项目的任何代码。

### 本地运行时依赖

以下依赖仅用于本地开发，不会发布:
- Appleskin (显示食物信息)
- JEI (物品显示)
- Jade (信息显示)

这些依赖通过 `localRuntime` 配置声明，在 `build.gradle` 中定义。
