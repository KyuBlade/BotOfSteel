package com.omega.command.impl;

import com.omega.MusicPermissionSupplier;
import com.omega.command.*;
import com.omega.database.entity.Playlist;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.PlaylistRepository;
import com.omega.util.MessageUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;

@Command(name = "playlists", aliases = "pl")
public class PlaylistListCommand extends AbstractCommand {

    public PlaylistListCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_LIST)
    @Signature(help = "Get the list of playlists")
    public void playlistListCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        StringBuilder builder = new StringBuilder();
        builder.append(MessageBuilder.Styles.CODE.getMarkdown()).append('\n');

        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);

        List<? extends Playlist> userPlaylists = playlistRepository.findBasicPrivate(by);

        printPlaylistsCategory(builder, "Private", userPlaylists);
        builder.append('\n');

        List<? extends Playlist> guildPlaylists = playlistRepository.findBasicPublic(message.getGuild());
        printPlaylistsCategory(builder, "Guild", guildPlaylists);

        builder.append(MessageBuilder.Styles.CODE.getReverseMarkdown());

        MessageUtils.sendPrivateMessage(by, builder.toString());
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_LIST_USER)
    @Signature(help = "Show playlist for the mentioned user")
    public void playlistListCommand(@Parameter(name = "user") IUser user) {
        MessageUtils.reply(message, "Not for you");
    }

    private void printPlaylistsCategory(StringBuilder builder, String categoryName, List<? extends Playlist> playlists) {
        builder.append(categoryName).append(" :").append('\n').append('\n');
        if (!playlists.isEmpty()) {
            playlists.forEach(playlist -> builder.append(playlist.getName()).append('(').append(playlist.getSize()).append(")\n"));
        } else {
            builder.append("None\n");
        }
    }
}
