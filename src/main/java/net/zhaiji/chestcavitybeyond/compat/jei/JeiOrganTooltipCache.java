package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * JEI 胸腔页面悬停器官时的 tooltip 拦截状态与虚拟胸腔数据缓存
 */
public class JeiOrganTooltipCache {
    private static long lastActiveTick = Long.MIN_VALUE;

    private static int currentHoveredOrganIndex = -1;
    private static @Nullable ChestCavityTypeDisplay currentHoveredDisplay;
    private static @Nullable ChestCavityType cachedTooltipType;
    private static @Nullable ChestCavityData cachedTooltipData;

    /**
     * 记录当前悬停的器官槽位
     */
    public static void setHover(int organIndex, ChestCavityTypeDisplay display) {
        currentHoveredOrganIndex = organIndex;
        currentHoveredDisplay = display;
    }

    /**
     * 清除悬停状态
     */
    public static void clearHover() {
        currentHoveredDisplay = null;
        currentHoveredOrganIndex = -1;
    }

    /**
     * 由胸腔页面 widget 在绘制时调用，刷新最近活跃的游戏刻
     */
    public static void updateActiveTick() {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            lastActiveTick = level.getGameTime();
        }
    }

    /**
     * 尝试为 JEI 胸腔页面悬停的器官生成 tooltip，不在 JEI 页面悬停时返回 false
     */
    public static boolean handlerTooltip(
        Organ organ,
        ItemStack stack,
        Item.TooltipContext context,
        List<Component> tooltip,
        TooltipFlag flags
    ) {
        if (!(Minecraft.getInstance().screen instanceof IRecipesGui)) return false;
        if (currentHoveredDisplay == null) return false;
        Level level = Minecraft.getInstance().level;
        if (level == null) return false;
        if (level.getGameTime() - lastActiveTick > 3) {
            clearHover();
            return false;
        }
        NonNullList<Item> organs = currentHoveredDisplay.getOrgans();
        if (currentHoveredOrganIndex < 0 || currentHoveredOrganIndex >= organs.size()) return false;
        if (organs.get(currentHoveredOrganIndex) != stack.getItem()) return false;
        ChestCavityData data = getOrCreateCachedData(currentHoveredDisplay);
        TooltipsKeyContext keyContext = new TooltipsKeyContext(Screen.hasShiftDown(), Screen.hasControlDown());
        organ.organTooltip(
            ChestCavityUtil.createContext(data, currentHoveredOrganIndex, stack),
            keyContext,
            context,
            tooltip,
            flags
        );
        return true;
    }

    /**
     * 获取或创建指定胸腔类型页面的虚拟胸腔数据，优先复用已缓存的虚拟实体
     * <p>
     * 无对应实体时回退到本地玩家实例，保证 tooltip 始终有有效数据源
     * </p>
     */
    private static ChestCavityData getOrCreateCachedData(ChestCavityTypeDisplay display) {
        ChestCavityType type = display.type();
        if (cachedTooltipType == type && cachedTooltipData != null) return cachedTooltipData;
        cachedTooltipType = null;
        cachedTooltipData = null;

        LivingEntity livingEntity = null;
        for (EntityType<?> entityType : display.entities()) {
            Optional<LivingEntity> cached = JeiEntityCache.getOrCreate(entityType);
            if (cached.isPresent()) {
                livingEntity = cached.get();
                break;
            }
        }
        if (livingEntity == null) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                livingEntity = player;
            }
        }
        if (livingEntity == null) return null;

        ChestCavityData data = ChestCavityUtil.getData(livingEntity);
        data.init();
        cachedTooltipType = type;
        cachedTooltipData = data;
        return data;
    }

    /**
     * 清空 tooltip 缓存的虚拟数据，在世界卸载等场景调用
     */
    public static void clear() {
        cachedTooltipType = null;
        cachedTooltipData = null;
    }
}
