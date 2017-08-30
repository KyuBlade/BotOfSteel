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
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@CommandInfo(name = "resume", aliases = "rsm")
public class ResumeCommand extends AbstractCommand {

    public ResumeCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_RESUME)
    @Signature(help = "Resume audio player")
    public void resumeCommand() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        audioPlayer.pause(false);
    }
}
