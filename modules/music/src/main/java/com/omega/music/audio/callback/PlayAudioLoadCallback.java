package com.omega.music.audio.callback;

import com.omega.music.command.impl.PlayCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PlayAudioLoadCallback implements AudioLoadResultHandler {

    private final PlayCommand command;

    public PlayAudioLoadCallback(PlayCommand command) {
        this.command = command;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        command.sendStateMessage("Will play track " + track.getInfo().title);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        command.sendStateMessage("Will play playlist " + playlist.getName() +
            "(" + playlist.getTracks().size() + " tracks).");
    }

    @Override
    public void noMatches() {
        command.sendErrorMessage("No tracks found.");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        command.sendExceptionMessage("Everything exploded, try again or not.", exception);
    }
}
