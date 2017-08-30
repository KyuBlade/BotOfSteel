package com.omega.music.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import com.omega.music.MusicPermissionSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.EmbedBuilder;

@CommandInfo(name = "leave", aliases = "l")
public class LeaveCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveCommand.class);

    public LeaveCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_LEAVE)
    @Signature(help = "Leave the current voice channel")
    public void leaveCommand() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        IVoiceChannel voiceChannel = guild.getConnectedVoiceChannel();
        if (voiceChannel != null) {
            voiceChannel.leave();

            embedBuilder.withDescription("Left voice channel " + voiceChannel.getName() + " :'(");
        } else {
            embedBuilder.withDescription("I'm not in a voice channel 0_0");
        }

        sendStateMessage(embedBuilder.build());
    }
}
