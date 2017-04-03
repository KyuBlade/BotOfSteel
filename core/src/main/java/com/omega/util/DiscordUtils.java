package com.omega.util;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscordUtils extends sx.blah.discord.api.internal.DiscordUtils {

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
            MessageUtils.sendErrorMessage(messageToReplyTo.getChannel(), "User " + userName + " not found");
        } else if (resultCount > 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.withDescription("More than one users matches the given username : \n\n");

            IntStream.range(0, resultCount)
                .forEach(i -> {
                    IUser user = result.get(i);

                    embedBuilder.appendDescription(user.getName() + "#" + user.getDiscriminator());

                    if (i < resultCount) {
                        embedBuilder.appendDescription("\n");
                    }
                });

            MessageUtils.sendMessage(messageToReplyTo.getChannel(), embedBuilder.build());
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
            MessageUtils.sendErrorMessage(messageToReplyTo.getChannel(), "Role " + roleName + " not found");
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

            MessageUtils.reply(messageToReplyTo, builder.toString());
        } else {
            return result.get(0);
        }

        return null;
    }

    public static int getColorInteger(Color color) {
        return ((color.getRed() & 0xFF) << 16) |
            ((color.getGreen() & 0xFF) << 8) |
            (color.getBlue() & 0xFF);
    }
}
