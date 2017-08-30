package com.omega.music.guild.property;

import com.omega.core.database.entity.property.BooleanProperty;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.property.GuildPropertyChangeTask;
import com.omega.music.MusicModule;
import com.omega.music.audio.GuildAudioPlayer;

public class MusicQueueShuffleGuildPropertyChangeTask implements GuildPropertyChangeTask<BooleanProperty> {

    @Override
    public void execute(GuildContext context, BooleanProperty property, boolean init) {
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) context.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        audioPlayer.setShuffle(property.getValue());
    }
}
