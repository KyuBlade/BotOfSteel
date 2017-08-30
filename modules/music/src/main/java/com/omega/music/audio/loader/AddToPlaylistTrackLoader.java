package com.omega.music.audio.loader;

import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.music.database.AudioTrackRepository;
import com.omega.music.database.PlaylistRepository;
import com.omega.music.database.entity.Playlist;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddToPlaylistTrackLoader extends TrackLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddToPlaylistTrackLoader.class);

    private final Playlist playlist;

    public AddToPlaylistTrackLoader(AudioPlayerManager manager, Playlist playlist) {
        super(manager);

        this.playlist = playlist;
    }

    @Override
    void trackLoaded(AudioTrack track) {
        LOGGER.info("Loaded track : {}", track.getInfo().title);
        com.omega.music.database.entity.AudioTrack audioTrack = DatastoreManagerSingleton.getInstance()
            .getRepository(AudioTrackRepository.class)
            .create(track);
        playlist.addTrack(audioTrack);
        DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class)
            .save(playlist);
    }

    @Override
    void playlistLoaded(AudioPlaylist playlist) {
        LOGGER.info("Loaded playlist : {} ({} tracks)", playlist.getName(), playlist.getTracks().size());

        playlist.getTracks().forEach(track -> {
            com.omega.music.database.entity.AudioTrack audioTrack = DatastoreManagerSingleton.getInstance()
                .getRepository(AudioTrackRepository.class)
                .create(track);
            this.playlist.addTrack(audioTrack);
        });

        DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class)
            .save(this.playlist);
    }

    @Override
    void noMatches() {
        LOGGER.info("No matches found");
    }

    @Override
    void loadFailed(FriendlyException exception) {
        LOGGER.warn("Source loading failed", exception);
    }
}
