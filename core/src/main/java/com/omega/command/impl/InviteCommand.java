package com.omega.command.impl;

import com.omega.BotManager;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Permission;
import com.omega.command.Signature;
import com.omega.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.*;

import java.util.EnumSet;

@Command(name = "invite", aliases = "inv")
public class InviteCommand extends AbstractCommand {

    public InviteCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_INVITE)
    @Signature(help = "Get the bot invite link")
    public void inviteCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        IDiscordClient client = BotManager.getInstance().getClient();
        BotInviteBuilder builder = new BotInviteBuilder(client);
        String clientId = client.getApplicationClientID();

        String link = builder
            .withClientID(clientId)
            .withPermissions(
                EnumSet.of(
                    Permissions.EMBED_LINKS, Permissions.MANAGE_CHANNELS,
                    Permissions.READ_MESSAGES, Permissions.READ_MESSAGE_HISTORY,
                    Permissions.MANAGE_PERMISSIONS,
                    Permissions.SEND_MESSAGES, Permissions.MANAGE_CHANNEL,
                    Permissions.MANAGE_MESSAGES, Permissions.VOICE_CONNECT,
                    Permissions.VOICE_MUTE_MEMBERS, Permissions.VOICE_SPEAK,
                    Permissions.KICK, Permissions.BAN
                )
            ).build();

        sendPrivateStateMessage(
            new EmbedBuilder()
                .appendField("Invitation link", link, false)
                .build()
        );
    }
}
