package com.omega.audio.callback;

import com.omega.util.SenderUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class QueueAudioLoadCallback implements AudioLoadResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueAudioLoadCallback.class);

    private final IMessage message;

    public QueueAudioLoadCallback(IMessage message) {
        this.message = message;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        SenderUtil.reply(message, "Track " + track.getInfo().title + " added to queue");
        deleteMessage();
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        SenderUtil.reply(message, "Playlist " + playlist.getName() + " (" + playlist.getTracks().size() + " tracks) added to queue");
        deleteMessage();
    }

    @Override
    public void noMatches() {
        SenderUtil.reply(message, "The provided source may not be supported");
        deleteMessage();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        if (!exception.severity.equals(FriendlyException.Severity.COMMON)) {
            SenderUtil.reply(message, "Unable to add to queue");
            deleteMessage();
        }
    }

    private void deleteMessage() {
        try {
            message.delete();
        } catch (MissingPermissionsException e) {
            LOGGER.warn("Permission needed : {}", e.getMissingPermissions());
        } catch (RateLimitException | DiscordException e) {
            LOGGER.warn("Unable to delete message", e);
        }
    }
}
