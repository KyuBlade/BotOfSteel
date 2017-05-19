package com.omega;

import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.entity.permission.*;
import com.omega.database.repository.GroupPermissionsRepository;
import com.omega.database.repository.GuildPermissionsRepository;
import com.omega.database.repository.PrivateChannelPermissionsRepository;
import com.omega.exception.*;
import com.omega.module.Suppliable;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.*;

public class PermissionManager implements Suppliable<PermissionSupplier> {

    private final GroupPermissions DEFAULT_GROUP;

    private Set<String> permissions;

    private Map<IGuild, GuildPermissions> guildPermissionsMap;
    private PrivateChannelPermissions privateChannelPermissions;

    private PermissionManager() {
        this.permissions = new HashSet<>();
        this.guildPermissionsMap = new HashMap<>();

        GroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(GroupPermissionsRepository.class);

        DEFAULT_GROUP = repository.create("default");
    }

    public static PermissionManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * @return an immutable set of the available permissions
     */
    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    /**
     * Check if the user have the permission for the guild.
     *
     * @param guild      guild to check to
     * @param user       user to check to
     * @param permission permission to check
     * @return true if user have permission, false otherwise
     */
    public boolean hasPermission(IGuild guild, IUser user, String permission) throws PermissionNotFoundException {
        if (isAdmin(guild, user)) {
            return true;
        }

        if (permissions.contains(permission)) {
            GuildPermissions guildPermissions = guildPermissionsMap.get(guild);

            return guildPermissions.hasPermission(user, permission);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * @param guild guild from which to get permissions
     * @return all the permissions of a guild (users and groups)
     */
    public GuildPermissions getPermissionsFor(IGuild guild) {
        return guildPermissionsMap.get(guild);
    }

    /**
     * Add a user permission override for the specified guild.
     *
     * @param guild      target guild
     * @param user       target user
     * @param permission permission to add
     * @throws PermissionNotFoundException   if the permission is not available
     * @throws ImmutablePermissionsException if the user is an admin
     */
    public void addUserPermission(IGuild guild, IUser user, String permission)
        throws PermissionNotFoundException, ImmutablePermissionsException {
        if (isAdmin(guild, user)) {
            throw new ImmutablePermissionsException();
        }

        if (permissions.contains(permission)) {
            GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
            guildPermissions.addUserPermission(user, permission);

            saveGuildPermissions(guildPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Add user permission overrides for the specified guild.
     *
     * @param guild       target guild
     * @param user        target user
     * @param permissions permissions to add
     * @throws PermissionNotFoundException   if one of permission is not available
     * @throws ImmutablePermissionsException if the user is an admin
     */
    public void addUserPermissions(IGuild guild, IUser user, String... permissions)
        throws ImmutablePermissionsException, PermissionNotFoundException {
        if (isAdmin(guild, user)) {
            throw new ImmutablePermissionsException();
        }

        String notFoundProperty = Arrays.stream(permissions)
            .filter(permission -> !this.permissions.contains(permission))
            .findFirst()
            .orElse(null);

        if (notFoundProperty != null) {
            throw new PermissionNotFoundException(notFoundProperty);
        }

        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        guildPermissions.addUserPermissions(user, permissions);

        saveGuildPermissions(guildPermissions);
    }

    /**
     * Add a user permission override for private channel.
     *
     * @param user       target user
     * @param permission permission to add
     * @throws PermissionNotFoundException   if the permission is not available
     * @throws ImmutablePermissionsException if the user is an admin
     */
    public void addPrivateChannelUserPermission(IUser user, String permission)
        throws PermissionNotFoundException, ImmutablePermissionsException {
        if (isAdmin(null, user)) {
            throw new ImmutablePermissionsException();
        }

        if (permissions.contains(permission)) {
            privateChannelPermissions.addUserPermission(user, permission);
            savePrivateChannelPermissions(privateChannelPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Add a user permission override for private channel.
     *
     * @param user        target user
     * @param permissions permissions to add
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void addPrivateChannelUserPermissions(IUser user, String... permissions)
        throws PermissionNotFoundException {
        String notFoundProperty = Arrays.stream(permissions)
            .filter(permission -> !this.permissions.contains(permission))
            .findFirst()
            .orElse(null);

        if (notFoundProperty != null) {
            throw new PermissionNotFoundException(notFoundProperty);
        }

        privateChannelPermissions.addUserPermissions(user, permissions);
        savePrivateChannelPermissions(privateChannelPermissions);
    }

    /**
     * Remove a user permission override for the specified guild.
     *
     * @param guild      target guild
     * @param user       target user
     * @param permission permission to remove
     * @throws PermissionNotFoundException   if the permission is not available
     * @throws ImmutablePermissionsException if the user is an admin
     */
    public void removeUserPermission(IGuild guild, IUser user, String permission)
        throws PermissionNotFoundException, ImmutablePermissionsException {
        if (isAdmin(guild, user)) {
            throw new ImmutablePermissionsException();
        }

        if (permission.contains(permission)) {
            GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
            guildPermissions.removeUserPermission(user, permission);
            saveGuildPermissions(guildPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Remove user permission overrides for the specified guild.
     *
     * @param guild       target guild
     * @param user        target user
     * @param permissions permissions to remove
     * @throws PermissionNotFoundException   if one of permission is not available
     * @throws ImmutablePermissionsException if the user is an admin
     */
    public void removeUserPermissions(IGuild guild, IUser user, String... permissions)
        throws ImmutablePermissionsException, PermissionNotFoundException {
        if (isAdmin(guild, user)) {
            throw new ImmutablePermissionsException();
        }

        String notFoundProperty = Arrays.stream(permissions)
            .filter(permission -> !this.permissions.contains(permission))
            .findFirst()
            .orElse(null);

        if (notFoundProperty != null) {
            throw new PermissionNotFoundException(notFoundProperty);
        }

        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        guildPermissions.removeUserPermissions(user, permissions);

        saveGuildPermissions(guildPermissions);
    }

    /**
     * @param guild target guild
     * @param user  target user
     * @return the permissions for the specified user for the specified guild
     */
    public UserPermissions getPermissionsFor(IGuild guild, IUser user) {
        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        return guildPermissions.getPermissionsFor(user);
    }

    /**
     * Remove a user permission override for all private channels.
     *
     * @param user       target user
     * @param permission permission to remove
     * @throws PermissionNotFoundException   if the permission is not available
     * @throws ImmutablePermissionsException if the user is an admin
     */
    public void removePrivateChannelUserPermission(IUser user, String permission)
        throws PermissionNotFoundException, ImmutablePermissionsException {
        if (isAdmin(null, user)) {
            throw new ImmutablePermissionsException();
        }

        if (permissions.contains(permission)) {
            privateChannelPermissions.removeUserPermission(user, permission);

            savePrivateChannelPermissions(privateChannelPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Remove a user permission override for private channel.
     *
     * @param user        target user
     * @param permissions permissions to remove
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void removePrivateChannelUserPermissions(IUser user, String... permissions)
        throws PermissionNotFoundException {
        String notFoundProperty = Arrays.stream(permissions)
            .filter(permission -> !this.permissions.contains(permission))
            .findFirst()
            .orElse(null);

        if (notFoundProperty != null) {
            throw new PermissionNotFoundException(notFoundProperty);
        }

        privateChannelPermissions.removeUserPermissions(user, permissions);

        savePrivateChannelPermissions(privateChannelPermissions);
    }

    /**
     * @param user
     * @return the private channels permission overrides for the specified user
     */
    public UserPermissions getPrivateChannelPermissionsFor(IUser user) {
        return privateChannelPermissions.getPermissionsFor(user);
    }

    /**
     * Check if the user have the permission for private channels.
     *
     * @param user       user to check to
     * @param permission permission to check
     * @return true if user have permission, false otherwise
     */
    public boolean hasPermission(IUser user, String permission) throws PermissionNotFoundException {
        if (isAdmin(user)) {
            return true;
        }

        if (permissions.contains(permission)) {
            return privateChannelPermissions.hasPermission(user, permission);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Add a group to the specified guild.
     *
     * @param guild     target guild
     * @param groupName new group name
     * @return the created group
     * @throws GroupAlreadyExistsException if the group name already exists
     */
    public GroupPermissions addGroup(IGuild guild, String groupName) throws GroupAlreadyExistsException {
        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        GroupPermissions group = guildPermissions.addGroup(groupName);

        GroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(GroupPermissionsRepository.class);
        repository.save(group);

        saveGuildPermissions(guildPermissions);

        return group;
    }

    /**
     * Add a group to the specified guild.
     *
     * @param guild target guild
     * @param group group to add
     * @throws GroupAlreadyExistsException if the group name already exists
     */
    public void addGroup(IGuild guild, GroupPermissions group) throws GroupAlreadyExistsException {
        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        guildPermissions.addGroup(group);

        GroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(GroupPermissionsRepository.class);
        repository.save(group);

        saveGuildPermissions(guildPermissions);
    }

    /**
     * Remove a group from the specified guild.
     *
     * @param guild     target guild
     * @param groupName group name to remove
     * @throws GroupNotFoundException if the group doesn't exists
     */
    public void removeGroup(IGuild guild, String groupName) throws GroupNotFoundException, UnremovableGroupException {
        if (groupName.equalsIgnoreCase("default")) {
            throw new UnremovableGroupException();
        }

        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        GroupPermissions groupPermissions = guildPermissions.removeGroup(groupName);

        GroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(GroupPermissionsRepository.class);
        repository.delete(groupPermissions);

        saveGuildPermissions(guildPermissions);
    }

    /**
     * Add permission to the specified group of the specified guild.
     *
     * @param guild      target guild
     * @param groupName  target group name
     * @param permission permission to add
     * @throws GroupNotFoundException      if group doesn't exists
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void addGroupPermission(IGuild guild, String groupName, String permission)
        throws GroupNotFoundException, PermissionNotFoundException {
        if (permissions.contains(permission)) {
            GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
            guildPermissions.addGroupPermission(groupName, permission);

            saveGuildPermissions(guildPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Add permissions to the specified group of the specified guild.
     *
     * @param guild       target guild
     * @param groupName   target group name
     * @param permissions permissions to add
     * @throws GroupNotFoundException      if group doesn't exists
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void addGroupPermissions(IGuild guild, String groupName, String... permissions)
        throws GroupNotFoundException, PermissionNotFoundException {
        String notFoundProperty = Arrays.stream(permissions)
            .filter(permission -> !this.permissions.contains(permission))
            .findFirst()
            .orElse(null);

        if (notFoundProperty != null) {
            throw new PermissionNotFoundException(notFoundProperty);
        }

        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        guildPermissions.addGroupPermissions(groupName, permissions);

        saveGuildPermissions(guildPermissions);
    }

    /**
     * Add a group permission for all private channels.
     *
     * @param permission permission to add
     * @throws GroupNotFoundException      if group doesn't exists
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void addPrivateChannelGroupPermission(String permission)
        throws GroupNotFoundException, PermissionNotFoundException {
        if (permissions.contains(permission)) {
            privateChannelPermissions.addGroupPermission(permission);

            savePrivateChannelPermissions(privateChannelPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Remove permission to the specified group of the specified guild.
     *
     * @param guild      target guild
     * @param groupName  target group name
     * @param permission permission to remove
     * @throws GroupNotFoundException      if group doesn't exists
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void removeGroupPermission(IGuild guild, String groupName, String permission)
        throws GroupNotFoundException, PermissionNotFoundException {
        if (permissions.contains(permission)) {
            GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
            guildPermissions.removeGroupPermission(groupName, permission);


            saveGuildPermissions(guildPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Remove permissions from the specified group of the specified guild.
     *
     * @param guild       target guild
     * @param groupName   target group name
     * @param permissions permissions to remove
     * @throws GroupNotFoundException      if group doesn't exists
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void removeGroupPermissions(IGuild guild, String groupName, String... permissions)
        throws GroupNotFoundException, PermissionNotFoundException {
        String notFoundProperty = Arrays.stream(permissions)
            .filter(permission -> !this.permissions.contains(permission))
            .findFirst()
            .orElse(null);

        if (notFoundProperty != null) {
            throw new PermissionNotFoundException(notFoundProperty);
        }

        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        guildPermissions.removeGroupPermissions(groupName, permissions);

        saveGuildPermissions(guildPermissions);
    }

    /**
     * Remove a group permission for all private channels.
     *
     * @param permission permission to remove
     * @throws GroupNotFoundException      if found doesn't exists
     * @throws PermissionNotFoundException if the permission is not available
     */
    public void removePrivateChannelGroupPermission(String permission)
        throws GroupNotFoundException, PermissionNotFoundException {
        if (permissions.contains(permission)) {
            privateChannelPermissions.removeGroupPermission(permission);

            savePrivateChannelPermissions(privateChannelPermissions);
        } else {
            throw new PermissionNotFoundException();
        }
    }

    /**
     * Set the group for the specified user.
     *
     * @param guild     target guild
     * @param user      target user
     * @param groupName group name of the group to set
     * @throws GroupNotFoundException if group doesn't exists
     */
    public void setUserGroup(IGuild guild, IUser user, String groupName) throws GroupNotFoundException {
        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        guildPermissions.setUserGroup(user, groupName);

        saveGuildPermissions(guildPermissions);
    }

    /**
     * @param guild     target guild
     * @param groupName group target
     * @return the permissions of the group
     * @throws GroupNotFoundException if the group doesn't exists
     */
    public GroupPermissions getPermissionsFor(IGuild guild, String groupName) throws GroupNotFoundException {
        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);

        return guildPermissions.getPermissionsFor(groupName);
    }

    /**
     * @param guild target guild
     * @param user  target user
     * @return true if the user is the bot author, bot owner or the guild owner, false otherwise
     */
    public boolean isAdmin(IGuild guild, IUser user) {
        if (guild != null && guild.getOwner().equals(user)) {
            return true;
        } else {
            return isAdmin(user);
        }
    }

    /**
     * @param user target user
     * @return true if the user is the bot author, bot owner or the guild owner, false otherwise
     */
    public boolean isAdmin(IUser user) {
        IUser appOwner = BotManager.getInstance().getApplicationOwner();
        if (appOwner != null && appOwner.equals(user)) {
            return true;
        } else if (user.getStringID().equals("221359162930626562")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void supply(PermissionSupplier supplier) {
        this.permissions.addAll(Arrays.asList(supplier.supply()));
        DEFAULT_GROUP.addPermissions(supplier.supplyDefault());
    }

    @Override
    public void unsupply(PermissionSupplier supplier) {
        this.permissions.removeAll(Arrays.asList(supplier.supply()));
        DEFAULT_GROUP.removePermissions(supplier.supplyDefault());
    }

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
        GuildPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(GuildPermissionsRepository.class);

        GuildPermissions guildPermissions = repository.findByGuild(event.getGuild());
        if (guildPermissions == null) {
            guildPermissions = repository.create(event.getGuild());
        }

        guildPermissionsMap.put(guildPermissions.getGuild(), guildPermissions);

        saveGuildPermissions(guildPermissions);
    }

    @EventSubscriber
    public void onGuildLeave(GuildLeaveEvent event) {
        guildPermissionsMap.remove(event.getGuild());
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        PrivateChannelPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(PrivateChannelPermissionsRepository.class);

        this.privateChannelPermissions = repository.find();

        if (privateChannelPermissions == null) {
            this.privateChannelPermissions = repository.create();
        }
    }

    private void saveGuildPermissions(IGuild guild) {
        GuildPermissions guildPermissions = guildPermissionsMap.get(guild);
        saveGuildPermissions(guildPermissions);
    }

    private void saveGuildPermissions(GuildPermissions guildPermissions) {
        GuildPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(GuildPermissionsRepository.class);

        repository.save(guildPermissions);
    }

    private void savePrivateChannelPermissions(PrivateChannelPermissions privateChannelPermissions) {
        PrivateChannelPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(PrivateChannelPermissionsRepository.class);

        repository.save(privateChannelPermissions);
    }

    /**
     * @return a new instance of the default group populated with default permissions.
     */
    public GroupPermissions createDefaultGroup() {
        GroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(GroupPermissionsRepository.class);

        return repository.create(DEFAULT_GROUP);
    }

    /**
     * Get the default group used as template.
     *
     * @return the default group
     */
    public GroupPermissions getDefaultGroup() {
        return DEFAULT_GROUP;
    }

    private static class SingletonHolder {
        private static final PermissionManager INSTANCE = new PermissionManager();
    }
}
