package xyz.pbsi.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Utils.Roles;

import java.util.Objects;

import static xyz.pbsi.Utils.Roles.permissionCheck;

public class ResetAndStop implements DiscordCommand {
    private static final Logger log = LoggerFactory.getLogger(ResetAndStop.class);

    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        if(!permissionCheck(event, Roles.getRoleID(Roles.RolesList.LEADERSHIP), false)) return;

        try{
            Objects.requireNonNull(event.getGuild()).retrieveCommands().queue(commands -> {
                for (Command command: commands){
                    //Deletes all commands (Used if a command needs to be removed or edited, all commands are re-added on bot boot)
                    command.delete().queue();
                }

                System.exit(0);
            });
        }catch (NullPointerException e){
            log.warn("No commands");
        }

    }
}
