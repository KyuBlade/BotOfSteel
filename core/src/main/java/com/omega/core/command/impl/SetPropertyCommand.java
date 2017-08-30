package com.omega.core.command.impl;

import com.omega.core.command.*;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import com.omega.core.database.entity.property.*;
import com.omega.core.exception.PropertyNotFoundException;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.core.util.MessageUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

@CommandInfo(name = "set")
public class SetPropertyCommand extends AbstractCommand {

    public SetPropertyCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_SET)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") String value) {
        setProperty(property, new StringProperty(value));
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_SET)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Long value) {
        setProperty(property, new LongProperty(value));
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_SET)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Double value) {
        setProperty(property, new DoubleProperty(value));
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_SET)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") Boolean value) {
        setProperty(property, new BooleanProperty(value));
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_SET)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IUser value) {
        setProperty(property, new UserProperty(value));
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_SET)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IRole value) {
        setProperty(property, new RoleProperty(value));
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_SET)
    @Signature(help = "Set a bot property")
    public void setPropertyCommand(@Parameter(name = "property") String property, @Parameter(name = "value") IChannel value) {
        setProperty(property, new ChannelProperty(value));
    }

    private void setProperty(String property, Property value) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildProperties properties = guildContext.getProperties();
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
