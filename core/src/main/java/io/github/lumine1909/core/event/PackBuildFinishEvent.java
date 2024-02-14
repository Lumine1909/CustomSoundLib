package io.github.lumine1909.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PackBuildFinishEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
