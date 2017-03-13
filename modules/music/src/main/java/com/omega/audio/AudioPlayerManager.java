package com.omega.audio;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.audio.IAudioProvider;
import sx.blah.discord.util.audio.AudioPlayer;
import sx.blah.discord.util.audio.providers.AudioInputStreamProvider;

import javax.sound.sampled.AudioInputStream;
import java.io.File;

public class AudioPlayerManager extends DefaultAudioPlayerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioPlayerManager.class);

    private AudioPlayerManager() {
        AudioSourceManagers.registerRemoteSources(this);
        AudioSourceManagers.registerLocalSource(this);
    }

    public static AudioPlayerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static class Tracks {
        public static class Metadata {
            public static final String NAME = "title";
        }
    }

    public static class Track extends AudioPlayer.Track {

        private String title;
        private File audioFile;
        private String source;

        public Track(IAudioProvider provider) {
            super(provider);
        }

        public Track(AudioInputStreamProvider provider) {
            super(provider);
        }

        public Track(AudioInputStream stream) {
            super(stream);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public File getAudioFile() {
            return audioFile;
        }

        public void setAudioFile(File audioFile) {
            this.audioFile = audioFile;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    private static class SingletonHolder {
        private static final AudioPlayerManager INSTANCE = new AudioPlayerManager();
    }
}
