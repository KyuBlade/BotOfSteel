package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.util.MessageUtil;
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

    @Signature(help = "Get a random number after x rolls. Format : %count%d%range% or %count%D%range%")
    public void rollCommand(@Parameter(name = "rollLiteral") String rollLiteral) {
        String[] split = rollLiteral.split("d|D");
        if (split.length < 2) {
            MessageUtil.reply(message, "Malformed literal");
        } else {
            try {
                long count = Long.valueOf(split[0]);
                long range = Long.valueOf(split[1]);
                rollCommand(count, range);
            } catch (NumberFormatException e) {
                MessageUtil.reply(message, "Malformed literal");
            }
        }
    }

    @Signature(help = "Get a random number after x rolls")
    public void rollCommand(@Parameter(name = "count") Long count, @Parameter(name = "range") Long range) {
        if (count > MAX_ROLL_COUNT) {
            MessageUtil.reply(message, "You can't roll more than " + MAX_ROLL_COUNT + " times");
        } else if (count <= 0) {
            MessageUtil.reply(message, "Roll count must higher than 1");
        } else if (range < 0) {
            MessageUtil.reply(message, "Roll range must be positive");
        } else {
            StringBuilder builder = new StringBuilder(" (");
            LongStream.range(0, count).forEach(i -> {
                long next = ThreadLocalRandom.current().nextLong(range + 1);
                result += next;
                if (result < 0) {
                    MessageUtil.reply(message, "Result is too long");
                    return;
                }
                builder.append(next);
                if (i < count - 1) {
                    builder.append(" + ");
                }
            });
            if (result >= 0) {
                builder.append(')');
                MessageUtil.reply(message, "Roll gave " + result + builder.toString());
            }
        }
    }
}
