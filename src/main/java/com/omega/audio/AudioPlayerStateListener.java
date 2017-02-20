package com.omega.audio;

import com.omega.guild.GuildContext;
import com.omega.guild.Property;
import com.omega.util.SenderUtil;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import sx.blah.discord.handle.obj.IChannel;

public class AudioPlayerStateListener extends AudioEventAdapter {

    private GuildContext guildContext;

    public AudioPlayerStateListener(GuildContext guildContext) {
        this.guildContext = guildContext;
    }

    @Override
    public void onTrackStart(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, com.sedmelluq.discord.lavaplayer.track.AudioTrack track) {
        IChannel textChannel = guildContext.getProperties().getProperty(Property.MUSIC_TEXT_CHANNEL, IChannel.class);
        SenderUtil.sendMessage(textChannel, "Playing track " + track.getInfo().title);
    }

    @Override
    public void onTrackStuck(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, com.sedmelluq.discord.lavaplayer.track.AudioTrack track, long thresholdMs) {
        IChannel textChannel = guildContext.getProperties().getProperty(Property.MUSIC_TEXT_CHANNEL, IChannel.class);
        SenderUtil.sendMessage(textChannel, "Track is stuck, skipping to next track");
    }

    @Override
    public void onPlayerPause(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        IChannel textChannel = guildContext.getProperties().getProperty(Property.MUSIC_TEXT_CHANNEL, IChannel.class);
        SenderUtil.sendMessage(textChannel, "Audio player paused");
    }

    @Override
    public void onPlayerResume(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
        IChannel textChannel = guildContext.getProperties().getProperty(Property.MUSIC_TEXT_CHANNEL, IChannel.class);
        SenderUtil.sendMessage(textChannel, "Audio player resumed");
    }
}
