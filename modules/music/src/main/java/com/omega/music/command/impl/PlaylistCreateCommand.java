package com.omega.music.command.impl;

import com.omega.core.command.*;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.music.MusicModule;
import com.omega.music.MusicPermissionSupplier;
import com.omega.music.audio.GuildAudioPlayer;
import com.omega.music.database.entity.Playlist;
import com.omega.music.exception.PlaylistAlreadyExists;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@CommandInfo(name = "newPlaylist", aliases = "np")
public class PlaylistCreateCommand extends AbstractCommand {

    public PlaylistCreateCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_CREATE)
    @Signature(help = "Create a new private playlist")
    public void playlistCreateCommand(@Parameter(name = "playlistName") String playlistName) {
        createPlaylist(playlistName, Playlist.Privacy.USER);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_CREATE)
    @Signature(help = "Create a new playlist with the specified privacy (0 = private, 1 = public)")
    public void playlistCreateCommand(@Parameter(name = "playlistName") String playlistName, @Parameter(name = "privacy") Long privacy) {
        Playlist.Privacy resolvedPrivacy = Playlist.Privacy.findById(privacy.intValue());
        if (resolvedPrivacy != null) {
            createPlaylist(playlistName, resolvedPrivacy);
        } else {
            sendErrorMessage("Wrong privacy, must be 0 (private) or 1 (public)");
        }
    }

    private void createPlaylist(String name, Playlist.Privacy privacy) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        try {
            audioPlayer.createPlaylist(name, privacy, message.getGuild(), by);

            sendStateMessage("Added playlist " + name);
        } catch (PlaylistAlreadyExists e) {
            sendErrorMessage("Playlist " + name + " already exists");
        }
    }
}
