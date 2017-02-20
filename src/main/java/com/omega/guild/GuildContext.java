package com.omega.guild;


import com.omega.BotManager;
import com.omega.audio.AudioPlayerManager;
import com.omega.audio.AudioPlayerStateListener;
import com.omega.audio.GuildAudioPlayer;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.GuildPropertiesRepository;
import com.omega.event.GuildPropertyChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Arrays;

public class GuildContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildContext.class);

    private final IGuild guild;

    private GuildProperties properties;

    private final GuildAudioPlayer audioPlayer;

    public GuildContext(IGuild guild) {
        this.guild = guild;

        GuildPropertiesRepository propertiesRepository = DatastoreManagerSingleton.getInstance().getRepository(GuildPropertiesRepository.class);
        this.properties = propertiesRepository.findByGuild(guild);
        if (properties != null) {
            LOGGER.debug("Saved properties found, {}", properties.toString());
        } else {
            LOGGER.debug("Loading default properties");
            this.properties = new GuildProperties(guild);
            initProperties();
        }

        audioPlayer = new GuildAudioPlayer(AudioPlayerManager.getInstance(), guild);

        guild.getAudioManager().setAudioProvider(audioPlayer.getAudioProvider());
        audioPlayer.addListener(new AudioPlayerStateListener(this));

        BotManager.getInstance().registerEventListener(this);
    }

    private void initProperties() {
        Arrays.stream(Property.values()).forEach(property -> properties.setProperty(property, property.getDefaultValue()));
    }

    public void destroy() {
        audioPlayer.cleanup();
    }

    public GuildProperties getProperties() {
        return properties;
    }

    public GuildAudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    @EventSubscriber
    public void onGuildPropertyChange(GuildPropertyChangedEvent event) {
        if (event.getGuild().getID().equals(guild.getID())) {
            Property property = event.getProperty();
            switch (property) {
                case MUSIC_TEXT_CHANNEL:
                    if (event.isInit()) {
                        if (event.getValue() == null) {
                            properties.setProperty(Property.MUSIC_TEXT_CHANNEL, guild.getChannels().get(0));
                        }
                    }
                    break;
                case MUSIC_VOICE_CHANNEL:
                    break;
                case MUSIC_QUEUE_LOOP:
                    audioPlayer.setLoop((boolean) event.getValue());
                    break;
                case MUSIC_QUEUE_SHUFFLE:
                    audioPlayer.setShuffle((boolean) event.getValue());
                    break;
                case COMMAND_PREFIX:
                    break;
            }
        }
    }
}
