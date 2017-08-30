package com.omega.music.database.entity;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;
import java.util.List;

public abstract class Playlist {

    public enum Fields {
        id, name, normalizedName, tracks, size, user, guild, privacy
    }

    public enum Privacy {
        USER(0), GUILD(1);

        private int id;

        Privacy(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Privacy findById(int id) {
            return Arrays.stream(values()).filter(privacy1 -> privacy1.id == id).findFirst().orElse(null);
        }
    }


    public Playlist() {
    }

    public abstract Object getId();

    public abstract void setName(String name);

    public abstract String getName();

    public abstract List<? extends AudioTrack> getTracks();

    public abstract void addTrack(AudioTrack audioTrack);

    public abstract void removeTrack(AudioTrack audioTrack);

    public abstract int getSize();

    public abstract IUser getUser();

    public abstract void setUser(IUser user);

    public abstract IGuild getGuild();

    public abstract void setGuild(IGuild guild);

    public abstract void setPrivacy(Privacy privacy);

    public abstract Privacy getPrivacy();
}
