package com.omega.music.audio.callback;

import com.omega.music.command.impl.SearchCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.util.EmbedBuilder;

public class SearchAudioLoadCallback implements AudioLoadResultHandler {

    private final SearchCommand command;
    private final String keywords;

    public SearchAudioLoadCallback(SearchCommand command, String keywords) {
        this.command = command;
        this.keywords = keywords;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withTitle(track.getInfo().title)
            .withUrl(track.getInfo().uri)
            .withDescription("Track added to queue")
            .appendField("Keywords", keywords, true);

        command.sendStateMessage(embedBuilder.build());
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withTitle(playlist.getName() + " (" + playlist.getTracks().size() + " tracks)")
            .withDescription("Playlist added to queue")
            .appendField("Keywords", keywords, true);

        command.sendStateMessage(embedBuilder.build());
    }

    @Override
    public void noMatches() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.withTitle("YouTube search").withDescription("The provided source may not be supported");

        command.sendStateMessage(embedBuilder.build());
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        command.sendExceptionMessage("YouTube search", "Unable to add to queue", exception,
            new Embed.EmbedField("Keywords", keywords, true)
        );
    }
}
