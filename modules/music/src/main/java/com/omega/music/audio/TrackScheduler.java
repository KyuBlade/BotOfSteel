package com.omega.music.audio;

import com.omega.music.exception.NotSeekableException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The track scheduler handle the offering of tracks to the audio player.
 */
public class TrackScheduler extends AudioEventAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackScheduler.class);

    private final AudioPlayer player;
    private final LinkedList<AudioTrack> queue;
    private boolean loop = false;
    private boolean shuffle = false;

    /**
     * @param player the player that will handle this scheduler.
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    /**
     * Add track(s) to the tail of the queue.
     *
     * @param track track to add to queue
     */
    public void queue(AudioTrack track) {
        queue(track, false);
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track   The track to play or add to queue.
     * @param addHead True to put it at the start of the queue
     */
    public void queue(AudioTrack track, boolean addHead) {
        boolean interupt = false;
        if (player.isPaused() || !(interupt = player.startTrack(track, true)) || loop) {
            AudioTrack queueTrack = (interupt) ? track.makeClone() : track;
            if (addHead) {
                queue.addFirst(queueTrack);
            } else {
                queue.addLast(queueTrack);
            }
        }
    }

    /**
     *
     */
    public void queue(AudioPlaylist playlist) {
        queue(playlist, false);
    }

    /**
     * @param playlist audio playlist to add to queue
     * @param addHead add at start or end of the queue
     */
    public void queue(AudioPlaylist playlist, boolean addHead) {
        playlist.getTracks().forEach(track -> queue(track, addHead));
    }

    /**
     * Stop and skip the current playing track.
     */
    public void skip() {
        skip(1);
    }

    /**
     * Skip x tracks and stop the one playing.
     *
     * @param count the number of tracks to skip
     */
    public void skip(int count) {
        IntStream.range(0, count - 1).forEach(value -> {
            AudioTrack track = queue.poll();
            if (loop) {
                queue(track);
            }
        });

        AudioTrack nextTrack = queue.poll();
        player.startTrack(nextTrack, false);
        if (loop) {
            queue.addLast(nextTrack.makeClone());
        }
    }

    /**
     * Seek to the given position.
     *
     * @param position position to seek to
     * @throws IllegalStateException if trying to seek while no track playing
     * @throws NotSeekableException if the current track is not seekable
     */
    public void seek(long position) {
        AudioTrack currentTrack = getPlayingTrack();
        if (currentTrack != null) {
            if (!currentTrack.isSeekable()) {
                throw new NotSeekableException();
            }

            currentTrack.setPosition(position);
        } else {
            throw new IllegalStateException("No track playing");
        }
    }

    /**
     * Randomize the queue.
     */
    public void shuffle() {
        Collections.shuffle(queue);
    }

    /**
     * Empty the queue.
     */
    public void clear() {
        queue.clear();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        LOGGER.debug("Track ended, reason : {}", endReason);
        if (endReason.mayStartNext) {
            skip();
        }
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
//        if (track.isSeekable()) {
//            AudioTrack newTrack = track.makeClone();
//            newTrack.setPosition(track.getPosition());
//            player.playTrack(newTrack);
//        } else {
        skip();
//        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        LOGGER.error("Error for track {}({})", track.getInfo().title, track.getIdentifier(), exception);
    }

    /**
     * Return a copy of the queue.
     * Changes to this list will not be reflected on the queue.
     *
     * @return the queue as a list
     */
    public List<AudioTrack> getQueue() {
        return Collections.unmodifiableList(queue);
    }

    /**
     * @return the size of the queue
     */
    public int queueSize() {
        return queue.size();
    }

    /**
     * @return if queue is played in loop
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * Set queue loop behavior.
     *
     * @param loop true to loop the queue, false otherwise
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /**
     * @return the state of the shuffle mode
     */
    public boolean isShuffle() {
        return shuffle;
    }

    /**
     * Activate or deactivate the shuffle mode.
     *
     * @param shuffle true to activate, false otherwise
     */
    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    /**
     * @return the current playing track
     */
    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }
}
