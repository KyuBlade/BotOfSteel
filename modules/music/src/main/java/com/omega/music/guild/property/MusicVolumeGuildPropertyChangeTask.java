package com.omega.music.guild.property;

import com.omega.core.database.entity.property.LongProperty;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.property.GuildPropertyChangeTask;
import com.omega.music.MusicModule;
import com.omega.music.audio.GuildAudioPlayer;

public class MusicVolumeGuildPropertyChangeTask implements GuildPropertyChangeTask<LongProperty> {

    @Override
    public void execute(GuildContext context, LongProperty property, boolean init) {
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) context.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        audioPlayer.setVolume(
            property.getValue()
                .intValue()
        );
    }
}
