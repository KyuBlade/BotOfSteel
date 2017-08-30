package com.omega.core.database.impl.morphia.converter;

import com.omega.core.BotManager;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IRole;

public class RoleTypeConverter extends TypeConverter implements SimpleValueConverter {

    public RoleTypeConverter() {
        super(IRole.class, Role.class);
    }

    @Override
    public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
        if (fromDBObject == null) {
            return null;
        }

        return BotManager.getInstance().getClient().getRoleByID((long) fromDBObject);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }

        return ((IRole) value).getLongID();
    }
}
