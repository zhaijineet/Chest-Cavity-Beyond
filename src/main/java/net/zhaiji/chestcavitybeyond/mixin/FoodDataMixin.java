package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
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
    @Unique
    private Player player;

    @Unique
    @Override
    public void setPlayer(Player player) {
        if (this.player == null) {
            this.player = player;
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
        double digestion = ChestCavityUtil.getData(player).getDifferenceValue(InitAttribute.DIGESTION);
        return (int) (value * MathUtil.getDirectExpScale(digestion));
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
        double nutrition = ChestCavityUtil.getData(player).getDifferenceValue(InitAttribute.NUTRITION);
        return (float) (value * MathUtil.getDirectExpScale(nutrition));
    }

    /**
     * 根据{@link InitAttribute#METABOLISM}属性，修改Timer的累计速度，影响消耗饥饿回复生命值的速度
     */
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void chestCavityBeyond$tick(Player player, CallbackInfo ci) {
        ChestCavityData data = ChestCavityUtil.getData(player);
        double metabolism = data.getDifferenceValue(InitAttribute.METABOLISM);
        if (metabolism != 0) {
            double addTimer = MathUtil.getDirectExpScale(metabolism);
            if (metabolism > 0) {
                data.metabolismRemainder += addTimer;
            } else {
                data.metabolismRemainder -= 1 - addTimer;
            }
            tickTimer += (int) data.metabolismRemainder;
            data.metabolismRemainder %= 1;
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
        return (float) (value * MathUtil.getInverseExpScale(ChestCavityUtil.getData(player).getDifferenceValue(InitAttribute.ENDURANCE)));
    }
}
