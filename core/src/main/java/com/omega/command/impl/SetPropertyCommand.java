package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.database.entity.property.GuildProperties;
import com.omega.database.entity.property.*;
import com.omega.exception.PropertyNotFoundException;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.MessageUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "set")
public class SetPropertyCommand extends AbstractCommand {

    public SetPropertyCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") String value) {
        setProperty(property, new StringProperty(value));
    }

    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Long value) {
        setProperty(property, new LongProperty(value));
    }

    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Double value) {
        setProperty(property, new DoubleProperty(value));
    }

    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Boolean value) {
        setProperty(property, new BooleanProperty(value));
    }

    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IUser value) {
        setProperty(property, new UserProperty(value));
    }

    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IRole value) {
        setProperty(property, new RoleProperty(value));
    }

    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IChannel value) {
        setProperty(property, new ChannelProperty(value));
    }

    private void setProperty(String property, Property value) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildProperties properties = guildContext.getProperties();
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
