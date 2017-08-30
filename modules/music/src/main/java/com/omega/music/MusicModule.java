package com.omega.music;


import com.omega.core.PermissionManager;
import com.omega.core.command.CommandManager;
import com.omega.core.database.DatastoreManager;
import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.core.database.entity.property.BotProperties;
import com.omega.core.database.entity.property.GuildProperties;
import com.omega.core.database.impl.morphia.MorphiaDatastoreManager;
import com.omega.core.event.GuildContextCreatedEvent;
import com.omega.core.event.GuildContextDestroyedEvent;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.core.module.Module;
import com.omega.music.audio.GuildAudioPlayer;
import com.omega.music.command.impl.MusicCommandSupplier;
import com.omega.music.database.impl.morphia.MorphiaAudioTrackRepository;
import com.omega.music.database.impl.morphia.MorphiaPlaylistRepository;
import com.omega.music.guild.property.MusicPropertySupplier;
import com.omega.music.property.MusicBotPropertySupplier;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;

import java.util.Collection;

public class MusicModule extends Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicModule.class);

    public static final String AUDIO_PLAYER_COMPONENT = "audioPlayer";

    private AudioPlayerManager audioManager;

    @Override
    protected boolean load(IDiscordClient client) {
        LOGGER.info("Enable music module");
        DatastoreManager datastoreManager = DatastoreManagerSingleton.getInstance();
        if (datastoreManager instanceof MorphiaDatastoreManager) {
            Datastore datastore = ((MorphiaDatastoreManager) datastoreManager).getDatastore();
            datastoreManager.addRepositories(
                new MorphiaAudioTrackRepository(datastore),
                new MorphiaPlaylistRepository(datastore)
            );
        }

        audioManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioManager);

        BotProperties.supply(new MusicBotPropertySupplier());
        PermissionManager.getInstance().supply(new MusicPermissionSupplier());
        CommandManager.getInstance().supply(new MusicCommandSupplier());
        GuildProperties.supply(new MusicPropertySupplier());

        Collection<GuildContext> guildContexts = GuildManager.getInstance().getGuildContexts();
        guildContexts.forEach(this::initializeAudioPlayer);

        LOGGER.info("Music module enabled");

        return true;
    }

    @Override
    protected void unload() {
        LOGGER.info("Disable music module");
        CommandManager.getInstance().unsupply(new MusicCommandSupplier());
        BotProperties.unsupply(new MusicBotPropertySupplier());
        GuildProperties.unsupply(new MusicPropertySupplier());
        PermissionManager.getInstance().unsupply(new MusicPermissionSupplier());

        DatastoreManager datastoreManager = DatastoreManagerSingleton.getInstance();
        if (datastoreManager instanceof MorphiaDatastoreManager) {
            datastoreManager.removeRepositories(
                MorphiaAudioTrackRepository.class,
                MorphiaPlaylistRepository.class
            );
        }

        audioManager.shutdown();
        Collection<GuildContext> guildContexts = GuildManager.getInstance().getGuildContexts();
        guildContexts.forEach(guildContext -> {
            GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.removeModuleComponent(AUDIO_PLAYER_COMPONENT);
            audioPlayer.cleanup();
        });
    }

    @Override
    public String getName() {
        return "music";
    }

    @Override
    public String getAuthor() {
        return "Kyu";
    }

    @Override
    public String getVersion() {
        return "0.4.0";
    }

    @Override
    public String getMinimumDiscord4JVersion() {
        return "2.8.1";
    }

    @EventSubscriber
    public void onGuildContextCreated(GuildContextCreatedEvent event) {
        LOGGER.info("Init audio player");
        GuildContext guildContext = event.getGuildContext();
        initializeAudioPlayer(guildContext);
    }

    @EventSubscriber
    public void onGuildContextDestroyed(GuildContextDestroyedEvent event) {
        GuildContext guildContext = event.getGuildContext();
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.removeModuleComponent(AUDIO_PLAYER_COMPONENT);
        if (audioPlayer != null) {
            audioPlayer.cleanup();
        }
    }

    private void initializeAudioPlayer(GuildContext guildContext) {
        GuildAudioPlayer audioPlayer = new GuildAudioPlayer(audioManager, guildContext);
        guildContext.putModuleComponent(AUDIO_PLAYER_COMPONENT, audioPlayer);

        LOGGER.info("Audio player component set");
    }
}
