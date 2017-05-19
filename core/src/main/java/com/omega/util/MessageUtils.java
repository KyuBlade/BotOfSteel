package com.omega.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.EnumSet;

public class MessageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);

    /**
     * Reply to a message.
     *
     * @param messageToReply the message you want to reply to
     * @param content        the message content
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper reply(IMessage messageToReply, String content) {
        if (messageToReply == null) {
            throw new NullPointerException("messageToReply to must not be null");
        }

        final MessageWrapper messageWrapper = new MessageWrapper();

        RequestBuffer.request(() -> {
            try {
                IMessage message = messageToReply.getChannel()
                    .sendMessage(String.format("%s, %s", messageToReply.getAuthor(), content));
                messageWrapper.setMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to reply", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });

        return messageWrapper;
    }

    /**
     * Reply to a message.
     *
     * @param messageToReply the message you want to reply to
     * @param content        the message content
     * @param embed          the message embed
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper reply(IMessage messageToReply, String content, EmbedObject embed) {
        if (messageToReply == null) {
            throw new NullPointerException("messageToReply to must not be null");
        }

        final MessageWrapper messageWrapper = new MessageWrapper();

        RequestBuffer.request(() -> {
            try {
                IMessage message = messageToReply.getChannel()
                    .sendMessage(String.format("%s, %s", messageToReply.getAuthor(), content), embed);
                messageWrapper.setMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to reply", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });

        return messageWrapper;
    }

    /**
     * Send a private message.
     *
     * @param target  user to message
     * @param content message content
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper sendPrivateMessage(IUser target, String content) {
        if (target == null) {
            throw new NullPointerException("target must not be null");
        }

        final MessageWrapper messageWrapper = new MessageWrapper();

        RequestBuffer.request(() -> {
            try {
                IMessage message = target.getOrCreatePMChannel().sendMessage(content);
                messageWrapper.setMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send private message", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });

        return messageWrapper;
    }

    /**
     * Send a private message with an embed object.
     *
     * @param target  user to message
     * @param content message content
     * @param embed   the embed object to send
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper sendPrivateMessage(IUser target, String content, EmbedObject embed) {
        if (target == null) {
            throw new NullPointerException("target must not be null");
        }
        final MessageWrapper messageWrapper = new MessageWrapper();

        RequestBuffer.request(() -> {
            try {
                IMessage message = target.getOrCreatePMChannel().sendMessage(content, embed, false);
                messageWrapper.setMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send private message", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });

        return messageWrapper;
    }

    /**
     * Send a private message with an embed object.
     *
     * @param target user to message
     * @param embed  the embed object to send
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper sendPrivateMessage(IUser target, EmbedObject embed) {
        return sendPrivateMessage(target, "", embed);
    }

    /**
     * Send a message in a channel.
     *
     * @param channel channel to send to
     * @param content message content
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper sendMessage(IChannel channel, String content) {
        if (channel == null) {
            throw new NullPointerException("channel must not be null");
        }

        final MessageWrapper messageWrapper = new MessageWrapper();

        RequestBuffer.request(() -> {
            try {
                IMessage message = channel.sendMessage(content);
                messageWrapper.setMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send message on channel " + channel.getName(), e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });

        return messageWrapper;
    }

    /**
     * Send a message in a channel with an embed object.
     *
     * @param channel channel to send to
     * @param content message content
     * @param embed   the embed object to send
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper sendMessage(IChannel channel, String content, EmbedObject embed) {
        if (channel == null) {
            throw new NullPointerException("channel must not be null");
        }

        final MessageWrapper messageWrapper = new MessageWrapper();

        RequestBuffer.request(() -> {
            try {
                IMessage message = channel.sendMessage(content, embed, false);
                messageWrapper.setMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send message on channel " + channel.getName(), e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });

        return messageWrapper;
    }

    /**
     * Send a message in a channel with an embed object.
     *
     * @param channel channel to send to
     * @param embed   the embed object to send
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper sendMessage(IChannel channel, EmbedObject embed) {
        return sendMessage(channel, "", embed);
    }

    public static void sendMissingPermissionsMessage(IChannel channel, EnumSet<Permissions> missingPermissions) {
        sendMissingPermissionsMessage(channel, null, missingPermissions);
    }

    public static void sendMissingPermissionsMessage(IChannel channel, String description, EnumSet<Permissions> missingPermissions) {
        if (channel == null) {
            throw new NullPointerException("channel must not be null");
        }
        if (missingPermissions == null) {
            throw new NullPointerException("missingPermissions must not be null");
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        missingPermissions.forEach(permission -> {
            stringBuilder.append(permission.name()).append('\n');
        });

        IUser botUser = channel.getClient().getOurUser();
        embedBuilder
            .withColor(Color.RED)
            .withAuthorName(botUser.getName())
            .withAuthorIcon(botUser.getAvatarURL())
            .withDescription(description)
            .appendField("Missing permissions", stringBuilder.toString(), true);

        sendMessage(channel, embedBuilder.build());
    }

    public static MessageWrapper sendErrorMessage(IChannel channel, String description) {
        return sendErrorMessage(channel, null, description);
    }

    public static MessageWrapper sendErrorMessage(IChannel channel, String title, String description) {
        return sendErrorMessage(channel, title, description, null);
    }

    public static MessageWrapper sendErrorMessage(IChannel channel, String title, String description, IEmbed.IEmbedField... fields) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withColor(Color.RED)
            .withTitle(title)
            .withDescription(description);

        if (fields != null) {
            Arrays.stream(fields).forEachOrdered(embedBuilder::appendField);
        }
        
        return sendMessage(channel, embedBuilder.build());
    }

    /**
     * Send an error message in a channel with a stacktrace.
     *
     * @param channel     channel to send to
     * @param title       embed title
     * @param description embed description
     * @param t           the stacktrace to send
     * @param fields      embed fields to append
     * @return a message wrapper object that will queue operations on the message until it is sent
     */
    public static MessageWrapper sendErrorMessage(IChannel channel, String title, String description,
                                                  Throwable t, IEmbed.IEmbedField... fields) {
        if (channel == null) {
            throw new NullPointerException("channel must not be null");
        }


        try (StringWriter stacktraceWritter = new StringWriter()) {
            stacktraceWritter.write(t.getMessage());
            stacktraceWritter.write(": ");

            t.printStackTrace(new PrintWriter(stacktraceWritter));

            // Check if description is not too long
            if (stacktraceWritter.getBuffer().length() <= EmbedBuilder.DESCRIPTION_CONTENT_LIMIT + description.length()) {
                return sendErrorMessage(channel, title, description + "\n\n" + stacktraceWritter.toString());
            } else { // Send stacktrace in txt file
                MessageWrapper messageWrapper = new MessageWrapper();

                RequestBuffer.request(() -> {
                    try {
                        IMessage message = channel.sendFile(
                            description,
                            new ByteArrayInputStream(stacktraceWritter.toString().getBytes("UTF-8")),
                            "stacktrace.txt");

                        messageWrapper.setMessage(message);
                    } catch (UnsupportedEncodingException e) {
                        LOGGER.warn("UTF-8 charset not supported, can't send stacktrace error message");
                    }
                });

                return messageWrapper;
            }
        } catch (IOException e) {
            LOGGER.warn("Error while printing stacktrace", e);

            return sendErrorMessage(channel, "Exception thrown but unable to print the stacktrace");
        }
    }

    /**
     * Delete a message.
     *
     * @param message message to delete
     */

    public static void deleteMessage(IMessage message) {
        if (message == null) {
            throw new NullPointerException("message must not be null");
        }

        if (!message.getChannel().isPrivate()) {
            RequestBuffer.request(() -> {
                try {
                    message.delete();
                } catch (MissingPermissionsException e) {
                    LOGGER.info("Missing permissions", e);
                } catch (DiscordException e) {
                    LOGGER.info("Error while deleting message", e);
                }
            });
        }
    }

    /**
     * Edit a message.
     *
     * @param message     message to edit
     * @param embedObject new message content
     */
    public static void editMessage(IMessage message, EmbedObject embedObject) {
        RequestBuffer.request(() -> {
            try {
                message.edit(embedObject);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Missing permissions", e);
            } catch (DiscordException e) {
                LOGGER.info("Error while deleting message", e);
            }
        });
    }
}
