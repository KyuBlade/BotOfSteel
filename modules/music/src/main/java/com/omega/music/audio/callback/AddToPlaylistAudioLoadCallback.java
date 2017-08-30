package com.omega.music.audio.callback;

import com.omega.music.command.impl.AddToPlaylistCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.util.EmbedBuilder;

public class AddToPlaylistAudioLoadCallback implements AudioLoadResultHandler {

    private final AddToPlaylistCommand command;
    private final String playlistName;

    public AddToPlaylistAudioLoadCallback(AddToPlaylistCommand command, String playlistName) {
        this.command = command;
        this.playlistName = playlistName;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withTitle(track.getInfo().title)
            .withAuthorName(track.getInfo().author)
            .withUrl(track.getInfo().uri)
            .withDescription("Track added to playlist ")
            .appendDescription(playlistName);

        command.sendStateMessage(embedBuilder.build());
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        command.sendStateMessage(
            "Playlist " + playlist.getName() +
                " (" + playlist.getTracks().size() + " tracks) added to playlist " + playlistName
        );
    }

    @Override
    public void noMatches() {
        command.sendErrorMessage("The provided source may not be supported");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        command.sendExceptionMessage("Track load failed", exception);
    }
}
