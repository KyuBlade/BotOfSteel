package com.omega.core.command.impl;

import com.omega.core.command.*;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import com.omega.core.util.DiscordUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.PermissionUtils;
import sx.blah.discord.util.RequestBuffer;

import java.util.EnumSet;

@CommandInfo(name = "ban")
public class BanCommand extends AbstractCommand {

    public BanCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_BAN)
    @Signature(help = "Ban a user from the server by his username")
    public void banCommand(@Parameter(name = "username") String username) {
        IUser user = DiscordUtils.findUserWithReply(message, guild.getUsers(), username);
        if (user != null) {
            ban(user);
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_BAN)
    @Signature(help = "Ban the mentioned user from the server")
    public void banCommand(@Parameter(name = "userMention") IUser user) {
        ban(user);
    }

    private void ban(IUser user) {
        PermissionUtils.requirePermissions(guild, botUser, EnumSet.of(Permissions.BAN));

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
