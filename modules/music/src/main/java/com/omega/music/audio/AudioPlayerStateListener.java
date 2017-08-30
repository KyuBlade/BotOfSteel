package com.omega.music.audio;

import com.omega.core.guild.GuildContext;
import com.omega.core.util.MessageUtils;
import com.omega.music.guild.property.MusicPropertySupplier;
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
            MessageUtils.sendMessage(textChannel, "Playing track " + track.getInfo().title);
        }
    }

    @Override
    public void onTrackStuck(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, com.sedmelluq.discord.lavaplayer.track.AudioTrack track, long thresholdMs) {
        IChannel textChannel = guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, IChannel.class);
        MessageUtils.sendMessage(textChannel, "Track is stuck, skipping to next track");
    }

    @Override
    public void onPlayerPause(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        IChannel textChannel = guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, IChannel.class);
        MessageUtils.sendMessage(textChannel, "Audio player paused");
    }

    @Override
    public void onPlayerResume(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        IChannel textChannel = guildContext.getProperties().getProperty(MusicPropertySupplier.MUSIC_CHANNEL_TEXT, IChannel.class);
        MessageUtils.sendMessage(textChannel, "Audio player resumed");
    }
}
