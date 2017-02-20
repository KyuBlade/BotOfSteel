package com.omega.audio;

import com.omega.audio.loader.AddToPlaylistTrackLoader;
import com.omega.audio.loader.PlayPlaylistTrackLoader;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.PlaylistRepository;
import com.omega.exception.PlaylistAlreadyExists;
import com.omega.exception.PlaylistNotFoundException;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.*;

public class GuildAudioPlayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildAudioPlayer.class);

    private final com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager manager;
    private final AudioPlayer player;
    private final TrackScheduler scheduler;

    private final PlayPlaylistTrackLoader ppTrackLoader;

    private final IGuild guild;

    public GuildAudioPlayer(com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager manager, IGuild guild) {
        this.manager = manager;
        this.player = manager.createPlayer();

        this.scheduler = new TrackScheduler(player);
        this.guild = guild;

        player.addListener(scheduler);

        this.ppTrackLoader = new PlayPlaylistTrackLoader(manager, scheduler);
    }

    public void addListener(AudioEventListener listener) {
        this.player.addListener(listener);
    }

    public void addToPlaylist(String playlistName, String source, AudioLoadResultHandler callbackHandler)
        throws PlaylistNotFoundException {
        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        Playlist playlist = playlistRepository.findByName(playlistName);
        if (playlist == null) {
            throw new PlaylistNotFoundException();
        }

        AddToPlaylistTrackLoader loader = new AddToPlaylistTrackLoader(manager, playlist);
        loader.setCallbackHandler(callbackHandler);
        loader.load(source);
    }

    public void queue(String source, boolean addHead, AudioLoadResultHandler callbackHandler) {
        manager.loadItem(source, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                LOGGER.info("Loaded track : {}", track.getInfo().title);
                scheduler.queue(track, addHead);
                if (callbackHandler != null) {
                    callbackHandler.trackLoaded(track);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                LOGGER.info("Loaded playlist : {} ({} tracks)", playlist.getName(), playlist.getTracks().size());
                playlist.getTracks().forEach(scheduler::queue);

                if (callbackHandler != null) {
                    callbackHandler.playlistLoaded(playlist);
                }
            }

            @Override
            public void noMatches() {
                LOGGER.info("No matches found");
                if (callbackHandler != null) {
                    callbackHandler.noMatches();
                }
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                LOGGER.warn("Source loading failed", exception);
                if (callbackHandler != null) {
                    callbackHandler.loadFailed(exception);
                }
            }
        });
    }

    public void playPlaylist(String playlistName) throws PlaylistNotFoundException {
        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        Playlist playlist = playlistRepository.findByName(playlistName);
        if (playlist == null) {
            throw new PlaylistNotFoundException();
        }

        ppTrackLoader.load(playlist);
    }

    public void play(String source) {
        queue(source, true, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                skip();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                skip();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }

    public void pause(boolean pause) {
        player.setPaused(pause);
    }

    public boolean isPause() {
        return player.isPaused();
    }

    /**
     * Skip the current track.
     */
    public void skip() {
        skip(1);
    }

    /**
     * Skip {@code count} tracks.
     *
     * @param count the number of tracks to skip
     */
    public void skip(int count) {
        scheduler.skip(count);
    }

    /**
     * @param playlistName The name of the new playlist
     * @param guild        The guild where this playlist was created
     * @param user         The author of the playlist
     * @return The created playlist or null
     * @throws PlaylistAlreadyExists
     */
    public Playlist createPlaylist(String playlistName, Playlist.Privacy privacy, IGuild guild, IUser user) throws PlaylistAlreadyExists {
        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        boolean exists = playlistRepository.exists(playlistName);
        if (!exists) {
            Playlist playlist = new Playlist(playlistName, privacy, guild, user);
            playlistRepository.save(playlist);

            return playlist;
        } else {
            throw new PlaylistAlreadyExists();
        }
    }

    /**
     * Set a voice channel as music channel.
     * Sets the bitrate to the max and disallow users to talk.
     *
     * @param voiceChannel voice channel to set-up
     * @throws DiscordException
     * @throws RateLimitException
     * @throws MissingPermissionsException
     */
    public void setMusicChannel(IVoiceChannel voiceChannel) throws DiscordException, RateLimitException,
        MissingPermissionsException {
        Set<String> roles = voiceChannel.getRoleOverrides().keySet();
        Iterator<String> it = roles.iterator();
        while (it.hasNext()) {
            IRole role = guild.getRoleByID(it.next());
            if (!role.isEveryoneRole() && !role.isManaged()) {
                voiceChannel.removePermissionsOverride(role);
            } else {
                voiceChannel.overrideRolePermissions(role, null, EnumSet.of(Permissions.VOICE_SPEAK));
            }
        }

        Set<String> users = voiceChannel.getUserOverrides().keySet();
        it = users.iterator();
        while (it.hasNext()) {
            IUser user = guild.getUserByID(it.next());
            if (!user.isBot()) {
                voiceChannel.removePermissionsOverride(user);
            } else {
                voiceChannel.overrideUserPermissions(user, null, EnumSet.of(Permissions.VOICE_SPEAK));
            }
        }

        voiceChannel.changeBitrate(96000);

        if (!voiceChannel.isConnected()) {
            voiceChannel.join();
        }
    }

    public AudioTrack getPlayingTrack() {
        return scheduler.getPlayingTrack();
    }

    public void cleanup() {
        player.destroy();
    }

    public List<AudioTrack> getQueue() {
        return scheduler.getQueue();
    }

    public AudioProvider getAudioProvider() {
        return new AudioProvider(player);
    }

    public IGuild getGuild() {
        return guild;
    }
}
