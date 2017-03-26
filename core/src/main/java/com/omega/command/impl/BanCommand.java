package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.DiscordUtils;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IGuild;
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
        if (DiscordUtils.checkPermissions(message, EnumSet.of(Permissions.BAN))) {
            IGuild guild = message.getGuild();
            IUser user = DiscordUtils.findUserWithReply(message, guild.getUsers(), username);

            if (user != null) {
                ban(user);
            }
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_BAN)
    @Signature(help = "Ban the mentioned user from the server")
    public void banCommand(@Parameter(name = "userMention") IUser user) {
        if (DiscordUtils.checkPermissions(message, EnumSet.of(Permissions.BAN))) {
            ban(user);
        }
    }

    private void ban(IUser user) {
        final IGuild guild = message.getGuild();
        if (!user.isBot()) {
            RequestBuffer.request(() -> {
                guild.banUser(user);

                MessageUtil.sendMessage(message.getChannel(), "Banned user " + user);
            });
        } else {
            MessageUtil.reply(message, "You can't ban me");
        }
    }
}
