package com.omega.core.command.impl;

import com.omega.core.command.*;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import com.omega.core.database.entity.property.GuildProperties;
import com.omega.core.database.entity.property.Property;
import com.omega.core.exception.PropertyNotFoundException;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.core.util.MessageUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

import java.util.Iterator;
import java.util.Map;

@CommandInfo(name = "get")
public class GetPropertyCommand extends AbstractCommand {

    public GetPropertyCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GET)
    @Signature(help = "Get all properties")
    public void getProperty() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildProperties guildProperties = guildContext.getProperties();
        Map<String, Property> properties = guildProperties.getProperties();

        StringBuilder builder = new StringBuilder(512);
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown());

        Iterator<Map.Entry<String, Property>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Property> entry = it.next();
            builder.append(entry.getKey()).append(" = ").append(entry.getValue());
            if (it.hasNext()) {
                builder.append('\n');
            }
        }

        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());

        MessageUtils.sendPrivateMessage(by, builder.toString());
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GET)
    @Signature(help = "Get the property for the given property")
    public void getProperty(@Parameter(name = "property") String property) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildProperties properties = guildContext.getProperties();

        try {
            Object value = properties.getProperty(property);
            MessageUtils.sendPrivateMessage(by, String.format("Property %s = %s", property, value));
        } catch (PropertyNotFoundException e) {
            MessageUtils.sendPrivateMessage(by, "Property " + property + " not found");
        }
    }
}
