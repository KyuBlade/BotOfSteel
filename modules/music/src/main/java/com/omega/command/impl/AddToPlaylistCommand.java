package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.audio.callback.AddToPlaylistAudioLoadCallback;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Permission;
import com.omega.command.Signature;
import com.omega.exception.PlaylistNotFoundException;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "addToPlaylist", aliases = {"atp"})
public class AddToPlaylistCommand extends AbstractCommand {

    public AddToPlaylistCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_ADD)
    @Signature(help = "Add the source track(s) to the specified playlist")
    public void addToPlaylistCommand(String playlistName, String source) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        try {
            audioPlayer.addToPlaylist(playlistName, source,
                new AddToPlaylistAudioLoadCallback(message, playlistName));
        } catch (PlaylistNotFoundException e) {
            MessageUtil.reply(message, "Playlist " + playlistName + " not found");
        }
    }
}
