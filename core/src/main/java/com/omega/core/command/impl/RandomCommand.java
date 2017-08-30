package com.omega.core.command.impl;

import com.omega.core.command.*;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@CommandInfo(name = "rand")
public class RandomCommand extends AbstractCommand {

    private static final long LONG_MIN = Long.MIN_VALUE;
    private static final long LONG_MAX = Long.MAX_VALUE;

    private static final double DOUBLE_MIN = Double.MIN_VALUE;
    private static final double DOUBLE_MAX = Double.MAX_VALUE;


    public RandomCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_RANDOM)
    @Signature(help = "Pick a random number in the given range")
    public void randomCommand(@Parameter(name = "min") Long min, @Parameter(name = "max") Long max)
        throws RateLimitException, DiscordException, MissingPermissionsException {

        if (min > max) {
            sendErrorMessage("Random", "Min value must be lower than max value");
        } else if (min <= LONG_MIN) {
            sendErrorMessage("Random", "Min value must be higher than " + LONG_MIN);
        } else if (max >= LONG_MAX) {
            sendErrorMessage("Random", "Max value must be lower than " + LONG_MAX);
        } else if (Objects.equals(min, max)) {
            sendErrorMessage("Random", "Min and max values must not be the same");
        } else {
            long randomNumber = ThreadLocalRandom.current().nextLong(min, max + 1);

            sendStateMessage("Random", "The random number is " + randomNumber);
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_RANDOM)
    @Signature(help = "Pick a random decimal number in the given range")
    public void randomCommand(@Parameter(name = "min") Double min, @Parameter(name = "max") Double max) throws
        RateLimitException, DiscordException, MissingPermissionsException {

        if (min > max) {
            sendErrorMessage("Random", "Min value must be lower than max value");
        } else if (min <= DOUBLE_MIN) {
            sendErrorMessage("Random", "Min value must be higher than " + DOUBLE_MIN);
        } else if (max >= LONG_MAX) {
            sendErrorMessage("Random", "Max value must be lower than " + DOUBLE_MAX);
        } else if (Objects.equals(min, max)) {
            sendErrorMessage("Random", "Min and max values must not be the same");
        } else {
            double randomNumber = ThreadLocalRandom.current().nextDouble(min, max);

            sendStateMessage("Random", "The random number is " + randomNumber);
        }
    }
}
