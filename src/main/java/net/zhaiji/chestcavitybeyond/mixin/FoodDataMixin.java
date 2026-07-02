package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements IFoodData {
    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;
    @Shadow
    private float exhaustionLevel;
    /**
     * FoodData 所属玩家，用于获取胸腔数据
     */
    @Unique
    private Player player;
    /**
     * 缓存当前食用食物
     */
    @Unique
    private ItemStack food = ItemStack.EMPTY;
    /**
     * 光合作用计时器
     */
    @Unique
    private double photosynthesisTimer;

    @Shadow
    public abstract void add(int foodLevel, float saturationLevel);

    /**
     * 按需获取所属玩家的胸腔数据，避免在 Player 构造未完成阶段触发 attachment 创建
     */
    @Unique
    private ChestCavityData getChestCavityData() {
        return ChestCavityUtil.getData(player);
    }

    /**
     * 修改食物获取到的饥饿值和饱食度
     */
    @Inject(
        method = "eat(Lnet/minecraft/world/food/FoodProperties;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void chestCavityBeyond$eat(FoodProperties foodProperties, CallbackInfo ci) {
        MixinUtil.eat((FoodData) (Object) this, foodProperties);
        ci.cancel();
    }

    @Unique
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * 根据{@link InitAttribute#PHOTOSYNTHESIS}属性，新增在白天可以进行光合作用增加饥饿值和饱食度
     */
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void chestCavityBeyond$tick(Player player, CallbackInfo ci) {
        MixinUtil.tickPhotosynthesis((FoodData) (Object) this, player);
    }

    @Unique
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * 根据{@link InitAttribute#METABOLISM}属性，修改饱和回血阈值
     */
    @ModifyConstant(
        method = "tick",
        constant = @Constant(intValue = 10)
    )
    public int chestCavityBeyond$modifySaturationRegenerationThreshold(int original) {
        return MixinUtil.modifyMetabolismThreshold(original, getChestCavityData());
    }

    @Unique
    @Override
    public void setFood(ItemStack food) {
        this.food = food;
    }

    /**
     * 根据{@link InitAttribute#METABOLISM}属性，修改普通回血阈值
     */
    @ModifyConstant(
        method = "tick",
        constant = @Constant(intValue = 80, ordinal = 0)
    )
    public int chestCavityBeyond$modifyFoodRegenerationThreshold(int original) {
        return MixinUtil.modifyMetabolismThreshold(original, getChestCavityData());
    }

    @Unique
    @Override
    public ItemStack getFood() {
        return food;
    }

    /**
     * 根据{@link InitAttribute#METABOLISM}属性，修改饥饿伤害阈值
     */
    @ModifyConstant(
        method = "tick",
        constant = @Constant(intValue = 80, ordinal = 1)
    )
    public int chestCavityBeyond$modifyStarvationThreshold(int original) {
        return MixinUtil.modifyMetabolismThreshold(original, getChestCavityData());
    }

    @Unique
    @Override
    public double getPhotosynthesisTimer() {
        return photosynthesisTimer;
    }

    /**
     * 饱和回血阈值到1后，将可安全支付的溢出倍率转为单次回血量
     */
    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;heal(F)V",
            ordinal = 0
        ),
        index = 0
    )
    public float chestCavityBeyond$modifySaturationRegenerationAmount(float amount) {
        return MixinUtil.modifyMetabolismRegenerationAmount(
            amount,
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            Math.min(saturationLevel, 6.0F),
            getChestCavityData(),
            10
        );
    }

    @Unique
    @Override
    public void setPhotosynthesisTimer(double photosynthesisTimer) {
        this.photosynthesisTimer = photosynthesisTimer;
    }

    /**
     * 饱和回血阈值到1后，同步提高本次饥饿消耗
     */
    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V",
            ordinal = 0
        ),
        index = 0
    )
    public float chestCavityBeyond$modifySaturationRegenerationExhaustion(float exhaustion) {
        return MixinUtil.modifyMetabolismRegenerationExhaustion(
            exhaustion,
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            getChestCavityData(),
            10
        );
    }

    /**
     * 普通回血阈值到1后，将可安全支付的溢出倍率转为单次回血量
     */
    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;heal(F)V",
            ordinal = 1
        ),
        index = 0
    )
    public float chestCavityBeyond$modifyFoodRegenerationAmount(float amount) {
        return MixinUtil.modifyMetabolismRegenerationAmount(
            amount,
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            6.0F,
            getChestCavityData(),
            80
        );
    }

    /**
     * 普通回血阈值到1后，同步提高本次饥饿消耗
     */
    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V",
            ordinal = 1
        ),
        index = 0
    )
    public float chestCavityBeyond$modifyFoodRegenerationExhaustion(float exhaustion) {
        return MixinUtil.modifyMetabolismRegenerationExhaustion(
            exhaustion,
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            getChestCavityData(),
            80
        );
    }

    /**
     * 饥饿伤害阈值到1后，将溢出的新陈代谢倍率转为单次伤害量
     */
    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
        ),
        index = 1
    )
    public float chestCavityBeyond$modifyStarvationDamageAmount(float amount) {
        return MixinUtil.modifyMetabolismOverflowAmount(amount, getChestCavityData(), 80);
    }

    /**
     * 饱和回血前检查本次回血消耗是否会让饥饿值突破18下限
     */
    @WrapWithCondition(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;heal(F)V",
            ordinal = 0
        )
    )
    public boolean chestCavityBeyond$shouldSaturationRegenerate(Player instance, float amount) {
        return MixinUtil.canApplyMetabolismRegeneration(
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            Math.min(saturationLevel, 6.0F),
            getChestCavityData(),
            10
        );
    }

    /**
     * 饱和回血前检查本次饥饿消耗是否会让饥饿值突破18下限
     */
    @WrapWithCondition(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V",
            ordinal = 0
        )
    )
    public boolean chestCavityBeyond$shouldSaturationRegenerationAddExhaustion(FoodData instance, float exhaustion) {
        return MixinUtil.canApplyMetabolismRegeneration(
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            Math.min(saturationLevel, 6.0F),
            getChestCavityData(),
            10
        );
    }

    /**
     * 普通回血前检查本次回血消耗是否会让饥饿值突破18下限
     */
    @WrapWithCondition(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;heal(F)V",
            ordinal = 1
        )
    )
    public boolean chestCavityBeyond$shouldFoodRegenerate(Player instance, float amount) {
        return MixinUtil.canApplyMetabolismRegeneration(
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            6.0F,
            getChestCavityData(),
            80
        );
    }

    /**
     * 普通回血前检查本次饥饿消耗是否会让饥饿值突破18下限
     */
    @WrapWithCondition(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V",
            ordinal = 1
        )
    )
    public boolean chestCavityBeyond$shouldFoodRegenerationAddExhaustion(FoodData instance, float exhaustion) {
        return MixinUtil.canApplyMetabolismRegeneration(
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            6.0F,
            getChestCavityData(),
            80
        );
    }

    /**
     * 修改向exhaustion添加的值
     */
    @ModifyVariable(
        method = "addExhaustion",
        at = @At("HEAD"),
        argsOnly = true
    )
    public float chestCavityBeyond$modifyExhaustion(float value) {
        return MixinUtil.modifyExhaustion(value, getChestCavityData());
    }
}
