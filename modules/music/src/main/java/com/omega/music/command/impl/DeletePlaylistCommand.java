package com.omega.music.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.music.MusicPermissionSupplier;
import com.omega.music.database.PlaylistRepository;
import com.omega.music.database.entity.Playlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@CommandInfo(name = "deletePlaylist", aliases = "dp")
public class DeletePlaylistCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeletePlaylistCommand.class);

    public DeletePlaylistCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_DELETE)
    @Signature(help = "Remove the specified playlist")
    public void deletePlaylist(String playlistName) {
        PlaylistRepository repository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        Playlist playlist = repository.findByName(playlistName);

        if (playlist != null) {
            Playlist.Privacy privacy = playlist.getPrivacy();

            if (privacy.equals(Playlist.Privacy.USER) && by.equals(playlist.getUser())) { // Requested by the owner of the playlist
                repository.delete(playlist);

                sendPrivateStateMessage("Deleted playlist named " + playlist.getName());
            } else if (privacy.equals(Playlist.Privacy.GUILD)) { // Requested by anyone in the guild
                IGuild guild = playlist.getGuild();

                if (guild == null) {
                    LOGGER.warn("Playlist {} doesn't have a guild bound to it", playlist.getName());

                    sendErrorMessage("No guild bound to the playlist");
                } else if (guild.getOwnerLongID() == by.getLongID()) { // Requested by the owner of the playlist
                    repository.delete(playlist);

                    sendStateMessage("Deleted playlist named " + playlist.getName());
                } else {
                    sendErrorMessage("You are not the owner of the playlist named " + playlist.getName());
                }
            }
        } else {
            sendErrorMessage("Playlist with name " + playlistName + " not found");
        }
    }
}
