package com.omega;

import com.omega.audio.AudioPlayerStateListener;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.CommandManager;
import com.omega.command.impl.MusicCommandSupplier;
import com.omega.database.DatastoreManager;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.entity.property.BotProperties;
import com.omega.database.entity.property.GuildProperties;
import com.omega.database.impl.morphia.AudioTrackMorphiaRepository;
import com.omega.database.impl.morphia.MorphiaDatastoreManager;
import com.omega.database.impl.morphia.MorphiaPlaylistRepository;
import com.omega.event.GuildContextCreatedEvent;
import com.omega.event.GuildContextDestroyedEvent;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.guild.property.MusicPropertySupplier;
import com.omega.module.Module;
import com.omega.property.MusicBotPropertySupplier;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Collection;

public class MusicModule extends Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicModule.class);

    public static final String AUDIO_PLAYER_COMPONENT = "audioPlayer";

    private AudioPlayerManager audioManager;

    @Override
    protected boolean load(IDiscordClient iDiscordClient) {
        LOGGER.info("Enable music module");
        DatastoreManager datastoreManager = DatastoreManagerSingleton.getInstance();
        if (datastoreManager instanceof MorphiaDatastoreManager) {
            Datastore datastore = ((MorphiaDatastoreManager) datastoreManager).getDatastore();
            datastoreManager.addRepositories(
                new AudioTrackMorphiaRepository(datastore),
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
                AudioTrackMorphiaRepository.class,
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
        return "0.3.0";
    }

    @Override
    public String getMinimumDiscord4JVersion() {
        return "2.7.0";
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
        IGuild guild = guildContext.getGuild();
        GuildAudioPlayer audioPlayer = new GuildAudioPlayer(audioManager, guild);

        guild.getAudioManager().setAudioProvider(audioPlayer.getAudioProvider());
        audioPlayer.addListener(new AudioPlayerStateListener(guildContext));

        guildContext.putModuleComponent(AUDIO_PLAYER_COMPONENT, audioPlayer);
    }
}
