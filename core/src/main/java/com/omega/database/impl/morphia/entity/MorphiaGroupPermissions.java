package com.omega.database.impl.morphia.entity;

import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.repository.GroupPermissionsRepository;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Reference;

import java.util.HashSet;
import java.util.Set;

@Entity(value = "group_permissions", noClassnameStored = true)
public class MorphiaGroupPermissions extends GroupPermissions {

    @Id
    private ObjectId id;

    private Set<String> permissions;
    private String name;

    public MorphiaGroupPermissions() {
        this.permissions = new HashSet<>();
    }

    public MorphiaGroupPermissions(String name) {
        this();

        this.id = new ObjectId();
        this.name = name;
    }

    public MorphiaGroupPermissions(GroupPermissions groupPermissions) {
        this();

        this.id = new ObjectId();
        this.permissions.addAll(groupPermissions.getPermissions());
        this.name = groupPermissions.getName();
    }

    public ObjectId getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getPermissions() {
        return permissions;
    }

    @Entity(value = "group_permissions_reference")
    public static class GroupPermissionsReferenceProxy extends MorphiaGroupPermissions {

        @Reference
        private MorphiaGroupPermissions group;

        public GroupPermissionsReferenceProxy() {
        }

        public GroupPermissionsReferenceProxy(MorphiaGroupPermissions group) {
            this.group = group;
        }

        @PrePersist
        private void onPrePersist() {
            GroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
                .getRepository(GroupPermissionsRepository.class);
            repository.save(group);
        }

        @Override
        public String getName() {
            return group.getName();
        }

        @Override
        public Set<String> getPermissions() {
            return group.getPermissions();
        }

        public MorphiaGroupPermissions getGroup() {
            return group;
        }
    }
}
