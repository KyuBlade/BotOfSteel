package com.omega.core.command.impl;

import com.omega.core.BotManager;
import com.omega.core.command.*;
import com.omega.core.database.entity.property.*;
import com.omega.core.exception.PropertyNotFoundException;
import com.omega.core.util.MessageUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

@CommandInfo(name = "setbotproperty", aliases = "setbprop")
public class SetBotPropertyCommand extends AbstractCommand {

    public SetBotPropertyCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") String value) {
        setProperty(property, new StringProperty(value));
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Long value) {
        setProperty(property, new LongProperty(value));
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Double value) {
        setProperty(property, new DoubleProperty(value));
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Boolean value) {
        setProperty(property, new BooleanProperty(value));
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IUser value) {
        setProperty(property, new UserProperty(value));
    }

    @Permission(botOwnerOnly = true)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IRole value) {
        setProperty(property, new RoleProperty(value));
    }

    private void setProperty(String property, Property value) {
        BotProperties properties = BotManager.getInstance().getBotProperties();
        try {
            properties.setProperty(property, value);
            MessageUtils.reply(message, "Property " + property + " set to " + value);
        } catch (PropertyNotFoundException e) {
            MessageUtils.reply(message, "Property " + property + " not found");
        } catch (IllegalArgumentException e) {
            MessageUtils.reply(message, "Wrong value for property " + property);
        }
    }
}
