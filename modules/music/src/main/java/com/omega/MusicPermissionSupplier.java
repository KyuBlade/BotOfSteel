package com.omega;

import com.omega.database.entity.permission.PermissionSupplier;

public class MusicPermissionSupplier implements PermissionSupplier {

    public static final String COMMAND_PLAYLIST_CREATE = "command.music.playlist.create";
    public static final String COMMAND_PLAYLIST_ADD = "command.music.playlist.add";
    public static final String COMMAND_PLAYLIST_DELETE = "command.music.playlist.delete";
    public static final String COMMAND_PLAYLIST_LIST = "command.music.playlist.list";
    public static final String COMMAND_PLAYLIST_PLAY = "command.music.playlist.play";
    public static final String COMMAND_QUEUE_CLEAR = "command.music.queue.clear";
    public static final String COMMAND_QUEUE_LIST = "command.music.queue.list";
    public static final String COMMAND_QUEUE_ADD = "command.music.queue.add";
    public static final String COMMAND_QUEUE_SHUFFLE = "command.music.queue.shuffle";
    public static final String COMMAND_QUEUE_SKIP = "command.music.queue.skip";
    public static final String COMMAND_SETMUSICCHANNEL = "command.music.setmusicchannel";
    public static final String COMMAND_JOIN = "command.music.join";
    public static final String COMMAND_LEAVE = "command.music.leave";
    public static final String COMMAND_LYRICS = "command.music.lyrics";
    public static final String COMMAND_PAUSE = "command.music.pause";
    public static final String COMMAND_RESUME = "command.music.resume";
    public static final String COMMAND_PLAY = "command.music.play";
    public static final String COMMAND_SEEK = "command.music.seek";
    public static final String COMMAND_TRACK = "command.music.track";

    @Override
    public String[] supply() {
        return new String[]{
            COMMAND_PLAYLIST_CREATE, COMMAND_PLAYLIST_ADD, COMMAND_PLAYLIST_DELETE, COMMAND_PLAYLIST_LIST,
            COMMAND_PLAYLIST_PLAY, COMMAND_QUEUE_CLEAR, COMMAND_QUEUE_LIST, COMMAND_QUEUE_ADD,
            COMMAND_QUEUE_SHUFFLE, COMMAND_QUEUE_SKIP, COMMAND_SETMUSICCHANNEL, COMMAND_JOIN,
            COMMAND_LEAVE, COMMAND_LYRICS, COMMAND_PAUSE, COMMAND_RESUME, COMMAND_PLAY, COMMAND_SEEK,
            COMMAND_TRACK
        };
    }

    @Override
    public String[] supplyDefault() {
        return new String[]{
            COMMAND_PLAYLIST_CREATE, COMMAND_PLAYLIST_ADD, COMMAND_PLAYLIST_DELETE, COMMAND_PLAYLIST_LIST,
            COMMAND_QUEUE_LIST, COMMAND_QUEUE_ADD, COMMAND_LYRICS, COMMAND_TRACK
        };
    }
}
