package com.omega.guild.property;

import com.omega.MusicModule;
import com.omega.audio.GuildAudioPlayer;
import com.omega.database.entity.property.BooleanProperty;
import com.omega.guild.GuildContext;

public class MusicQueueLoopGuildPropertyChangeTask implements GuildPropertyChangeTask<BooleanProperty> {

    @Override
    public void execute(GuildContext context, BooleanProperty property, boolean init) {
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) context.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        audioPlayer.setLoop(property.getValue());
    }
}
