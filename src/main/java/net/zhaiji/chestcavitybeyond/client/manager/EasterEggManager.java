package net.zhaiji.chestcavitybeyond.client.manager;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;

import java.time.LocalDate;
import java.util.UUID;

/**
 * 管理游戏内的节日彩蛋，当前包含愚人节和万圣节的 Herobrine 玩家列表注入
 */
public class EasterEggManager {
    private static final UUID HEROBRINE_UUID = UUID.fromString("f84c6a9d-33d4-4b53-95f5-4b74c1f9e83d");
    private static final String HEROBRINE_NAME = "Herobrine";
    private static final PlayerSkin HEROBRINE_SKIN = new PlayerSkin(
        ChestCavityBeyond.of("textures/entity/herobrine.png"),
        null,
        null,
        null,
        PlayerSkin.Model.WIDE,
        true
    );

    private static PlayerInfo herobrineInfo;
    private static RemotePlayer herobrinePlayer;
    private static boolean useHerobrine = false;

    /**
     * 判断当前日期是否为愚人节或万圣节
     */
    public static boolean isFoolOrHalloween() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        return (month == 4 && day == 1) || (month == 10 && day == 31);
    }

    /**
     * 每次打开 JEI 页面时随机决定本次是否使用 Herobrine，仅节日期间生效
     */
    public static void rollHerobrine() {
        useHerobrine = isFoolOrHalloween() && Math.random() < 0.3;
    }

    /**
     * 返回本次是否应使用 Herobrine 玩家
     */
    public static boolean shouldUseHerobrine() {
        return useHerobrine;
    }

    /**
     * 获取或创建用于 JEI 实体渲染的 Herobrine RemotePlayer
     */
    public static RemotePlayer getOrCreateHerobrinePlayer(ClientLevel level) {
        if (herobrinePlayer == null) {
            GameProfile profile = new GameProfile(HEROBRINE_UUID, HEROBRINE_NAME);
            herobrinePlayer = new RemotePlayer(level, profile) {
                @Override
                public PlayerSkin getSkin() {
                    return HEROBRINE_SKIN;
                }

                @Override
                public boolean isInvisibleTo(Player player) {
                    return true;
                }
            };
        }
        return herobrinePlayer;
    }

    /**
     * 获取或创建 Herobrine 的玩家信息，用于在 Tab 列表中显示自定义皮肤
     */
    public static PlayerInfo getOrCreateHerobrineInfo() {
        if (herobrineInfo == null) {
            GameProfile profile = new GameProfile(HEROBRINE_UUID, HEROBRINE_NAME);
            herobrineInfo = new PlayerInfo(profile, false) {
                @Override
                public PlayerSkin getSkin() {
                    return HEROBRINE_SKIN;
                }
            };
        }
        return herobrineInfo;
    }

    /**
     * 清理 Herobrine 玩家缓存和标志，在 JEI runtime 不可用时调用
     */
    public static void clearHerobrinePlayerCache() {
        herobrinePlayer = null;
        useHerobrine = false;
    }
}
