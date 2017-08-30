package com.omega.core.util;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.Signature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CommandExtractHelper {

    public static class CommandInfo {
        private final Class<? extends AbstractCommand> type;
        private final String name;


        public CommandInfo(Class<? extends AbstractCommand> type, String name) {
            this.type = type;
            this.name = name;
        }

        public Class<? extends AbstractCommand> getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    public static class MainCommandInfo extends CommandInfo {

        private final String[] aliases;

        public MainCommandInfo(Class<? extends AbstractCommand> type, String name, String[] aliases) {
            super(type, name);

            this.aliases = aliases;
        }

        public String[] getAliases() {
            return aliases;
        }
    }

    public static class AliasCommandInfo extends CommandInfo {

        public AliasCommandInfo(Class<? extends AbstractCommand> type, String name) {
            super(type, name);
        }
    }

    public static class CommandSignatureInfo {
        private final String help;
        private final List<Parameter> parameters = new ArrayList<>();

        public CommandSignatureInfo(String help) {
            this.help = help;
        }

        public void addParameter(String name, Class type) {
            parameters.add(new Parameter(name, type));
        }

        public String getHelp() {
            return help;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public static class Parameter {
            private final String name;
            private final Class type;

            public Parameter(String name, Class type) {
                this.name = name;
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public Class getType() {
                return type;
            }
        }
    }

    public static CommandInfo[] getCommandInfos(Class<? extends AbstractCommand> commandType) {
        com.omega.core.command.CommandInfo commandInfoAnnot = commandType.getAnnotation(com.omega.core.command.CommandInfo.class);
        if (commandInfoAnnot != null) {
            String commandName = commandInfoAnnot.name();
            String[] aliases = commandInfoAnnot.aliases();

            CommandInfo[] cmdInfos = new CommandInfo[aliases.length + 1];
            cmdInfos[0] = new MainCommandInfo(commandType, commandName, aliases);

            IntStream.range(0, aliases.length).forEach(i -> cmdInfos[i + 1] = new AliasCommandInfo(commandType, aliases[i]));

            return cmdInfos;
        } else {
            throw new IllegalStateException("No CommandInfo annotation found for class " + commandType.getSimpleName());
        }
    }

    public static List<CommandSignatureInfo> getCommandSignatureInfos(Class<? extends AbstractCommand> commandType) {
        Method[] methods = commandType.getDeclaredMethods();
        List<CommandSignatureInfo> signatureInfos = new ArrayList<>();
        Arrays.stream(methods).filter(method -> method.isAnnotationPresent(Signature.class)).forEachOrdered(method -> {
            Signature sigAnnot = method.getAnnotation(Signature.class);
            CommandSignatureInfo info = new CommandSignatureInfo(sigAnnot.help());
            Parameter[] params = method.getParameters();
            Arrays.stream(params).forEachOrdered(parameter -> {
                String paramName;
                if (parameter.isAnnotationPresent(com.omega.core.command.Parameter.class)) {
                    com.omega.core.command.Parameter paramAnnot = parameter.getAnnotation(com.omega.core.command.Parameter.class);
                    paramName = paramAnnot.name();
                } else {
                    paramName = parameter.getName();
                }

                info.addParameter(paramName, parameter.getType());
            });
            signatureInfos.add(info);
        });

        return signatureInfos;
    }
}
