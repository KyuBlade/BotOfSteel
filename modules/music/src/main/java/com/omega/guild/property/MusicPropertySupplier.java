package com.omega.guild.property;

import com.omega.database.entity.property.BooleanProperty;
import com.omega.database.entity.property.ChannelProperty;
import com.omega.database.entity.property.LongProperty;

public class MusicPropertySupplier implements PropertySupplier {

    public static final PropertyDefinition MUSIC_CHANNEL_TEXT = new PropertyDefinition(
        "music_channel_text", new ChannelProperty(null), new MusicTextChannelPropertyChangeTask()
    );
    public  static final PropertyDefinition MUSIC_CHANNEL_VOICE = new PropertyDefinition(
        "music_channel_voice", new ChannelProperty(null), new MusicVoiceChannelPropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_QUEUE_LOOP = new PropertyDefinition(
        "music_queue_loop", new BooleanProperty(false), new MusicQueueLoopPropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_QUEUE_SHUFFLE = new PropertyDefinition(
        "music_queue_shuffle", new BooleanProperty(false), new MusicQueueShufflePropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_VOLUME = new PropertyDefinition(
        "music_volume", new LongProperty(50L), new MusicVolumePropertyChangeTask()
    );

    public MusicPropertySupplier() {
    }

    @Override
    public PropertyDefinition[] supply() {
        return new PropertyDefinition[]{
            MUSIC_CHANNEL_TEXT, MUSIC_CHANNEL_VOICE, MUSIC_QUEUE_LOOP, MUSIC_QUEUE_SHUFFLE, MUSIC_VOLUME
        };
    }
}
