package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.util.MessageUtil;
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

    private static String MODULE_LIST_CACHE;

    public ModuleCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Get the list of loaded modules")
    public void moduleCommand() {
        if (MODULE_LIST_CACHE == null) {
            StringBuilder builder = new StringBuilder();
            builder
                .append("**").append("Loaded modules :").append("**").append("\n\n");

            ModuleLoader moduleLoader = by.getClient().getModuleLoader();
            List<IModule> modules = moduleLoader.getLoadedModules();
            int moduleCount = modules.size();
            IntStream.range(0, moduleCount).forEach(i -> {
                IModule module = modules.get(i);
                builder.append("**").append(module.getName()).append("**")
                    .append("(**v").append(module.getVersion()).append("**)")
                    .append(" by **").append(module.getAuthor()).append("**");

                if (i < moduleCount - 1) {
                    builder.append('\n');
                }
            });

            MODULE_LIST_CACHE = builder.toString();
        }

        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.withDescription(MODULE_LIST_CACHE);

        MessageUtil.sendMessage(message.getChannel(), "", embBuilder.build());
    }

    @Signature(help = "Perform actions on modules, actions can be enable, disable or reload")
    public void moduleCommand(@Parameter(name = "moduleName") String moduleName, @Parameter(name = "action") String action) {
        ModuleLoader moduleLoader = by.getClient().getModuleLoader();
        List<IModule> modules = moduleLoader.getLoadedModules();
        modules = modules.stream().filter(iModule -> {
            String name = iModule.getName();
            if (name != null && name.equalsIgnoreCase(moduleName)) {
                return true;
            }

            return false;
        }).collect(Collectors.toList());

        int moduleCount = modules.size();
        if (moduleCount == 0) {
            MessageUtil.reply(message, "Module " + moduleName + " not found");
        } else if (moduleCount > 1) {
            MessageUtil.reply(message, "More than one module found");
        } else {
            IModule module = modules.get(0);
            switch (action.toLowerCase()) {
                case "enable":
                    boolean enabled = module.enable(by.getClient());
                    if (enabled) {
                        MessageUtil.reply(message, "Module " + moduleName + " enabled");
                    } else {
                        MessageUtil.reply(message, "Error while enabling module " + moduleName);
                    }
                    break;
                case "disable":
                    module.disable();
                    MessageUtil.reply(message, "Module " + moduleName + " disabled");
                    break;

                case "reload":
                    module.disable();
                    enabled = module.enable(by.getClient());
                    if (enabled) {
                        MessageUtil.reply(message, "Module " + moduleName + " reloaded");
                    } else {
                        MessageUtil.reply(message, "Error while enabling module " + moduleName);
                    }
                    break;
            }
        }
    }
}
