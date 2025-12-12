package net.zhaiji.chestcavitybeyond.mixinapi;

import net.minecraft.world.entity.player.Player;

public interface IFoodData {
    /**
     * 设置玩家
     * <p>
     * 只允许设置一次，因为FoodData所属不能变更
     * </P>
     *
     * @param player 玩家
     */
    void setPlayer(Player player);
}
