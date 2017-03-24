package com.omega.audio;

import com.omega.guild.GuildContext;
import com.omega.guild.property.MusicPropertySupplier;
import com.omega.util.MessageUtil;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import sx.blah.discord.handle.obj.IChannel;

public class AudioPlayerStateListener extends AudioEventAdapter {

    private GuildContext guildContext;

    public AudioPlayerStateListener(GuildContext guildContext) {
        this.guildContext = guildContext;
    }

    @Override
    public void onTrackStart(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, com.sedmelluq.discord.lavaplayer.track.AudioTrack track) {
        if (guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_ANNOUNCE_TRACK, Boolean.class)) {
            IChannel textChannel = guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, IChannel.class);
            MessageUtil.sendMessage(textChannel, "Playing track " + track.getInfo().title);
        }
    }

    @Override
    public void onTrackStuck(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, com.sedmelluq.discord.lavaplayer.track.AudioTrack track, long thresholdMs) {
        IChannel textChannel = guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, IChannel.class);
        MessageUtil.sendMessage(textChannel, "Track is stuck, skipping to next track");
    }

    @Override
    public void onPlayerPause(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        IChannel textChannel = guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, IChannel.class);
        MessageUtil.sendMessage(textChannel, "Audio player paused");
    }

    @Override
    public void onPlayerResume(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        IChannel textChannel = guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, IChannel.class);
        MessageUtil.sendMessage(textChannel, "Audio player resumed");
    }
}
