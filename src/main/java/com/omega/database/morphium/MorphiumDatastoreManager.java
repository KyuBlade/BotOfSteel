package com.omega.database.morphium;

import com.omega.database.DatastoreManager;
import com.omega.database.morphium.mapper.ChannelTypeMapper;
import com.omega.database.morphium.mapper.RoleTypeMapper;
import com.omega.database.morphium.mapper.UserTypeMapper;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import sx.blah.discord.handle.impl.obj.Channel;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.impl.obj.User;

import java.net.UnknownHostException;

public class MorphiumDatastoreManager extends DatastoreManager {

    private final Morphium morphium;

    public MorphiumDatastoreManager(MorphiumConfig morphiumConfig) throws ClassNotFoundException, UnknownHostException,
        InstantiationException, IllegalAccessException, NoSuchFieldException {
        this.morphium = new Morphium(morphiumConfig);

        addRepositories(new MorphiumRepository[]{
            new AudioTrackMorphiumRepository(morphium),
            new PlaylistMorphiumRepository(morphium),
            new GuildPropertiesMorphiumRepository(morphium)
        });


        morphium.registerTypeMapper(Channel.class, new ChannelTypeMapper());
        morphium.registerTypeMapper(User.class, new UserTypeMapper());
        morphium.registerTypeMapper(Role.class, new RoleTypeMapper());
    }

    public Object getId(Object entity) {
        return morphium.getId(entity);
    }

    public Morphium getMorphium() {
        return morphium;
    }
}
