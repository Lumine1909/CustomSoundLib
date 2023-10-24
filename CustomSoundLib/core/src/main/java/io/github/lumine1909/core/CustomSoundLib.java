package io.github.lumine1909.core;

import io.github.lumine1909.api.sound.SoundManager;
import io.github.lumine1909.core.resourcepack.PackBuilder;
import io.github.lumine1909.core.sound.CustomSoundManager;
import io.github.lumine1909.nms.VersionFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;


public class CustomSoundLib extends JavaPlugin {
    public static boolean isServerLoaded = false;
    public static boolean FORCE_RESOURCE_PACK;
    public static int RESOURCE_PACK_PORT;
    public static String RESOURCE_URL;
    public static CustomSoundLib instance;
    public static CustomSoundManager manager;
    public static boolean isDispatcherEnabled;
    static String prefix = ChatColor.AQUA + "[CustomSoundLib] ";
    @Override
    public void onLoad() {
        saveDefaultConfig();
    }
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        instance = this;
        isDispatcherEnabled = false;
        manager = new CustomSoundManager();
        Bukkit.getServicesManager().register(SoundManager.class, manager, this, ServicePriority.Normal);
        long startTime = System.nanoTime();
        reload();
        log(ChatColor.GREEN + "插件加载完成, 总用时 " + (System.nanoTime() - startTime)/1000000 + " 毫秒");
    }
    public static void reload() {
        log("正在加载配置文件...");
        FORCE_RESOURCE_PACK = instance.getConfig().getBoolean("settings.force-resource-pack", false);
        RESOURCE_PACK_PORT = instance.getConfig().getInt("settings.resource-pack-port");
        RESOURCE_URL = instance.getConfig().getString("settings.resource-url");
        log("正在加载资源包设置...");
        if (isServerLoaded) {
            log("重新构建资源包...");
            PackBuilder.init();
        }
    }
    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(prefix + msg);
    }
}