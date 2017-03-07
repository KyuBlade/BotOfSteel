package com.omega.guild.property;

import com.omega.database.entity.property.LongProperty;
import com.omega.guild.GuildContext;

public class MusicVolumePropertyChangeTask implements PropertyChangeTask<LongProperty> {

    @Override
    public void execute(GuildContext context, LongProperty property, boolean init) {
        context.getAudioPlayer().setVolume(property.getValue().intValue());
    }
}
