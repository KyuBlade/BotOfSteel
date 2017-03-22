package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.*;
import com.omega.exception.PlaylistNotFoundException;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "playPlaylist", aliases = "pp")
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
        } catch (PlaylistNotFoundException e) {
            MessageUtil.reply(message, "Playlist " + playlistName + " not found");
        }
    }
}
