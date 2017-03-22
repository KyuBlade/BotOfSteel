package com.omega.database;

import com.omega.database.entity.AudioTrack;
import com.omega.database.repository.Repository;

public interface AudioTrackRepository extends Repository<AudioTrack> {

    AudioTrack create(com.sedmelluq.discord.lavaplayer.track.AudioTrack audioTrack);

    AudioTrack create(String title, String author, String source, int length);
}
