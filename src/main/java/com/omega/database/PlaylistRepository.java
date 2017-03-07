package com.omega.database;

import com.omega.database.entity.Playlist;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public interface PlaylistRepository extends Repository<Playlist> {

    Playlist create(String name, Playlist.Privacy privacy, IGuild guild, IUser user);

    Playlist create(String name, IGuild guild, IUser user);

    boolean exists(String playlistName);

    void deleteByName(String PlaylistName);

    Playlist findByName(String playlistName);

    List<? extends Playlist> findByUserPrivacy(IUser user);

    List<? extends Playlist> findByGuildPrivacy(IGuild guild);
}
