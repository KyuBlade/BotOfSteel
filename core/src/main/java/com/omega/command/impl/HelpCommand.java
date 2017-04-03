package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.CommandExtractHelper;
import com.omega.util.MessageUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.*;
import java.util.stream.IntStream;

@Command(name = "help", aliases = "h")
public class HelpCommand extends AbstractCommand {

    public HelpCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_HELP)
    @Signature(help = "Get the list of available commands")
    public void helpCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        StringBuilder builder = new StringBuilder();
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown()).append("Commands list : \n\n");

        Map<String, CommandExtractHelper.CommandInfo> commandMap = CommandManager.getInstance().getCommands();
        Collection<CommandExtractHelper.CommandInfo> commands = commandMap.values();
        Iterator<CommandExtractHelper.CommandInfo> it = commands.stream().iterator();

        while (it.hasNext()) {
            CommandExtractHelper.CommandInfo cmdInfo = it.next();
            if (cmdInfo instanceof CommandExtractHelper.AliasCommandInfo) {
                continue;
            }

            builder.append(cmdInfo.getName());

            if (!(cmdInfo instanceof CommandExtractHelper.MainCommandInfo)) {
                throw new IllegalStateException("Command info should be of type MainCommandInfo");
            }

            String[] aliases = ((CommandExtractHelper.MainCommandInfo) cmdInfo).getAliases();
            Iterator<String> aIt = Arrays.stream(aliases).iterator();
            while (aIt.hasNext()) {
                String alias = aIt.next();

                builder.append(", ").append(alias);
            }
            if (it.hasNext()) {
                builder.append('\n');
            }
        }
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());

        MessageUtils.deleteMessage(message);
        MessageUtils.sendPrivateMessage(by, builder.toString());
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_HELP)
    @Signature(help = "Get help for the specified method")
    public void helpCommand(@Parameter(name = "commandName") String commandName) {
        CommandExtractHelper.CommandInfo commandInfo = CommandManager.getInstance().getCommand(commandName.toLowerCase());
        if (commandInfo != null) {
            List<CommandExtractHelper.CommandSignatureInfo> signatureInfos = CommandExtractHelper.getCommandSignatureInfos(commandInfo.getType());

            StringBuilder builder = new StringBuilder();
            builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown());

            IntStream.range(0, signatureInfos.size()).forEach(i -> {
                CommandExtractHelper.CommandSignatureInfo signatureInfo = signatureInfos.get(i);
                builder.append(commandInfo.getName()).append(' ');
                signatureInfo.getParameters().forEach(parameter ->
                    builder.append(parameter.getName())
                        .append('(')
                        .append(parameter.getType().getSimpleName())
                        .append(") ")
                );
                builder.append("- ")
                    .append(signatureInfo.getHelp());

                if (i < signatureInfos.size()) {
                    builder.append("\n\n");
                }
            });

            builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());
            MessageUtils.sendPrivateMessage(by, builder.toString());
        }
    }
}
