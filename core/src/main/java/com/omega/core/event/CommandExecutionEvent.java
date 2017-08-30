package com.omega.core.event;

import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class CommandExecutionEvent extends Event {

    private final IMessage message;
    private final String command;
    private final IUser by;
    private final List<String> args;

    public CommandExecutionEvent(IMessage message, String command, IUser by, List<String> args) {
        this.message = message;
        this.command = command;
        this.by = by;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

    public IMessage getMessage() {
        return message;
    }

    public IUser getBy() {
        return by;
    }
}
