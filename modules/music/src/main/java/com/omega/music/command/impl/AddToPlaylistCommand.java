package com.omega.music.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.music.MusicModule;
import com.omega.music.MusicPermissionSupplier;
import com.omega.music.audio.GuildAudioPlayer;
import com.omega.music.audio.callback.AddToPlaylistAudioLoadCallback;
import com.omega.music.exception.PlaylistNotFoundException;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.awt.*;

@CommandInfo(name = "addToPlaylist", aliases = {"atp"})
public class AddToPlaylistCommand extends AbstractCommand {

    public AddToPlaylistCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_ADD)
    @Signature(help = "Add the source track(s) to the specified playlist")
    public void addToPlaylistCommand(String playlistName, String source) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        sendStateMessage("Processing tracks to add, please wait ...", Color.BLUE);

        try {
            audioPlayer.addToPlaylist(playlistName, source,
                new AddToPlaylistAudioLoadCallback(this, playlistName));
        } catch (PlaylistNotFoundException e) {
            sendErrorMessage("Playlist " + playlistName + " not found");
        }
    }
}
