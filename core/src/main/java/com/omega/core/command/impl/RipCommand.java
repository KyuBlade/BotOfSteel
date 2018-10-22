package com.omega.core.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

@CommandInfo(name = "rip")
public class RipCommand extends AbstractCommand {

    public RipCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_RIP)
    @Signature(help = "Print the list of banned users")
    public void ripCommand() {
        if (!message.getChannel().isPrivate()) {
            final EmbedBuilder embedBuilder = new EmbedBuilder().setLenient(true);

            RequestBuffer.request(() -> {
                try {
                    List<IUser> bannedUsers = message.getGuild().getBannedUsers();

                    if (bannedUsers.isEmpty()) {
                        embedBuilder.withDescription("No one got banned on this server");
                    } else {
                        StringBuilder stringBuilder = new StringBuilder("**Rest In Peace**\n\n");

                        for (int i = 0; i < bannedUsers.size() && stringBuilder.length() < EmbedBuilder.DESCRIPTION_CONTENT_LIMIT; i++) {
                            IUser bannedUser = bannedUsers.get(i);

                            stringBuilder.append(":skull_crossbones: ")
                                .append(bannedUser.getName())
                                .append(" :skull_crossbones:");

                            if (i < bannedUsers.size()) {
                                stringBuilder.append('\n');
                            }
                        }

                        embedBuilder.withDescription(stringBuilder.toString());
                    }

                    sendStateMessage(embedBuilder.build());
                } catch (DiscordException e) {
                    sendExceptionMessage(
                        "rip command",
                        "Error occurred while getting list of banned users",
                        e
                    );
                } catch (MissingPermissionsException e) {
                    sendMissingPermissionsMessage(e.getMissingPermissions());
                }
            });
        } else {
            EmbedObject embedObject = new EmbedObject();
            embedObject.description = "Unavailable in private channel";

            sendStateMessage(embedObject);
        }
    }
}
