package com.omega.database.impl.morphia.converter;

import com.omega.BotManager;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.IUser;

public class UserTypeConverter extends TypeConverter implements SimpleValueConverter {

    public UserTypeConverter() {
        super(IUser.class, User.class);
    }

    @Override
    public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
        if (fromDBObject == null) {
            return null;
        }

        return BotManager.getInstance().getClient().getUserByID((long) fromDBObject);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }

        return ((IUser) value).getLongID();
    }
}
