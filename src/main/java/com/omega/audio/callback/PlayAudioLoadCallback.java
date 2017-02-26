package com.omega.audio.callback;

import com.omega.util.MessageUtil;
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

public class PlayAudioLoadCallback implements AudioLoadResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayAudioLoadCallback.class);

    private final IMessage message;

    public PlayAudioLoadCallback(IMessage message) {
        this.message = message;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        deleteMessage();
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        MessageUtil.reply(message, "Will play playlist {}({} tracks).");
        deleteMessage();
    }

    @Override
    public void noMatches() {
        MessageUtil.reply(message, "No tracks found.");
        deleteMessage();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        MessageUtil.reply(message, "Everything exploded, try again or not.");
        deleteMessage();
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
