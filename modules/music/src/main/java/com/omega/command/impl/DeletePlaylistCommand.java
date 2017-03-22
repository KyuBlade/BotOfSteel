package com.omega.command.impl;

import com.omega.MusicPermissionSupplier;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Permission;
import com.omega.command.Signature;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.PlaylistRepository;
import com.omega.database.entity.Playlist;
import com.omega.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "deletePlaylist", aliases = "dp")
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
                MessageUtil.sendPrivateMessage(by, "Deleted playlist named " + playlist.getName());
            } else if (privacy.equals(Playlist.Privacy.GUILD)) { // Requested by anyone in the guild
                IGuild guild = playlist.getGuild();

                if (guild == null) {
                    LOGGER.warn("Playlist {} doesn't have a guild bound to it", playlist.getName());
                } else if (guild.getOwnerID().equals(by.getID())) { // Requested by the owner of the playlist
                    repository.delete(playlist);
                    MessageUtil.reply(message, "Deleted playlist named " + playlist.getName());
                } else {
                    MessageUtil.reply(message, "You are not the owner of the playlist named " + playlist.getName());
                }
            }
        } else {
            MessageUtil.reply(message, "Playlist with name " + playlistName + " not found");
        }
    }
}
