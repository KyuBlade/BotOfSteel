package com.omega.command.impl;

import com.omega.BotManager;
import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

@Command(name = "kick")
public class KickCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(KickCommand.class);

    public KickCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_KICK)
    @Signature(help = "Kick a motherfucker from his username")
    public void kickCommand(@Parameter(name = "username") String username) {
        List<IUser> foundUsers = message.getGuild().getUsersByName(username);
        if (foundUsers.size() > 0) {
            IUser user = foundUsers.get(0);
            kick(user);
        } else {
            MessageUtil.reply(message, "User with username " + username + " not found");
        }
    }


    @Permission(permission = CorePermissionSupplier.COMMAND_KICK)
    @Signature(help = "Kick a motherfucker from a mention")
    public void kickCommand(@Parameter(name = "user") IUser user) {
        kick(user);
    }

    private void kick(IUser user) {
        String reply;
        if (user.isBot()) {
            reply = "Nope :angry:";
        } else {
            IUser owner = BotManager.getInstance().getApplicationOwner();
            if (owner == null) {
                reply = "Unable to determine user";
            } else if (owner.getID().equals(user.getID())) {
                reply = "I won't kick my master :heart_eyes:";
            } else {
                reply = "GTFO " + user.getName();
            }
        }

        MessageUtil.reply(message, reply);
    }
}
