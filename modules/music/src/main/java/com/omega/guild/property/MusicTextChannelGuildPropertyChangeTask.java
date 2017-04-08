package com.omega.guild.property;

import com.omega.database.entity.property.ChannelProperty;
import com.omega.guild.GuildContext;
import sx.blah.discord.Discord4J;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

public class MusicTextChannelGuildPropertyChangeTask implements GuildPropertyChangeTask<ChannelProperty> {

    @Override
    public void execute(GuildContext context, ChannelProperty property, boolean init) {
        if (init) {
            IGuild guild = context.getGuild();

            Discord4J.LOGGER.debug("Init music_text_channel property for guild {}", guild.getName());
            List<IChannel> channels = guild.getChannels();
            IUser botUser = guild.getClient().getOurUser();

            IChannel writableChannel = channels.stream()
                .filter(channel -> channel.getModifiedPermissions(botUser).contains(Permissions.SEND_MESSAGES))
                .findFirst()
                .orElse(null);

            if (writableChannel != null) {
                context.getProperties().setProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, new ChannelProperty(writableChannel));
            }
        }
    }
}
