package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Permission;
import com.omega.command.Signature;
import com.omega.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

@Command(name = "ping")
public class PingCommand extends AbstractCommand {

    public PingCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_PING)
    @Signature(help = "Send a ping to the bot to know if he is online and get the response time")
    public void pingCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        long ping = message.getShard().getResponseTime();
        by.getOrCreatePMChannel().sendMessage(String.format("Pong - %dms", +ping));
    }
}
