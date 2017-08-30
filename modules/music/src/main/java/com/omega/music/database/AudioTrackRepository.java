package com.omega.music.database;

import com.omega.core.database.repository.Repository;
import com.omega.music.database.entity.AudioTrack;

public interface AudioTrackRepository extends Repository<AudioTrack> {

    AudioTrack create(com.sedmelluq.discord.lavaplayer.track.AudioTrack audioTrack);

    AudioTrack create(String title, String author, String source, int length);
}
