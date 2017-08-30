package com.omega.core.database.impl.morphia.entity;

import com.omega.core.PermissionManager;
import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.core.database.entity.permission.GuildPermissions;
import com.omega.core.database.impl.morphia.repository.MorphiaGroupPermissionsRepository;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashMap;
import java.util.Map;

@Entity(value = "guild_permissions", noClassnameStored = true)
public class MorphiaGuildPermissions extends GuildPermissions<MorphiaUserPermissions, MorphiaGroupPermissions> {

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    @Embedded
    private IGuild guild;

    @Embedded
    private Map<IUser, MorphiaUserPermissions> userPermissionsMap = new HashMap<>();

    @Embedded
    private Map<String, MorphiaGroupPermissions> groupPermissionsMap = new HashMap<>();

    public MorphiaGuildPermissions() {
    }

    public MorphiaGuildPermissions(IGuild guild) {
        this.id = new ObjectId();
        this.guild = guild;

        MorphiaGroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(MorphiaGroupPermissionsRepository.class);

        MorphiaGroupPermissions group = (MorphiaGroupPermissions) repository.create(PermissionManager.getInstance()
            .createDefaultGroup());
        getGroupPermissionsMap().put("default", group);

        repository.save(group);
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
    public Map<IUser, MorphiaUserPermissions> getUserPermissionsMap() {
        return userPermissionsMap;
    }

    @Override
    public Map<String, MorphiaGroupPermissions> getGroupPermissionsMap() {
        return groupPermissionsMap;
    }
}
