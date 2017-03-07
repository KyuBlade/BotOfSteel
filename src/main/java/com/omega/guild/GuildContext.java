package com.omega.guild;


import com.omega.audio.AudioPlayerManager;
import com.omega.audio.AudioPlayerStateListener;
import com.omega.audio.GuildAudioPlayer;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.GuildPropertiesRepository;
import com.omega.database.entity.GuildProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;

public class GuildContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildContext.class);

    private final IGuild guild;

    private GuildProperties properties;

    private final GuildAudioPlayer audioPlayer;

    public GuildContext(IGuild guild) {
        this.guild = guild;

        audioPlayer = new GuildAudioPlayer(AudioPlayerManager.getInstance(), guild);

        guild.getAudioManager().setAudioProvider(audioPlayer.getAudioProvider());
        audioPlayer.addListener(new AudioPlayerStateListener(this));

        GuildPropertiesRepository propertiesRepository = DatastoreManagerSingleton.getInstance().getRepository(GuildPropertiesRepository.class);
        this.properties = propertiesRepository.findByGuild(guild);
        if (properties != null) {
            properties.initLoad(this);
            LOGGER.debug("Saved properties found, {}", properties.toString());
        } else {
            LOGGER.debug("Loading default properties");
            this.properties = propertiesRepository.create(guild);
            properties.initDefault(this);
        }
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

    public IGuild getGuild() {
        return guild;
    }
}
