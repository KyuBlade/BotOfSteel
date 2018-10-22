package com.omega.core.command.impl;

import com.omega.core.command.*;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@CommandInfo(name = "userinfo", aliases = "ui")
public class UserInfoCommand extends AbstractCommand {

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")
            .withZone(ZoneId.systemDefault());

    public UserInfoCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_USERINFO)
    @Signature(help = "Get info on a user")
    public void userinfoCommand(@Parameter(name = "user") IUser user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withTitle("User info")
            .withThumbnail(user.getAvatarURL())
            .appendField("Avatar", user.getAvatarURL(), true)
            .appendField("Name", user.getName(), true)
            .appendField("Presence", user.getPresence().toString(), true)
            .appendField("Creation date", DATE_FORMATTER.format(user.getCreationDate()), true)
            .appendField("Guild join date", DATE_FORMATTER.format(guild.getJoinTimeForUser(user)), true);

        sendPrivateStateMessage(embedBuilder.build());
    }
}
