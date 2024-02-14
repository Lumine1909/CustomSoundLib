package io.github.lumine1909.api;

import io.github.lumine1909.api.sound.SoundManager;

import java.lang.reflect.Method;

public class SoundAPI {
    /**
     Get the manager for add/remove/get sounds.
     */
    public static SoundManager getSoundManager() {
        try {
            Class<?> clazz = Class.forName("io.github.lumine1909.core.CustomSoundAPI");
            Method method = clazz.getMethod("getSoundManager");
            return (SoundManager) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}
