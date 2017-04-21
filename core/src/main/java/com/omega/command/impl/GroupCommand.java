package com.omega.command.impl;

import com.omega.PermissionManager;
import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.entity.permission.GuildPermissions;
import com.omega.exception.GroupAlreadyExistsException;
import com.omega.exception.GroupNotFoundException;
import com.omega.exception.PermissionNotFoundException;
import com.omega.exception.UnremovableGroupException;
import com.omega.util.MessageUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Collection;
import java.util.Set;

@Command(name = "group", aliases = "grp")
public class GroupCommand extends AbstractCommand {

    public GroupCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GROUP)
    @Signature(help = "Get all groups of this guild")
    public void groupCommand() {
        GuildPermissions guildPermissions = PermissionManager.getInstance().getPermissionsFor(message.getGuild());
        Collection<GroupPermissions> groupPermissionsList = guildPermissions.getGroupPermissions();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("** Available groups for guild ")
            .append(guildPermissions.getGuild().getName())
            .append(" :**\n\n");

        groupPermissionsList.forEach(groupPermissions -> {
            stringBuilder.append(groupPermissions.getName()).append('\n');
        });

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withDescription(stringBuilder.toString());

        MessageUtils.sendPrivateMessage(by, "", embedBuilder.build());
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GROUP)
    @Signature(help = "Get the permissions of the given group")
    public void groupCommand(@Parameter(name = "groupName") String groupName) {
        String lowGroupName = groupName.toLowerCase();
        try {
            GroupPermissions groupPermissions = PermissionManager.getInstance().getPermissionsFor(message.getGuild(), lowGroupName);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("**Permissions for group ").append(groupPermissions.getName())
                .append("**").append("\n\n");
            groupPermissions.getPermissions().forEach(permission ->
                stringBuilder.append(permission).append('\n')
            );
            stringBuilder.append("\n\n");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withDescription(stringBuilder.toString());

            MessageUtils.sendPrivateMessage(by, "", embedBuilder.build());
        } catch (GroupNotFoundException e) {
            MessageUtils.reply(message, "Group " + groupName + " not found");
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GROUP)
    @Signature(help = "Create or remove a group. Usage : group action<create|delete> groupName")
    public void groupCommand(@Parameter(name = "action") String action,
                             @Parameter(name = "groupName") String groupName) {
        String lowGroupName = groupName.toLowerCase();
        try {
            if (action.equalsIgnoreCase("create")) {
                PermissionManager.getInstance().addGroup(message.getGuild(), lowGroupName);
                MessageUtils.reply(message, "Created group " + lowGroupName);
            } else if (action.equalsIgnoreCase("delete")) {
                PermissionManager.getInstance().removeGroup(message.getGuild(), lowGroupName);
                MessageUtils.reply(message, "Removed group " + lowGroupName);
            } else {
                MessageUtils.reply(message, "Wrong action, must be create or delete");
            }
        } catch (GroupNotFoundException e) {
            MessageUtils.reply(message, "Group " + lowGroupName + " not found");
        } catch (GroupAlreadyExistsException e) {
            MessageUtils.reply(message, "Group " + lowGroupName + " already exists");
        } catch (UnremovableGroupException e) {
            MessageUtils.reply(message, "Can't delete group " + lowGroupName);
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GROUP)
    @Signature(help = "Add or remove a permission to the group. Usage : permission action<add|remove> groupName permission<permissionName|all>")
    public void permissionCommand(@Parameter(name = "action") String action,
                                  @Parameter(name = "groupName") String groupName,
                                  @Parameter(name = "permission") String permission) {
        String lowGroupName = groupName.toLowerCase();
        String lowPermission = permission.toLowerCase();
        try {
            if (action.equalsIgnoreCase("add")) {
                if (permission.equalsIgnoreCase("all")) {
                    Set<String> permissions = PermissionManager.getInstance().getPermissions();
                    PermissionManager.getInstance().addGroupPermissions(
                        message.getGuild(),
                        lowGroupName,
                        permissions.toArray(new String[permissions.size()])
                    );
                    MessageUtils.reply(message, "All permissions added to group " + lowGroupName);
                } else {
                    PermissionManager.getInstance().addGroupPermission(message.getGuild(), groupName, lowPermission);
                    MessageUtils.reply(message, "Permission " + lowPermission + " added to group " + lowGroupName);
                }
            } else if (action.equalsIgnoreCase("remove")) {
                if (permission.equalsIgnoreCase("all")) {
                    Set<String> permissions = PermissionManager.getInstance().getPermissions();
                    PermissionManager.getInstance().removeGroupPermissions(
                        message.getGuild(),
                        lowGroupName,
                        permissions.toArray(new String[permissions.size()])
                    );
                    MessageUtils.reply(message, "All permissions removed from group " + lowGroupName);
                } else {
                    PermissionManager.getInstance().removeGroupPermission(message.getGuild(), groupName, lowPermission);
                    MessageUtils.reply(message, "Permission " + lowPermission + " removed from group " + lowGroupName);
                }
            } else {
                MessageUtils.reply(message, "Wrong action, must be create or remove");
            }
        } catch (PermissionNotFoundException e) {
            if (e.getPermission() != null) {
                lowPermission = e.getPermission();
            }
            MessageUtils.reply(message, "Permission " + lowPermission + " not found");
        } catch (GroupNotFoundException e) {
            MessageUtils.reply(message, "Group " + lowGroupName + " not found");
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GROUP)
    @Signature(help = "Set the group of the mentioned user")
    public void groupCommnand(@Parameter(name = "userMention") IUser user,
                              @Parameter(name = "groupName") String groupName) {
        String lowGroupName = groupName.toLowerCase();
        try {
            PermissionManager.getInstance().setUserGroup(message.getGuild(), user, lowGroupName);
            MessageUtils.reply(message, "Set group of the user " + user.getName() + " to " + lowGroupName);
        } catch (GroupNotFoundException e) {
            MessageUtils.reply(message, "Group " + lowGroupName + " not found");
        }
    }
}
