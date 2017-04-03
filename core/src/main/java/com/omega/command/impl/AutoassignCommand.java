package com.omega.command.impl;

import com.omega.command.*;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.database.entity.property.NullProperty;
import com.omega.database.entity.property.RoleProperty;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.guild.property.CoreGuildPropertySupplier;
import com.omega.util.DiscordUtils;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Command(name = "autoassign")
public class AutoassignCommand extends AbstractCommand {

    public AutoassignCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_AUTOASSIGN)
    @Signature(help = "Automatically assign a role to users when they are joining the server")
    public void autoassignCommand(@Parameter(name = "roleMention") IRole role) {
        IGuild guild = message.getGuild();
        IUser botUser = guild.getClient().getOurUser();

        if (role == null) {
            GuildContext guildContext = GuildManager.getInstance().getContext(guild);
            guildContext.getProperties().setProperty(CoreGuildPropertySupplier.AUTOROLL, new NullProperty());

            sendStateMessage("autoassign", "No role will be assign to users when they join this server");
        } else if (role.isEveryoneRole()) {
            sendErrorMessage("autoassign", "Everyone role can't be assigned to a user");
        } else if (role.isManaged()) {
            sendErrorMessage("autoassign", "The managed role " + role.getName() + " can't be assigned to a user");
        } else {
            List<IRole> botRoles = guild.getRolesForUser(botUser);
            Optional<IRole> highestRole = botRoles.stream().max(Comparator.comparingInt(IRole::getPosition));
            if (highestRole.isPresent() && role.getPosition() < highestRole.get().getPosition()) {
                GuildContext guildContext = GuildManager.getInstance().getContext(guild);
                guildContext.getProperties().setProperty(CoreGuildPropertySupplier.AUTOROLL, new RoleProperty(role));

                sendStateMessage("autoassign", "New users will now be auto assigned to role " + role.getName() +
                    " when they join this server");
            } else {
                sendErrorMessage("autoassign", "Role " + role.getName() + " is higher or equals to my own highest role");
            }
        }
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_AUTOASSIGN)
    @Signature(help = "Automatically assign a role to users when they are joining the server. Use none as role name to don't assign any role")
    public void autoassignCommand(@Parameter(name = "roleName") String roleName) {
        if (roleName.equalsIgnoreCase("none")) {
            autoassignCommand((IRole) null);
        } else {
            IGuild guild = message.getGuild();
            List<IRole> roles = guild.getRoles();
            IRole role = DiscordUtils.findRoleWithReply(message, roles, roleName);
            if (role != null) {
                autoassignCommand(role);
            }
        }
    }
}
