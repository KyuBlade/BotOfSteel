package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.DiscordUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.RequestBuffer;

import java.util.EnumSet;

@Command(name = "kick")
public class KickCommand extends AbstractCommand {

    public KickCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_KICK)
    @Signature(help = "Kick a user from the server by his username")
    public void kickCommand(@Parameter(name = "username") String username) {
        DiscordUtils.checkPermissions(botUser, guild, EnumSet.of(Permissions.KICK));

        IUser user = DiscordUtils.findUserWithReply(message, guild.getUsers(), username);
        if (user != null) {
            kick(user);
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_KICK)
    @Signature(help = "Kick the mentioned user from the server")
    public void kickCommand(@Parameter(name = "userMention") IUser user) {
        DiscordUtils.checkPermissions(botUser, guild, EnumSet.of(Permissions.KICK));

        kick(user);
    }

    private void kick(IUser user) {
        if (!user.isBot()) {
            RequestBuffer.request(() -> {
                guild.kickUser(user);

                sendStateMessage("Kicked user " + user);
            });
        } else {
            sendErrorMessage("You can't kick me");
        }
    }
}
