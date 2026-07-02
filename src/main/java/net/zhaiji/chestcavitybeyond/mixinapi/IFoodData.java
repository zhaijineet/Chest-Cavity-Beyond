package net.zhaiji.chestcavitybeyond.mixinapi;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IFoodData {
    /**
     * 设置 FoodData 所属的玩家
     * <p>
     * FoodData 所属玩家不能变更，只在玩家初始化时设置一次
     * </P>
     *
     * @param player 所属玩家
     */
    void setPlayer(Player player);

    /**
     * 获取 FoodData 所属的玩家
     *
     * @return 所属玩家
     */
    Player getPlayer();

    /**
     * 设置当前食用食物
     *
     * @param food 食物
     */
    void setFood(ItemStack food);

    /**
     * 获取当前食用食物
     *
     * @return 食物
     */
    ItemStack getFood();

    /**
     * 获取 photosynthesisTimer
     *
     * @return photosynthesisTimer
     */
    double getPhotosynthesisTimer();

    /**
     * 设置 photosynthesisTimer
     *
     * @param photosynthesisTimer photosynthesisTimer
     */
    void setPhotosynthesisTimer(double photosynthesisTimer);
}
