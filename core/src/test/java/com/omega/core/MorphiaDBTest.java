package com.omega.core;

import com.mongodb.MongoClient;
import com.omega.core.database.entity.property.GuildProperties;
import com.omega.core.database.impl.morphia.MorphiaDatastoreManager;
import com.omega.core.database.repository.GuildPropertiesRepository;
import com.omega.core.object.GuildMock;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class MorphiaDBTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MorphiaDBTest.class);

    private static final int PORT = 12345;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    private MongodExecutable mongodExe;
    private MongodProcess mongod;

    private MongoClient mongo;

    private MorphiaDatastoreManager datastoreManager;
    private Morphia morphia;

    @Before
    public void setUp() throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        mongodExe = starter.prepare(new MongodConfigBuilder()
            .version(Version.Main.DEVELOPMENT)
            .net(new Net("localhost", PORT, Network.localhostIsIPv6()))
            .cmdOptions(new MongoCmdOptionsBuilder()
                .syncDelay(10)
                .useNoPrealloc(false)
                .useSmallFiles(false)
                .useNoJournal(false)
                .enableTextSearch(true)
                .build())
            .build());
        mongod = mongodExe.start();
        mongo = new MongoClient("localhost", PORT);

        Morphia morphia = new Morphia();
        datastoreManager = new MorphiaDatastoreManager(morphia, morphia.createDatastore(mongo, "discord_bot_test"));
    }

    @After
    public void cleanup() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

//    @Test
//    public void canPersistAndLoadTracks() {
//        AudioTrackRepository repository = datastoreManager.getRepository(AudioTrackRepository.class);
//        AudioTrack track = repository.create("Test title", "Author here", "source here", 1545);
//
//        LOGGER.info("Persist track");
//        repository.save(track);
//        Object key = track;
//        LOGGER.info("Generated ID : {}", key);
//
//        LOGGER.info("Find persisted track");
//        AudioTrack loadedTrack = repository.findById(key);
//
//        assertNotNull("Unable to find persisted track", loadedTrack);
//        assertEquals("Loaded track title doesn't match with persisted one", track.getTitle(), loadedTrack.getTitle());
//    }
//
//    @Test
//    public void canPersistAndLoadPlaylist() {
//        AudioTrackRepository trackRepository = datastoreManager.getRepository(AudioTrackRepository.class);
//        PlaylistRepository playlistRepository = datastoreManager.getRepository(PlaylistRepository.class);
//        Playlist playlist = playlistRepository.create("Playlist test", Playlist.Privacy.USER, null, null);
//        AudioTrack firstTrack = trackRepository.create("Test title", "Author here", "source here", 1545);
//        AudioTrack secondTrack = trackRepository.create("Test title 2", "Author here", "source here", 45448);
//
//        playlist.addTrack(firstTrack);
//        playlist.addTrack(secondTrack);
//        int playlistSize = playlist.getTracks().size();
//
//        LOGGER.info("Persist playlist");
//        playlistRepository.save(playlist);
//        Object key = playlist.getId();
//        LOGGER.info("Generated ID : {}", key);
//
//        LOGGER.info("Find persisted playlist");
//        Playlist loadedPlaylist = playlistRepository.findById(key);
//        int loadedPlaylistSize = loadedPlaylist.getTracks().size();
//
//        assertNotNull("Unable to find persisted track", loadedPlaylist);
//        assertEquals(
//            String.format(
//                "Loaded playlist doesn't have the same number of tracks than the original one (%s / %s)",
//                loadedPlaylistSize, playlistSize
//            ), loadedPlaylistSize, playlistSize);
//    }

    @Test
    public void canPersistAndLoadGuildProperties() {
        GuildPropertiesRepository repository = datastoreManager.getRepository(GuildPropertiesRepository.class);

        GuildProperties guildProps = repository.create(new GuildMock("265847274377052161"));

        repository.save(guildProps);
    }
}
