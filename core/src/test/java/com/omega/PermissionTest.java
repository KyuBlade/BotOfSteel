package com.omega;

import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.entity.permission.GuildPermissions;
import com.omega.database.entity.permission.PermissionSupplier;
import com.omega.database.entity.permission.UserPermissions;
import com.omega.exception.*;
import com.omega.object.ChannelMock;
import com.omega.object.GuildMock;
import com.omega.object.PrivateChannelMock;
import com.omega.object.UserMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.Collection;
import java.util.stream.Collectors;

public class PermissionTest {

    private static String permission = "permission.key";
    private static IUser user = new UserMock("1");
    private static IGuild guild = new GuildMock("1");
    private static IChannel guildChannel = new ChannelMock("1");
    private static IPrivateChannel privateChannel = new PrivateChannelMock("1");

    private static PermissionManager permMgr;

    @BeforeClass
    public static void setupOnce() {
        permMgr = PermissionManager.getInstance();
        permMgr.supply(new PermissionSupplier() {
            @Override
            public String[] supplyDefault() {
                return new String[]{permission};
            }

            @Override
            public String[] supply() {
                return new String[]{permission};
            }
        });
        permMgr.onGuildCreate(new GuildCreateEvent(guild));
    }

    @SuppressWarnings("ConstantConditions")
    @Before
    public void reset() throws GroupNotFoundException, GroupAlreadyExistsException {
        GuildPermissions guildPermissions = permMgr.getPermissionsFor(guild);
        Collection<UserPermissions> userPermissionsList = guildPermissions.getUserPermissions();
        Collection<GroupPermissions> groupPermissionsList = guildPermissions.getGroupPermissions();

        userPermissionsList.forEach(userPermissions -> {
            try {
                permMgr.setUserGroup(guild, user, "default");
            } catch (GroupNotFoundException e) {
                e.printStackTrace();
                Assert.assertTrue(false);
            }

            userPermissions.getPermissions().forEach(permissionOverride -> {
                try {
                    permMgr.removeUserPermission(guild, userPermissions.getUser(), permissionOverride.getPermission());
                } catch (PermissionNotFoundException | ImmutablePermissionsException e) {
                    e.printStackTrace();
                    Assert.assertTrue(false);
                }
            });
        });

        groupPermissionsList.stream().collect(Collectors.toList()).forEach(groupPermissions -> {
            try {
                permMgr.removeGroup(guild, groupPermissions.getName());
            } catch (GroupNotFoundException e) {
                e.printStackTrace();
                Assert.assertTrue(false);
            } catch (UnremovableGroupException e) {
            }
        });

        permMgr.addGroup(guild, PermissionManager.createDefaultGroup());
    }

    @Test
    public void guildPermissionTest() throws GroupAlreadyExistsException, PermissionNotFoundException,
        GroupNotFoundException, ImmutablePermissionsException {
        GroupPermissions group = permMgr.addGroup(guild, "group");
        permMgr.setUserGroup(guild, user, group.getName());

        // Allow group
        permMgr.addGroupPermission(guild, group.getName(), permission);
        Assert.assertTrue("Guild group add permission broken", permMgr.hasPermission(guild, user, permission));

        // Disallow group
        permMgr.removeGroupPermission(guild, group.getName(), permission);
        Assert.assertFalse("Guild group remove permission broken", permMgr.hasPermission(guild, user, permission));

        // Disallow group, allow user
        permMgr.addUserPermission(guild, user, permission);
        Assert.assertTrue("Guild user add permission broken", permMgr.hasPermission(guild, user, permission));

        // Allow group, disallow user
        permMgr.addGroupPermission(guild, group.getName(), permission);
        permMgr.removeUserPermission(guild, user, permission);
        Assert.assertFalse("Guild user remove permission broken", permMgr.hasPermission(guild, user, permission));
    }

    @Test
    public void privateChannelPermission() throws GroupAlreadyExistsException, GroupNotFoundException,
        PermissionNotFoundException, ImmutablePermissionsException {
        // Allow group
        permMgr.addPrivateChannelGroupPermission(permission);
        Assert.assertTrue("Private channel default group add permission broken", permMgr.hasPermission(user, permission));

        // Disallow group
        permMgr.removePrivateChannelGroupPermission(permission);
        Assert.assertFalse("Private channel group remove permission broken", permMgr.hasPermission(user, permission));

        // Disallow group, allow user
        permMgr.addPrivateChannelUserPermission(user, permission);
        Assert.assertTrue("Private channel user add permission broken", permMgr.hasPermission(user, permission));

        // Disallow group, disallow user
        permMgr.removePrivateChannelUserPermission(user, permission);
        Assert.assertFalse("Private channel user remove permission broken", permMgr.hasPermission(user, permission));

        // Allow group, disallow user
        permMgr.addPrivateChannelGroupPermission(permission);
        Assert.assertFalse("User permission override broken", permMgr.hasPermission(user, permission));
    }
}
