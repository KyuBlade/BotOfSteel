package com.omega.database.impl.morphia;

import com.omega.database.DatastoreManager;
import com.omega.database.impl.morphia.converter.ChannelTypeConverter;
import com.omega.database.impl.morphia.converter.GuildTypeConverter;
import com.omega.database.impl.morphia.converter.RoleTypeConverter;
import com.omega.database.impl.morphia.converter.UserTypeConverter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.converters.Converters;

public class MorphiaDatastoreManager extends DatastoreManager {

    private final Morphia morphia;
    private final Datastore datastore;

    public MorphiaDatastoreManager(Morphia morphia, Datastore datastore) {
        this.morphia = morphia;
        this.datastore = datastore;

        Converters converters = morphia.getMapper().getConverters();
        converters.addConverter(new GuildTypeConverter());
        converters.addConverter(new ChannelTypeConverter());
        converters.addConverter(new UserTypeConverter());
        converters.addConverter(new RoleTypeConverter());

        morphia.mapPackage("com.omega.database.impl.morphia.entity");
        addRepositories(
            new AudioTrackMorphiaRepository(datastore),
            new PlaylistMorphiaRepository(datastore),
            new GuildPropertiesMorphiaRepository(datastore)
        );
    }


    public Datastore getDatastore() {
        return datastore;
    }

    public Morphia getMorphia() {
        return morphia;
    }
}
