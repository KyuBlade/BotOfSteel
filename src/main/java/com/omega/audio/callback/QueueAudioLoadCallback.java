package com.omega.audio.callback;

import com.omega.util.MessageUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.obj.IMessage;

public class QueueAudioLoadCallback implements AudioLoadResultHandler {

    private final IMessage message;

    public QueueAudioLoadCallback(IMessage message) {
        this.message = message;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        MessageUtil.reply(message, "Track " + track.getInfo().title + " added to queue");
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        MessageUtil.reply(message, "Playlist " + playlist.getName() + " (" + playlist.getTracks().size() + " tracks) added to queue");
    }

    @Override
    public void noMatches() {
        MessageUtil.reply(message, "The provided source may not be supported");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        if (!exception.severity.equals(FriendlyException.Severity.COMMON)) {
            MessageUtil.reply(message, "Unable to add to queue");
        }
    }
}
