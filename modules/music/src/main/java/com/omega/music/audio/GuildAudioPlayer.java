package com.omega.music.audio;

import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildModuleComponent;
import com.omega.music.audio.loader.AddToPlaylistTrackLoader;
import com.omega.music.audio.loader.PlayPlaylistTrackLoader;
import com.omega.music.database.PlaylistRepository;
import com.omega.music.database.entity.Playlist;
import com.omega.music.exception.PlaylistAlreadyExists;
import com.omega.music.exception.PlaylistNotFoundException;
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
import sx.blah.discord.util.PermissionUtils;
import sx.blah.discord.util.RequestBuffer;

import java.util.EnumSet;
import java.util.List;

/**
 * A {@code GuildAudioPlayer} handle all the audio processes and executions for one guild.
 */
public class GuildAudioPlayer implements GuildModuleComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildAudioPlayer.class);

    private final com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager manager;
    private final AudioPlayer player;
    private final TrackScheduler scheduler;

    private final PlayPlaylistTrackLoader ppTrackLoader;

    private final GuildContext guildContext;
    private final IGuild guild;

    public GuildAudioPlayer(com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager manager, GuildContext guildContext) {
        this.manager = manager;
        this.player = manager.createPlayer();

        this.scheduler = new TrackScheduler(player);
        this.guildContext = guildContext;
        this.guild = guildContext.getGuild();

        player.addListener(scheduler);
        player.addListener(new AudioPlayerStateListener(guildContext));

        this.ppTrackLoader = new PlayPlaylistTrackLoader(manager, scheduler);

        guild.getAudioManager().setAudioProvider(getAudioProvider());
    }

    /**
     * Add an audio event listener to the audio player.
     *
     * @param listener audio listener to add
     */
    public void addListener(AudioEventListener listener) {
        this.player.addListener(listener);
    }

    /**
     * Add tracks to a playlist.
     *
     * @param playlistName    playlist to add to
     * @param source          the track(s) source to add
     * @param callbackHandler result callback
     * @throws PlaylistNotFoundException if playlist was not found
     */
    public void addToPlaylist(String playlistName, String source, AudioLoadResultHandler callbackHandler)
        throws PlaylistNotFoundException {
        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        Playlist playlist = playlistRepository.findByName(playlistName);
        if (playlist == null) {
            throw new PlaylistNotFoundException();
        }

        AddToPlaylistTrackLoader loader = new AddToPlaylistTrackLoader(manager, playlist);
        loader.setCallbackHandler(callbackHandler);
        loader.loadOrdered(playlistName, source);
    }

    /**
     * Add the source track(s) to the queue.
     *
     * @param source          source to add
     * @param addHead         true to add the tracks to the head, false to add to the tail
     * @param callbackHandler result callback
     */
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

    /**
     * Clear the queue, add the playlist to it and start to play.
     *
     * @param playlistName playlist to play
     * @throws PlaylistNotFoundException if playlist don't exist
     */
    public void playPlaylist(String playlistName) throws PlaylistNotFoundException {
        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        Playlist playlist = playlistRepository.findByName(playlistName);

        if (playlist == null) {
            throw new PlaylistNotFoundException();
        }

        ppTrackLoader.load(playlist);
    }

    /**
     * Add the source to the head of the queue and play it immediately.
     *
     * @param source          the source to play
     * @param callbackHandler result callback
     */
    public void play(String source, AudioLoadResultHandler callbackHandler) {
        queue(source, true, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (scheduler.queueSize() > 0) {
                    skip();
                }

                if (callbackHandler != null) {
                    callbackHandler.trackLoaded(track);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (scheduler.queueSize() > 0) {
                    skip();
                }

                if (callbackHandler != null) {
                    callbackHandler.playlistLoaded(playlist);
                }
            }

            @Override
            public void noMatches() {
                if (callbackHandler != null) {
                    callbackHandler.noMatches();
                }
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if (callbackHandler != null) {
                    callbackHandler.loadFailed(exception);
                }
            }
        });
    }

    /**
     * Clear the queue.
     */
    public void clearQueue() {
        scheduler.clear();
    }

    /**
     * Pause or resume the audio player.
     *
     * @param pause true to pause, false to resume
     */
    public void pause(boolean pause) {
        player.setPaused(pause);
    }

    /**
     * @return true if the player is paused, false otherwise
     */
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
     * Seek to the given position.
     *
     * @param position position to seek to
     */
    public void seek(long position) {
        scheduler.seek(position);
    }

    /**
     * Randomize the queue.
     */
    public void shuffle() {
        scheduler.shuffle();
    }

    /**
     * @param playlistName The name of the new playlist
     * @param guild        The guild where this playlist was created
     * @param user         The author of the playlist
     * @return The created playlist or null
     * @throws PlaylistAlreadyExists if a playlist with this name already exist
     */
    public Playlist createPlaylist(String playlistName, Playlist.Privacy privacy, IGuild guild, IUser user) throws PlaylistAlreadyExists {
        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        boolean exists = playlistRepository.exists(playlistName);
        if (!exists) {
            Playlist playlist = playlistRepository.create(playlistName, privacy, guild, user);
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
     * @throws DiscordException if discord error
     * @throws MissingPermissionsException if MANAGE_CHANNEL or MANAGE_PERMISSIONS is missing
     */
    public void setMusicChannel(IVoiceChannel voiceChannel) throws DiscordException, MissingPermissionsException {
        IUser botUser = voiceChannel.getClient().getOurUser();

        PermissionUtils.requirePermissions(voiceChannel, botUser, EnumSet.of(
            Permissions.MANAGE_CHANNEL, Permissions.MANAGE_PERMISSIONS
        ));

        // Remove all role permission overrides
        voiceChannel.getRoleOverrides().forEach((roleId, permissionOverride) -> {
            final IRole role = guild.getRoleByID(roleId);

            RequestBuffer.request(() -> {
                    try {
                        voiceChannel.removePermissionsOverride(role);
                    } catch (MissingPermissionsException ignored) {
                    }
                }
            );
        });

        // Remove all user permission overrides
        voiceChannel.getUserOverrides().forEach((userId, permissionOverride) -> {
            final IUser user = guild.getUserByID(userId);

            RequestBuffer.request(() ->
                voiceChannel.removePermissionsOverride(user)
            );
        });


        // Remove VOICE_SPEAK permission for @everyone
        RequestBuffer.request(() ->
            voiceChannel.overrideRolePermissions(
                guild.getEveryoneRole(),
                null,
                EnumSet.of(Permissions.VOICE_SPEAK)
            )
        );

        // Add VOICE_SPEAK permission to bot
        RequestBuffer.request(() ->
            voiceChannel.overrideUserPermissions(
                botUser,
                EnumSet.of(Permissions.VOICE_SPEAK),
                null
            )
        );

        RequestBuffer.request(() ->
            voiceChannel.changeBitrate(96000)
        );

        if (!voiceChannel.isConnected()) {
            RequestBuffer.request(voiceChannel::join);
        }
    }

    /**
     * @return if queue is played in loop
     */
    public boolean isLoop() {
        return scheduler.isLoop();
    }

    /**
     * Enable or disable the queue loop mode.
     *
     * @param loop true to loop, false otherwise
     */
    public void setLoop(boolean loop) {
        scheduler.setLoop(loop);
    }

    /**
     * @return the state of the shuffle mode
     */
    public boolean isShuffle() {
        return scheduler.isShuffle();
    }

    /**
     * Activate or deactivate the shuddle mode.
     *
     * @param shuffle true to activate, false otherwise
     */
    public void setShuffle(boolean shuffle) {
        scheduler.setShuffle(shuffle);
    }

    /**
     * @return the playing track
     */
    public AudioTrack getPlayingTrack() {
        return scheduler.getPlayingTrack();
    }

    /**
     * @return the audio player volume
     */
    public int getVolume() {
        return player.getVolume();
    }

    /**
     * Set audio player volume.
     *
     * @param volume volume in range 0 to 150
     */
    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    /**
     * Cleanup the audio player.
     */
    public void cleanup() {
        player.destroy();
    }

    /**
     * @return an immutable list of the queue
     */
    public List<AudioTrack> getQueue() {
        return scheduler.getQueue();
    }

    /**
     * @return the audio provider providing audio frames.
     */
    public AudioProvider getAudioProvider() {
        return new AudioProvider(player);
    }

    /**
     * @return the guild linked to this player
     */
    public IGuild getGuild() {
        return guild;
    }
}
