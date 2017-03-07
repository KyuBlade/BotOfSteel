package com.omega.guild.property;

import com.omega.database.entity.property.*;

public enum PropertyDefinition {

    MUSIC_TEXT_CHANNEL("music_channel_text", new ChannelProperty(null), new MusicTextChannelPropertyChangeTask()),
    MUSIC_VOICE_CHANNEL("music_channel_voice", new ChannelProperty(null), new MusicVoiceChannelPropertyChangeTask()),
    MUSIC_QUEUE_LOOP("music_queue_loop", new BooleanProperty(false), new MusicQueueLoopPropertyChangeTask()),
    MUSIC_QUEUE_SHUFFLE("music_queue_shuffle", new BooleanProperty(false), new MusicQueueShufflePropertyChangeTask()),
    COMMAND_PREFIX("command_prefix", new StringProperty("!"), new CommandPrefixPropertyChangeTask()),
    MUSIC_VOLUME("music_volume", new LongProperty(50L), new MusicVolumePropertyChangeTask());

    private final String propertyKey;
    private final Property defaultProperty;
    private final PropertyChangeTask task;

    PropertyDefinition(String propertyKey, Property defaultProperty, PropertyChangeTask task) {
        this.propertyKey = propertyKey;
        this.defaultProperty = defaultProperty;
        this.task = task;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public Property getDefaultProperty() {
        return defaultProperty;
    }

    public PropertyChangeTask getTask() {
        return task;
    }
}
