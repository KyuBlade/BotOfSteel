package com.omega.property;

import com.omega.BotManager;
import com.omega.TwitchApi;
import com.omega.database.entity.property.StringProperty;

public class ClientIdChangeTask implements BotPropertyChangeTask<StringProperty> {

    @Override
    public void execute(BotManager botManager, StringProperty property, boolean init) {
        String clientId = property.getValue();

        if (clientId != null) {
            TwitchApi.getInstance().setClientId(clientId);
        }
    }
}
