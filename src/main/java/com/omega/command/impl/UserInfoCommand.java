package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
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

    @Signature(help = "Get info on a user")
    public void userinfoCommand(@Parameter(name = "user") IUser user) {
        String message = String.format(TEMPLATE, user.getAvatarURL(),
            user.getName(), user.getPresence());

        SenderUtil.sendPrivateMessage(by, message);
    }
}
