package com.omega.command;

import com.omega.BotManager;
import com.omega.PermissionManager;
import com.omega.command.impl.*;
import com.omega.event.CommandExecutionEvent;
import com.omega.exception.PermissionNotFoundException;
import com.omega.module.Suppliable;
import com.omega.util.CommandExtractHelper;
import com.omega.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CommandManager implements Suppliable<CommandSupplier> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    private Map<String, CommandExtractHelper.CommandInfo> commands = new HashMap<>();

    private ExecutorService executorService;

    private CommandManager() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @EventSubscriber
    public void onCommandExecution(CommandExecutionEvent event) {
        executorService.execute(() -> commandExecution(event.getBy(), event.getMessage(), event.getCommand(), event.getArgs()));
    }

    private void commandExecution(IUser by, IMessage message, String commandName, List<String> args) {
        if (!commands.containsKey(commandName)) {
            return;
        }

        LOGGER.debug("Command : {}, Arguments : {}", commandName, args.toString());
        CommandExtractHelper.CommandInfo cmdInfo = commands.get(commandName);

        // Instantiate command
        AbstractCommand commandInstance;
        Class<? extends AbstractCommand> cmdType = cmdInfo.getType();
        try {
            Constructor<? extends AbstractCommand> constructor = cmdType.getConstructor(IUser.class, IMessage.class);
            commandInstance = constructor.newInstance(by, message);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Command class " + cmdType.getName() + " need a (IUser, IMessage) constructor");
            return;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOGGER.error("Unable to create new instance for command " + cmdType.getName());
            return;
        }

        // Resolve string arguments to objects
        List<Object> castedArgs = new ArrayList<>(args.size());
        CommandParser parser = new CommandParser(message.getGuild());
        args.forEach(arg -> {
            Object parsedArg = parser.parseCommandArg(arg);
            castedArgs.add(parsedArg);
        });


        if (LOGGER.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            castedArgs.forEach(o -> sb.append(o.getClass().getSimpleName()).append(' '));

            LOGGER.debug("Search for signature {}", sb.toString());
        }

        // Find correct command signature method
        Method commandMethod = Arrays.stream(cmdType.getDeclaredMethods())
            .filter(method -> {
                if (method.isAnnotationPresent(Signature.class)
                    && method.getParameterCount() == castedArgs.size()) { // Keep same arguments sized methods

                    if (method.getParameterCount() > 0) { // If command have arguments

                        Class<?>[] paramTypes = method.getParameterTypes();
                        IntStream intStream = IntStream.range(
                            0, castedArgs.size()
                        )
                            .filter(i -> {
                                Class<?> paramType = paramTypes[i];
                                Object castedArg = castedArgs.get(i);

                                return paramType.equals(castedArg.getClass()) ||
                                    paramType.isAssignableFrom(castedArg.getClass());
                            });

                        if (intStream.count() == castedArgs.size()) {
                            LOGGER.debug("Command method found");
                            return true;
                        }
                    } else {
                        return true;
                    }
                }

                return false;
            })
            .findFirst()
            .orElse(null);

        if (commandMethod != null) {
            // Check permissions
            boolean canExecute = false;
            if (commandMethod.isAnnotationPresent(Permission.class)) {
                Permission permissionAnnot = commandMethod.getAnnotation(Permission.class);
                String permission = permissionAnnot.permission();
                if ((permissionAnnot.botOwnerOnly() && BotManager.getInstance().getApplicationOwner().equals(by)) ||
                    (!permissionAnnot.botOwnerOnly() && permission.isEmpty())) {
                    canExecute = true;
                } else {
                    PermissionManager permMgr = PermissionManager.getInstance();
                    try {
                        if (message.getChannel().isPrivate()) {
                            canExecute = permMgr.hasPermission(by, permission);
                        } else {
                            canExecute = permMgr.hasPermission(message.getGuild(), by, permission);
                        }
                    } catch (PermissionNotFoundException e) {
                        LOGGER.warn("Permission {} not found for command {}", permission, commandInstance.getClass().getName());
                    }
                }
            } else {
                canExecute = true;
            }

            if (canExecute) {
                try {
                    MessageUtil.deleteMessage(message);
                    LOGGER.debug("Invoke method {}", commandMethod);
                    commandMethod.invoke(commandInstance, castedArgs.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error("Unable to invoke method " + commandMethod.toGenericString() + " of class " + cmdType.getName(), e);
                }
            } else {
                LOGGER.info("User {} don't have permission to use command {}", by.getName(), commandName);
            }
        }
    }

    /**
     * Add commands.
     *
     * @param supplier command supplier with commands to add
     */
    @Override
    public void supply(CommandSupplier supplier) {
        Class<AbstractCommand>[] commands = supplier.supply();
        Arrays.stream(commands).forEach(this::registerCommand);
    }

    /**
     * Remove commands.
     *
     * @param supplier command supplier with commands to remove
     */
    @Override
    public void unsupply(CommandSupplier supplier) {
        Class<AbstractCommand>[] commands = supplier.supply();
        Arrays.stream(commands).forEach(this::unregisterCommand);
    }

    private <T extends AbstractCommand> void registerCommand(Class<T> clazz) {
        try {
            CommandExtractHelper.CommandInfo[] cmdInfos = CommandExtractHelper.getCommandInfos(clazz);
            Arrays.stream(cmdInfos).forEach(commandInfo -> commands.put(commandInfo.getName().toLowerCase(), commandInfo));
        } catch (IllegalStateException e) {
            LOGGER.warn("Unable to register command {}", clazz.getSimpleName(), e);
        }
    }

    private <T extends AbstractCommand> void unregisterCommand(Class<T> clazz) {
        try {
            CommandExtractHelper.CommandInfo[] cmdInfos = CommandExtractHelper.getCommandInfos(clazz);
            Arrays.stream(cmdInfos).forEach(commandInfo -> commands.remove(commandInfo.getName().toLowerCase()));
        } catch (IllegalStateException e) {
            LOGGER.warn("Unable to register command {}", clazz.getSimpleName(), e);
        }
    }

    public CommandExtractHelper.CommandInfo getCommand(String commandName) {
        return commands.get(commandName);
    }

    public Map<String, CommandExtractHelper.CommandInfo> getCommands() {
        return commands;
    }

    public static CommandManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommandManager INSTANCE = new CommandManager();
    }
}
