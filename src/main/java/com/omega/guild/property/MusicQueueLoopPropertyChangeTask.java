package com.omega.guild.property;

import com.omega.database.entity.property.BooleanProperty;
import com.omega.guild.GuildContext;

public class MusicQueueLoopPropertyChangeTask implements PropertyChangeTask<BooleanProperty> {

    @Override
    public void execute(GuildContext context, BooleanProperty property, boolean init) {
        context.getAudioPlayer().setLoop(property.getValue());
    }
}
