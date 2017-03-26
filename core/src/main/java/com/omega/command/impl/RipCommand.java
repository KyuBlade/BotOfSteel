package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Permission;
import com.omega.command.Signature;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

@Command(name = "rip")
public class RipCommand extends AbstractCommand {

    public RipCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_RIP)
    @Signature(help = "Print the list of banned users")
    public void ripCommand() {
        if (!message.getChannel().isPrivate()) {
            final StringBuilder builder = new StringBuilder();
            RequestBuffer.request(() -> {
                List<IUser> bannedUsers = message.getGuild().getBannedUsers();
                if (bannedUsers.isEmpty()) {
                    builder.append("**No one got banned on this server**");
                } else {
                    builder.append("**Rest In Peace**").append('\n').append('\n');
                    for (int i = 0; i < bannedUsers.size(); i++) {
                        IUser bannedUser = bannedUsers.get(i);
                        builder.append(":skull_crossbones: ").append(bannedUser).append(" :skull_crossbones:");

                        if (i < bannedUsers.size()) {
                            builder.append('\n');
                        }
                    }
                }
            }).get();
            RequestBuffer.request(() -> MessageUtil.sendMessage(message.getChannel(), builder.toString()));
        } else {
            MessageUtil.reply(message, "Unavailable in private channel");
        }
    }
}
