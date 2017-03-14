package com.omega.module;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.modules.IModule;

public abstract class Module implements IModule {

    private boolean enabled;

    @Override
    public boolean enable(IDiscordClient client) {
        if (enabled) {
            return false;
        }

        enabled = load(client);
        return enabled;
    }

    @Override
    public void disable() {
        if (enabled) {
            unload();
            enabled = false;
        }
    }

    protected abstract boolean load(IDiscordClient client);

    protected abstract void unload();

    public boolean isEnabled() {
        return enabled;
    }
}
