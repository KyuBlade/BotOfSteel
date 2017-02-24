package com.omega.command.impl;

import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.SenderUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;

@Command(name = "seek")
public class SeekCommand extends AbstractCommand {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SeekCommand.class);

    public SeekCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Seek to the given position. Joint hours, minutes and seconds by a ':'. eg. 10:54:37 or 1:30 or just 45")
    public void seekCommand(@Parameter(name = "time") String time) {
        String[] split = time.split(":");
        ArrayUtils.reverse(split);
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        LOGGER.debug("Split : " + Arrays.toString(split));
        try {
            if (split.length > 0) {
                for (int i = split.length - 1; i >= 0; i--) {
                    switch (i) {
                        case 0: // seconds
                            seconds = Long.parseLong(split[i]);
                            break;
                        case 1: // minutes
                            minutes = Long.parseLong(split[i]);
                            break;
                        case 2: // hours
                            hours = Long.parseLong(split[i]);
                            break;
                    }
                }
            } else {
                seconds = Long.parseLong(time);
            }

            seekCommand(hours, minutes, seconds);
        } catch (NumberFormatException e) {
            SenderUtil.reply(message, "Malformed time");
        }

    }

    @Signature(help = "Seek the at given seconds")
    public void seekCommand(@Parameter(name = "seconds") Long seconds) {
        seekCommand(0L, 0L, seconds);
    }

    @Signature(help = "Seek at the given minutes and seconds")
    public void seekCommand(@Parameter(name = "minutes") Long minutes, @Parameter(name = "seconds") Long seconds) {
        seekCommand(0L, minutes, seconds);
    }

    @Signature(help = "Seek at the given hours, minutes and seconds")
    public void seekCommand(@Parameter(name = "hours") Long hours, @Parameter(name = "minutes") Long minutes, @Parameter(name = "seconds") Long seconds) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = guildContext.getAudioPlayer();

        LOGGER.debug("{}:{}:{}", hours, minutes, seconds);

        long seekTime = hours * 60 * 60;
        seekTime += minutes * 60;
        seekTime += seconds;
        seekTime *= 1000L;

        if (seekTime < 0) {
            SenderUtil.reply(message, "Seek time must not be negative");
        } else {
            try {
                audioPlayer.seek(seekTime);
                SenderUtil.reply(message, DurationFormatUtils.formatDuration(seekTime, "HH:mm:ss"));
            } catch (IllegalStateException e) {
                SenderUtil.reply(message, "No track playing");
            }
        }
    }
}
