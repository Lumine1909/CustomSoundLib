package io.github.lumine1909.core.sound;

import io.github.lumine1909.api.sound.Sound;
import io.github.lumine1909.api.sound.SoundManager;
import io.github.lumine1909.core.CustomSoundLib;
import io.github.lumine1909.core.event.DownloadFinishEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static io.github.lumine1909.core.CustomSoundLib.instance;

public class CustomSoundManager implements SoundManager {
    List<CustomSound> sounds = new ArrayList<>();
    List<VirtualSound> virtualSounds = new ArrayList<>();
    @Override

    public Sound addSound(File sound, String name, String path, String key) {
        if (getSound(path, name) != null) {
            CustomSoundLib.log(ChatColor.RED + "音效 " + path + " 已经存在了, 无法再次注册");
            return null;
        }
        CustomSound sound1 = new CustomSound(sound, name, path, key);
        sounds.add(sound1);
        return sound1;
    }

    public List<CustomSound> getAllSounds() {
        return sounds;
    }


    public void downloadSounds() {
        virtualSounds.forEach(VirtualSound::download);
    }
    public int getVirtuals() {
        return virtualSounds.size();
    }

    @Override
    public void addSound(String url, String name, String path, String key) {
        virtualSounds.add(new VirtualSound(url, name, path, key));
    }

    @Override

    public void removeSound(String path, String name) {
        sounds.removeIf(x -> (path + "/" + name).equals(x.getFullName()));
    }

    @Override
    public Sound getSound(String path, String name) {
        if (sounds.isEmpty()) {
            return null;
        }
        List<CustomSound> temp = sounds.stream().filter(x -> (path + "/" + name).equals(x.getFullName())).toList();
        return temp.isEmpty() ? null : temp.get(0);
    }

    class VirtualSound {
        String url;
        String name;
        String path;
        String key;

        public VirtualSound(String url, String name, String path, String key) {
            this.url = url;
            this.name = name;
            this.path = path;
            this.key = key;
        }
        public void download() {
            Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
                try (InputStream ins = new URL(url).openStream()) {
                    Path path1 = Paths.get(instance.getDataFolder().toURI());
                    Files.createDirectories(path1.getParent());
                    Files.copy(ins, path1, StandardCopyOption.REPLACE_EXISTING);
                    Bukkit.getScheduler().runTask(instance, () -> {
                        addSound(path1.toFile(), name, path, key);
                        Bukkit.getPluginManager().callEvent(new DownloadFinishEvent());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}