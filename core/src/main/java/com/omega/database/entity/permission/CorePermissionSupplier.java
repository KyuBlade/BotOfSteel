package com.omega.database.entity.permission;

public class CorePermissionSupplier implements PermissionSupplier {

    public static final String COMMAND_KICK = "command.kick";
    public static final String COMMAND_BAN = "command.ban";
    public static final String COMMAND_BOTINFO = "command.botinfo";
    public static final String COMMAND_USERINFO = "command.userinfo";
    public static final String COMMAND_GET = "command.get";
    public static final String COMMAND_SET = "command.set";
    public static final String COMMAND_GROUP = "command.group";
    public static final String COMMAND_PERMISSION = "command.permission";
    public static final String COMMAND_GETVOICECHANNELINFO = "command.getvoicechannelinfo";
    public static final String COMMAND_HELP = "command.help";
    public static final String COMMAND_INVITE = "command.invite";
    public static final String COMMAND_MODULE = "command.module";
    public static final String COMMAND_PING = "command.ping";
    public static final String COMMAND_RANDOM = "command.random";
    public static final String COMMAND_RIP = "command.rip";
    public static final String COMMAND_ROLL = "command.roll";

    public CorePermissionSupplier() {
    }

    @Override
    public String[] supply() {
        return new String[]{
            COMMAND_KICK, COMMAND_BAN, COMMAND_BOTINFO, COMMAND_GET, COMMAND_SET, COMMAND_GROUP, COMMAND_PERMISSION,
            COMMAND_GETVOICECHANNELINFO, COMMAND_HELP, COMMAND_INVITE, COMMAND_MODULE, COMMAND_PING, COMMAND_RANDOM,
            COMMAND_RIP, COMMAND_ROLL, COMMAND_USERINFO
        };
    }

    @Override
    public String[] supplyDefault() {
        return new String[]{COMMAND_ROLL, COMMAND_RANDOM, COMMAND_RIP, COMMAND_PING, COMMAND_HELP, COMMAND_INVITE};
    }
}
