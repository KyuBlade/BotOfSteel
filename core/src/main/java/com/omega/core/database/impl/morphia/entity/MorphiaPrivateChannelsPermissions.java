package com.omega.core.database.impl.morphia.entity;

import com.omega.core.database.entity.permission.PrivateChannelPermissions;
import com.omega.core.database.entity.permission.UserPermissions;
import com.omega.core.database.impl.morphia.converter.UserTypeConverter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashMap;
import java.util.Map;

@Converters(value = UserTypeConverter.class)
@CappedAt(count = 1)
@Entity(value = "private_channels_permissions", noClassnameStored = true)
public class MorphiaPrivateChannelsPermissions extends PrivateChannelPermissions {

    @Id
    private ObjectId id;

    @Embedded
    private Map<IUser, UserPermissions> userPermissionsMap;

    public MorphiaPrivateChannelsPermissions() {
        super();

        this.userPermissionsMap = new HashMap<>();
    }

    @Override
    public Map<IUser, UserPermissions> getUserPermissionsMap() {
        return userPermissionsMap;
    }
}
