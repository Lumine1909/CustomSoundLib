package io.github.lumine1909.core;

import io.github.lumine1909.api.sound.Sound;
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

import java.util.Collections;
import java.util.List;


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
        Bukkit.getServicesManager().register(SoundManager.class, manager, this, ServicePriority.Normal);
        instance = this;
        isDispatcherEnabled = false;
        manager = new CustomSoundManager();
        Bukkit.getServicesManager().register(SoundManager.class, manager, this, ServicePriority.Normal);
        long startTime = System.nanoTime();
        reload();
        log(ChatColor.GREEN + "插件加载完成, 总用时 " + (System.nanoTime() - startTime)/1000000 + " 毫秒");
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregisterAll(this);
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            reload();
            sender.sendMessage(ChatColor.AQUA + "正在重新加载, 请留意服务器后台消息...");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length < 2) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }
}