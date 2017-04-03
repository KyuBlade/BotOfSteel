package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.DiscordUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.RequestBuffer;

import java.util.EnumSet;

@Command(name = "ban")
public class BanCommand extends AbstractCommand {

    public BanCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_BAN)
    @Signature(help = "Ban a user from the server by his username")
    public void banCommand(@Parameter(name = "username") String username) {
        DiscordUtils.checkPermissions(botUser, guild, EnumSet.of(Permissions.BAN));

        IUser user = DiscordUtils.findUserWithReply(message, guild.getUsers(), username);
        if (user != null) {
            ban(user);
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_BAN)
    @Signature(help = "Ban the mentioned user from the server")
    public void banCommand(@Parameter(name = "userMention") IUser user) {
        DiscordUtils.checkPermissions(botUser, guild, EnumSet.of(Permissions.BAN));

        ban(user);
    }

    private void ban(IUser user) {
        if (!user.isBot()) {
            RequestBuffer.request(() -> {
                guild.banUser(user);

                sendStateMessage("Banned user " + user);
            });
        } else {
            sendErrorMessage("You can't ban me");
        }
    }
}
