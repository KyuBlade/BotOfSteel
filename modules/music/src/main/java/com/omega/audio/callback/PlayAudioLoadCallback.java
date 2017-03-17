package com.omega.audio.callback;

import com.omega.util.MessageUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.obj.IMessage;

public class PlayAudioLoadCallback implements AudioLoadResultHandler {

    private final IMessage message;

    public PlayAudioLoadCallback(IMessage message) {
        this.message = message;
    }

    @Override
    public void trackLoaded(AudioTrack track) {

    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        MessageUtil.reply(message, "Will play playlist " + playlist.getName() +
            "(" + playlist.getTracks().size() + " tracks).");
    }

    @Override
    public void noMatches() {
        MessageUtil.reply(message, "No tracks found.");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        MessageUtil.reply(message, "Everything exploded, try again or not.");
    }
}
