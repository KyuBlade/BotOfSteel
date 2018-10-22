package com.omega.core.object;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IShard;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.*;
import sx.blah.discord.util.cache.LongMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class PrivateChannelMock implements IPrivateChannel {

    private final String id;

    public PrivateChannelMock(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public MessageHistory getMessageHistory() {
        return null;
    }

    @Override
    public MessageHistory getMessageHistory(int i) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryFrom(Instant startDate) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryFrom(Instant startDate, int maxMessageCount) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryFrom(long id) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryFrom(long id, int maxMessageCount) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryTo(Instant endDate) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryTo(Instant endDate, int maxMessageCount) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryTo(long id) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryTo(long id, int maxMessageCount) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryIn(Instant startDate, Instant endDate) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryIn(Instant startDate, Instant endDate, int maxMessageCount) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryIn(long beginID, long endID) {
        return null;
    }

    @Override
    public MessageHistory getMessageHistoryIn(long beginID, long endID, int maxMessageCount) {
        return null;
    }

    @Override
    public MessageHistory getFullMessageHistory() {
        return null;
    }

    @Override
    public List<IMessage> bulkDelete() {
        return null;
    }

    @Override
    public List<IMessage> bulkDelete(List<IMessage> list) {
        return null;
    }

    @Override
    public int getMaxInternalCacheCount() {
        return 0;
    }

    @Override
    public int getInternalCacheCount() {
        return 0;
    }

    @Override
    public IMessage getMessageByID(long messageID) {
        return null;
    }

    @Override
    public IMessage fetchMessage(long messageID) {
        return null;
    }

    @Override
    public IGuild getGuild() {
        return null;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public String getTopic() {
        return null;
    }

    @Override
    public String mention() {
        return null;
    }

    @Override
    public IMessage sendMessage(String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendMessage(EmbedObject embedObject) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendMessage(String s, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendMessage(String s, EmbedObject embedObject) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendMessage(String s, EmbedObject embedObject, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(File file) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFiles(File... files) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(String s, File file) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFiles(String content, File... files) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(EmbedObject embedObject, File file) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFiles(EmbedObject embed, File... files) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(String s, InputStream inputStream, String s1) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFiles(String content, AttachmentPartEntry... entries) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(EmbedObject embedObject, InputStream inputStream, String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFiles(EmbedObject embed, AttachmentPartEntry... entries) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(String s, boolean b, InputStream inputStream, String s1) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFiles(String content, boolean tts, AttachmentPartEntry... entries) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(String s, boolean b, InputStream inputStream, String s1, EmbedObject embedObject) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFiles(String content, boolean tts, EmbedObject embed, AttachmentPartEntry... entries) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IMessage sendFile(MessageBuilder messageBuilder, InputStream inputStream, String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public void toggleTypingStatus() {

    }

    @Override
    public void setTypingStatus(boolean b) {

    }

    @Override
    public boolean getTypingStatus() {
        return false;
    }

    @Override
    public void edit(String s, int i, String s1) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeName(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changePosition(int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void changeTopic(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public IExtendedInvite createInvite(int maxAge, int maxUses, boolean temporary, boolean unique) {
        return null;
    }

    @Override
    public void changeNSFW(boolean isNSFW) {

    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public void delete() throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public LongMap<PermissionOverride> getUserOverrides() {
        return null;
    }

    @Override
    public LongMap<PermissionOverride> getRoleOverrides() {
        return null;
    }

    @Override
    public EnumSet<Permissions> getModifiedPermissions(IUser iUser) {
        return null;
    }

    @Override
    public EnumSet<Permissions> getModifiedPermissions(IRole iRole) {
        return null;
    }

    @Override
    public void removePermissionsOverride(IUser iUser) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void removePermissionsOverride(IRole iRole) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void overrideRolePermissions(IRole iRole, EnumSet<Permissions> enumSet, EnumSet<Permissions> enumSet1) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void overrideUserPermissions(IUser iUser, EnumSet<Permissions> enumSet, EnumSet<Permissions> enumSet1) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public List<IExtendedInvite> getExtendedInvites() {
        return null;
    }

    @Override
    public List<IUser> getUsersHere() {
        return null;
    }

    @Override
    public List<IMessage> getPinnedMessages() throws DiscordException, RateLimitException {
        return null;
    }

    @Override
    public void pin(IMessage iMessage) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public void unpin(IMessage iMessage) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    @Override
    public List<IWebhook> getWebhooks() {
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
    public IWebhook createWebhook(String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IWebhook createWebhook(String s, Image image) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public IWebhook createWebhook(String s, String s1) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public void changeCategory(ICategory category) {

    }

    @Override
    public ICategory getCategory() {
        return null;
    }

    @Override
    public long getLongID() {
        return Long.valueOf(id);
    }

    @Override
    public String getStringID() {
        return null;
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
    public Instant getCreationDate() {
        return null;
    }

    @Override
    public IUser getRecipient() {
        return null;
    }

    @Override
    public IPrivateChannel copy() {
        return null;
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean equals(Object other) {
        return other != null &&
            (this.getClass().isAssignableFrom(other.getClass()) &&
                ((IPrivateChannel) other).getLongID() == this.getLongID());
    }
}
