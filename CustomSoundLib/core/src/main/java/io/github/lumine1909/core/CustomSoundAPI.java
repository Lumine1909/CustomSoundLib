package io.github.lumine1909.core;

import io.github.lumine1909.core.sound.CustomSoundManager;

public class CustomSoundAPI {
    public static CustomSoundManager getSoundManager() {
        return CustomSoundLib.manager;
    }
}
