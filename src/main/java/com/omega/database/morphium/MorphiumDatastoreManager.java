package com.omega.database.morphium;

import com.omega.BotManager;
import com.omega.database.DatastoreManager;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import de.caluga.morphium.TypeMapper;
import sx.blah.discord.handle.impl.obj.Channel;
import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.net.UnknownHostException;
import java.util.Map;

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


        morphium.registerTypeMapper(Channel.class, new TypeMapper<IChannel>() {
            @Override
            public Object marshall(IChannel o) {
                return o.getID();
            }

            @Override
            public IChannel unmarshall(Object d) {
                return BotManager.getInstance().getClient().getChannelByID((String) d);
            }
        });
        morphium.registerTypeMapper(User.class, new TypeMapper<IUser>() {
            @Override
            public Object marshall(IUser o) {
                return o.getID();
            }

            @Override
            public IUser unmarshall(Object d) {
                return BotManager.getInstance().getClient().getUserByID((String) d);
            }
        });
        morphium.registerTypeMapper(Role.class, new TypeMapper<IRole>() {
            @Override
            public Object marshall(IRole o) {
                return o.getID();
            }

            @Override
            public IRole unmarshall(Object d) {
                return BotManager.getInstance().getClient().getRoleByID((String) d);
            }
        });
    }

    public Object getId(Object entity) {
        return morphium.getId(entity);
    }

    public Morphium getMorphium() {
        return morphium;
    }
}
