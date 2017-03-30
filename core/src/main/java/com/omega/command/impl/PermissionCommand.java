package com.omega.command.impl;

import com.omega.PermissionManager;
import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.entity.permission.PermissionOverride;
import com.omega.database.entity.permission.UserPermissions;
import com.omega.exception.GroupNotFoundException;
import com.omega.exception.ImmutablePermissionsException;
import com.omega.exception.PermissionNotFoundException;
import com.omega.util.DiscordUtils;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Command(name = "permission", aliases = "perm")
public class PermissionCommand extends AbstractCommand {

    private static GroupPermissions DEFAULT_GROUP = PermissionManager.createDefaultGroup();

    public PermissionCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_PERMISSION)
    @Signature(help = "Get the list of all permissions")
    public void permissionCommand() {
        StringBuilder sBuild = new StringBuilder();
        Set<String> permissions = PermissionManager.getInstance().getPermissions();
        permissions.forEach(permission ->
            sBuild.append(permission).append('\n')
        );

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("Available permissions");
        embedBuilder.withDescription(sBuild.toString());

        MessageUtil.sendPrivateMessage(by, "", embedBuilder.build());
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_PERMISSION)
    @Signature(help = "Get the permissions for a user")
    public void permissionCommand(@Parameter(name = "userName") String userName) throws GroupNotFoundException {
        IUser user = DiscordUtils.findUserWithReply(message, message.getGuild().getUsers(), userName.toLowerCase());
        if (user != null) {
            permissionCommand(user);
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_PERMISSION)
    @Signature(help = "Get the permissions for a user")
    public void permissionCommand(@Parameter(name = "userMention") IUser user) throws GroupNotFoundException {
        PermissionManager permMgr = PermissionManager.getInstance();

        final StringBuilder stringBuilder = new StringBuilder();
        if (permMgr.isAdmin(message.getGuild(), user)) {
            stringBuilder.append("**User ").append(user.getName())
                .append(" is an admin and so have full permissions on the guild ")
                .append(message.getGuild().getName()).append("**");
        } else {
            UserPermissions userPermissions = permMgr.getPermissionsFor(message.getGuild(), user);
            List<String> permissions = new ArrayList<>();
            if (userPermissions != null) {
                permissions.addAll(userPermissions.getGroup().getPermissions());
            } else {
                GroupPermissions defaultGroup = permMgr.getPermissionsFor(guild, "default");
                permissions.addAll(defaultGroup.getPermissions());
            }

            if (userPermissions != null) {
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
            }

            stringBuilder.append("**User ").append(user.getName()).append('#').append(user.getDiscriminator())
                .append(" have the permissions below for guild ")
                .append(message.getGuild().getName())
                .append(" :** \n\n");
            int permCount = permissions.size();
            IntStream.range(0, permCount).forEach(i -> {
                String permission = permissions.get(i);
                stringBuilder.append(permission);

                if (i < permCount) {
                    stringBuilder.append('\n');
                }
            });
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("Permissions for user " + user.getName());
        embedBuilder.withDescription(stringBuilder.toString());

        MessageUtil.sendPrivateMessage(by, "", embedBuilder.build());
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_PERMISSION)
    @Signature(help = "Add or remove a permission to the user. Usage : permission action<add|remove> userMention permission<permissionName|all>")
    public void permissionCommand(@Parameter(name = "action") String action,
                                  @Parameter(name = "userMention") IUser user,
                                  @Parameter(name = "permission") String permission) {
        String lowPermission = permission.toLowerCase();
        try {
            if (action.equalsIgnoreCase("add")) {
                if (permission.equalsIgnoreCase("all")) {
                    Set<String> permissions = PermissionManager.getInstance().getPermissions();
                    PermissionManager.getInstance().addUserPermissions(
                        message.getGuild(),
                        user,
                        permissions.toArray(new String[permissions.size()])
                    );
                    MessageUtil.reply(message, "All permissions added to user " + user.getName());
                } else {
                    PermissionManager.getInstance().addUserPermission(message.getGuild(), user, lowPermission);
                    MessageUtil.reply(message, "Permission " + lowPermission + " added to user " + user.getName());
                }
            } else if (action.equalsIgnoreCase("remove")) {
                if (permission.equalsIgnoreCase("all")) {
                    Set<String> permissions = PermissionManager.getInstance().getPermissions();
                    PermissionManager.getInstance().removeUserPermissions(
                        message.getGuild(),
                        user,
                        permissions.toArray(new String[permissions.size()])
                    );
                    MessageUtil.reply(message, "All permissions removed from user " + user.getName());
                } else {
                    PermissionManager.getInstance().removeUserPermission(message.getGuild(), user, lowPermission);
                    MessageUtil.reply(message, "Permission " + lowPermission + " removed from user " + user.getName());
                }
            } else {
                MessageUtil.reply(message, "Wrong action, must be create or remove");
            }
        } catch (PermissionNotFoundException e) {
            MessageUtil.reply(message, "Permission " + lowPermission + " not found");
        } catch (ImmutablePermissionsException e) {
            MessageUtil.reply(message, "You can't modify admin permissions");
        }
    }
}
