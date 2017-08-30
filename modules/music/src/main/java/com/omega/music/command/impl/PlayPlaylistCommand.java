package com.omega.music.command.impl;

import com.omega.core.command.*;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.music.MusicModule;
import com.omega.music.MusicPermissionSupplier;
import com.omega.music.audio.GuildAudioPlayer;
import com.omega.music.exception.PlaylistNotFoundException;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@CommandInfo(name = "playPlaylist", aliases = "pp")
public class PlayPlaylistCommand extends AbstractCommand {

    public PlayPlaylistCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAYLIST_PLAY)
    @Signature(help = "Clear the queue and add the specified playlist to queue")
    public void playCommand(@Parameter(name = "playlistName") String playlistName) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        try {
            audioPlayer.playPlaylist(playlistName);
            sendStateMessage("Will play playlist " + playlistName);
        } catch (PlaylistNotFoundException e) {
            sendErrorMessage("Playlist " + playlistName + " not found");
        }
    }
}
