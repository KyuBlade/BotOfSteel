package com.omega.core.command.impl;

import com.omega.core.PermissionManager;
import com.omega.core.command.*;
import com.omega.core.database.entity.permission.PermissionOverride;
import com.omega.core.database.entity.permission.UserPermissions;
import com.omega.core.exception.ImmutablePermissionsException;
import com.omega.core.exception.PermissionNotFoundException;
import com.omega.core.util.DiscordUtils;
import com.omega.core.util.MessageUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@CommandInfo(name = "privatePermission", aliases = "privPerm")
public class PrivateChannelPermissionCommand extends AbstractCommand {

    public PrivateChannelPermissionCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Get private channel permissions for the given username")
    public void onCommand(@Parameter(name = "userName") String userName) {
        IUser user = DiscordUtils.findUserWithReply(message, by.getClient().getUsers(), userName.toLowerCase());
        if (user != null) {
            onCommand(user);
        }
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Get private channel permissions for the mentioned user")
    public void onCommand(@Parameter(name = "userMention") IUser user) {
        StringBuilder stringBuilder = new StringBuilder();
        PermissionManager permMgr = PermissionManager.getInstance();
        if (permMgr.isAdmin(user)) {
            stringBuilder.append("**User ").append(user.getName()).append('#').append(user.getDiscriminator())
                .append(" is an admin and so have all permissions**");
        } else {
            UserPermissions userPermissions = permMgr.getPrivateChannelPermissionsFor(user);
            List<String> permissions = new ArrayList<>();
            permissions.addAll(PermissionManager.getInstance().getDefaultGroup().getPermissions());

            Set<PermissionOverride> permOverrides = userPermissions.getPermissions();
            for (PermissionOverride permOverride : permOverrides) {
                PermissionOverride.OverrideType overrideType = permOverride.getOverrideType();
                String permission = permOverride.getPermission();
                if (overrideType.equals(PermissionOverride.OverrideType.ADD)) {
                    if (!permissions.contains(permission)) {
                        permissions.add(permission);
                    }
                } else {
                    permissions.remove(permission);
                }
            }

            stringBuilder.append("**User ").append(user.getName()).append('#').append(user.getDiscriminator())
                .append(" have the permissions below for private channel :** \n\n");
            int permCount = permissions.size();
            IntStream.range(0, permCount).forEach(i -> {
                String permission = permissions.get(i);
                stringBuilder.append(permission);

                if (i < permCount) {
                    stringBuilder.append('\n');
                }
            });
        }

        MessageUtils.sendPrivateMessage(by, stringBuilder.toString());
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Add or remove a permission for the mentioned user in bot private channel. " +
        "Usage privatePermission action<add|remove> userMention permission<permissionName|all>")
    public void onCommand(@Parameter(name = "action") String action,
                          @Parameter(name = "userMention") IUser user,
                          @Parameter(name = "permission") String permission) {
        String lowPermission = permission.toLowerCase();
        try {
            if (action.equalsIgnoreCase("add")) {
                if (permission.equalsIgnoreCase("all")) {
                    Set<String> permissions = PermissionManager.getInstance().getPermissions();
                    PermissionManager.getInstance().addPrivateChannelUserPermissions(
                        user,
                        permissions.toArray(new String[permissions.size()])
                    );
                    MessageUtils.reply(message, "All permissions removed from user " + user.getName() + " in bot private channel");
                } else {
                    PermissionManager.getInstance().addPrivateChannelUserPermission(user, lowPermission);
                    MessageUtils.reply(message, "Added private channel permission " + lowPermission + " for user "
                        + user.getName());
                }
            } else if (action.equalsIgnoreCase("remove")) {
                if (permission.equalsIgnoreCase("all")) {
                    Set<String> permissions = PermissionManager.getInstance().getPermissions();
                    PermissionManager.getInstance().removePrivateChannelUserPermissions(
                        user,
                        permissions.toArray(new String[permissions.size()])
                    );
                    MessageUtils.reply(message, "All permissions removed from user " + user.getName() + " in bot private channel");
                } else {
                    PermissionManager.getInstance().removePrivateChannelUserPermission(user, lowPermission);
                    MessageUtils.reply(message, "Removed private channel permission " + lowPermission + " for user "
                        + user.getName());
                }
            } else {
                MessageUtils.reply(message, "Wrong action, must be add or remove");
            }
        } catch (PermissionNotFoundException e) {
            MessageUtils.reply(message, "Permission " + lowPermission + " not found");
        } catch (ImmutablePermissionsException e) {
            MessageUtils.reply(message, "You can't modify permissions of the user " + user.getName());
        }
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Add or remove a permission for the given username in bot private channel. Usage privatePermission action<add|remove> userMention permission")
    public void onCommand(@Parameter(name = "action") String action,
                          @Parameter(name = "userName") String userName,
                          @Parameter(name = "permission") String permission) {
        IUser user = DiscordUtils.findUserWithReply(message, by.getClient().getUsers(), userName);
        if (user != null) {
            onCommand(action, user, permission);
        }
    }
}
