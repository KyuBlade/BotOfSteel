package com.omega.database.impl.morphia.entity;

import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.entity.permission.PermissionOverride;
import com.omega.database.entity.permission.UserPermissions;
import com.omega.database.impl.morphia.converter.UserTypeConverter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashSet;
import java.util.Set;

@Converters(value = UserTypeConverter.class)
@Entity(value = "user_permissions", noClassnameStored = true)
public class MorphiaUserPermissions extends UserPermissions {

    @Id
    private ObjectId id;

    private Set<PermissionOverride> permissions;

    @Embedded
    private IUser user;

    @Reference
    private MorphiaGroupPermissions group;

    public MorphiaUserPermissions() {
    }

    public MorphiaUserPermissions(IUser user, MorphiaGroupPermissions group) {
        this.id = new ObjectId();
        this.permissions = new HashSet<>();
        this.user = user;

        if (group != null) {
            if (group instanceof MorphiaGroupPermissions.GroupPermissionsReferenceProxy) {
                this.group = ((MorphiaGroupPermissions.GroupPermissionsReferenceProxy) group).getGroup();
            } else {
                this.group = group;
            }
        }
    }

    public IUser getUser() {
        return user;
    }

    public GroupPermissions getGroup() {
        return group;
    }

    public void setGroup(GroupPermissions group) {
        this.group = (MorphiaGroupPermissions) group;
    }

    public Set<PermissionOverride> getPermissions() {
        return permissions;
    }
}
