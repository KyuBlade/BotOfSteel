package com.omega.command.impl;

import com.omega.BotManager;
import com.omega.command.*;
import com.omega.database.entity.property.*;
import com.omega.exception.PropertyNotFoundException;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "setbotproperty", aliases = "setbprop")
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
            MessageUtil.reply(message, "Property " + property + " set to " + value);
        } catch (PropertyNotFoundException e) {
            MessageUtil.reply(message, "Property " + property + " not found");
        } catch (IllegalArgumentException e) {
            MessageUtil.reply(message, "Wrong value for property " + property);
        }
    }
}
