package com.omega.database.impl.morphia;

import com.omega.database.PlaylistRepository;
import com.omega.database.entity.Playlist;
import com.omega.database.impl.morphia.entity.MorphiaPlaylist;
import org.mongodb.morphia.Datastore;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class PlaylistMorphiaRepository extends MorphiaBaseRepository implements PlaylistRepository {

    public PlaylistMorphiaRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public Playlist create() throws IllegalAccessException, InstantiationException {
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
        return datastore.createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase()).count() >= 1;
    }

    @Override
    public void deleteByName(String playlistName) {
        datastore.delete(datastore.createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase()));
    }

    @Override
    public Playlist findByName(String playlistName) {
        return datastore.createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase())
            .get();
    }

    @Override
    public List<MorphiaPlaylist> findByUserPrivacy(IUser user) {
        return datastore.createQuery(MorphiaPlaylist.class).field(Playlist.Fields.user.name()).equal(user.getID())
            .field(Playlist.Fields.privacy.name()).equal(Playlist.Privacy.USER.name())
            .asList();
    }

    @Override
    public List<MorphiaPlaylist> findByGuildPrivacy(IGuild guild) {
        return datastore.createQuery(MorphiaPlaylist.class)
            .field(Playlist.Fields.guild.name()).equal(guild)
            .field(Playlist.Fields.privacy.name()).equal(Playlist.Privacy.GUILD.name())
            .asList();
    }
}
