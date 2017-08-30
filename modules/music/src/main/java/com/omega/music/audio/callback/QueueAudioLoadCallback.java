package com.omega.music.audio.callback;

import com.omega.core.command.AbstractCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.util.EmbedBuilder;

public class QueueAudioLoadCallback implements AudioLoadResultHandler {

    private final AbstractCommand command;

    public QueueAudioLoadCallback(AbstractCommand command) {
        this.command = command;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withTitle(track.getInfo().title)
            .withUrl(track.getInfo().uri)
            .withDescription("Track added to queue");

        command.sendStateMessage(embedBuilder.build());
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withTitle(playlist.getName() + " (" + playlist.getTracks().size() + " tracks)")
            .withDescription("Playlist added to queue");

        command.sendStateMessage(embedBuilder.build());
    }

    @Override
    public void noMatches() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.withDescription("The provided source may not be supported");

        command.sendStateMessage(embedBuilder.build());
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        command.sendExceptionMessage("Unable to add to queue", exception);
    }
}
