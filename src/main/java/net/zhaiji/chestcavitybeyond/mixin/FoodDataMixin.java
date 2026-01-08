package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements IFoodData {
    @Shadow
    private int tickTimer;
    @Shadow
    private int foodLevel;
    @Unique
    private ChestCavityData data;
    /**
     * 缓存当前食用食物
     */
    @Unique
    private ItemStack food = ItemStack.EMPTY;
    /**
     * 剩余新陈代谢缓存
     */
    @Unique
    private double metabolismRemainder;
    /**
     * 光合作用计时器
     */
    @Unique
    private double photosynthesisTimer;

    @Shadow
    protected abstract void add(int foodLevel, float saturationLevel);

    @Unique
    @Override
    public void setChestCavityData(ChestCavityData data) {
        if (this.data == null) {
            this.data = data;
        }
    }

    @Unique
    @Override
    public void setFood(ItemStack food) {
        this.food = food;
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
        int foodLevel = foodProperties.nutrition();
        float saturation = foodProperties.saturation();
        float saturationAdd = 0;
        boolean isPoisoning = food.is(Tags.Items.FOODS_FOOD_POISONING);
        boolean isMeat = food.is(ItemTags.MEAT);
        if (data.getCurrentValue(InitAttribute.DIGESTION) <= 0
                && data.getCurrentValue(InitAttribute.CARNIVOROUS_DIGESTION) <= 0
                && data.getCurrentValue(InitAttribute.HERBIVOROUS_DIGESTION) <= 0
                && (!isPoisoning || data.getCurrentValue(InitAttribute.SCAVENGER_DIGESTION) <= 0)
        ) {
            foodLevel = 0;
        }
        if (data.getCurrentValue(InitAttribute.NUTRITION) <= 0
                && data.getCurrentValue(InitAttribute.CARNIVOROUS_NUTRITION) <= 0
                && data.getCurrentValue(InitAttribute.HERBIVOROUS_NUTRITION) <= 0
                && (!isPoisoning || data.getCurrentValue(InitAttribute.SCAVENGER_NUTRITION) <= 0)
        ) {
            saturation = 0;
        }
        double digestion = data.getDifferenceValue(InitAttribute.DIGESTION);
        double carnivorousDigestion = data.getDifferenceValue(InitAttribute.CARNIVOROUS_DIGESTION);
        double herbivorousDigestion = data.getDifferenceValue(InitAttribute.HERBIVOROUS_DIGESTION);
        double scavengerDigestion = data.getDifferenceValue(InitAttribute.SCAVENGER_DIGESTION);

        double nutrition = data.getDifferenceValue(InitAttribute.NUTRITION);
        double carnivorousNutrition = data.getDifferenceValue(InitAttribute.CARNIVOROUS_NUTRITION);
        double herbivorousNutrition = data.getDifferenceValue(InitAttribute.HERBIVOROUS_NUTRITION);
        double scavengerNutrition = data.getDifferenceValue(InitAttribute.SCAVENGER_NUTRITION);

        double digestionDiff = isMeat
                ? digestion + carnivorousDigestion
                : digestion + herbivorousDigestion;
        double nutritionDiff = isMeat
                ? nutrition + carnivorousNutrition
                : nutrition + herbivorousNutrition;

        if (isPoisoning) {
            digestionDiff = digestionDiff + scavengerDigestion;
            nutritionDiff = nutritionDiff + scavengerNutrition;
            // 有毒食物额外加饱和
            for (FoodProperties.PossibleEffect possibleEffect : foodProperties.effects()) {
                if (((IMobEffectInstance) possibleEffect.effect()).isHarmful()) {
                    saturationAdd += (float) (possibleEffect.probability() * scavengerNutrition);
                }
            }
        }
        foodLevel = (int) (foodLevel * MathUtil.getDirectScale(digestionDiff));
        // 正数时，应该小心增加，负数时，应该大胆减少
        nutritionDiff = nutritionDiff > 0 ? nutritionDiff / 4 : nutritionDiff;
        saturation = (float) (saturation * MathUtil.getDirectScale(nutritionDiff) + saturationAdd);
        foodLevel = Math.max(0, foodLevel);
        saturation = Math.max(0, saturation);
        add(foodLevel, saturation);
        food = ItemStack.EMPTY;
        ci.cancel();
    }

    /**
     * 根据{@link InitAttribute#METABOLISM}属性，修改Timer的累计速度，影响消耗饥饿回复生命值的速度
     * <p>
     * 根据{@link InitAttribute#PHOTOSYNTHESIS}属性，新增在白天可以进行光合作用增加饥饿值和饱食度
     * </p>
     */
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void chestCavityBeyond$tick(Player player, CallbackInfo ci) {
        double metabolism = data.getDifferenceValue(InitAttribute.METABOLISM);
        if (metabolism != 0 && (foodLevel >= 18 || foodLevel <= 0)) {
            double addTimer = MathUtil.getDirectScale(metabolism);
            if (metabolism > 0) {
                metabolismRemainder += addTimer;
            } else {
                metabolismRemainder -= 1 - addTimer;
            }
            tickTimer += (int) metabolismRemainder;
            metabolismRemainder %= 1;
        }
        double photosynthesis = data.getCurrentValue(InitAttribute.PHOTOSYNTHESIS);
        Level level = player.level();
        // 白天且能看见天空时
        if (photosynthesis > 0 && level.isDay() && level.canSeeSky(player.blockPosition())) {
            photosynthesisTimer += photosynthesis;
            if (photosynthesisTimer >= 800) {
                photosynthesisTimer = 0;
                if (foodLevel < 20) {
                    add(1, 0);
                } else {
                    add(0, 1);
                }
            }
        }
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
        return (float) (value * MathUtil.getInverseScale(data.getDifferenceValue(InitAttribute.ENDURANCE)));
    }
}
