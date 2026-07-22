不允许使用变量名简称：例如如context写成ctx，不允许
但多个单词使用其中一个单词命名是允许的：例如ItemStackData的变量名写成data。
不过如果有两种，例如ItemStackData和TooltipData，就需要把所有的单词写上了：例如ItemStackData的变量名应该是itemStackData

不允许有致死量的注释，当信息足够自解释时，不需要添加注释
若要加javadoc需要一句话写出核心内容，不能有javadoc的注释头尾和注释内容加起来只占一行的情况，必须展开
若是为Minecraft或NeoForge的方法添加javadoc时，除非是渲染相关，否则不允许添加@param

注释应当写在需要被注释的代码行的上方，不允许写在同行

NeoForge源码： E:Mod Project\neoforge-21.1.230-merged
JEI源码：E:\Mod Project\JustEnoughItems
苹果皮源码：E:\Mod Project\AppleSkin

---

## Mixin 编写规范

1. **类声明**：`public abstract class XxxMixin`（Xxx为Target Class），需要访问父类 protected 成员（如 `Entity#tickCount`、`Entity#moveTo`、`level()`）时，`extends 目标类的父类` 并提供匹配的 `public` 构造器透传 super ，仅用 @Shadow、无需访问 protected 成员时可省略 extends
2. **自身引用**：在 @Inject / @Redirect / @ModifyArg 等注入方法中，若需要将 `this` 强转回目标类传给外部方法，统一定义 `@Unique modid$self()` 辅助方法返回 `(目标类)(Object)this`，禁止内联强转
3. **方法命名**：注入/重定向方法统一用 `modid$<注入目标方法名>` 前缀，其中 `<注入目标方法名>` 是 @Inject / @Redirect 等注解中 `method = "xxx"` 参数指定的目标方法名（如 `method = "attack(...)"` → 用 `attack`）。同一 Mixin 类中若多个注入方法的 `method` 指向**同一目标方法**导致方法名冲突，则追加 `$<被重定向/被注入的目标成员名>` 消歧（即 `modid$<method值>$<被重定向的方法名>`）；若仍冲突，继续追加 `$<用途>`，直到方法名不冲突为止
4. **@Shadow**：保留原可见性，字段按需加 `@Final`；方法用 `abstract`；`@Nullable` 等注解跟随原声明
5. **@Inject**：`method` 带完整签名（泛型/重载必需），`at` 明确，需要提前返回时 `cancellable = true`；参数表完整匹配原方法 + CallbackInfo / CallbackInfoReturnable；回调参数命名用 `ci` / `cir`
6. **注册**：`modid.mixins.json` 的 `mixins` 数组按字母序插入；客户端专用进 `client` 数组
7. **命名一致性**：注入方法可见性首选 `public`（对齐 ChesedMiniRayMixin / LivingEntityMixin / PlayerMixin）；static 原方法对应 static 注入，且 static 注入方法必须用 `private`
8. **注解多行格式**：@Inject / @ModifyArg / @ModifyExpressionValue 等注入注解的参数必须使用多行格式，每个参数独占一行，禁止单行压缩写法