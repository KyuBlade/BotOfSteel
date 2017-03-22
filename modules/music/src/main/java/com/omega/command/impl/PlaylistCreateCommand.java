package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.*;
import com.omega.database.entity.Playlist;
import com.omega.exception.PlaylistAlreadyExists;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "newPlaylist", aliases = "np")
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
            MessageUtil.reply(message, "Wrong privacy");
        }
    }

    private void createPlaylist(String name, Playlist.Privacy privacy) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        try {
            audioPlayer.createPlaylist(name, privacy, message.getGuild(), by);
            MessageUtil.reply(message, "Added playlist " + name);
        } catch (PlaylistAlreadyExists e) {
            MessageUtil.reply(message, "Playlist " + name + " already exists");
        }
    }
}
