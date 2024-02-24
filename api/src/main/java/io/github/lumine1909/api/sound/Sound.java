package io.github.lumine1909.api.sound;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public interface Sound {
    /**
     *
     * @return the key for play this sound
     */
    String getKey();

    /**
     *
     * @return sound's path in resource pack
     */
    String getPath();

    /**
     *
     * @return sound's name
     */
    String getName();

    /**
     *
     * @return path + "/" + name
     */
    String getFullName();

    /**
     *
     * @param player the player who will receive the sound.
     * @param location play location.
     * @param category the category for play (such as master, ambient).
     * @param volume sound's volume.
     * @param pitch sound's pitch.
     */
    void play(Player player, Location location, SoundCategory category, float volume, float pitch);

    void play(Location location, SoundCategory category, float volume, float pitch);
}
