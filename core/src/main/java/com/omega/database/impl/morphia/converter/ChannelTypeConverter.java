package com.omega.database.impl.morphia.converter;

import com.omega.BotManager;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import sx.blah.discord.handle.impl.obj.Channel;
import sx.blah.discord.handle.obj.IChannel;

public class ChannelTypeConverter extends TypeConverter implements SimpleValueConverter {

    public ChannelTypeConverter() {
        super(IChannel.class, Channel.class);
    }

    @Override
    public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
        return BotManager.getInstance().getClient().getChannelByID((String) fromDBObject);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }

        return ((IChannel) value).getID();
    }
}
