package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.CommandSupplier;

public class MusicCommandSupplier implements CommandSupplier {

    private static final Class[] COMMANDS = new Class[]{
        JoinCommand.class, LeaveCommand.class, QueueCommand.class, PlaylistCreateCommand.class,
        PlaylistListCommand.class, AddToPlaylistCommand.class, PlayPlaylistCommand.class,
        DeletePlaylistCommand.class, PlayCommand.class, PauseCommand.class, ResumeCommand.class,
        SkipCommand.class, SetMusicChannelCommand.class, TrackCommand.class, ShuffleCommand.class,
        ClearQueueCommand.class, SeekCommand.class, LyricsCommand.class
    };

    public MusicCommandSupplier() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<AbstractCommand>[] supply() {
        return COMMANDS;
    }
}
