package com.omega.util;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscordUtils {

    public static List<IUser> findUser(List<IUser> users, String userName) {
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
        List<IUser> result = DiscordUtils.findUser(users, userName);
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
}
