package com.omega.command;

import com.omega.command.impl.*;

public class CoreCommandSupplier implements CommandSupplier {

    private static final Class[] COMMANDS = new Class[]{
        PingCommand.class, HelpCommand.class, RandomCommand.class, RollCommand.class, InviteCommand.class,
        SetPropertyCommand.class, GetPropertyCommand.class, GetVoiceChannelInfoCommand.class,
        KickCommand.class, BanCommand.class, RipCommand.class, UserInfoCommand.class, BotInfoCommand.class
    };

    public CoreCommandSupplier() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<AbstractCommand>[] supply() {
        return COMMANDS;
    }
}
