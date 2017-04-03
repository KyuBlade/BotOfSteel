package com.omega.command.impl;

import com.omega.command.*;
import com.omega.module.Module;
import com.omega.util.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.modules.IModule;
import sx.blah.discord.modules.ModuleLoader;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Command(name = "module")
public class ModuleCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCommand.class);

    public ModuleCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Get the list of loaded modules")
    public void moduleCommand() {
        StringBuilder builder = new StringBuilder();
        builder
            .append("**").append("Loaded modules :").append("**").append("\n\n");

        ModuleLoader moduleLoader = by.getClient().getModuleLoader();
        List<IModule> modules = moduleLoader.getLoadedModules();
        int moduleCount = modules.size();
        IntStream.range(0, moduleCount).forEach(i -> {
            IModule module = modules.get(i);
            if (!(module instanceof Module)) {
                LOGGER.warn("Module {} should extends {} class", module.getName(), Module.class.getName());
                return;
            }
            builder.append("**").append(module.getName()).append("**")
                .append("(**v").append(module.getVersion()).append("**)")
                .append(" by **").append(module.getAuthor()).append("**")
                .append(" : **").append(
                ((Module) module).isEnabled()
                    ? "enabled"
                    : "disabled"
            ).append("**");

            if (i < moduleCount - 1) {
                builder.append('\n');
            }
        });

        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.withDescription(builder.toString());

        MessageUtils.sendMessage(message.getChannel(), "", embBuilder.build());
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Perform actions on modules, actions can be enable, disable or reload")
    public void moduleCommand(@Parameter(name = "moduleName") String moduleName, @Parameter(name = "action") String action) {
        ModuleLoader moduleLoader = by.getClient().getModuleLoader();
        List<IModule> modules = moduleLoader.getLoadedModules();
        modules = modules.stream().filter(module -> {
            String name = module.getName();
            if (name != null && name.equalsIgnoreCase(moduleName)) {
                if (!(module instanceof Module)) {
                    LOGGER.warn("Module {} should extends {} class", module.getName(), Module.class.getName());
                } else {
                    return true;
                }
            }

            return false;
        }).collect(Collectors.toList());

        int moduleCount = modules.size();
        if (moduleCount == 0) {
            MessageUtils.reply(message, "Module " + moduleName + " not found");
        } else if (moduleCount > 1) {
            MessageUtils.reply(message, "More than one module found");
        } else {
            IModule module = modules.get(0);
            boolean enabled = (((Module) module).isEnabled());
            switch (action.toLowerCase()) {
                case "enable":
                    if (enabled) {
                        MessageUtils.reply(message, "Module " + moduleName + " already enabled");
                    } else {
                        enabled = module.enable(by.getClient());
                        if (enabled) {
                            MessageUtils.reply(message, "Module " + moduleName + " enabled");
                        } else {
                            MessageUtils.reply(message, "Error while enabling module " + moduleName);
                        }
                    }
                    break;
                case "disable":
                    if (!enabled) {
                        MessageUtils.reply(message, "Module " + moduleName + " already enabled");
                    } else {
                        module.disable();
                        MessageUtils.reply(message, "Module " + moduleName + " disabled");
                    }
                    break;

                case "reload":
                    if (enabled) {
                        module.disable();
                    } else {
                        enabled = module.enable(by.getClient());
                        if (enabled) {
                            MessageUtils.reply(message, "Module " + moduleName + " reloaded");
                        } else {
                            MessageUtils.reply(message, "Error while enabling module " + moduleName);
                        }
                    }
                    break;
            }
        }
    }
}
