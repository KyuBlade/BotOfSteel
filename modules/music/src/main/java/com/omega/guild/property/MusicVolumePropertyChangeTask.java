package com.omega.guild.property;

import com.omega.MusicModule;
import com.omega.audio.GuildAudioPlayer;
import com.omega.database.entity.property.LongProperty;
import com.omega.guild.GuildContext;

public class MusicVolumePropertyChangeTask implements PropertyChangeTask<LongProperty> {

    @Override
    public void execute(GuildContext context, LongProperty property, boolean init) {
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) context.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        audioPlayer.setVolume(property.getValue().intValue());
    }
}
