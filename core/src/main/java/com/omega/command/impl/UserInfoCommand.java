package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.time.format.DateTimeFormatter;

@Command(name = "userinfo", aliases = "ui")
public class UserInfoCommand extends AbstractCommand {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

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
            .appendField("Creation date", user.getCreationDate().format(DATE_FORMATTER), true)
            .appendField("Guild join date", guild.getJoinTimeForUser(user).format(DATE_FORMATTER), true);

        sendPrivateStateMessage(embedBuilder.build());
    }
}
