package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ghast.GhastShootFireballGoal.class)
public abstract class GhastShootFireballGoalMixin extends Goal {
    @Shadow
    @Final
    private Ghast ghast;

    /**
     * 没有可怖属性不允许发射大火球
     */
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void chestCavityBeyond$tick(CallbackInfo ci) {
        if (ChestCavityUtil.getData(ghast).getCurrentValue(InitAttribute.GHASTLY) <= 0) {
            ci.cancel();
        }
    }
}
