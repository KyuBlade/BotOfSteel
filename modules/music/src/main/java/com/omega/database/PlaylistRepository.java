package com.omega.database;

import com.omega.database.entity.Playlist;
import com.omega.database.repository.Repository;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public interface PlaylistRepository extends Repository<Playlist> {

    Playlist create(String name, Playlist.Privacy privacy, IGuild guild, IUser user);

    Playlist create(String name, IGuild guild, IUser user);

    boolean exists(String playlistName);

    void deleteByName(String PlaylistName);

    Playlist findByName(String playlistName);

    /**
     * @param user the playlist owner
     * @return the playlists(without loading tracks) whose owner is {@code user}
     */
    List<? extends Playlist> findBasicPrivate(IUser user);

    /**
     * @param guild the playlist guild ownership
     * @return the playlists(without loading tracks) whose guild ownership is {@code guild}
     */
    List<? extends Playlist> findBasicPublic(IGuild guild);
}
