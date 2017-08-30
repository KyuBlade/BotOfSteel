package com.omega.core.command;

import com.omega.core.command.impl.*;

public class CoreCommandSupplier implements CommandSupplier {

    private static final Class[] COMMANDS = new Class[]{
        PingCommand.class, HelpCommand.class, RandomCommand.class, RollCommand.class, InviteCommand.class,
        SetPropertyCommand.class, GetPropertyCommand.class, GetVoiceChannelInfoCommand.class,
        KickCommand.class, BanCommand.class, RipCommand.class, UserInfoCommand.class, BotInfoCommand.class,
        ModuleCommand.class, PermissionCommand.class, GroupCommand.class, PrivateChannelPermissionCommand.class,
        AutoassignCommand.class, SetBotPropertyCommand.class, LogCommand.class
    };

    public CoreCommandSupplier() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<AbstractCommand>[] supply() {
        return COMMANDS;
    }
}
