package com.omega.core.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;

@CommandInfo(name = "log")
public class LogCommand extends AbstractCommand {

    public LogCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Get log file")
    public void onCommand() {
        File logFile = new File("logs/bot.log");
        if (!logFile.exists() || !logFile.isFile()) {
            sendErrorMessage("No log file available");
        } else {
            sendPrivateMessage("Latest logs", logFile);
        }
    }
}
