package com.omega.util;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IMessage;

public class MessageWrapper {

    private IMessage message;

    private Runnable editOperation;

    protected void setMessage(IMessage message) {
        this.message = message;

        if (editOperation != null) {
            editOperation.run();
        }
    }

    public void editMessage(String content) {
        if (message != null) {
            message.edit(content);
        } else {
            editOperation = () -> {
                message.edit(content);
            };
        }
    }

    public void editMessage(EmbedObject embed) {
        if (message != null) {
            message.edit(embed);
        } else {
            editOperation = () -> {
                message.edit(embed);
            };
        }
    }

    public boolean isSent() {
        return message != null;
    }
}
