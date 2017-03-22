package com.omega.database.impl.morphia.entity;

import com.omega.PermissionManager;
import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.entity.permission.GuildPermissions;
import com.omega.database.entity.permission.UserPermissions;
import com.omega.database.impl.morphia.converter.UserTypeConverter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashMap;
import java.util.Map;

@Converters(value = UserTypeConverter.class)
@Entity(value = "guild_permissions", noClassnameStored = true)
public class MorphiaGuildPermissions extends GuildPermissions {

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    @Embedded
    private IGuild guild;

    @Embedded
    private Map<IUser, UserPermissions> userPermissionsMap;

    @Embedded
    private Map<String, GroupPermissions> groupPermissionsMap;

    public MorphiaGuildPermissions() {
        this.userPermissionsMap = new HashMap<>();
        this.groupPermissionsMap = new HashMap<>();

        groupPermissionsMap.put("default", new GroupPermissions(PermissionManager.createDefaultGroup()));
    }

    public MorphiaGuildPermissions(IGuild guild) {
        this();

        this.guild = guild;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public IGuild getGuild() {
        return guild;
    }

    @Override
    public Map<IUser, UserPermissions> getUserPermissionsMap() {
        return userPermissionsMap;
    }

    @Override
    public Map<String, GroupPermissions> getGroupPermissionsMap() {
        return groupPermissionsMap;
    }
}
