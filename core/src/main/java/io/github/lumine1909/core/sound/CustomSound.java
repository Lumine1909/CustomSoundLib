package io.github.lumine1909.core.sound;

import io.github.lumine1909.api.sound.Sound;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

import static io.github.lumine1909.core.CustomSoundLib.instance;

public class CustomSound implements Sound {
    String key;
    String path;
    String name;
    File soundFile;
    public CustomSound(File sound, String name, String path, String key) {
        this.soundFile = sound;
        this.name = name;
        this.path = path;
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    public File getSoundFile() {
        return soundFile;
    }
    @Override
    public void play(Player player, Location location, SoundCategory category, float volume, float pitch) {
        player.playSound(location, getKey(), category, volume, pitch);
    }
    @Override
    public String getFullName() {
        return path + "/"  + name;
    }

    @Override
    public void play(Location location, SoundCategory category, float volume, float pitch) {
        location.getWorld().playSound(location, getKey(), category, volume, pitch);
    }
}
