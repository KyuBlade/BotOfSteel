package com.omega.core.command.impl;

import com.omega.core.BotManager;
import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import com.omega.core.util.MessageUtils;
import com.omega.core.util.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.EnumSet;

@CommandInfo(name = "botInfo")
public class BotInfoCommand extends AbstractCommand {

    public BotInfoCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_BOTINFO)
    @Signature(help = "Get information on the bot")
    public void botInfoCommand() {
        IDiscordClient client = by.getClient();
        IUser ownerUser = BotManager.getInstance().getApplicationOwner();

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long usedMemory = totalMemory - runtime.freeMemory();

        IChannel channel = message.getChannel();
        boolean privateChannel = channel.isPrivate();
        EnumSet<Permissions> channelPerms = channel.getModifiedPermissions(botUser);

        IGuild guild = message.getGuild();
        EnumSet<Permissions> guildPerms = null;
        if (!privateChannel) {
            guildPerms = botUser.getPermissionsForGuild(guild);
        }

        StringBuilder descBuilder = new StringBuilder();
        descBuilder
            .append(MessageBuilder.Styles.BOLD.getMarkdown())
            .append("General information : ")
            .append(MessageBuilder.Styles.BOLD.getReverseMarkdown()).append("\n\n")
            .append("Connected servers : ").append(client.getGuilds().size()).append('\n')
            .append("Uptime : ").append(DurationFormatUtils.formatDuration(runtimeMXBean.getUptime(), "HH:mm:ss")).append('\n')
            .append("Memory usage : ").append(StringUtils.formatBinarySize(usedMemory)).append('/')
            .append(StringUtils.formatBinarySize(totalMemory)).append("\n\n");

//        if (!privateChannel) {
//            GuildContext guildContext = GuildManager.getInstance().getContext(guild);
//            GuildAudioPlayer audioPlayer = guildContext.getAudioPlayer();
//            int volume = audioPlayer.getVolume();
//            AudioTrack currentTrack = audioPlayer.getPlayingTrack();
//            String trackTitle = null;
//            String trackPosition = null;
//            String trackState = null;
//            boolean trackSeekable = false;
//            boolean trackStream = false;
//            if (currentTrack != null) {
//                trackTitle = currentTrack.getInfo().title;
//                trackPosition = DurationFormatUtils.formatDuration(currentTrack.getPosition(), "HH:mm:ss") + " / "
//                    + DurationFormatUtils.formatDuration(currentTrack.getDuration(), "HH:mm:ss");
//                trackState = currentTrack.getState().name();
//                trackSeekable = currentTrack.isSeekable();
//                trackStream = currentTrack.getInfo().isStream;
//            }
//            boolean pauseState = audioPlayer.isPause();
//            boolean loopState = audioPlayer.isLoop();
//            boolean shuffleState = audioPlayer.isShuffle();
//
//            // Audio player
//            descBuilder
//                .append(MessageBuilder.Styles.BOLD.getMarkdown())
//                .append("Audio player : ")
//                .append(MessageBuilder.Styles.BOLD.getReverseMarkdown()).append("\n\n")
//                .append("Current track title : ").append((currentTrack != null) ? trackTitle : "None").append('\n');
//
//            if (currentTrack != null) {
//                descBuilder.append("Current track position : ").append(trackPosition).append('\n')
//                    .append("Current track state : ").append(trackState).append('\n')
//                    .append("Current track seekable : ").append(trackSeekable).append('\n')
//                    .append("Current track is stream : ").append(trackStream).append('\n');
//            }
//
//            descBuilder.append("Pause : ").append(pauseState).append('\n')
//                .append("Loop : ").append(loopState).append('\n')
//                .append("Shuffle : ").append(shuffleState).append('\n')
//                .append("Volume : ").append(volume).append('\n')
//                .append("\n\n");
//        }

        // Guild perms
        if (!privateChannel) {
            descBuilder
                .append(MessageBuilder.Styles.BOLD.getMarkdown())
                .append("Guild permissions : ")
                .append(MessageBuilder.Styles.BOLD.getReverseMarkdown()).append("\n\n")
                .append(guildPerms).append("\n\n");
        }

        // Channel perms
        descBuilder
            .append(MessageBuilder.Styles.BOLD.getMarkdown())
            .append("Channel permissions : ")
            .append(MessageBuilder.Styles.BOLD.getReverseMarkdown()).append("\n\n")
            .append(channelPerms);


        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
            .withColor(Color.GREEN)
            .withAuthorName(botUser.getName())
            .withAuthorUrl("https://github.com/KyuBlade/BotOfSteel")
            .withAuthorIcon(botUser.getAvatarURL())
            .withThumbnail(botUser.getAvatarURL())
            .withDescription(descBuilder.toString())
            .withFooterIcon(ownerUser.getAvatarURL())
            .withFooterText(ownerUser.getName());

        MessageUtils.sendPrivateMessage(by, "", embedBuilder.build());
    }
}
