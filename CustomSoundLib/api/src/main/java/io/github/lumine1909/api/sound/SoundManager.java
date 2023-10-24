package io.github.lumine1909.api.sound;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.List;

public interface SoundManager {
    /**
     *
     * @param sound sound's file.
     * @param name sound's name in resource pack, may not same as filename
     * @param path final path of the sound
     * @param key the key for play sound
     * @return instance of this sound
     */
    Sound addSound(File sound, String name, String path, String key);

    /**
     *
     * @param url url for sound's resource file (will be downloaded)
     * @param name sound's name in resource pack, may not same as filename
     * @param path final path of the sound
     * @param key the key for play sound
     */
    void addSound(String url, String name, String path, String key);
    void removeSound(String path, String name);
    Sound getSound(String path, String name);
}
