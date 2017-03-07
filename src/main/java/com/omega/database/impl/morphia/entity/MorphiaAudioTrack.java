package com.omega.database.impl.morphia.entity;

import com.omega.database.entity.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "audio_track", noClassnameStored = true)
public class MorphiaAudioTrack extends AudioTrack {

    @Id
    private ObjectId id;

    private String title;
    private String author;
    private String source;
    private long length;

    public MorphiaAudioTrack() {
    }

    public MorphiaAudioTrack(com.sedmelluq.discord.lavaplayer.track.AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        this.title = info.title;
        this.author = info.author;
        this.source = info.identifier;
        this.length = info.length;
    }

    public MorphiaAudioTrack(String title, String author, String source, int length) {
        this.title = title;
        this.author = author;
        this.source = source;
        this.length = length;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public void setLength(long length) {
        this.length = length;
    }
}
