package net.zhaiji.chestcavitybeyond.mixinapi;

import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

public interface IFoodData {
    /**
     * 设置玩家胸腔
     * <p>
     * 只允许设置一次，因为FoodData所属不能变更
     * </P>
     *
     * @param data 胸腔数据
     */
    void setChestCavityData(ChestCavityData data);

    /**
     * 获取胸腔数据
     *
     * @return 胸腔数据
     */
    ChestCavityData getChestCavityData();

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
