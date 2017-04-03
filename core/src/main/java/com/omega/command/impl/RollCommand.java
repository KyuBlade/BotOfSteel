package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

@Command(name = "roll")
public class RollCommand extends AbstractCommand {

    private static final short MAX_ROLL_COUNT = 50;

    private long result;

    public RollCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_ROLL)
    @Signature(help = "Get a random number after x rolls. Format : %count%d%range% or %count%D%range%")
    public void rollCommand(@Parameter(name = "rollLiteral") String rollLiteral) {
        String[] split = rollLiteral.split("d|D");
        if (split.length < 2) {
            sendErrorMessage("Roll", "Malformed literal");
        } else {
            try {
                long count = Long.valueOf(split[0]);
                long range = Long.valueOf(split[1]);
                rollCommand(count, range);
            } catch (NumberFormatException e) {
                sendErrorMessage("Roll", "Malformed literal");
            }
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_ROLL)
    @Signature(help = "Get a random number after x rolls")
    public void rollCommand(@Parameter(name = "count") Long count, @Parameter(name = "range") Long range) {
        if (count > MAX_ROLL_COUNT) {
            sendErrorMessage("Roll", "You can't roll more than " + MAX_ROLL_COUNT + " times");
        } else if (count <= 0) {
            sendErrorMessage("Roll", "Roll count must be higher than 1");
        } else if (range < 0) {
            sendErrorMessage("Roll", "Roll range must be positive");
        } else {
            StringBuilder builder = new StringBuilder(" (");
            LongStream.range(0, count).forEach(i -> {
                long next = ThreadLocalRandom.current().nextLong(range + 1);

                result += next;

                if (result < 0) {
                    sendErrorMessage("Roll", "Result is too long");
                    return;
                }
                builder.append(next);

                if (i < count - 1) {
                    builder.append(" + ");
                }
            });
            if (result >= 0) {
                builder.append(')');
                sendStateMessage("Roll gave " + result + builder.toString());
            }
        }
    }
}
