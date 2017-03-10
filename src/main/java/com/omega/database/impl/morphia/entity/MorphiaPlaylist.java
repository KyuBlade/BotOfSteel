package com.omega.database.impl.morphia.entity;

import com.omega.database.entity.AudioTrack;
import com.omega.database.entity.Playlist;
import com.omega.database.impl.morphia.converter.GuildTypeConverter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
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
    private List<MorphiaAudioTrack> tracks;

    private int size;

    @Embedded
    private IUser user;

    @Embedded
    private IGuild guild;

    private Privacy privacy;

    public MorphiaPlaylist() {
        this.tracks = new ArrayList<>();
    }

    public MorphiaPlaylist(String name, Privacy privacy, IGuild guild, IUser user) {
        this();

        this.name = name;
        this.normalizedName = name.toLowerCase();
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
        this.tracks.add((MorphiaAudioTrack) audioTrack);
        size++;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void removeTrack(AudioTrack audioTrack) {
        this.tracks.remove(audioTrack);
        size--;
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
