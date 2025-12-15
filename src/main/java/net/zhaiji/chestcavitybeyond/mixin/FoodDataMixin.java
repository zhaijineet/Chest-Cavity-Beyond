package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
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
     * 剩余新陈代谢缓存
     */
    @Unique
    private double metabolismRemainder;

    @Unique
    @Override
    public void setChestCavityData(ChestCavityData data) {
        if (this.data == null) {
            this.data = data;
        }
    }

    /**
     * 修改饥饿值的增加，受{@link InitAttribute#DIGESTION}属性影响
     */
    @ModifyVariable(
            method = "add",
            at = @At("HEAD"),
            argsOnly = true
    )
    public int chestCavityBeyond$modifyFoodLevel(int value) {
        if (data.getCurrentValue(InitAttribute.DIGESTION) <= 0) {
            return 0;
        }
        double digestion = data.getDifferenceValue(InitAttribute.DIGESTION);
        return (int) (value * MathUtil.getDirectScale(digestion));
    }

    /**
     * 修改饱和度的增加，受{@link InitAttribute#NUTRITION}属性影响
     */
    @ModifyVariable(
            method = "add",
            at = @At("HEAD"),
            argsOnly = true
    )
    public float chestCavityBeyond$modifySaturationLevel(float value) {
        if (data.getCurrentValue(InitAttribute.NUTRITION) <= 0) {
            return 0;
        }
        double nutrition = data.getDifferenceValue(InitAttribute.NUTRITION);
        return (float) (value * MathUtil.getDirectScale(nutrition));
    }

    /**
     * 根据{@link InitAttribute#METABOLISM}属性，修改Timer的累计速度，影响消耗饥饿回复生命值的速度
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
