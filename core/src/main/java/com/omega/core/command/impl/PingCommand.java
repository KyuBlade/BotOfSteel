package com.omega.core.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.awt.*;

@CommandInfo(name = "ping")
public class PingCommand extends AbstractCommand {

    public PingCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_PING)
    @Signature(help = "Send a ping to the bot to know if he is online and get the response time")
    public void pingCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        sendStateMessage("Ping sent, waiting for pong ...", Color.BLUE);

        long ping = message.getShard().getResponseTime();

        sendStateMessage(String.format(":hourglass: Ping - %dms", ping));
    }
}
