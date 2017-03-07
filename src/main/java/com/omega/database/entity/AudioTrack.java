package com.omega.database.entity;

public abstract class AudioTrack {

    public enum Fields {
        id, title, author, source, length;
    }

    public AudioTrack() {
    }

    public abstract Object getId();

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract String getAuthor();

    public abstract void setAuthor(String author);

    public abstract String getSource();

    public abstract void setSource(String source);

    public abstract long getLength();

    public abstract void setLength(long length);
}
