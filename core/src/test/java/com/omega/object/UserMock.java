package com.omega.object;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IShard;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserMock implements IUser {

    private final String id;

    public UserMock(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getAvatar() {
        return null;
    }

    @Override
    public String getAvatarURL() {
        return null;
    }

    @Override
    public IPresence getPresence() {
        return null;
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public String getDisplayName(IGuild iGuild) {
        return null;
    }

    @Override
    public String mention() {
        return null;
    }

    @Override
    public String mention(boolean b) {
        return null;
    }

    @Override
    public String getDiscriminator() {
        return null;
    }

    @Override
    public List<IRole> getRolesForGuild(IGuild iGuild) {
        return null;
    }

    @Override
    public EnumSet<Permissions> getPermissionsForGuild(IGuild iGuild) {
        return null;
    }

    @Override
    public Optional<String> getNicknameForGuild(IGuild iGuild) {
        return null;
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public void moveToVoiceChannel(IVoiceChannel iVoiceChannel) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public List<IVoiceChannel> getConnectedVoiceChannels() {
        return null;
    }

    @Override
    public IPrivateChannel getOrCreatePMChannel() throws DiscordException, RateLimitException {
        return null;
    }

    @Override
    public boolean isDeaf(IGuild iGuild) {
        return false;
    }

    @Override
    public boolean isMuted(IGuild iGuild) {
        return false;
    }

    @Override
    public boolean isDeafLocally() {
        return false;
    }

    @Override
    public boolean isMutedLocally() {
        return false;
    }

    @Override
    public void addRole(IRole iRole) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void removeRole(IRole iRole) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public IDiscordClient getClient() {
        return null;
    }

    @Override
    public IShard getShard() {
        return null;
    }

    @Override
    public IUser copy() {
        return null;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id});
    }

    public boolean equals(Object other) {
        return other == null ? false : this.getClass().isAssignableFrom(other.getClass()) && ((IUser) other).getID().equals(this.getID());
    }
}
