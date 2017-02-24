package com.omega.audio.loader;

import com.omega.audio.TrackScheduler;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.PlaylistRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueTrackLoader extends TrackLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueTrackLoader.class);

    private final TrackScheduler scheduler;

    public QueueTrackLoader(AudioPlayerManager manager, TrackScheduler scheduler) {
        super(manager);

        this.scheduler = scheduler;
    }

    @Override
    void trackLoaded(AudioTrack track) {
        LOGGER.info("Loaded track : {}", track.getInfo().title);
        scheduler.queue(track);
    }

    @Override
    void playlistLoaded(AudioPlaylist playlist) {
        LOGGER.info("Loaded playlist : {} ({} tracks)", playlist.getName(), playlist.getTracks().size());
        scheduler.queue(playlist);
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
