package com.omega.music.database.impl.morphia;

import com.omega.core.database.impl.morphia.repository.MorphiaBaseRepository;
import com.omega.music.database.PlaylistRepository;
import com.omega.music.database.entity.Playlist;
import com.omega.music.database.impl.morphia.entity.MorphiaPlaylist;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class MorphiaPlaylistRepository extends MorphiaBaseRepository implements PlaylistRepository {

    public MorphiaPlaylistRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public Playlist create() {
        return new MorphiaPlaylist();
    }

    @Override
    public Playlist create(String name, Playlist.Privacy privacy, IGuild guild, IUser user) {
        return new MorphiaPlaylist(name, privacy, guild, user);
    }

    @Override
    public Playlist create(String name, IGuild guild, IUser user) {
        return new MorphiaPlaylist(name, guild, user);
    }

    @Override
    public Playlist findById(Object id) {
        return datastore.get(MorphiaPlaylist.class, id);
    }

    @Override
    public void save(Playlist entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(Playlist entity) {
        datastore.delete(entity);
    }

    public boolean exists(String playlistName) {
        return datastore
            .createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase()).count() >= 1;
    }

    @Override
    public void deleteByName(String playlistName) {
        datastore
            .delete(datastore.createQuery(MorphiaPlaylist.class)
                .field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase()));
    }

    @Override
    public Playlist findByName(String playlistName) {
        return datastore
            .createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase())
            .get();
    }

    @Override
    public List<MorphiaPlaylist> findBasicPrivate(IUser user) {
        Query<MorphiaPlaylist> query = datastore.createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.user.name()).equal(user);

        query.project(Playlist.Fields.tracks.name(), false);

        return query.asList();
    }

    @Override
    public List<MorphiaPlaylist> findBasicPublic(IGuild guild) {
        Query<MorphiaPlaylist> query = datastore
            .createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.guild.name()).equal(guild)
            .field(Playlist.Fields.privacy.name()).equal(Playlist.Privacy.GUILD);

        query.project(Playlist.Fields.tracks.name(), false);

        return query.asList();
    }
}
