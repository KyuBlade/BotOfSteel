package com.omega.object;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IShard;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GuildMock implements IGuild {

    private final String id;

    public GuildMock(String id) {
        this.id = id;
    }

    @Override
    public String getOwnerID() {
        return "221359162930626562";
    }

    @Override
    public long getOwnerLongID() {
        return 221359162930626562L;
    }

    @Override
    public IUser getOwner() {
        return new UserMock("221359162930626562");
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getIconURL() {
        return null;
    }

    @Override
    public List<IChannel> getChannels() {
        return null;
    }

    @Override
    public IChannel getChannelByID(String s) {
        return null;
    }

    @Override
    public IChannel getChannelByID(long id) {
        return null;
    }

    @Override
    public List<IUser> getUsers() {
        return null;
    }

    @Override
    public IUser getUserByID(String s) {
        return null;
    }

    @Override
    public IUser getUserByID(long id) {
        return null;
    }

    @Override
    public List<IChannel> getChannelsByName(String s) {
        return null;
    }

    @Override
    public List<IVoiceChannel> getVoiceChannelsByName(String s) {
        return null;
    }

    @Override
    public List<IUser> getUsersByName(String s) {
        return null;
    }

    @Override
    public List<IUser> getUsersByName(String s, boolean b) {
        return null;
    }

    @Override
    public List<IUser> getUsersByRole(IRole iRole) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<IRole> getRoles() {
        return null;
    }

    @Override
    public List<IRole> getRolesForUser(IUser iUser) {
        return null;
    }

    @Override
    public IRole getRoleByID(String s) {
        return null;
    }

    @Override
    public IRole getRoleByID(long id) {
        return null;
    }

    @Override
    public List<IRole> getRolesByName(String s) {
        return null;
    }

    @Override
    public List<IVoiceChannel> getVoiceChannels() {
        return null;
    }

    @Override
    public IVoiceChannel getVoiceChannelByID(String s) {
        return null;
    }

    @Override
    public IVoiceChannel getVoiceChannelByID(long id) {
        return null;
    }

    @Override
    public IVoiceChannel getConnectedVoiceChannel() {
        return null;
    }

    @Override
    public IVoiceChannel getAFKChannel() {
        return null;
    }

    @Override
    public int getAFKTimeout() {
        return 0;
    }

    @Override
    public IRole createRole() throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public List<IUser> getBannedUsers() throws DiscordException, RateLimitException {
        return null;
    }

    @Override
    public void banUser(IUser iUser) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void banUser(IUser iUser, int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void banUser(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void banUser(long userID) {

    }

    @Override
    public void banUser(String s, int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void banUser(long userID, int deleteMessagesForDays) {

    }

    @Override
    public void pardonUser(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void pardonUser(long userID) {

    }

    @Override
    public void kickUser(IUser iUser) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void editUserRoles(IUser iUser, IRole[] iRoles) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void setDeafenUser(IUser iUser, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void setMuteUser(IUser iUser, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void setUserNickname(IUser iUser, String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void edit(String s, IRegion iRegion, VerificationLevel verificationLevel, Image image, IVoiceChannel iVoiceChannel, int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeName(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeRegion(IRegion iRegion) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeVerificationLevel(VerificationLevel verificationLevel) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeIcon(Image image) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeAFKChannel(IVoiceChannel iVoiceChannel) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeAFKTimeout(int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void deleteGuild() throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void leaveGuild() throws DiscordException, RateLimitException {

    }

    @Override
    public void leave() throws DiscordException, RateLimitException {

    }

    @Override
    public IChannel createChannel(String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IVoiceChannel createVoiceChannel(String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IRegion getRegion() {
        return null;
    }

    @Override
    public VerificationLevel getVerificationLevel() {
        return null;
    }

    @Override
    public IRole getEveryoneRole() {
        return null;
    }

    @Override
    public IChannel getGeneralChannel() {
        return null;
    }

    @Override
    public List<IInvite> getInvites() throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public List<IExtendedInvite> getExtendedInvites() {
        return null;
    }

    @Override
    public void reorderRoles(IRole... iRoles) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public int getUsersToBePruned(int i) throws DiscordException, RateLimitException {
        return 0;
    }

    @Override
    public int pruneUsers(int i) throws DiscordException, RateLimitException {
        return 0;
    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public IAudioManager getAudioManager() {
        return null;
    }

    @Override
    public LocalDateTime getJoinTimeForUser(IUser iUser) throws DiscordException {
        return null;
    }

    @Override
    public IMessage getMessageByID(String s) {
        return null;
    }

    @Override
    public IMessage getMessageByID(long id) {
        return null;
    }

    @Override
    public List<IEmoji> getEmojis() {
        return null;
    }

    @Override
    public IEmoji getEmojiByID(String s) {
        return null;
    }

    @Override
    public IEmoji getEmojiByID(long id) {
        return null;
    }

    @Override
    public IEmoji getEmojiByName(String s) {
        return null;
    }

    @Override
    public IWebhook getWebhookByID(String s) {
        return null;
    }

    @Override
    public IWebhook getWebhookByID(long id) {
        return null;
    }

    @Override
    public List<IWebhook> getWebhooksByName(String s) {
        return null;
    }

    @Override
    public List<IWebhook> getWebhooks() {
        return null;
    }

    @Override
    public int getTotalMemberCount() {
        return 0;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public long getLongID() {
        return 0;
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
    public IGuild copy() {
        return null;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id});
    }

    public boolean equals(Object other) {
        return other == null ? false : this.getClass().isAssignableFrom(other.getClass()) && ((IGuild) other).getID().equals(this.getID());
    }
}
