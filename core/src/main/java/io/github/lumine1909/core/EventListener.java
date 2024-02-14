package io.github.lumine1909.core;

import io.github.lumine1909.core.dispatcher.Dispatcher;
import io.github.lumine1909.core.event.DownloadFinishEvent;
import io.github.lumine1909.core.event.InitFinishEvent;
import io.github.lumine1909.core.resourcepack.PackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.server.ServerLoadEvent;

import static io.github.lumine1909.core.CustomSoundLib.*;

public class EventListener implements Listener {
    public static int number;
    public static void setVirtualNumber(int num) {
        number = num;
    }
    @EventHandler
    public void onServerLoaded(ServerLoadEvent e) {
        if (e.getType().equals(ServerLoadEvent.LoadType.STARTUP)) {
            CustomSoundLib.isServerLoaded = true;
            CustomSoundLib.log("开始构建音效资源包...");
            PackBuilder.init();

        } else {
            CustomSoundLib.log(ChatColor.YELLOW + "警告: 您似乎使用/reload重载了服务器, 这可能会导致一些问题");
        }
    }
    @EventHandler
    public void onDownloadFinish(DownloadFinishEvent e) {
        number--;
        if (number == 0) {
            Bukkit.getPluginManager().callEvent(new InitFinishEvent());
        }
    }
    @EventHandler
    public void onInitFinish(InitFinishEvent e) {
        PackBuilder.build();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!isDispatcherEnabled) {
            return;
        }
        Player player = e.getPlayer();
        player.setResourcePack(RESOURCE_URL);
    }
    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent e) {
        if (!FORCE_RESOURCE_PACK) {
            return;
        }
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED) || e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            e.getPlayer().kickPlayer(ChatColor.RED + "您需要接受资源包才能进入游戏");
        }
    }
}
