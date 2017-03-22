package com.omega.database.impl.morphia;

import com.omega.database.AudioTrackRepository;
import com.omega.database.entity.AudioTrack;
import com.omega.database.impl.morphia.entity.MorphiaAudioTrack;
import com.omega.database.impl.morphia.repository.MorphiaBaseRepository;
import org.mongodb.morphia.Datastore;

public class AudioTrackMorphiaRepository extends MorphiaBaseRepository implements AudioTrackRepository {

    public AudioTrackMorphiaRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public AudioTrack create() {
        return new MorphiaAudioTrack();
    }

    @Override
    public MorphiaAudioTrack create(com.sedmelluq.discord.lavaplayer.track.AudioTrack audioTrack) {
        return new MorphiaAudioTrack(audioTrack);
    }

    @Override
    public MorphiaAudioTrack create(String title, String author, String source, int length) {
        return new MorphiaAudioTrack(title, author, source, length);
    }

    @Override
    public AudioTrack findById(Object id) {
        return datastore.get(MorphiaAudioTrack.class, id);
    }

    @Override
    public void save(AudioTrack entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(AudioTrack entity) {
        datastore.delete(entity);
    }
}
