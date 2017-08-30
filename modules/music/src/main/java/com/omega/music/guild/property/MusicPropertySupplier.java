package com.omega.music.guild.property;

import com.omega.core.database.entity.property.BooleanProperty;
import com.omega.core.database.entity.property.ChannelProperty;
import com.omega.core.database.entity.property.LongProperty;
import com.omega.core.guild.property.PropertyDefinition;
import com.omega.core.guild.property.PropertySupplier;

public class MusicPropertySupplier implements PropertySupplier {

    public static final PropertyDefinition MUSIC_CHANNEL_TEXT = new PropertyDefinition(
        "music_channel_text", new ChannelProperty(null), new MusicTextChannelGuildPropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_CHANNEL_VOICE = new PropertyDefinition(
        "music_channel_voice", new ChannelProperty(null), new MusicVoiceChannelGuildPropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_QUEUE_LOOP = new PropertyDefinition(
        "music_queue_loop", new BooleanProperty(false), new MusicQueueLoopGuildPropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_QUEUE_SHUFFLE = new PropertyDefinition(
        "music_queue_shuffle", new BooleanProperty(false), new MusicQueueShuffleGuildPropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_VOLUME = new PropertyDefinition(
        "music_volume", new LongProperty(50L), new MusicVolumeGuildPropertyChangeTask()
    );
    public static final PropertyDefinition MUSIC_ANNOUNCE_TRACK = new PropertyDefinition(
        "music_announce_track", new BooleanProperty(true), null
    );

    public MusicPropertySupplier() {
    }

    @Override
    public PropertyDefinition[] supply() {
        return new PropertyDefinition[]{
            MUSIC_CHANNEL_TEXT, MUSIC_CHANNEL_VOICE, MUSIC_QUEUE_LOOP, MUSIC_QUEUE_SHUFFLE, MUSIC_VOLUME,
            MUSIC_ANNOUNCE_TRACK
        };
    }
}
