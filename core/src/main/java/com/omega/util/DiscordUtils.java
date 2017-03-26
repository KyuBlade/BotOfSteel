package com.omega.util;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscordUtils {

    public static List<IUser> findUsers(List<IUser> users, String userName) {
        boolean hasDiscriminator = userName.contains("#");
        String username;
        String discriminator = null;
        if (hasDiscriminator) {
            String[] split = userName.split("#");
            username = split[0];
            discriminator = split[1];
        } else {
            username = userName;
        }
        final String finalDiscriminator = discriminator;

        return users.stream()
            .filter(user -> {
                if (hasDiscriminator && !user.getDiscriminator().equalsIgnoreCase(finalDiscriminator)) {
                    return false;
                }
                return user.getName().equalsIgnoreCase(username);
            })
            .collect(Collectors.toList());
    }

    public static IUser findUserWithReply(IMessage messageToReplyTo, List<IUser> users, String userName) {
        List<IUser> result = DiscordUtils.findUsers(users, userName);
        int resultCount = result.size();
        if (result.isEmpty()) {
            MessageUtil.reply(messageToReplyTo, "User " + userName + " not found");
        } else if (resultCount > 1) {
            StringBuilder builder = new StringBuilder();
            builder.append("**More than one users matches the given username : **\n\n```");
            IntStream.range(0, resultCount)
                .forEach(i -> {
                    IUser user = result.get(i);
                    builder.append(user.getName()).append('#').append(user.getDiscriminator());

                    if (i < resultCount) {
                        builder.append("\n");
                    }
                });
            builder.append("```");

            MessageUtil.reply(messageToReplyTo, builder.toString());
        } else {
            return result.get(0);
        }

        return null;
    }

    public static List<IRole> findRoles(List<IRole> roles, String roleName) {
        return roles.stream()
            .filter(role ->
                role.getName().equalsIgnoreCase(roleName)
            )
            .collect(Collectors.toList());
    }

    public static IRole findRoleWithReply(IMessage messageToReplyTo, List<IRole> roles, String roleName) {
        List<IRole> result = findRoles(roles, roleName);
        int resultCount = result.size();

        if (result.isEmpty()) {
            MessageUtil.reply(messageToReplyTo, "Role " + roleName + " not found");
        } else if (resultCount > 1) {
            StringBuilder builder = new StringBuilder();
            builder.append("**More than one roles matches the given name : **\n\n```");
            IntStream.range(0, resultCount)
                .forEach(i -> {
                    IRole role = result.get(i);
                    builder.append(role.getName());

                    if (i < resultCount) {
                        builder.append("\n");
                    }
                });
            builder.append("```");

            MessageUtil.reply(messageToReplyTo, builder.toString());
        } else {
            return result.get(0);
        }

        return null;
    }

    public static boolean checkPermissions(IMessage message, EnumSet<Permissions> permissions) {
        try {
            sx.blah.discord.api.internal.DiscordUtils.checkPermissions(
                message.getClient().getOurUser(),
                message.getGuild(),
                permissions
            );
            return true;
        } catch (MissingPermissionsException e) {
            StringBuilder builder = new StringBuilder("Need permissions : ");
            EnumSet<Permissions> missingPerms = e.getMissingPermissions();

            Iterator<Permissions> it = missingPerms.iterator();
            while (it.hasNext()) {
                Permissions permission = it.next();
                builder.append(permission.name());

                if (it.hasNext()) {
                    builder.append(", ");
                }
            }

            MessageUtil.reply(message, builder.toString());

            return false;
        }
    }
}
