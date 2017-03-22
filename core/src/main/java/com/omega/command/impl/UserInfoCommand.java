package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "userinfo", aliases = "ui")
public class UserInfoCommand extends AbstractCommand {

    private static final String TEMPLATE =
        "Avatar : (%s)\n" +
            "Name : %s\n" +
            "Presence : %s\n";

    public UserInfoCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_USERINFO)
    @Signature(help = "Get info on a user")
    public void userinfoCommand(@Parameter(name = "user") IUser user) {
        String message = String.format(TEMPLATE, user.getAvatarURL(),
            user.getName(), user.getPresence());

        MessageUtil.sendPrivateMessage(by, message);
    }
}
