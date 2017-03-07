package com.omega.database.impl.morphia.entity;

import com.omega.database.entity.AudioTrack;
import com.omega.database.entity.Playlist;
import com.omega.database.impl.morphia.converter.GuildTypeConverter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Converters;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "playlist", noClassnameStored = true)
@Converters(value = GuildTypeConverter.class)
public class MorphiaPlaylist extends Playlist {

    @Id
    private ObjectId id;

    private String name;

    private String normalizedName;

    @Embedded
    private List<AudioTrack> tracks;
    private int size;

    private IUser user;
    private IGuild guild;

    private Privacy privacy;

    public MorphiaPlaylist() {
    }

    public MorphiaPlaylist(String name, Privacy privacy, IGuild guild, IUser user) {
        this.name = name;
        this.normalizedName = name.toLowerCase();

        this.tracks = new ArrayList<>();
        this.guild = guild;
        this.user = user;
        this.privacy = privacy;
    }

    public MorphiaPlaylist(String name, IGuild guild, IUser user) {
        this(name, Privacy.USER, guild, user);
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<? extends AudioTrack> getTracks() {
        return tracks;
    }

    @Override
    public void addTrack(AudioTrack audioTrack) {
        this.tracks.add(audioTrack);
    }

    @Override
    public void removeTrack(AudioTrack audioTrack) {
        this.tracks.remove(audioTrack);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public void setUser(IUser user) {
        this.user = user;
    }

    @Override
    public IGuild getGuild() {
        return guild;
    }

    @Override
    public void setGuild(IGuild guild) {
        this.guild = guild;
    }

    @Override
    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    @Override
    public Privacy getPrivacy() {
        return privacy;
    }
}
