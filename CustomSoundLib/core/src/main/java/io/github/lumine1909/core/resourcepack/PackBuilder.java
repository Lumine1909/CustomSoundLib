package io.github.lumine1909.core.resourcepack;

import io.github.lumine1909.core.EventListener;
import io.github.lumine1909.core.dispatcher.Dispatcher;
import io.github.lumine1909.core.event.InitFinishEvent;
import io.github.lumine1909.core.sound.CustomSound;
import io.github.lumine1909.nms.VersionFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.lumine1909.core.CustomSoundLib.*;

public class PackBuilder {
    public static String description;
    public static Map<String, List<String>> keys = new HashMap<>();
    public static void init() {
        description = instance.getConfig().getString("pack.description");
        ConfigurationSection section = instance.getConfig().getConfigurationSection("pack.sounds");
        if (section != null) {
            for (String k : section.getKeys(false)) {
                ConfigurationSection section1 = section.getConfigurationSection(k);
                File file = new File(instance.getDataFolder(), section1.getString("file"));
                if (!file.exists()) {
                    log(ChatColor.RED + "错误: 音效文件 " + file.getPath() + " 不存在, 跳过加载");
                    continue;
                }
                String name = section1.getString("name");
                String path = section1.getString("path");
                String key = section1.getString("key");
                manager.addSound(file, name, path, key);
            }
        }
        if (manager.getVirtuals() == 0) {
            Bukkit.getPluginManager().callEvent(new InitFinishEvent());
            return;
        }
        EventListener.setVirtualNumber(manager.getVirtuals());
        manager.downloadSounds();
    }
    public static void build() {
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                File root = new File(instance.getDataFolder(), "ResourcePack/CustomSoundLib");
                root.delete();
                root.mkdirs();
                FileWriter writer;
                File mcmeta = new File(root, "pack.mcmeta");
                String meta = "{\n  \"pack\": {\n     \"pack_format\": %ver%,\n     \"description\": \"%des%\"\n  }\n}".replace("%ver%", String.valueOf(VersionFetcher.getPackVersion())).replace("%des%", description);
                writer = new FileWriter(mcmeta);
                writer.write(meta);
                writer.flush();
                writer.close();
                File mcroot = new File(root, "assets/minecraft/sounds");
                mcroot.mkdirs();
                List<CustomSound> sounds = manager.getAllSounds();
                sounds.forEach(s -> {
                    File f = new File(mcroot, s.getPath());
                    f.mkdirs();
                    try {
                        Files.copy(s.getSoundFile().toPath(), new File(f, s.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String key = s.getKey();
                    String path = s.getPath();
                    String name = s.getName();
                    if (!keys.containsKey(key)) {
                        keys.put(key, new ArrayList<>());
                    }
                    keys.get(key).add(path + "/" + name);
                });
                File jsonFolder = mcroot.getParentFile();
                File soundJson = new File(jsonFolder, "sounds.json");
                soundJson.createNewFile();
                writer = new FileWriter(soundJson);
                String model = "  \"%key%\": {\n    \"sounds\": [\n";
                StringBuilder builder = new StringBuilder("{\n");
                List<String> var0 = keys.keySet().stream().toList();
                if (var0.size() > 1) {
                    for (int i = 0; i < var0.size() - 1; i++) {
                        String var1 = var0.get(i);
                        builder.append(model.replaceAll("%key%", var1));
                        List<String> var2 = keys.get(var1);
                        if (var2.size() > 1) {
                            for (int j = 0; j < var2.size() - 1; j++) {
                                String path = var2.get(j).substring(0, var2.get(j).length() - 4);
                                builder.append("      \"").append(path).append("\",\n");
                            }
                        }
                        String path = var2.get(var2.size() - 1).substring(0, var2.get(var2.size() - 1).length() - 4);
                        builder.append("      \"").append(path).append("\"\n");
                        builder.append("    ]\n").append("  }").append(",").append("\n");
                    }
                }
                String var1 = var0.get(var0.size() - 1);
                builder.append(model.replaceAll("%key%", var1));
                List<String> var2 = keys.get(var1);
                if (var2.size() > 1) {
                    for (int j = 0; j < var2.size() - 1; j++) {
                        String path = var2.get(j).substring(0, var2.get(j).length() - 4);
                        builder.append("      \"").append(path).append("\",\n");
                    }
                }
                String path = var2.get(var2.size() - 1).substring(0, var2.get(var2.size() - 1).length() - 4);
                builder.append("      \"").append(path).append("\"\n");
                builder.append("    ]\n").append("  }");
                builder.append("\n}");
                writer.write(builder.toString());
                writer.flush();
                writer.close();
                File file = new File(root.getParentFile(), "CustomSoundLib.zip");
                file.delete();
                ZipUtil.pack(root, file);
                try {
                    Dispatcher.runServer(file);
                } catch (IOException e) {
                    log(ChatColor.RED + "资源包分发服务器因端口占用加载失败, 请更换一个端口");
                    Bukkit.getPluginManager().disablePlugin(instance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
